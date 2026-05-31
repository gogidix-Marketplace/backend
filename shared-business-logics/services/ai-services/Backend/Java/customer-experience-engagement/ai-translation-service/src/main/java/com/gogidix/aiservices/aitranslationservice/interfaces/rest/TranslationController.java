package com.gogidix.aiservices.aitranslationservice.interfaces.rest;

import com.gogidix.aiservices.aitranslationservice.application.service.TranslationService;
import com.gogidix.aiservices.aitranslationservice.domain.model.Language;
import com.gogidix.aiservices.aitranslationservice.domain.model.TranslationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/translation")
@RequiredArgsConstructor
public class TranslationController {

    private final TranslationService translationService;

    @PostMapping("/translate")
    public ResponseEntity<TranslationResult> translate(
            @RequestParam String text,
            @RequestParam Language target) {
        TranslationResult result = translationService.translate(text, target);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/translate-with-source")
    public ResponseEntity<TranslationResult> translateWithSource(
            @RequestParam String text,
            @RequestParam Language source,
            @RequestParam Language target) {
        TranslationResult result = translationService.translate(text, source, target);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/detect")
    public ResponseEntity<Language> detectLanguage(@RequestParam String text) {
        Language language = translationService.detectLanguage(text);
        return ResponseEntity.ok(language);
    }
}
