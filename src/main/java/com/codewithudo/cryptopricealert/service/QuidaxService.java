package com.codewithudo.cryptopricealert.service;

import com.codewithudo.cryptopricealert.dto.MarketData;
import com.codewithudo.cryptopricealert.dto.SingleTickerResponse;
import com.codewithudo.cryptopricealert.dto.Ticker;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class QuidaxService {

    private final RestTemplate restTemplate;

    public QuidaxService() {
        this.restTemplate = new RestTemplate();
    }

    public Ticker getTicker(String market) {
        String url = "https://app.quidax.com/api/v1/markets/tickers/" + market;

        // Tell RestTemplate to expect our wrapper object
        SingleTickerResponse response = restTemplate.getForObject(url, SingleTickerResponse.class);

        // Unwrap the nested data to get the final Ticker object
        if (response != null && "success".equals(response.getStatus())) {
            MarketData marketData = response.getData();
            if (marketData != null) {
                return marketData.getTicker();
            }
        }

        return null;
    }
}