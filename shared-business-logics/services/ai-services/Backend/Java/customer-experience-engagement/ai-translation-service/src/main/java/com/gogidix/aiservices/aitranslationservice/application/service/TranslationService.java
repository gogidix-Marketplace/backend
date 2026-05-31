package com.gogidix.aiservices.aitranslationservice.application.service;

import com.gogidix.aiservices.aitranslationservice.domain.model.Language;
import com.gogidix.aiservices.aitranslationservice.domain.model.TranslationResult;
import com.gogidix.aiservices.aitranslationservice.domain.port.in.TranslationUseCase;
import com.gogidix.aiservices.aitranslationservice.domain.port.out.TranslationPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TranslationService implements TranslationUseCase {

    private final TranslationPort translationPort;

    @Override
    public TranslationResult translate(String text, Language targetLanguage) {
        Language detectedLanguage = translationPort.detectLanguage(text);
        return translationPort.translate(text, detectedLanguage, targetLanguage);
    }

    @Override
    public TranslationResult translate(String text, Language sourceLanguage, Language targetLanguage) {
        return translationPort.translate(text, sourceLanguage, targetLanguage);
    }

    @Override
    public Language detectLanguage(String text) {
        return translationPort.detectLanguage(text);
    }
}
