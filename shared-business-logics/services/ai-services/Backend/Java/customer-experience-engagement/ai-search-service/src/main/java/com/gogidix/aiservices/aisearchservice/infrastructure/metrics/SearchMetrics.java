package com.gogidix.aiservices.aisearchservice.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Micrometer metrics for Search Service.
 * Tracks key performance indicators for SLO monitoring.
 */
@Component
public class SearchMetrics {

    private final MeterRegistry meterRegistry;

    // Counters
    private final Counter searchTotalCounter;
    private final Counter searchSuccessCounter;
    private final Counter searchFailureCounter;
    private final Counter suggestionsTotalCounter;
    private final Counter indexDocumentCounter;

    // Timers
    private final Timer searchTimer;
    private final Timer searchEngineTimer;
    private final Timer indexTimer;

    public SearchMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        // Initialize counters
        this.searchTotalCounter = Counter.builder("search.total")
                .description("Total number of search requests")
                .tag("service", "ai-search")
                .register(meterRegistry);

        this.searchSuccessCounter = Counter.builder("search.success")
                .description("Number of successful search requests")
                .tag("service", "ai-search")
                .register(meterRegistry);

        this.searchFailureCounter = Counter.builder("search.failure")
                .description("Number of failed search requests")
                .tag("service", "ai-search")
                .register(meterRegistry);

        this.suggestionsTotalCounter = Counter.builder("search.suggestions.total")
                .description("Number of suggestion requests")
                .tag("service", "ai-search")
                .register(meterRegistry);

        this.indexDocumentCounter = Counter.builder("search.index.total")
                .description("Number of documents indexed")
                .tag("service", "ai-search")
                .register(meterRegistry);

        // Initialize timers
        this.searchTimer = Timer.builder("search.duration")
                .description("Search processing time")
                .tag("service", "ai-search")
                .publishPercentiles(0.5, 0.95, 0.99)
                .publishPercentileHistogram()
                .register(meterRegistry);

        this.searchEngineTimer = Timer.builder("search.engine.duration")
                .description("Search engine query time")
                .tag("service", "ai-search")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.indexTimer = Timer.builder("search.index.duration")
                .description("Document indexing time")
                .tag("service", "ai-search")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);
    }

    // Counter methods
    public void incrementSearchTotal() { searchTotalCounter.increment(); }
    public void incrementSearchSuccess() { searchSuccessCounter.increment(); }
    public void incrementSearchFailure() { searchFailureCounter.increment(); }
    public void incrementSuggestionsTotal() { suggestionsTotalCounter.increment(); }
    public void incrementIndexDocument() { indexDocumentCounter.increment(); }

    // Timer methods
    public Timer.Sample startSearchTimer() { return Timer.start(meterRegistry); }
    public void stopSearchTimer(Timer.Sample sample) { sample.stop(searchTimer); }

    public Timer.Sample startSearchEngineTimer() { return Timer.start(meterRegistry); }
    public void stopSearchEngineTimer(Timer.Sample sample) { sample.stop(searchEngineTimer); }

    public Timer.Sample startIndexTimer() { return Timer.start(meterRegistry); }
    public void stopIndexTimer(Timer.Sample sample) { sample.stop(indexTimer); }

    // SLO compliance methods
    public double getSearchLatencyP95() { return searchTimer.percentile(0.95, TimeUnit.MILLISECONDS); }
    public double getSearchLatencyP99() { return searchTimer.percentile(0.99, TimeUnit.MILLISECONDS); }
    public double getSearchEngineLatencyP95() { return searchEngineTimer.percentile(0.95, TimeUnit.MILLISECONDS); }
    public double getErrorRate() {
        long total = (long) searchTotalCounter.count();
        long failures = (long) searchFailureCounter.count();
        return total > 0 ? (double) failures / total : 0.0;
    }

    public MeterRegistry getMeterRegistry() { return meterRegistry; }
}
