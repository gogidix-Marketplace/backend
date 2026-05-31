package com.gogidix.aiservices.voicerecognitionservice.domain.port.in;

import com.gogidix.aiservices.voicerecognitionservice.domain.model.RecognitionResult;

public interface VoiceRecognitionUseCase {
    RecognitionResult recognize(String audioData, String format);
}
