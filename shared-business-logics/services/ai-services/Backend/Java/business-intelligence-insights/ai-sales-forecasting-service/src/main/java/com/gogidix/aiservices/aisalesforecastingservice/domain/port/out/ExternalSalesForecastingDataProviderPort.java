package com.gogidix.aiservices.aisalesforecastingservice.domain.port.out;

import com.gogidix.aiservices.aisalesforecastingservice.domain.model.ForecastModel;

import java.util.List;

/**
 * Output port for retrieving customer data from external systems.
 * Abstracts the customer data provider from the domain.
 */
public interface ExternalSalesForecastingDataProviderPort {

    /**
     * Get customer profiles by IDs.
     *
     * @param customerIds the customer IDs to retrieve
     * @param tenantId    the tenant ID
     * @return list of customer profiles
     */
    List<ForecastModel> getForecastModels(List<String> customerIds, String tenantId);

    /**
     * Get all customer IDs for a tenant.
     *
     * @param tenantId the tenant ID
     * @return list of all customer IDs
     */
    List<String> getAllForecastModelIds(String tenantId);

    /**
     * Find customers matching specific criteria.
     *
     * @param tenantId the tenant ID
     * @param criteria the search criteria
     * @param limit    the maximum number of results
     * @return list of matching customer IDs
     */
    List<String> findForecastModelsByCriteria(String tenantId, Object criteria, int limit);

    /**
     * Get total customer count for a tenant.
     *
     * @param tenantId the tenant ID
     * @return the total customer count
     */
    Long getTotalForecastModelCount(String tenantId);
}
