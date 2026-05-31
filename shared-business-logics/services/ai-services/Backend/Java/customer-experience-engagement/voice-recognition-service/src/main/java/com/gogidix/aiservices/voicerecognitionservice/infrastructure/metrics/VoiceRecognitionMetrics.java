package com.gogidix.aiservices.voicerecognitionservice.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;

@Component
public class VoiceRecognitionMetrics {
    private final MeterRegistry meterRegistry;
    private final Counter recognitionTotalCounter;
    private final Counter recognitionSuccessCounter;
    private final Counter recognitionFailureCounter;
    private final Timer recognitionTimer;

    public VoiceRecognitionMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        this.recognitionTotalCounter = Counter.builder("ai.voice.recognition.total").tag("service", "voice-recognition").register(meterRegistry);
        this.recognitionSuccessCounter = Counter.builder("ai.voice.recognition.success").tag("service", "voice-recognition").register(meterRegistry);
        this.recognitionFailureCounter = Counter.builder("ai.voice.recognition.failure").tag("service", "voice-recognition").register(meterRegistry);
        this.recognitionTimer = Timer.builder("ai.voice.recognition.duration").tag("service", "voice-recognition")
                .publishPercentiles(0.5, 0.95, 0.99).publishPercentileHistogram().register(meterRegistry);
    }

    public void incrementRecognitionTotal() { recognitionTotalCounter.increment(); }
    public void incrementRecognitionSuccess() { recognitionSuccessCounter.increment(); }
    public void incrementRecognitionFailure() { recognitionFailureCounter.increment(); }
    public void recordRecognitionTime(long ms) { recognitionTimer.record(ms, TimeUnit.MILLISECONDS); }
    public Timer.Sample startRecognitionTimer() { return Timer.start(meterRegistry); }
    public void stopRecognitionTimer(Timer.Sample s) { s.stop(recognitionTimer); }
    public double getRecognitionLatencyP95() { return recognitionTimer.percentile(0.95, TimeUnit.MILLISECONDS); }
    public double getRecognitionLatencyP99() { return recognitionTimer.percentile(0.99, TimeUnit.MILLISECONDS); }
    public double getErrorRate() {
        long t = (long) recognitionTotalCounter.count(); long f = (long) recognitionFailureCounter.count();
        return t > 0 ? (double) f / t : 0.0;
    }
    public MeterRegistry getMeterRegistry() { return meterRegistry; }
}
