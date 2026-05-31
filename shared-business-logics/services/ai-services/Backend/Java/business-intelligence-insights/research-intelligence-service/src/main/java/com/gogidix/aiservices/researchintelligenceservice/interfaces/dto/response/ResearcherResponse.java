package com.gogidix.aiservices.researchintelligenceservice.interfaces.dto.response;

import com.gogidix.aiservices.researchintelligenceservice.domain.model.Researcher;

public record ResearcherResponse(
        String researcherId,
        String name,
        String email,
        Researcher.ResearchRole role,
        String affiliation
) {
    public static ResearcherResponse from(Researcher researcher) {
        return new ResearcherResponse(
                researcher.getResearcherId(),
                researcher.getName(),
                researcher.getEmail(),
                researcher.getRole(),
                researcher.getAffiliation()
        );
    }
}
