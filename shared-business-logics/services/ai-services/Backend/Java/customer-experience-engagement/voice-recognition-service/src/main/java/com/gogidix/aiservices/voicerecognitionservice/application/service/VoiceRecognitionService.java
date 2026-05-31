package com.gogidix.aiservices.voicerecognitionservice.application.service;

import com.gogidix.aiservices.voicerecognitionservice.domain.model.RecognitionResult;
import com.gogidix.aiservices.voicerecognitionservice.domain.port.out.VoiceRecognitionPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VoiceRecognitionService {

    private final VoiceRecognitionPort voiceRecognitionPort;

    public RecognitionResult recognize(byte[] audioData, String language, String format) {
        return voiceRecognitionPort.recognize(audioData, language, format);
    }

    public RecognitionResult recognizeFromUrl(String audioUrl, String language) {
        return voiceRecognitionPort.recognizeFromUrl(audioUrl, language);
    }
}
