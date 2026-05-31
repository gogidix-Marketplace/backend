package com.gogidix.aiservices.aisearchservice.infrastructure.metrics;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;

class SearchMetricsGenTest {
    private SearchMetrics metrics;

    @BeforeEach
    void setup() { metrics = new SearchMetrics(new SimpleMeterRegistry()); }

    @Test
    void incrementSearchTotal() { metrics.incrementSearchTotal(); }

    @Test
    void incrementSearchSuccess() { metrics.incrementSearchSuccess(); }

    @Test
    void incrementSearchFailure() { metrics.incrementSearchFailure(); }

    @Test
    void incrementSuggestionsTotal() { metrics.incrementSuggestionsTotal(); }

    @Test
    void incrementIndexDocument() { metrics.incrementIndexDocument(); }

    @Test
    void stopSearchTimer() { var s = metrics.startSearchTimer(); metrics.stopSearchTimer(s); }

    @Test
    void stopSearchEngineTimer() { var s = metrics.startSearchEngineTimer(); metrics.stopSearchEngineTimer(s); }

    @Test
    void stopIndexTimer() { var s = metrics.startIndexTimer(); metrics.stopIndexTimer(s); }

    @Test
    void getSearchLatencyP95() { assertThat(metrics.getSearchLatencyP95()).isNotNull(); }

    @Test
    void getSearchEngineLatencyP95() { assertThat(metrics.getSearchEngineLatencyP95()).isNotNull(); }

    @Test
    void getSearchLatencyP99() { assertThat(metrics.getSearchLatencyP99()).isNotNull(); }

    @Test
    void getErrorRate() { assertThat(metrics.getErrorRate()).isNotNull(); }

    @Test
    void getMeterRegistry() { assertThat(metrics.getMeterRegistry()).isNotNull(); }

}
