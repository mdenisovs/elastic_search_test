package com.java_club.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Currency {
    @JsonProperty
    private String date;
    @JsonProperty
    private String symbol;
    @JsonProperty
    private Double open;
    @JsonProperty
    private Double high;
    @JsonProperty
    private Double low;
    @JsonProperty
    private Double close;
    @JsonProperty
    private Double volumeBase;
    @JsonProperty
    private Double volumeQuote;
}
