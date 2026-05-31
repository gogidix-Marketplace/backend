package com.gogidix.aiservices.aivoiceservice.interfaces.rest;

import com.gogidix.aiservices.aivoiceservice.application.service.VoiceService;
import com.gogidix.aiservices.aivoiceservice.domain.model.SynthesisResult;
import com.gogidix.aiservices.aivoiceservice.domain.model.VoiceType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/voice")
@RequiredArgsConstructor
public class VoiceController {

    private final VoiceService voiceService;

    @PostMapping("/synthesize")
    public ResponseEntity<SynthesisResult> synthesize(
            @RequestParam String text,
            @RequestParam(defaultValue = "NEUTRAL") VoiceType voiceType,
            @RequestParam(defaultValue = "mp3") String format) {

        SynthesisResult result = voiceService.synthesize(text, voiceType, format);
        return ResponseEntity.ok(result);
    }
}
