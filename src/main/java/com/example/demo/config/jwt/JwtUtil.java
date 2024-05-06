package com.example.demo.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    @Value("${jwt.issuer}")
    private String appName;

    @Value("${jwt.secret_key}")
    private String secretKey;

    @Value("${jwt.expires_in}")
    private int expiresIn;

    private SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;


    /**
     *
     * @param token
     * @return
     */
    private Claims getAllClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }


    /**
     *
     * @param token
     * @return
     */
    public String getUsernameFromToken(String token) {
        String username;
        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    /**
     *
     * @param username
     * @return
     */
    public String generateToken(String username){

        return Jwts.builder()
                .setIssuer( appName )
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(generateExpirationDate())
                .signWith( SIGNATURE_ALGORITHM, secretKey ) //TODO: Убрать exception в этом месте
                .compact();
    }

    /**
     *
     * @return
     */
    private Date generateExpirationDate() {
        return new Date(new Date().getTime() + expiresIn * 1000);
    }

    /**
     *
     * @param token
     * @param userDetails
     * @return
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (
                username != null &&
                        username.equals(userDetails.getUsername()) &&
                        !isTokenExpired(token)
        );
    }

    /**
     *
     * @param token
     * @return
     */
    public boolean isTokenExpired(String token) {
        Date expireDate=getExpirationDate(token);
        return expireDate.before(new Date());
    }


    /**
     *
     * @param token
     * @return
     */
    private Date getExpirationDate(String token) {
        Date expireDate;
        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            expireDate = claims.getExpiration();
        } catch (Exception e) {
            expireDate = null;
        }
        return expireDate;
    }


    /**
     *
     * @param token
     * @return
     */
    public Date getIssuedAtDateFromToken(String token) {
        Date issueAt;
        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            issueAt = claims.getIssuedAt();
        } catch (Exception e) {
            issueAt = null;
        }
        return issueAt;
    }

    /**
     *
     * @param request
     * @return
     */
    public String getToken( HttpServletRequest request ) {

        String authHeader = getAuthHeaderFromHeader( request );
        if ( authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        return null;
    }

    /**
     *
     * @param request
     * @return
     */
    public String getAuthHeaderFromHeader( HttpServletRequest request ) {
        return request.getHeader("Authorization");
    }
}
