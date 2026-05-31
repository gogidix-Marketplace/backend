package com.gogidix.courier.etaservice.domain.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

/**
 * Domain Entity representing traffic factors for a specific area and time.
 * Used to calculate realistic ETAs based on traffic conditions.
 */
@Document(collection = "traffic_factors")
@CompoundIndex(name = "idx_area_time", def = "{'areaCode': 1, 'hourOfDay': 1, 'dayOfWeek': 1}")
public class TrafficFactor {

    @Id
    private String id;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

    @Indexed
    @Field("area_code")
    private String areaCode;

    @Field("area_name")
    private String areaName;

    @Field("latitude")
    private Double latitude;

    @Field("longitude")
    private Double longitude;

    @Field("radius_km")
    private Double radiusKm;

    @Field("day_of_week")
    private Integer dayOfWeek; // 1-7 (Monday-Sunday)

    @Field("hour_of_day")
    private Integer hourOfDay; // 0-23

    @Field("traffic_level")
    private String trafficLevel;

    @Field("multiplier")
    private Double multiplier;

    @Field("average_speed_kmh")
    private Double averageSpeedKmh;

    @Field("sample_count")
    private Integer sampleCount;

    @Field("confidence")
    private Double confidence;

    @Field("is_peak_hour")
    private Boolean isPeakHour;

    @Field("seasonal_factor")
    private Double seasonalFactor;

    @Field("weather_factor")
    private Double weatherFactor;

    @Field("created_at")
    private Instant createdAt;

    @Field("updated_at")
    private Instant updatedAt;

    @Field("last_observed_at")
    private Instant lastObservedAt;

    /**
     * Default constructor for persistence.
     */
    protected TrafficFactor() {
    }

    /**
     * Create a new traffic factor.
     *
     * @param tenantId    the tenant ID
     * @param areaCode    the area code
     * @param latitude    the latitude
     * @param longitude   the longitude
     * @param dayOfWeek   the day of week (1-7)
     * @param hourOfDay   the hour of day (0-23)
     * @param trafficLevel the traffic level
     * @param multiplier  the traffic multiplier
     */
    public TrafficFactor(String tenantId, String areaCode,
                         Double latitude, Double longitude,
                         Integer dayOfWeek, Integer hourOfDay,
                         String trafficLevel, Double multiplier) {
        this.id = java.util.UUID.randomUUID().toString();
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId is required");
        this.areaCode = Objects.requireNonNull(areaCode, "areaCode is required");
        this.latitude = latitude;
        this.longitude = longitude;
        this.dayOfWeek = Objects.requireNonNull(dayOfWeek, "dayOfWeek is required");
        this.hourOfDay = Objects.requireNonNull(hourOfDay, "hourOfDay is required");
        this.trafficLevel = Objects.requireNonNull(trafficLevel, "trafficLevel is required");
        this.multiplier = Objects.requireNonNull(multiplier, "multiplier is required");
        this.sampleCount = 1;
        this.confidence = 0.5;
        this.isPeakHour = false;
        this.seasonalFactor = 1.0;
        this.weatherFactor = 1.0;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.lastObservedAt = Instant.now();

        validateDayOfWeek(dayOfWeek);
        validateHourOfDay(hourOfDay);
    }

    // Domain Logic Methods

    /**
     * Update traffic data with a new observation.
     *
     * @param newMultiplier   the new multiplier
     * @param newSpeed        the new average speed
     * @param isPeakHour      whether it's peak hour
     */
    public void addObservation(Double newMultiplier, Double newSpeed, Boolean isPeakHour) {
        // Update multiplier using weighted average
        double currentWeight = Math.min(sampleCount / 100.0, 0.9); // Max 90% weight to existing
        double newWeight = 1.0 - currentWeight;

        this.multiplier = (this.multiplier * currentWeight) + (newMultiplier * newWeight);

        // Update average speed
        if (newSpeed != null) {
            if (this.averageSpeedKmh == null) {
                this.averageSpeedKmh = newSpeed;
            } else {
                this.averageSpeedKmh = (this.averageSpeedKmh * currentWeight) + (newSpeed * newWeight);
            }
        }

        // Update peak hour status
        if (isPeakHour != null && isPeakHour) {
            this.isPeakHour = true;
        }

        // Increment sample count
        this.sampleCount = this.sampleCount != null ? this.sampleCount + 1 : 1;

        // Update confidence based on sample count
        this.confidence = Math.min(0.5 + (sampleCount / 200.0), 1.0);

        // Update traffic level based on multiplier
        updateTrafficLevel();

        this.lastObservedAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    /**
     * Apply weather factor.
     *
     * @param weatherFactor the weather factor (e.g., 1.2 for rain)
     */
    public void applyWeatherFactor(Double weatherFactor) {
        this.weatherFactor = Objects.requireNonNull(weatherFactor, "weatherFactor is required");
        this.updatedAt = Instant.now();
    }

    /**
     * Apply seasonal factor.
     *
     * @param seasonalFactor the seasonal factor (e.g., 1.1 for holiday season)
     */
    public void applySeasonalFactor(Double seasonalFactor) {
        this.seasonalFactor = Objects.requireNonNull(seasonalFactor, "seasonalFactor is required");
        this.updatedAt = Instant.now();
    }

    /**
     * Get the effective multiplier including seasonal and weather factors.
     *
     * @return the effective multiplier
     */
    public Double getEffectiveMultiplier() {
        double effective = multiplier;
        if (seasonalFactor != null) {
            effective *= seasonalFactor;
        }
        if (weatherFactor != null) {
            effective *= weatherFactor;
        }
        return effective;
    }

    /**
     * Check if this traffic factor matches the given time.
     *
     * @param timestamp the timestamp to check
     * @return true if matches
     */
    public boolean matchesTime(Instant timestamp) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(
                timestamp, ZoneId.systemDefault());
        return dateTime.getDayOfWeek().getValue() == this.dayOfWeek
                && dateTime.getHour() == this.hourOfDay;
    }

    /**
     * Check if this traffic factor is for peak hours.
     *
     * @return true if peak hour
     */
    public boolean isPeakHour() {
        return Boolean.TRUE.equals(isPeakHour);
    }

    /**
     * Check if the traffic factor has sufficient data.
     *
     * @param minSamples minimum sample count
     * @return true if has sufficient data
     */
    public boolean hasSufficientData(int minSamples) {
        return sampleCount != null && sampleCount >= minSamples;
    }

    private void updateTrafficLevel() {
        if (multiplier == null) {
            this.trafficLevel = "LOW";
            return;
        }

        if (multiplier <= 1.15) {
            this.trafficLevel = "LOW";
        } else if (multiplier <= 1.5) {
            this.trafficLevel = "MEDIUM";
        } else if (multiplier <= 2.0) {
            this.trafficLevel = "HIGH";
        } else {
            this.trafficLevel = "SEVERE";
        }
    }

    private void validateDayOfWeek(int dayOfWeek) {
        if (dayOfWeek < 1 || dayOfWeek > 7) {
            throw new IllegalArgumentException("dayOfWeek must be between 1 and 7: " + dayOfWeek);
        }
    }

    private void validateHourOfDay(int hourOfDay) {
        if (hourOfDay < 0 || hourOfDay > 23) {
            throw new IllegalArgumentException("hourOfDay must be between 0 and 23: " + hourOfDay);
        }
    }

    /**
     * Validate the traffic factor.
     *
     * @throws IllegalArgumentException if validation fails
     */
    public void validate() {
        if (tenantId == null || tenantId.isBlank()) {
            throw new IllegalArgumentException("tenantId is required");
        }
        if (areaCode == null || areaCode.isBlank()) {
            throw new IllegalArgumentException("areaCode is required");
        }
        if (multiplier == null || multiplier < 0.5 || multiplier > 5.0) {
            throw new IllegalArgumentException("multiplier must be between 0.5 and 5.0: " + multiplier);
        }
        validateDayOfWeek(dayOfWeek);
        validateHourOfDay(hourOfDay);
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getTenantId() {
        return tenantId;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public String getAreaName() {
        return areaName;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getRadiusKm() {
        return radiusKm;
    }

    public Integer getDayOfWeek() {
        return dayOfWeek;
    }

    public Integer getHourOfDay() {
        return hourOfDay;
    }

    public String getTrafficLevel() {
        return trafficLevel;
    }

    public Double getMultiplier() {
        return multiplier;
    }

    public Double getAverageSpeedKmh() {
        return averageSpeedKmh;
    }

    public Integer getSampleCount() {
        return sampleCount;
    }

    public Double getConfidence() {
        return confidence;
    }

    public Boolean getIsPeakHour() {
        return isPeakHour;
    }

    public Double getSeasonalFactor() {
        return seasonalFactor;
    }

    public Double getWeatherFactor() {
        return weatherFactor;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getLastObservedAt() {
        return lastObservedAt;
    }

    // Setters for persistence
    protected void setId(String id) {
        this.id = id;
    }

    protected void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    protected void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    protected void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    protected void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    protected void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    protected void setRadiusKm(Double radiusKm) {
        this.radiusKm = radiusKm;
    }

    protected void setDayOfWeek(Integer dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    protected void setHourOfDay(Integer hourOfDay) {
        this.hourOfDay = hourOfDay;
    }

    protected void setTrafficLevel(String trafficLevel) {
        this.trafficLevel = trafficLevel;
    }

    protected void setMultiplier(Double multiplier) {
        this.multiplier = multiplier;
    }

    protected void setAverageSpeedKmh(Double averageSpeedKmh) {
        this.averageSpeedKmh = averageSpeedKmh;
    }

    protected void setSampleCount(Integer sampleCount) {
        this.sampleCount = sampleCount;
    }

    protected void setConfidence(Double confidence) {
        this.confidence = confidence;
    }

    protected void setIsPeakHour(Boolean isPeakHour) {
        this.isPeakHour = isPeakHour;
    }

    protected void setSeasonalFactor(Double seasonalFactor) {
        this.seasonalFactor = seasonalFactor;
    }

    protected void setWeatherFactor(Double weatherFactor) {
        this.weatherFactor = weatherFactor;
    }

    protected void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    protected void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    protected void setLastObservedAt(Instant lastObservedAt) {
        this.lastObservedAt = lastObservedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrafficFactor that = (TrafficFactor) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "TrafficFactor{" +
                "id='" + id + '\'' +
                ", areaCode='" + areaCode + '\'' +
                ", dayOfWeek=" + dayOfWeek +
                ", hourOfDay=" + hourOfDay +
                ", trafficLevel='" + trafficLevel + '\'' +
                ", multiplier=" + multiplier +
                '}';
    }

    /**
     * Traffic level enum values.
     */
    public static final class TrafficLevels {
        public static final String LOW = "LOW";
        public static final String MEDIUM = "MEDIUM";
        public static final String HIGH = "HIGH";
        public static final String SEVERE = "SEVERE";
    }
}
