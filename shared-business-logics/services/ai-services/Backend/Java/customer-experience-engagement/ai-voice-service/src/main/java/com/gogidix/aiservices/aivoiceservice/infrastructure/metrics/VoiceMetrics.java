package com.gogidix.aiservices.aivoiceservice.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;

@Component
public class VoiceMetrics {
    private final MeterRegistry meterRegistry;
    private final Counter synthesisTotalCounter;
    private final Counter synthesisSuccessCounter;
    private final Counter synthesisFailureCounter;
    private final Timer synthesisTimer;

    public VoiceMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        this.synthesisTotalCounter = Counter.builder("ai.voice.synthesis.total").tag("service", "ai-voice").register(meterRegistry);
        this.synthesisSuccessCounter = Counter.builder("ai.voice.synthesis.success").tag("service", "ai-voice").register(meterRegistry);
        this.synthesisFailureCounter = Counter.builder("ai.voice.synthesis.failure").tag("service", "ai-voice").register(meterRegistry);
        this.synthesisTimer = Timer.builder("ai.voice.synthesis.duration").tag("service", "ai-voice")
                .publishPercentiles(0.5, 0.95, 0.99).publishPercentileHistogram().register(meterRegistry);
    }

    public void incrementSynthesisTotal() { synthesisTotalCounter.increment(); }
    public void incrementSynthesisSuccess() { synthesisSuccessCounter.increment(); }
    public void incrementSynthesisFailure() { synthesisFailureCounter.increment(); }
    public void recordSynthesisTime(long ms) { synthesisTimer.record(ms, TimeUnit.MILLISECONDS); }
    public Timer.Sample startSynthesisTimer() { return Timer.start(meterRegistry); }
    public void stopSynthesisTimer(Timer.Sample s) { s.stop(synthesisTimer); }
    public double getSynthesisLatencyP95() { return synthesisTimer.percentile(0.95, TimeUnit.MILLISECONDS); }
    public double getSynthesisLatencyP99() { return synthesisTimer.percentile(0.99, TimeUnit.MILLISECONDS); }
    public double getErrorRate() {
        long t = (long) synthesisTotalCounter.count(); long f = (long) synthesisFailureCounter.count();
        return t > 0 ? (double) f / t : 0.0;
    }
    public MeterRegistry getMeterRegistry() { return meterRegistry; }
}
