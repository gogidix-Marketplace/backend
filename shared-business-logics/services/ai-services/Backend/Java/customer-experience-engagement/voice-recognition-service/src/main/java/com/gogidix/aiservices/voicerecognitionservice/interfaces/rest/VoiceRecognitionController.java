package com.gogidix.aiservices.voicerecognitionservice.interfaces.rest;

import com.gogidix.aiservices.voicerecognitionservice.application.service.VoiceRecognitionService;
import com.gogidix.aiservices.voicerecognitionservice.domain.model.RecognitionResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/voice-recognition")
@RequiredArgsConstructor
public class VoiceRecognitionController {

    private final VoiceRecognitionService voiceRecognitionService;

    @PostMapping("/recognize")
    public ResponseEntity<RecognitionResult> recognize(
            @RequestParam("audio") MultipartFile audio,
            @RequestParam(defaultValue = "en") String language) {

        try {
            byte[] audioData = audio.getBytes();
            RecognitionResult result = voiceRecognitionService.recognize(
                    audioData, language, audio.getContentType()
            );
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/recognize-url")
    public ResponseEntity<RecognitionResult> recognizeFromUrl(
            @RequestParam String audioUrl,
            @RequestParam(defaultValue = "en") String language) {

        RecognitionResult result = voiceRecognitionService.recognizeFromUrl(audioUrl, language);
        return ResponseEntity.ok(result);
    }
}
