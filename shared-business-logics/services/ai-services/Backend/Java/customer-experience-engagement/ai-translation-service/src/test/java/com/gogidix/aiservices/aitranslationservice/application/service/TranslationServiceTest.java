package com.gogidix.aiservices.aitranslationservice.application.service;

import com.gogidix.aiservices.aitranslationservice.domain.model.Language;
import com.gogidix.aiservices.aitranslationservice.domain.model.TranslationResult;
import com.gogidix.aiservices.aitranslationservice.domain.port.out.TranslationPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TranslationServiceTest {

    @Mock
    private TranslationPort translationPort;

    @InjectMocks
    private TranslationService translationService;

    @Test
    void shouldTranslate() {
        TranslationResult result = TranslationResult.builder()
                .translationId("trans-1")
                .sourceText("Hello")
                .translatedText("Hola")
                .sourceLanguage(Language.ENGLISH)
                .targetLanguage(Language.SPANISH)
                .confidence(0.95)
                .createdAt(Instant.now())
                .characterCount(5)
                .build();

        when(translationPort.translate(any(), any(), any())).thenReturn(result);

        TranslationResult translation = translationService.translate("Hello", Language.ENGLISH, Language.SPANISH);

        assertThat(translation).isNotNull();
        assertThat(translation.getTranslatedText()).isEqualTo("Hola");
    }

    @Test
    void shouldDetectLanguage() {
        when(translationPort.detectLanguage(any())).thenReturn(Language.ENGLISH);

        Language language = translationService.detectLanguage("Hello world");

        assertThat(language).isEqualTo(Language.ENGLISH);
    }
}
