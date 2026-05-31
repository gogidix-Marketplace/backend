package com.gogidix.globalbusinessmanagement.countryingestion.application.service;

import com.gogidix.globalbusinessmanagement.countryingestion.domain.model.CountryData;
import com.gogidix.globalbusinessmanagement.countryingestion.domain.model.ValidationError;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DataValidationService {

    public List<ValidationError> validateCountryData(CountryData countryData) {
        return new ArrayList<>();
    }
}
