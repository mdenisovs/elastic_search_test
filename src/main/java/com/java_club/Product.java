package com.java_club;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Product {
    @JsonProperty
    private String id;
    @JsonProperty
    private String name;
}
