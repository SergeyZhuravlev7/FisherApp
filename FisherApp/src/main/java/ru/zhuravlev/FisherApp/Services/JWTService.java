package ru.zhuravlev.FisherApp.Services;

import org.springframework.beans.factory.annotation.Value;

public class JWTService {

    @Value("${jwt.key}")
    private String jwtSecretKey;

    @Value("${jwt.time")
    private long jwtExpirationTime;



}
