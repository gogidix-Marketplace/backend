package com.gogidix.sales.dashboard.domain.valueobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Currency;

/**
 * Value Object representing a geographical region
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Region {

    private String code;
    private String name;
    private String currencyCode;
    private String timezone;

    public static Region northAmerica() {
        return Region.builder()
                .code("NA")
                .name("North America")
                .currencyCode("USD")
                .timezone("America/New_York")
                .build();
    }

    public static Region europe() {
        return Region.builder()
                .code("EU")
                .name("Europe")
                .currencyCode("EUR")
                .timezone("Europe/London")
                .build();
    }

    public static Region asiaPacific() {
        return Region.builder()
                .code("APAC")
                .name("Asia Pacific")
                .currencyCode("SGD")
                .timezone("Asia/Singapore")
                .build();
    }

    public static Region latinAmerica() {
        return Region.builder()
                .code("LATAM")
                .name("Latin America")
                .currencyCode("BRL")
                .timezone("America/Sao_Paulo")
                .build();
    }

    public static Region middleEast() {
        return Region.builder()
                .code("MEA")
                .name("Middle East & Africa")
                .currencyCode("AED")
                .timezone("Asia/Dubai")
                .build();
    }

    public Currency getCurrency() {
        return Currency.getInstance(currencyCode);
    }
}
