package com.gogidix.courier.assignmentservice.domain.model;

import java.util.Objects;

public final class AssignmentScoringCriteria {

    private final double distanceWeight;
    private final double driverRatingWeight;
    private final double workloadBalanceWeight;
    private final double urgencyWeight;

    public AssignmentScoringCriteria(double distanceWeight, double driverRatingWeight,
                                     double workloadBalanceWeight, double urgencyWeight) {
        this.distanceWeight = distanceWeight;
        this.driverRatingWeight = driverRatingWeight;
        this.workloadBalanceWeight = workloadBalanceWeight;
        this.urgencyWeight = urgencyWeight;
        validateWeights();
    }

    public static AssignmentScoringCriteria defaultCriteria() {
        return new AssignmentScoringCriteria(0.4, 0.25, 0.2, 0.15);
    }

    private void validateWeights() {
        double sum = distanceWeight + driverRatingWeight + workloadBalanceWeight + urgencyWeight;
        if (Math.abs(sum - 1.0) > 0.001) {
            throw new IllegalArgumentException("Scoring weights must sum to 1.0, got: " + sum);
        }
    }

    public double calculateScore(double distanceScore, double driverRatingScore,
                                  double workloadScore, double urgencyScore) {
        return (distanceWeight * distanceScore)
                + (driverRatingWeight * driverRatingScore)
                + (workloadBalanceWeight * workloadScore)
                + (urgencyWeight * urgencyScore);
    }

    public double getDistanceWeight() { return distanceWeight; }
    public double getDriverRatingWeight() { return driverRatingWeight; }
    public double getWorkloadBalanceWeight() { return workloadBalanceWeight; }
    public double getUrgencyWeight() { return urgencyWeight; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AssignmentScoringCriteria that = (AssignmentScoringCriteria) o;
        return Double.compare(that.distanceWeight, distanceWeight) == 0
                && Double.compare(that.driverRatingWeight, driverRatingWeight) == 0
                && Double.compare(that.workloadBalanceWeight, workloadBalanceWeight) == 0
                && Double.compare(that.urgencyWeight, urgencyWeight) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(distanceWeight, driverRatingWeight, workloadBalanceWeight, urgencyWeight);
    }

    @Override
    public String toString() {
        return "AssignmentScoringCriteria{distance=" + distanceWeight
                + ", rating=" + driverRatingWeight
                + ", workload=" + workloadBalanceWeight
                + ", urgency=" + urgencyWeight + '}';
    }
}
