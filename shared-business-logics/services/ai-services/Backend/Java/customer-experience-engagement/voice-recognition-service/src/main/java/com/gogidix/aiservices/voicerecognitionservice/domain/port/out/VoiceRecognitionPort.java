package com.gogidix.aiservices.voicerecognitionservice.domain.port.out;

import com.gogidix.aiservices.voicerecognitionservice.domain.model.RecognitionResult;

public interface VoiceRecognitionPort {
    RecognitionResult recognize(byte[] audioData, String language, String format);

    RecognitionResult recognizeFromUrl(String audioUrl, String language);
}
