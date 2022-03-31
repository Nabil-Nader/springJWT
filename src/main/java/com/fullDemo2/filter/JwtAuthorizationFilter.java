package com.fullDemo2.filter;


import com.fullDemo2.Entity.MyUser;
import com.fullDemo2.Entity.UserPrincipal;
import com.fullDemo2.repo.MyUserRepo;
import com.fullDemo2.services.impl.UserServiceImpl;
import com.fullDemo2.utility.JWTTokenProvider;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static com.fullDemo2.constant.SecurityConstant.OPTIONS_HTTP_METHOD;
import static com.fullDemo2.constant.SecurityConstant.TOKEN_PREFIX;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.OK;
@Slf4j
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    private JWTTokenProvider jwtTokenProvider;

    @Autowired
    private MyUserRepo userRepository ;

    @Autowired
    private UserServiceImpl userDetailsService ;




    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getMethod().equalsIgnoreCase(OPTIONS_HTTP_METHOD)) {
            response.setStatus(OK.value());
        } else {
            try {

                String authorizationHeader = request.getHeader(AUTHORIZATION);

                if (authorizationHeader == null || !authorizationHeader.startsWith(TOKEN_PREFIX)) {
                    filterChain.doFilter(request, response);
                    return;
                }
                // JWT token is in the form "Bearer token ", Remove Bearer word
                //and get only token
                String token = authorizationHeader.substring(TOKEN_PREFIX.length());

                String username = jwtTokenProvider.getUsernameFromToken(token);

                MyUser checkMyUser = userRepository.findMyUserByName(username);


                if (jwtTokenProvider.isTokenValid (token) && SecurityContextHolder.getContext().getAuthentication() == null) {

                    UserPrincipal userPrincipal = (UserPrincipal) userDetailsService.loadUserByUsername(username);

                    List<GrantedAuthority> authorities = jwtTokenProvider.getAuthorities(userPrincipal);

                /*
                    After setting the Authentication in the context, we specify
                    that the current user is authenticated. so it passes the
                    spring security configuration successfully
                 */

                    Authentication auth = jwtTokenProvider.getAuthentication(username, authorities, request);

                    SecurityContextHolder.getContext().setAuthentication(auth);

                } else {
                    logger.info("Cannot set the security context!!");
                    SecurityContextHolder.clearContext();
                }
            } catch (Exception e) {
                logger.error("Cannot set user authentication: {}");
            }
            }

        filterChain.doFilter(request, response);
    }


    /***************************************************************************
                    Helper Method
     **********************************************************************/

    private void allowForRefreshToken(ExpiredJwtException ex,HttpServletRequest request ){

        /* Create a UsernamePasswordAuthenticationToken
           with null value
          */
        Authentication auth = jwtTokenProvider.getAuthentication(null, null, null);

        /* After setting the Authentication in the context, we specify
            that the current user is authenticated.
            so ut passes the spring security configuration successfully

         */
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Set the claims so that in controller we will be using it new JWT

        request.setAttribute("claims",ex.getClaims());


    }




}
