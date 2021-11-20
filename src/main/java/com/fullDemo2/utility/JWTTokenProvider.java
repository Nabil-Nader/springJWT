package com.fullDemo2.utility;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.fullDemo2.Entity.UserPrincipal;
import io.jsonwebtoken.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;


import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static com.fullDemo2.constant.SecurityConstant.*;
import static io.jsonwebtoken.SignatureAlgorithm.HS512;
import static java.util.Arrays.stream;


/*
    1-Generate JWT
    2-get Username from JWT
    3-Validate JWT
 */

@Component
public class JWTTokenProvider implements Serializable {


    @Value("${jwt.secret}")
    private String secret;

    /*
        1-Claims
        2-expression
        3-sign
        4-compact()->build
     */


    public String generateJwtToken(UserPrincipal userPrincipal) {


        return generateTokenFromUsername(userPrincipal.getUsername());
    }

    public String getUserNameFromToken(String token){
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token).getBody()
                .getSubject();

    }





   /* public String generateJwtToken(
            UserPrincipal userPrincipal) {


        String[] claims =
                getClaimsFromUser(userPrincipal);
        return JWT.create().withIssuer(GET_KIWE).withAudience(GET_ARRAYS_ADMINISTRATION)
                .withIssuedAt(new Date()).withSubject(userPrincipal.getUsername())
                .withArrayClaim(AUTHORITIES, claims).withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(HMAC512(secret.getBytes()));
    }

*/

    public String generateRefreshToken(Map<String,Object> claims,String username ){

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN))
                .signWith(HS512,secret)
                .compact();
    }

    public String generateTokenFromUsername(String username) {

        return Jwts.builder()

                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() +EXPIRATION_TIME))
                .signWith(HS512,secret)
                .compact();
    }





    public List<GrantedAuthority> getAuthorities(UserPrincipal user) {
     //   UserDetails user = getUsernameFromToken(token);
        String[] claims = getClaimsFromUser(user);
        return stream(claims)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public Authentication getAuthentication(String username, List<GrantedAuthority> authorities, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken userPasswordAuthToken = new
                UsernamePasswordAuthenticationToken(username, null, authorities);

        userPasswordAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return userPasswordAuthToken;
    }

    /*
        validateToken
     */

    public boolean isTokenValid(String token) {

            try {
                Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
                return true;
            } catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
                throw new BadCredentialsException("INVALID_CREDENTIALS", ex);
            } catch (ExpiredJwtException ex) {
                throw ex;
            }
        }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        return claims.getSubject();

    }

    /*public String getSubject(String token) {
        JWTVerifier verifier = getJWTVerifier();
        return verifier.verify(token).getSubject();
    }*/

    private boolean isTokenExpired(JWTVerifier verifier, String token) {
        Date expiration = verifier.verify(token).getExpiresAt();
        return expiration.before(new Date());
    }

    /*private String[] getClaimsFromToken(String token) {
        JWTVerifier verifier = getJWTVerifier();
        return verifier.verify(token).getClaim(AUTHORITIES).asArray(String.class);
    }*/

    /*private JWTVerifier getJWTVerifier() {
        JWTVerifier verifier;
        try {
            Algorithm algorithm = HS512 ;
            verifier = JWT.require(algorithm).withIssuer(GET_KIWE).build();
        }catch (JWTVerificationException exception) {
            throw new JWTVerificationException(TOKEN_CANNOT_BE_VERIFIED);
        }
        return verifier;
    }
*/
    private String[] getClaimsFromUser(UserPrincipal user) {
        List<String> authorities = new ArrayList<>();
        for (GrantedAuthority grantedAuthority : user.getAuthorities()){
            authorities.add(grantedAuthority.getAuthority());
        }
        return authorities.toArray(new String[0]);
    }


   /* public String generateTokenFromUsername(String username) {

        return Jwts.builder()
                .setSubject(username)
                .setIssuer(GET_KIWE)
                .setAudience(GET_ARRAYS_ADMINISTRATION)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();

    }*/


    /*public String generateRefreshToken(String username){

        return Jwts.builder()
                .setSubject(username)
                .setIssuer(GET_KIWE)
                .setAudience(GET_ARRAYS_ADMINISTRATION)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + REFRESH_TOKEN))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

*/





}
