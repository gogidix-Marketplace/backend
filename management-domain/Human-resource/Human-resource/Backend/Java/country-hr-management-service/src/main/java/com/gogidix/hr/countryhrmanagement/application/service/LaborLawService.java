package com.gogidix.hr.countryhrmanagement.application.service;

import com.gogidix.hr.countryhrmanagement.domain.model.LaborLaw;
import com.gogidix.hr.countryhrmanagement.domain.repository.LaborLawRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LaborLawService {
    private final LaborLawRepository repository;

    public LaborLaw create(LaborLaw law) { return repository.save(law); }
    public LaborLaw getById(String id) { return repository.findById(id).orElse(null); }
    public List<LaborLaw> getAll() { return repository.findAll(); }
    public LaborLaw update(LaborLaw law) { return repository.save(law); }
    public void delete(String id) { repository.deleteById(id); }
}
