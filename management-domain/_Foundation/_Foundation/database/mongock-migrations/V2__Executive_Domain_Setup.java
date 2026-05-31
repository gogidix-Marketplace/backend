package com.gogidix.management.database.mongock;

import com.mongodb.client.MongoDatabase;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * V2 - Executive Domain Collections
 *
 * <p>Creates collections and indexes for Executive-Domain.</p>
 *
 * Collections:
 * - approvals (executive approvals)
 * - alerts (executive alerts)
 * - kpi_metrics (KPI data)
 * - audit_logs (shared audit trail)
 */
@ChangeUnit(id = "V2__executive-domain-setup", order = "002", author = "system")
public class V2__Executive_Domain_Setup {

    private static final Logger log = LoggerFactory.getLogger(V2__Executive_Domain_Setup.class);

    @Execution
    public void migrationChangeUnit(MongoDatabase database) {
        log.info("Starting V2__Executive_Domain_Setup migration");

        // Approvals collection
        var approvals = database.getCollection("approvals");
        approvals.createIndex(new Document("tenantId", 1).append("_id", 1));
        approvals.createIndex(new Document("tenantId", 1).append("status", 1));
        approvals.createIndex(new Document("tenantId", 1).append("approver", 1).append("createdAt", -1));
        log.info("✓ Created approvals collection indexes");

        // Alerts collection
        var alerts = database.getCollection("alerts");
        alerts.createIndex(new Document("tenantId", 1).append("_id", 1));
        alerts.createIndex(new Document("tenantId", 1).append("severity", 1).append("createdAt", -1));
        alerts.createIndex(new Document("tenantId", 1).append("status", 1));
        log.info("✓ Created alerts collection indexes");

        // KPI Metrics collection
        var kpiMetrics = database.getCollection("kpi_metrics");
        kpiMetrics.createIndex(new Document("tenantId", 1).append("domain", 1).append("period", -1));
        kpiMetrics.createIndex(new Document("tenantId", 1).append("metricName", 1).append("timestamp", -1));
        log.info("✓ Created kpi_metrics collection indexes");

        // Audit Logs collection (shared)
        var auditLogs = database.getCollection("audit_logs");
        auditLogs.createIndex(new Document("tenantId", 1).append("timestamp", -1));
        auditLogs.createIndex(new Document("tenantId", 1).append("userId", 1).append("timestamp", -1));
        auditLogs.createIndex(new Document("tenantId", 1).append("entity", 1).append("entityId", 1).append("timestamp", -1));

        // TTL index for audit logs (1 year retention)
        auditLogs.createIndex(
            new Document("timestamp", 1),
            new org.bson.conversions.BsonDocument()
                .append("name", "ttl_idx")
                .append("expireAfterSeconds", 31536000L)  // 365 days
                .append("background", true)
        );
        log.info("✓ Created audit_logs collection indexes with TTL");

        log.info("V2__Executive_Domain_Setup migration completed successfully");
    }

    @RollbackExecution
    public void rollback(MongoDatabase database) {
        log.info("Rolling back V2__Executive_Domain_Setup migration");

        // Drop collections
        database.getCollection("approvals").drop();
        database.getCollection("alerts").drop();
        database.getCollection("kpi_metrics").drop();
        database.getCollection("audit_logs").drop();

        log.info("V2__Executive_Domain_Setup rollback completed");
    }
}
