package com.gogidix.aiservices.aichatbotservice.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class ChatbotMetrics {

    private final MeterRegistry meterRegistry;

    private final Counter chatbotTotalCounter;
    private final Counter chatbotSuccessCounter;
    private final Counter chatbotFailureCounter;
    private final Counter conversationsHandledCounter;

    private final Timer responseTimer;
    private final Timer nluProcessingTimer;

    public ChatbotMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        this.chatbotTotalCounter = Counter.builder("ai.chatbot.total")
                .description("Total number of chatbot requests")
                .tag("service", "ai-chatbot")
                .register(meterRegistry);

        this.chatbotSuccessCounter = Counter.builder("ai.chatbot.success")
                .description("Number of successful chatbot requests")
                .tag("service", "ai-chatbot")
                .register(meterRegistry);

        this.chatbotFailureCounter = Counter.builder("ai.chatbot.failure")
                .description("Number of failed chatbot requests")
                .tag("service", "ai-chatbot")
                .register(meterRegistry);

        this.conversationsHandledCounter = Counter.builder("ai.chatbot.conversations.handled")
                .description("Number of conversations handled")
                .tag("service", "ai-chatbot")
                .register(meterRegistry);

        this.responseTimer = Timer.builder("ai.chatbot.response.duration")
                .description("Chatbot response processing time")
                .tag("service", "ai-chatbot")
                .publishPercentiles(0.5, 0.95, 0.99)
                .publishPercentileHistogram()
                .register(meterRegistry);

        this.nluProcessingTimer = Timer.builder("ai.chatbot.nlu.processing.duration")
                .description("NLU processing time")
                .tag("service", "ai-chatbot")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);
    }

    public void incrementChatbotTotal() {
        chatbotTotalCounter.increment();
    }

    public void incrementChatbotSuccess() {
        chatbotSuccessCounter.increment();
    }

    public void incrementChatbotFailure() {
        chatbotFailureCounter.increment();
    }

    public void incrementConversationsHandled() {
        conversationsHandledCounter.increment();
    }

    public void recordResponseTime(long durationMs) {
        responseTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public Timer.Sample startResponseTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopResponseTimer(Timer.Sample sample) {
        sample.stop(responseTimer);
    }

    public double getResponseLatencyP95() {
        return responseTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getResponseLatencyP99() {
        return responseTimer.percentile(0.99, TimeUnit.MILLISECONDS);
    }

    public double getNluProcessingLatencyP95() {
        return nluProcessingTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getErrorRate() {
        long total = (long) chatbotTotalCounter.count();
        long failures = (long) chatbotFailureCounter.count();
        return total > 0 ? (double) failures / total : 0.0;
    }

    public MeterRegistry getMeterRegistry() {
        return meterRegistry;
    }
}
