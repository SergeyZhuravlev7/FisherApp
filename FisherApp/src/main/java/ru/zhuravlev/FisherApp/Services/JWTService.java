package ru.zhuravlev.FisherApp.Services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.zhuravlev.FisherApp.Configuration.Security.CustomUserDetails;
import java.util.Date;
import java.util.UUID;

@Component
public class JWTService {

    @Value ("${jwt.access.key}")
    private String jwtAccessKey;

    @Value ("${jwt.access.time}")
    private long jwtAccessExpirationTime;

    @Value ("${jwt.refresh.key")
    private String jwtRefreshKey;

    @Value ("${jwt.refresh.time}")
    private long jwtRefreshExpirationTime;

    public String createAccessToken(CustomUserDetails userDetails) {
        Date issuesDate = new Date();
        Date expiresDate = new Date(issuesDate.getTime() + jwtAccessExpirationTime);
        return JWT.create()
                .withJWTId(UUID.randomUUID().toString())
                .withClaim("alg","HMAC256")
                .withClaim("typ","access")
                .withIssuer("FisherApp")
                .withIssuedAt(issuesDate)
                .withSubject(userDetails.getUsername())
                .withClaim("ROLES",userDetails.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList())
                .withExpiresAt(expiresDate)
                .sign(Algorithm.HMAC256(jwtAccessKey));
    }

    public String createAccessToken(String refreshToken) {
        Date issuesDate = new Date();
        Date expiresDate = new Date(issuesDate.getTime() + jwtAccessExpirationTime);
        DecodedJWT token = JWT.decode(refreshToken);
        return JWT.create()
                .withJWTId(UUID.randomUUID().toString())
                .withClaim("alg","HMAC256")
                .withClaim("typ","access")
                .withIssuer("FisherApp")
                .withIssuedAt(issuesDate)
                .withSubject(token.getSubject())
                .withClaim("ROLES",token.getClaim("ROLES").asList(String.class))
                .withExpiresAt(expiresDate)
                .sign(Algorithm.HMAC256(jwtAccessKey));
    }

    public String createRefreshToken(CustomUserDetails userDetails) {
        Date issuesDate = new Date();
        Date expiresDate = new Date(issuesDate.getTime() + jwtRefreshExpirationTime);
        return JWT.create()
                .withJWTId(UUID.randomUUID().toString())
                .withClaim("alg","HMAC256")
                .withClaim("typ","refresh")
                .withIssuer("FisherApp")
                .withIssuedAt(issuesDate)
                .withSubject(userDetails.getUsername())
                .withClaim("ROLES",userDetails.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList())
                .withExpiresAt(expiresDate)
                .sign(Algorithm.HMAC256(jwtRefreshKey));
    }


    public String getUsername(String jwt) {
        return JWT.decode(jwt).getSubject();
    }

    public boolean isValidAccessToken(String jwt,UserDetails userDetails) {
        DecodedJWT token = JWT.decode(jwt);
        return getUsername(jwt).equals(userDetails.getUsername()) && token.getExpiresAt().after(new Date());
    }

    public boolean isValidRefreshToken(String jwt) {
        DecodedJWT token = JWT.decode(jwt);
        return token.getIssuer().equals("FisherApp") && token.getExpiresAt().after(new Date());
    }
}
