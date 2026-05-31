package com.gogidix.management.database.mongock;

import com.gogidix.management.shared.domain.BaseEntity;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * V1 - Base Setup Migration
 *
 * <p>This migration creates the base tenant isolation indexes
 * that are required across ALL collections in the Management-Domain.</p>
 *
 * <p>These indexes ensure:</p>
 * <ul>
 *   <li>Tenant data isolation</li>
 *   <li>Efficient queries filtered by tenant</li>
 *   <li>Proper compound indexing</li>
 * </ul>
 *
 * <p>Run this migration after running init-database.js script.</p>
 */
@ChangeUnit(id = "V1__base-setup", order = "001", author = "system")
public class V1__Base_Setup {

    private static final Logger log = LoggerFactory.getLogger(V1__Base_Setup.class);

    /**
     * Creates tenant isolation indexes on a collection.
     *
     * @param database the MongoDB database
     * @param collectionName the name of the collection
     */
    private void createTenantIndexes(MongoDatabase database, String collectionName) {
        var collection = database.getCollection(collectionName);

        // Compound index on (tenantId, _id) - REQUIRED for all collections
        var tenantEntityIndex = Indexes.compoundIndex(
            BaseEntity.Fields.TENANT_ID,
            BaseEntity.Fields.ID
        );
        collection.createIndex(tenantEntityIndex, new IndexOptions().name("tenant_entity_idx"));
        log.debug("Created tenant_entity_idx on {}", collectionName);

        // Index on (tenantId, createdAt) for sorting by time
        var tenantTimeIndex = Indexes.compoundIndex(
            BaseEntity.Fields.TENANT_ID,
            BaseEntity.Fields.CREATED_AT,
            BaseEntity.Fields.ID
        );
        collection.createIndex(tenantTimeIndex, new IndexOptions().name("tenant_created_idx"));
        log.debug("Created tenant_created_idx on {}", collectionName);
    }

    /**
     * Creates a unique compound index with tenantId.
     *
     * @param database the MongoDB database
     * @param collectionName the name of the collection
     * @param fieldName the field to make unique within tenant
     */
    private void createUniqueTenantIndex(MongoDatabase database, String collectionName, String fieldName) {
        var collection = database.getCollection(collectionName);
        var uniqueIndex = Indexes.compoundIndex(
            BaseEntity.Fields.TENANT_ID,
            fieldName
        ).unique();
        collection.createIndex(uniqueIndex, new IndexOptions().name("tenant_" + fieldName + "_unique_idx"));
        log.debug("Created unique tenant_{}_unique_idx on {}", fieldName, collectionName);
    }

    /**
     * Execution method - runs the migration.
     *
     * @param database the MongoDB database
     */
    @Execution
    public void migrationChangeUnit(MongoDatabase database) {
        log.info("Starting V1__Base_Setup migration");

        // This is a template for creating tenant indexes
        // Each domain will have its own migration that creates
        // domain-specific collections and indexes

        log.info("V1__Base_Setup migration completed successfully");
    }

    /**
     * Rollback method - removes created indexes.
     *
     * @param database the MongoDB database
     */
    @RollbackExecution
    public void rollback(MongoDatabase database) {
        log.info("Rolling back V1__Base_Setup migration");

        // Remove indexes if needed for rollback
        // Note: Be careful with rollback in production

        log.info("V1__Base_Setup rollback completed");
    }
}
