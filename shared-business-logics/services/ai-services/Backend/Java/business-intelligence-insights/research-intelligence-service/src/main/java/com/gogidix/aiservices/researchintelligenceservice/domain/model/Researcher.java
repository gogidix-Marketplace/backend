package com.gogidix.aiservices.researchintelligenceservice.domain.model;

import java.util.Objects;

public class Researcher {
    private final String researcherId;
    private final String name;
    private final String email;
    private final ResearchRole role;
    private final String affiliation;

    public Researcher(String researcherId, String name, String email, ResearchRole role) {
        this.researcherId = Objects.requireNonNull(researcherId, "researcherId cannot be null");
        this.name = Objects.requireNonNull(name, "name cannot be null");
        this.email = email;
        this.role = Objects.requireNonNull(role, "role cannot be null");
        this.affiliation = "";
    }

    public Researcher(String researcherId, String name, String email, ResearchRole role, String affiliation) {
        this.researcherId = Objects.requireNonNull(researcherId, "researcherId cannot be null");
        this.name = Objects.requireNonNull(name, "name cannot be null");
        this.email = email;
        this.role = Objects.requireNonNull(role, "role cannot be null");
        this.affiliation = affiliation;
    }

    public String getResearcherId() {
        return researcherId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public ResearchRole getRole() {
        return role;
    }

    public String getAffiliation() {
        return affiliation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Researcher that = (Researcher) o;
        return Objects.equals(researcherId, that.researcherId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(researcherId);
    }

    @Override
    public String toString() {
        return "Researcher{" +
                "researcherId='" + researcherId + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", affiliation='" + affiliation + '\'' +
                '}';
    }

    public enum ResearchRole {
        PRINCIPAL_INVESTIGATOR,
        CO_INVESTIGATOR,
        SENIOR_RESEARCHER,
        RESEARCH_ASSOCIATE,
        RESEARCH_ASSISTANT,
        DATA_ANALYST,
        INTERN,
        CONSULTANT
    }
}
