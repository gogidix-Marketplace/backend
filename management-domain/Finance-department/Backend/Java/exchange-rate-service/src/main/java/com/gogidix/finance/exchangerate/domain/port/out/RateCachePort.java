package com.gogidix.finance.exchangerate.domain.port.out;

import java.math.BigDecimal;
import java.util.Optional;

public interface RateCachePort {
    void cacheRate(String currencyPair, BigDecimal rate, long ttlSeconds);
    Optional<BigDecimal> getCachedRate(String currencyPair);
    void evict(String currencyPair);
    void clear();
}
