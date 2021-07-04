package com.vinsguru.redisspring.weather.service;

import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class ExternalServiceClient {

    @CachePut(value = "weather", key = "#zip")
    public int getWeatherInfo(int zip){
        return ThreadLocalRandom.current().nextInt(60, 100);
    }

}
