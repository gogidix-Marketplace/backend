package com.gogidix.aiservices.aimarketbasketanalysisservice.domain.policy;

import com.gogidix.aiservices.aimarketbasketanalysisservice.shared.exception.ConflictException;

import java.util.List;

/**
 * Business rule policy: Basket names must be unique within a tenant.
 * Enforces uniqueness to prevent confusion and conflicts.
 */
public class BasketNameUniquePolicy {

    /**
     * Validate that the segment name is unique within the tenant.
     *
     * @param existingNames the list of existing segment names
     * @param newName       the proposed new name
     * @param tenantId      the tenant ID
     * @throws ConflictException if name already exists
     */
    public void validate(List<String> existingNames, String newName, String tenantId) {
        if (existingNames == null) {
            return;
        }

        boolean nameExists = existingNames.stream()
                .anyMatch(existingName -> existingName.equalsIgnoreCase(newName));

        if (nameExists) {
            throw new ConflictException(
                    "MarketBasket",
                    newName,
                    "SEGMENT_NAME_NOT_UNIQUE"
            );
        }
    }

    /**
     * Check if a segment name is unique.
     *
     * @param existingNames the list of existing segment names
     * @param nameToCheck  the name to check
     * @return true if unique, false otherwise
     */
    public boolean isUnique(List<String> existingNames, String nameToCheck) {
        if (existingNames == null || nameToCheck == null) {
            return false;
        }

        return existingNames.stream()
                .noneMatch(existingName -> existingName.equalsIgnoreCase(nameToCheck));
    }

    /**
     * Generate a unique segment name based on a base name.
     *
     * @param baseName      the base name
     * @param existingNames the list of existing names
     * @return a unique name (appends number if needed)
     */
    public String generateUniqueName(String baseName, List<String> existingNames) {
        if (isUnique(existingNames, baseName)) {
            return baseName;
        }

        int counter = 1;
        String newName;
        do {
            newName = baseName + "_" + counter;
            counter++;
        } while (!isUnique(existingNames, newName));

        return newName;
    }
}
