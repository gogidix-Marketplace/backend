package com.gogidix.hr.countryhrmanagement.interfaces.rest;

import com.gogidix.hr.countryhrmanagement.application.service.CountryHRConfigService;
import com.gogidix.hr.countryhrmanagement.application.service.LaborLawService;
import com.gogidix.hr.countryhrmanagement.domain.model.CountryHRConfig;
import com.gogidix.hr.countryhrmanagement.domain.model.LaborLaw;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/country-hr")
@RequiredArgsConstructor
public class CountryHRController {
    private final CountryHRConfigService configService;
    private final LaborLawService laborLawService;

    @PostMapping("/configs")
    public ResponseEntity<CountryHRConfig> createConfig(@RequestBody CountryHRConfig entity) {
        return ResponseEntity.ok(configService.create(entity));
    }

    @GetMapping("/configs")
    public ResponseEntity<List<CountryHRConfig>> getAllConfigs() {
        return ResponseEntity.ok(configService.getAll());
    }

    @GetMapping("/configs/{id}")
    public ResponseEntity<CountryHRConfig> getConfigById(@PathVariable String id) {
        CountryHRConfig result = configService.getById(id);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/configs/{id}")
    public ResponseEntity<Void> deleteConfig(@PathVariable String id) {
        configService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/labor-laws")
    public ResponseEntity<LaborLaw> createLaborLaw(@RequestBody LaborLaw entity) {
        return ResponseEntity.ok(laborLawService.create(entity));
    }

    @GetMapping("/labor-laws")
    public ResponseEntity<List<LaborLaw>> getAllLaborLaws() {
        return ResponseEntity.ok(laborLawService.getAll());
    }

    @GetMapping("/labor-laws/{id}")
    public ResponseEntity<LaborLaw> getLaborLawById(@PathVariable String id) {
        LaborLaw result = laborLawService.getById(id);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/labor-laws/{id}")
    public ResponseEntity<Void> deleteLaborLaw(@PathVariable String id) {
        laborLawService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
