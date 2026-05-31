package com.gogidix.aiservices.aitranslationservice.domain.port.in;

import com.gogidix.aiservices.aitranslationservice.domain.model.Language;
import com.gogidix.aiservices.aitranslationservice.domain.model.TranslationResult;

public interface TranslationUseCase {
    TranslationResult translate(String text, Language targetLanguage);
    TranslationResult translate(String text, Language sourceLanguage, Language targetLanguage);
    Language detectLanguage(String text);
}
