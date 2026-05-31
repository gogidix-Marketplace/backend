package com.gogidix.aiservices.voicerecognitionservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.gogidix.aiservices.voicerecognitionservice")
public class VoiceRecognitionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(VoiceRecognitionServiceApplication.class, args);
    }
}
