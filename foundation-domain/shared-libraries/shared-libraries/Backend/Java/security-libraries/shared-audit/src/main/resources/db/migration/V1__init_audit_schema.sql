-- Initial schema for Gogidix Audit Service
-- Creates audit_events table with proper indexing for performance

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE audit_events (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    action VARCHAR(50) NOT NULL,
    service_name VARCHAR(100) NOT NULL,
    resource_type VARCHAR(100),
    resource_id VARCHAR(255),
    user_id VARCHAR(255),
    description VARCHAR(500),
    success BOOLEAN,
    severity VARCHAR(20),
    correlation_id VARCHAR(255),
    session_id VARCHAR(255),
    metadata JSONB,
    retention_days INTEGER DEFAULT 365,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Indexes for performance optimization
CREATE INDEX idx_audit_timestamp ON audit_events(timestamp);
CREATE INDEX idx_audit_user_id ON audit_events(user_id);
CREATE INDEX idx_audit_resource ON audit_events(resource_type, resource_id);
CREATE INDEX idx_audit_action ON audit_events(action);
CREATE INDEX idx_audit_service ON audit_events(service_name);
CREATE INDEX idx_audit_correlation ON audit_events(correlation_id);
CREATE INDEX idx_audit_session ON audit_events(session_id);
CREATE INDEX idx_audit_severity ON audit_events(severity);
CREATE INDEX idx_audit_success ON audit_events(success);

-- Composite indexes for common queries
CREATE INDEX idx_audit_user_time ON audit_events(user_id, timestamp);
CREATE INDEX idx_audit_service_time ON audit_events(service_name, timestamp);
CREATE INDEX idx_audit_resource_time ON audit_events(resource_type, resource_id, timestamp);

-- JSONB index for metadata queries
CREATE INDEX idx_audit_metadata ON audit_events USING gin(metadata);

-- Trigger to update updated_at timestamp
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_audit_events_updated_at
    BEFORE UPDATE ON audit_events
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();