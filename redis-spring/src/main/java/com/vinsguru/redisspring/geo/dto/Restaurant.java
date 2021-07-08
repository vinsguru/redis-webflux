package com.vinsguru.redisspring.geo.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Restaurant {

    private String id;
    private String city;
    private double latitude;
    private double longitude;
    private String name;
    private String zip;

}
