package com.fullDemo2.controller;


import com.fullDemo2.Entity.*;
import com.fullDemo2.exception.*;
import com.fullDemo2.exception.domain.UserNotFoundException;
import com.fullDemo2.exception.domain.UsernameExistException;
import com.fullDemo2.services.RefreshTokenService;
import com.fullDemo2.services.impl.UserService;
import com.fullDemo2.utility.JWTResponse;
import com.fullDemo2.utility.JWTTokenProvider;
import com.fullDemo2.utility.JWTTokenRefreshRequest;
import com.fullDemo2.utility.TokenRefreshResponse;
import io.jsonwebtoken.impl.DefaultClaims;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.fullDemo2.constant.SecurityConstant.JWT_TOKEN_HEADER;
import static com.fullDemo2.constant.SecurityConstant.JWT_TOKEN_REFRESH_HEADER;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping(path = "/user")
public class UserResource extends ExceptionHandling {
    public static final String EMAIL_SENT = "An email with a new password was sent to: ";
    public static final String USER_DELETED_SUCCESSFULLY = "User deleted successfully";

    private AuthenticationManager authenticationManager;
    private UserService userService;
    private JWTTokenProvider jwtTokenProvider;

    @Autowired
    private RefreshTokenService refreshTokenService;



    @Autowired
    public UserResource(AuthenticationManager authenticationManager, UserService userService, JWTTokenProvider jwtTokenProvider) {
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
        String access_token = jwtTokenProvider.generateJwtToken(userPrincipal);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userPrincipal.getUser().getId());

        return new ResponseEntity<>( new JWTResponse(access_token,refreshToken.getToken(),userPrincipal.getUser().getUserId()),OK);
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(@RequestBody JWTTokenRefreshRequest request) {
        // From the HttpRequest get the claims

        String requestRefreshTokeken = request.getRefreshToken();



        return refreshTokenService.findByToken(requestRefreshTokeken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtTokenProvider.generateTokenFromUsername(user.getUsername());
                    RefreshToken rftoken = refreshTokenService.createRefreshToken(user.getId());

                    refreshTokenService.savToken(rftoken,requestRefreshTokeken);
                    return ResponseEntity.ok(new TokenRefreshResponse(token, rftoken.getToken()));
                })
                .orElseThrow(() ->
                        new TokenRefreshException(requestRefreshTokeken,"Refresh token is not in database!"));
    }



    /*
    String name,
             String username,
             String role,
             String password,
             Long uId,
             Long cId,
             Long bId

     */

    @SneakyThrows
    @PostMapping("/register")
    public ResponseEntity<MyUser> register(@RequestBody MyUser user) throws UserNotFoundException, UsernameExistException {
        MyUser newUser = userService.register(
                user.getName(),
                user.getUsername(),
                user.getRole(),
                user.getPassword(),
                user.getUniversity_id(),
                user.getCollege_id(),
                user.getBranch_id()


        );

        System.out.println("USer branch: "+user.getBranch_id());
        System.out.println("user College"+user.getCollege_id());
        return new ResponseEntity<>(newUser, CREATED);
    }

    @PostMapping("/add")
    public ResponseEntity<MyUser> addNewUser(

            @RequestParam("name") String name,
            @RequestParam("username") String username,
            @RequestParam("role") String role,
            @RequestParam("password") String password,
            @RequestParam("uId") Long uId,
            @RequestParam("cId") Long cId,
            @RequestParam("bId") Long bId
    ){


        MyUser newUser = userService.addNewUser(
                name,username,role,password,uId,cId,bId


        );
        return new ResponseEntity<>(newUser, OK);
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

    @GetMapping("/student/branch")

    public ResponseEntity<?> getStudentByBranch(
            @RequestParam("bname") String bname,
            @RequestParam("username") String username) throws UsernameExistException, UserNotFoundException {

        MyUser user = userService.findMyUserByBranch(bname,username);

        if (user != null) {
            return new ResponseEntity<>(user, OK);
        }
        throw new UserNotFoundException("UserName or Branch are wrong");


    }

    @GetMapping("/student/college")

    public ResponseEntity<?> getStudentByCollege(
            @RequestParam("cname") String cname,
            @RequestParam("username") String username) throws UsernameExistException, UserNotFoundException {

        MyUser user = userService.findMyUserByCollage(cname,username);

        if (user != null) {
            return new ResponseEntity<>(user, OK);
        }
        throw new UserNotFoundException("UserName or College are wrong");

    }

    @GetMapping("/student/university")

    public ResponseEntity<?> getStudentByUniversity(
            @RequestParam("uname") String uname,
            @RequestParam("username") String username) throws UsernameExistException, UserNotFoundException {

        MyUser user = userService.findMyUserByUniversity(uname,username);

        if (user != null) {
            return new ResponseEntity<>(user, OK);
        }
        throw new UserNotFoundException("UserName or university are wrong");

    }

    @GetMapping("students")
    public ResponseEntity<List<MyUser>> getAllStudents(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy)
    {
        List<MyUser> list = userService.getAllUser(pageNo, pageSize, sortBy);

        return new ResponseEntity<List<MyUser>>(list, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("branchs")
    public ResponseEntity<List<Branch>> getAllBranchs(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy)
    {
        List<Branch> list = userService.getAllBranch(pageNo, pageSize, sortBy);

        return new ResponseEntity<List<Branch>>(list, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("colleges")
    public ResponseEntity<List<College>> getAllBCollege(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy)
    {
        List<College> list = userService.getAllCollege(pageNo, pageSize, sortBy);

        return new ResponseEntity<List<College>>(list, new HttpHeaders(), HttpStatus.OK);
    }


    @GetMapping("university")
    public ResponseEntity<List<University>> getAllBUniversity(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy)
    {
        List<University> list = userService.getAllUniversity(pageNo, pageSize, sortBy);

        return new ResponseEntity<List<University>>(list, new HttpHeaders(), HttpStatus.OK);
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


    private HttpHeaders getJwtHeaderRefersh( HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();

        DefaultClaims claims = (io.jsonwebtoken.impl.DefaultClaims) request.getAttribute("claims");
        Map<String,Object> expectedMap = getMapFromIoJsonwebtokenClaims(claims);

        String token = jwtTokenProvider.generateRefreshToken(expectedMap, expectedMap.get("sub").toString());


        headers.add(JWT_TOKEN_REFRESH_HEADER, jwtTokenProvider.generateRefreshToken(claims,token));
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
