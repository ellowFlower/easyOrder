package at.ac.tuwien.sepm.groupphase.backend.security;

import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class JwtTokenizer {

    private final SecurityProperties securityProperties;
    private UserService userService;
    public JwtTokenizer(SecurityProperties securityProperties, UserService userService) {
        this.securityProperties = securityProperties;
        this.userService = userService;
    }

    public String getAuthToken(String user, List<String> roles) {
        byte[] signingKey = securityProperties.getJwtSecret().getBytes();
        String token = Jwts.builder()
            .signWith(Keys.hmacShaKeyFor(signingKey), SignatureAlgorithm.HS512)
            .setHeaderParam("typ", securityProperties.getJwtType())
            .setIssuer(securityProperties.getJwtIssuer())
            .setAudience(securityProperties.getJwtAudience())
            .setSubject(user)
            .setExpiration(new Date(System.currentTimeMillis() + securityProperties.getJwtExpirationTime()))
            .claim("rol", roles)
            .claim("userid", userService.findApplicationUserByEmail(user).getId())
            .compact();
        return securityProperties.getAuthTokenPrefix() + token;
    }
}
