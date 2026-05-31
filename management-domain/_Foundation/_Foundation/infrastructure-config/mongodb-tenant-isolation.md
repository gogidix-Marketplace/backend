# =============================================================================
# MongoDB Tenant Isolation Configuration
# Management-Domain - Gogidix Ecosystem
# =============================================================================

## -----------------------------------------------------------------------------
# DATABASE STRUCTURE
# -----------------------------------------------------------------------------

# Each tenant has isolated data in separate collections with tenant_id field
# All queries MUST filter by tenant_id

# Collection Naming Convention: {entity_name}
# Example collections:
# - employees
# - customers
# - leads
# - deals
# - tickets
# - approvals

## -----------------------------------------------------------------------------
# COMPOUND INDEXES (CRITICAL FOR TENANT ISOLATION)
# -----------------------------------------------------------------------------

# ALL collections MUST have a compound index on (tenant_id, _id)
# This ensures efficient queries and proper tenant isolation

# Example indexes:
db.employees.createIndex(
  { "tenant_id": 1, "_id": 1 },
  { name: "tenant_entity_idx" }
)

# Additional common indexes:
db.employees.createIndex(
  { "tenant_id": 1, "email": 1 },
  { unique: true, name: "tenant_email_idx" }
)

db.employees.createIndex(
  { "tenant_id": 1, "created_at": -1 },
  { name: "tenant_created_idx" }
)

## -----------------------------------------------------------------------------
# TENANT ISOLATION RULES
# -----------------------------------------------------------------------------

# 1. ALL writes MUST include tenant_id
# 2. ALL reads MUST filter by tenant_id
# 3. tenant_id is IMMUTABLE after creation
# 4. Cross-tenant operations are FORBIDDEN
# 5. Aggregation pipelines MUST start with $match on tenant_id

## -----------------------------------------------------------------------------
# COLLECTION VALIDATION
# -----------------------------------------------------------------------------

# MongoDB Schema Validation Example:

db.createCollection("employees", {
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["tenant_id", "first_name", "last_name", "email"],
      properties: {
        tenant_id: {
          bsonType: "string",
          description: "Tenant ID is required and must be a string"
        },
        first_name: {
          bsonType: "string",
          description: "First name is required"
        },
        last_name: {
          bsonType: "string",
          description: "Last name is required"
        },
        email: {
          bsonType: "string",
          description: "Email is required"
        },
        created_at: {
          bsonType: "date",
          description: "Creation timestamp"
        },
        updated_at: {
          bsonType: "date",
          description: "Last update timestamp"
        }
      }
    }
  },
  validationLevel: "moderate",
  validationAction: "error"
})

## -----------------------------------------------------------------------------
# DATABASE PER TENANT STRATEGY (OPTIONAL)
# -----------------------------------------------------------------------------

# For high-security requirements, use separate database per tenant:
# - management_executive
# - management_hr_tenant_1
# - management_hr_tenant_2
# - management_sales_ireland
# - management_sales_nigeria

# Benefits:
# - Complete physical isolation
# - Easier backup/restore per tenant
# - Better performance for large tenants

# Drawbacks:
# - More complex management
# - Cross-tenant reporting requires aggregation

## -----------------------------------------------------------------------------
# TTL INDEXES FOR DATA RETENTION
# -----------------------------------------------------------------------------

# Example: Automatically delete audit logs after 1 year
db.audit_logs.createIndex(
  { "created_at": 1 },
  { expireAfterSeconds: 31536000, name: "ttl_idx" }
)

## -----------------------------------------------------------------------------
# CHANGE STREAMS FOR REAL-TIME SYNC
# -----------------------------------------------------------------------------

# Watch for changes with tenant filtering:

changeStream = db.watch(
  [
    {
      $match: {
        "fullDocument.tenant_id": "tenant-123"
      }
    }
  ],
  {
    fullDocument: "updateLookup"
  }
)
