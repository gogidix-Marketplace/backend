package com.gogidix.aiservices.timeseriesforecasting.application.dto.request;

import com.gogidix.aiservices.timeseriesforecasting.domain.model.Frequency;

import java.util.List;
import java.util.Objects;

public class CreateForecastRequest {
    private List<TimeSeriesPoint> timeSeriesData;
    private Integer forecastHorizon;
    private Frequency frequency;
    private Boolean includeSeasonality;

    public CreateForecastRequest() {
    }

    public List<TimeSeriesPoint> getTimeSeriesData() {
        return timeSeriesData;
    }

    public void setTimeSeriesData(List<TimeSeriesPoint> timeSeriesData) {
        this.timeSeriesData = timeSeriesData;
    }

    public Integer getForecastHorizon() {
        return forecastHorizon;
    }

    public void setForecastHorizon(Integer forecastHorizon) {
        this.forecastHorizon = forecastHorizon;
    }

    public Frequency getFrequency() {
        return frequency;
    }

    public void setFrequency(Frequency frequency) {
        this.frequency = frequency;
    }

    public Boolean getIncludeSeasonality() {
        return includeSeasonality;
    }

    public void setIncludeSeasonality(Boolean includeSeasonality) {
        this.includeSeasonality = includeSeasonality;
    }

    public static class TimeSeriesPoint {
        private String timestamp;
        private Double value;

        public TimeSeriesPoint() {
        }

        public TimeSeriesPoint(String timestamp, Double value) {
            this.timestamp = timestamp;
            this.value = value;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public Double getValue() {
            return value;
        }

        public void setValue(Double value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TimeSeriesPoint that = (TimeSeriesPoint) o;
            return Objects.equals(timestamp, that.timestamp) &&
                   Objects.equals(value, that.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(timestamp, value);
        }
    }
}
