package com.codewithudo.cryptopricealert;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CryptoPriceAlertApplication {

    public static void main(String[] args) {
        SpringApplication.run(CryptoPriceAlertApplication.class, args);
    }

}
