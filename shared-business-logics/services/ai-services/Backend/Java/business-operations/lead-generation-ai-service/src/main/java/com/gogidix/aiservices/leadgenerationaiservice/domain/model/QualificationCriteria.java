package com.gogidix.aiservices.leadgenerationaiservice.domain.model;

import lombok.Getter;

@Getter
public enum QualificationCriteria {
    BUDGET_CONFIRMED("budget_confirmed", "Budget has been confirmed"),
    DECISION_MAKER_IDENTIFIED("decision_maker", "Decision maker has been identified"),
    TIMELINE_ESTABLISHED("timeline", "Timeline has been established"),
    PAIN_POINTS_IDENTIFIED("pain_points", "Pain points have been identified"),
    COMPETITION_ANALYZED("competition", "Competition has been analyzed"),
    TECH_STACK_IDENTIFIED("tech_stack", "Technology stack has been identified"),
    AUTHORITY_CONFIRMED("authority", "Authority to purchase confirmed"),
    NEED_VALIDATED("need_validated", "Business need has been validated"),
    FIT_CONFIRMED("fit_confirmed", "Product fit has been confirmed"),
    BUDGET_ALLOCATED("budget_allocated", "Budget has been allocated");

    private final String value;
    private final String description;

    QualificationCriteria(String value, String description) {
        this.value = value;
        this.description = description;
    }

    @Override
    public String toString() {
        return value;
    }

    public static QualificationCriteria fromString(String value) {
        for (QualificationCriteria criteria : QualificationCriteria.values()) {
            if (criteria.value.equalsIgnoreCase(value)) {
                return criteria;
            }
        }
        throw new IllegalArgumentException("Unknown qualification criteria: " + value);
    }
}
