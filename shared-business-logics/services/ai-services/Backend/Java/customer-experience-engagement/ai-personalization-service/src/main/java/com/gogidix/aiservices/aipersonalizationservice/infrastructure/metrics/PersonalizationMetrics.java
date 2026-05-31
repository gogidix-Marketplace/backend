package com.gogidix.aiservices.aipersonalizationservice.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class PersonalizationMetrics {

    private final MeterRegistry meterRegistry;

    private final Counter personalizationTotalCounter;
    private final Counter personalizationSuccessCounter;
    private final Counter personalizationFailureCounter;
    private final Counter recommendationsGeneratedCounter;
    private final Counter userProfilesMatchedCounter;

    private final Timer personalizationTimer;
    private final Timer profileMatchingTimer;
    private final Timer recommendationTimer;

    public PersonalizationMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        this.personalizationTotalCounter = Counter.builder("ai.personalization.total")
                .description("Total number of personalization requests")
                .tag("service", "ai-personalization")
                .register(meterRegistry);

        this.personalizationSuccessCounter = Counter.builder("ai.personalization.success")
                .description("Number of successful personalization requests")
                .tag("service", "ai-personalization")
                .register(meterRegistry);

        this.personalizationFailureCounter = Counter.builder("ai.personalization.failure")
                .description("Number of failed personalization requests")
                .tag("service", "ai-personalization")
                .register(meterRegistry);

        this.recommendationsGeneratedCounter = Counter.builder("ai.personalization.recommendations")
                .description("Number of recommendations generated")
                .tag("service", "ai-personalization")
                .register(meterRegistry);

        this.userProfilesMatchedCounter = Counter.builder("ai.personalization.profiles.matched")
                .description("Number of user profiles matched")
                .tag("service", "ai-personalization")
                .register(meterRegistry);

        this.personalizationTimer = Timer.builder("ai.personalization.duration")
                .description("Personalization processing time")
                .tag("service", "ai-personalization")
                .publishPercentiles(0.5, 0.95, 0.99)
                .publishPercentileHistogram()
                .register(meterRegistry);

        this.profileMatchingTimer = Timer.builder("ai.personalization.profile.matching.duration")
                .description("Profile matching time")
                .tag("service", "ai-personalization")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.recommendationTimer = Timer.builder("ai.personalization.recommendation.duration")
                .description("Recommendation generation time")
                .tag("service", "ai-personalization")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);
    }

    public void incrementPersonalizationTotal() {
        personalizationTotalCounter.increment();
    }

    public void incrementPersonalizationSuccess() {
        personalizationSuccessCounter.increment();
    }

    public void incrementPersonalizationFailure() {
        personalizationFailureCounter.increment();
    }

    public void incrementRecommendationsGenerated() {
        recommendationsGeneratedCounter.increment();
    }

    public void incrementUserProfilesMatched() {
        userProfilesMatchedCounter.increment();
    }

    public void recordPersonalizationTime(long durationMs) {
        personalizationTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public Timer.Sample startPersonalizationTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopPersonalizationTimer(Timer.Sample sample) {
        sample.stop(personalizationTimer);
    }

    public Timer.Sample startProfileMatchingTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopProfileMatchingTimer(Timer.Sample sample) {
        sample.stop(profileMatchingTimer);
    }

    public Timer.Sample startRecommendationTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopRecommendationTimer(Timer.Sample sample) {
        sample.stop(recommendationTimer);
    }

    public double getPersonalizationLatencyP95() {
        return personalizationTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getPersonalizationLatencyP99() {
        return personalizationTimer.percentile(0.99, TimeUnit.MILLISECONDS);
    }

    public double getProfileMatchingLatencyP95() {
        return profileMatchingTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getErrorRate() {
        long total = (long) personalizationTotalCounter.count();
        long failures = (long) personalizationFailureCounter.count();
        return total > 0 ? (double) failures / total : 0.0;
    }

    public MeterRegistry getMeterRegistry() {
        return meterRegistry;
    }
}
