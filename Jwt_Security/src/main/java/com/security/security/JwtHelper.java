package com.security.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtHelper {

    private final String secret="655468576D5A7133743677397A24432646294A404E635266556A586E32723575";

    private static final long JWT_TOKEN_VALIDATION=24*60*60;
    public String getUsernameFromToken(String token){
        return getClaimsFromToken(token, Claims::getSubject);
    }

    private <T> T getClaimsFromToken(String token, Function<Claims,T> claimsResolver) {

        final Claims claims=getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {

      return  Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }
    private boolean isTokenExpired(String token){

        final Date expiration=getExpirationFromToken(token);
        return expiration.before(new Date());
    }

    private Date getExpirationFromToken(String token) {

        return getClaimsFromToken(token,Claims::getExpiration);
    }

    public String generateToken(UserDetails userDetails){

        Map<String,Object> claims=new HashMap<>();
        return doGenerateToken(claims,userDetails.getUsername());
    }

    private String doGenerateToken(Map<String, Object> claims, String username) {
        return Jwts.builder().setClaims(claims).setSubject(username).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+JWT_TOKEN_VALIDATION+1000))
                .signWith(SignatureAlgorithm.HS256,secret).compact();
    }
    public Boolean validationToken(String token,UserDetails userDetails){

        final String username=getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()))&&!isTokenExpired(token);
    }

}
