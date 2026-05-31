package com.gogidix.finance.exchangerate.domain.port.in;

import com.gogidix.finance.exchangerate.application.dto.ExchangeRateRequest;
import com.gogidix.finance.exchangerate.application.dto.ExchangeRateResponse;

import java.util.List;

public interface ExchangeRateCommandPort {
    ExchangeRateResponse createRate(ExchangeRateRequest request);
    ExchangeRateResponse updateRate(String id, ExchangeRateRequest request);
    void deleteRate(String id);
    List<ExchangeRateResponse> importRates(List<ExchangeRateRequest> requests);
}
