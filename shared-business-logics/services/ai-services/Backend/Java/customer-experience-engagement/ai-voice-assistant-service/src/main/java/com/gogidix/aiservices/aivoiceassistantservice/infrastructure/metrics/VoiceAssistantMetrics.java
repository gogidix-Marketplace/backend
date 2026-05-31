package com.gogidix.aiservices.aivoiceassistantservice.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class VoiceAssistantMetrics {

    private final MeterRegistry meterRegistry;

    private final Counter assistantTotalCounter;
    private final Counter assistantSuccessCounter;
    private final Counter assistantFailureCounter;
    private final Counter voiceCommandsProcessedCounter;

    private final Timer assistantTimer;
    private final Timer speechToTextTimer;

    public VoiceAssistantMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        this.assistantTotalCounter = Counter.builder("ai.voice.assistant.total")
                .description("Total number of voice assistant requests")
                .tag("service", "ai-voice-assistant")
                .register(meterRegistry);

        this.assistantSuccessCounter = Counter.builder("ai.voice.assistant.success")
                .description("Number of successful voice assistant requests")
                .tag("service", "ai-voice-assistant")
                .register(meterRegistry);

        this.assistantFailureCounter = Counter.builder("ai.voice.assistant.failure")
                .description("Number of failed voice assistant requests")
                .tag("service", "ai-voice-assistant")
                .register(meterRegistry);

        this.voiceCommandsProcessedCounter = Counter.builder("ai.voice.assistant.commands.processed")
                .description("Number of voice commands processed")
                .tag("service", "ai-voice-assistant")
                .register(meterRegistry);

        this.assistantTimer = Timer.builder("ai.voice.assistant.duration")
                .description("Voice assistant processing time")
                .tag("service", "ai-voice-assistant")
                .publishPercentiles(0.5, 0.95, 0.99)
                .publishPercentileHistogram()
                .register(meterRegistry);

        this.speechToTextTimer = Timer.builder("ai.voice.assistant.stt.duration")
                .description("Speech-to-text processing time")
                .tag("service", "ai-voice-assistant")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);
    }

    public void incrementAssistantTotal() {
        assistantTotalCounter.increment();
    }

    public void incrementAssistantSuccess() {
        assistantSuccessCounter.increment();
    }

    public void incrementAssistantFailure() {
        assistantFailureCounter.increment();
    }

    public void incrementVoiceCommandsProcessed() {
        voiceCommandsProcessedCounter.increment();
    }

    public void recordAssistantTime(long durationMs) {
        assistantTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public Timer.Sample startAssistantTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopAssistantTimer(Timer.Sample sample) {
        sample.stop(assistantTimer);
    }

    public double getAssistantLatencyP95() {
        return assistantTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getAssistantLatencyP99() {
        return assistantTimer.percentile(0.99, TimeUnit.MILLISECONDS);
    }

    public double getSpeechToTextLatencyP95() {
        return speechToTextTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getErrorRate() {
        long total = (long) assistantTotalCounter.count();
        long failures = (long) assistantFailureCounter.count();
        return total > 0 ? (double) failures / total : 0.0;
    }

    public MeterRegistry getMeterRegistry() {
        return meterRegistry;
    }
}
