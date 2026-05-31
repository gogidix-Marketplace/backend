package com.gogidix.hr.countryhrmanagement.application.service;

import com.gogidix.hr.countryhrmanagement.domain.model.CountryHRConfig;
import com.gogidix.hr.countryhrmanagement.domain.repository.CountryHRConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CountryHRConfigService {
    private final CountryHRConfigRepository repository;

    public CountryHRConfig create(CountryHRConfig config) { return repository.save(config); }
    public CountryHRConfig getById(String id) { return repository.findById(id).orElse(null); }
    public List<CountryHRConfig> getAll() { return repository.findAll(); }
    public CountryHRConfig update(CountryHRConfig config) { return repository.save(config); }
    public void delete(String id) { repository.deleteById(id); }
}
