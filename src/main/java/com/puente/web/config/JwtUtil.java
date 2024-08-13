package com.puente.web.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtil {

    private final MyProperties myProperties;
    public JwtUtil(MyProperties myProperties) {
        this.myProperties = myProperties;
    }

    private Algorithm algorithm;
    @PostConstruct
    public void init(){
        String secretKey = myProperties.getJwtPassword();
        this.algorithm = Algorithm.HMAC256(secretKey);
    }

    public String create(String username){
        return JWT.create()
                .withSubject(username)
                .withIssuer("infatlan")
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(15)))
                .sign(algorithm);
    }

    public Boolean isValid(String jwt){
        try {
            JWT.require(algorithm)
                    .build()
                    .verify(jwt);
            return true;
        }catch (JWTVerificationException e)
        {
            return false;
        }
    }

    public String getUsername(String jwt){
        return JWT.require(algorithm)
                .build()
                .verify(jwt)
                .getSubject();
    }
}
