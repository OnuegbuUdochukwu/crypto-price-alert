package com.codewithudo.cryptopricealert.dto;

import lombok.Data;

@Data
public class SingleTickerResponse {
    private String status;
    private String message;
    private MarketData data;
}
