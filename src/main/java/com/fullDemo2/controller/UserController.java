package com.fullDemo2.controller;


import com.fullDemo2.Entity.MyUser;
import com.fullDemo2.Entity.UserPrincipal;
import com.fullDemo2.exception.ExceptionHandling;
import com.fullDemo2.exception.HttpResponse;
import com.fullDemo2.exception.domain.UserNotFoundException;
import com.fullDemo2.exception.domain.UsernameExistException;
import com.fullDemo2.services.impl.UserService;
import com.fullDemo2.utility.JWTResponse;
import com.fullDemo2.utility.JWTTokenProvider;
import io.jsonwebtoken.impl.DefaultClaims;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.fullDemo2.constant.SecurityConstant.JWT_TOKEN_HEADER;
import static com.fullDemo2.constant.SecurityConstant.JWT_TOKEN_REFRESH_HEADER;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping(path = "/user")
public class UserController extends ExceptionHandling {
    public static final String EMAIL_SENT = "An email with a new password was sent to: ";
    public static final String USER_DELETED_SUCCESSFULLY = "User deleted successfully";

    private AuthenticationManager authenticationManager;
    private UserService userService;
    private JWTTokenProvider jwtTokenProvider;


    @Autowired
    public UserController(AuthenticationManager authenticationManager, UserService userService, JWTTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody MyUser user) {
        authenticate(user.getUsername(), user.getPassword());
        MyUser loginUser = userService.findUserByUsername(user.getUsername());
        UserPrincipal userPrincipal = new UserPrincipal(loginUser);

//        HttpHeaders jwtHeader = getJwtHeader(userPrincipal);
        String access_token = jwtTokenProvider.generateJwtTokenFromUser(userPrincipal);

        return new ResponseEntity<>(new JWTResponse(access_token, access_token, userPrincipal.getUser().getUsername()), OK);
    }


    @SneakyThrows
    @PostMapping("/register")
    public ResponseEntity<MyUser> register(@RequestBody MyUser user) throws UserNotFoundException, UsernameExistException {
        MyUser newUser = userService.register(user);


        return new ResponseEntity<>(newUser, CREATED);
    }


    @GetMapping("/role")
    @PreAuthorize("hasAnyAuthority('user:delete')")
    public ResponseEntity<String> displaySimpleText() {

        return new ResponseEntity<>("yes he have the authorize", ACCEPTED);
    }


    @GetMapping("/find/{username}")
    public ResponseEntity<MyUser> getUser(@PathVariable("username") String username) {
        MyUser user = userService.findUserByUsername(username);
        return new ResponseEntity<>(user, OK);
    }

    @GetMapping("/list")
    public ResponseEntity<List<MyUser>> getAllUsers() {
        List<MyUser> users = userService.getUsers();
        return new ResponseEntity<>(users, OK);
    }


    @DeleteMapping("/delete/{username}")
    @PreAuthorize("hasAnyAuthority('user:delete')")
    public ResponseEntity<HttpResponse> deleteUser(@PathVariable("username") String username) throws IOException {
        userService.deleteUser(username);
        return response(OK, USER_DELETED_SUCCESSFULLY);
    }

    /*********************************************************************************
     Helper Methode
     ******************************************************************************/


    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

    private HttpHeaders getJwtHeader(UserPrincipal user) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(JWT_TOKEN_HEADER, jwtTokenProvider.generateJwtToken(user));
        return headers;
    }


    private HttpHeaders getJwtHeaderRefersh(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();

        DefaultClaims claims = (io.jsonwebtoken.impl.DefaultClaims) request.getAttribute("claims");
        Map<String, Object> expectedMap = getMapFromIoJsonwebtokenClaims(claims);

        String token = jwtTokenProvider.generateRefreshToken(expectedMap, expectedMap.get("sub").toString());


        headers.add(JWT_TOKEN_REFRESH_HEADER, jwtTokenProvider.generateRefreshToken(claims, token));
        return headers;
    }


    private ResponseEntity<HttpResponse> response(HttpStatus httpStatus,
                                                  String message) {
        return new ResponseEntity<>(new
                HttpResponse(httpStatus.value(),
                httpStatus, httpStatus.getReasonPhrase().toUpperCase(),
                message), httpStatus);
    }


    private Map<String, Object> getMapFromIoJsonwebtokenClaims(DefaultClaims claims) {
        Map<String, Object> expectedMap = new HashMap<String, Object>();
        for (Map.Entry<String, Object> entry : claims.entrySet()) {
            expectedMap.put(entry.getKey(), entry.getValue());
        }
        return expectedMap;
    }


}
