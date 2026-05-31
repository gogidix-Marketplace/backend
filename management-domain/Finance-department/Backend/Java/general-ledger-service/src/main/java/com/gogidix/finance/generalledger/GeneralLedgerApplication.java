package com.gogidix.finance.generalledger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class GeneralLedgerApplication {
    public static void main(String[] args) {
        SpringApplication.run(GeneralLedgerApplication.class, args);
    }
}
