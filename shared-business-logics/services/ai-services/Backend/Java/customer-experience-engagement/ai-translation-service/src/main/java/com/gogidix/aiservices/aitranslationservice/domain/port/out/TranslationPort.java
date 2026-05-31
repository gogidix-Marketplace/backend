package com.gogidix.aiservices.aitranslationservice.domain.port.out;

import com.gogidix.aiservices.aitranslationservice.domain.model.Language;
import com.gogidix.aiservices.aitranslationservice.domain.model.TranslationResult;

public interface TranslationPort {
    TranslationResult translate(String text, Language source, Language target);

    Language detectLanguage(String text);

    TranslationResult translateBatch(String[] texts, Language source, Language target);
}
