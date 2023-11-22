package com.spring.course.config;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Service class for handling JSON Web Token (JWT) operations.
 */
@Service
public class JwtService {

    private final String secretKey;

    @Autowired
    public JwtService(@Value("${app.secret-key}") String secretKey) {
        this.secretKey = secretKey;
    }

    /**
     * Extracts the username from a JWT token.
     *
     * @param jwtToken The JWT token.
     * @return The username extracted from the token.
     */
    public String extractUsername(String jwtToken) {
        return extractClaim(jwtToken, Claims::getSubject);
    }

    /**
     * Extracts a specific claim from a JWT token.
     *
     * @param jwtToken       The JWT token.
     * @param claimsResolver The function to resolve the claim.
     * @param <T>            The type of the claim.
     * @return The resolved claim.
     */
    public <T> T extractClaim(String jwtToken, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(jwtToken);
        return claimsResolver.apply(claims);
    }

    /**
     * Generates a JWT token for a user with default expiration time.
     *
     * @param userDetails The UserDetails object for the user.
     * @return The generated JWT token.
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Generates a JWT token for a user with custom claims and expiration time.
     *
     * @param extraClaims Custom claims to include in the token.
     * @param userDetails The UserDetails object for the user.
     * @return The generated JWT token.
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extracts all claims from a JWT token.
     *
     * @param jwtToken The JWT token.
     * @return The Claims object containing all claims from the token.
     */
    private Claims extractAllClaims(String jwtToken) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(jwtToken)
                .getBody();
    }

    /**
     * Retrieves the signing key for JWT token verification.
     *
     * @return The signing key.
     */
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Checks if a JWT token is valid for a given user.
     *
     * @param jwtToken    The JWT token.
     * @param userDetails The UserDetails object for the user.
     * @return True if the token is valid, false otherwise.
     */
    public boolean isTokenValid(String jwtToken, UserDetails userDetails) {
        final String userName = extractUsername(jwtToken);
        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(jwtToken);
    }

    /**
     * Checks if a JWT token has expired.
     *
     * @param jwtToken The JWT token.
     * @return True if the token has expired, false otherwise.
     */
    private boolean isTokenExpired(String jwtToken) {
        return extractExpiration(jwtToken).before(new Date());
    }

    /**
     * Extracts the expiration date from a JWT token.
     *
     * @param jwtToken The JWT token.
     * @return The expiration date.
     */
    private Date extractExpiration(String jwtToken) {
        return extractClaim(jwtToken, Claims::getExpiration);
    }
}
