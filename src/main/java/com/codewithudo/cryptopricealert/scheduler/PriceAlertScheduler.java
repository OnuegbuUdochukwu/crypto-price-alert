package com.codewithudo.cryptopricealert.scheduler;

import com.codewithudo.cryptopricealert.dto.Ticker;
import com.codewithudo.cryptopricealert.service.QuidaxService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalTime;

@Component
public class PriceAlertScheduler {

    private final QuidaxService quidaxService;

    // --- Configuration for our alert ---
    private static final String MARKET_TO_WATCH = "btcngn";
    private static final BigDecimal TARGET_PRICE = new BigDecimal("200000000"); // 200 Million NGN
    private boolean alertTriggered = false; // Flag to ensure we only alert once

    public PriceAlertScheduler(QuidaxService quidaxService) {
        this.quidaxService = quidaxService;
    }

    @Scheduled(fixedRate = 10000) // Runs every 10,000 milliseconds (10 seconds)
    public void checkPriceAndAlert() {
        // If the alert has already been sent, do nothing.
        if (alertTriggered) {
            return;
        }

        System.out.println("Checking price for " + MARKET_TO_WATCH + " at " + LocalTime.now());
        Ticker ticker = quidaxService.getTicker(MARKET_TO_WATCH);

        if (ticker != null && ticker.getPrice() != null) {
            BigDecimal currentPrice = new BigDecimal(ticker.getPrice());

            // Check if the current price has crossed our target
            if (currentPrice.compareTo(TARGET_PRICE) >= 0) {
                System.out.println("==========================================");
                System.out.println("!!! PRICE ALERT TRIGGERED !!!");
                System.out.println(MARKET_TO_WATCH.toUpperCase() + " has reached the target price of " + TARGET_PRICE);
                System.out.println("Current Price: " + currentPrice);
                System.out.println("==========================================");

                // Set the flag to true so we don't send the alert again
                this.alertTriggered = true;
            }
        }
    }
}