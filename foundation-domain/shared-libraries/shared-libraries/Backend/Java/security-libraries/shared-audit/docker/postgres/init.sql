-- PostgreSQL initialization script for Shared Audit Service
-- This script sets up the database schema and initial configuration

-- Create audit schema
CREATE SCHEMA IF NOT EXISTS audit;

-- Set the search path to include audit schema
SET search_path TO audit, public;

-- Create extensions
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- Create audit events table
CREATE TABLE IF NOT EXISTS audit.audit_events (
    id BIGSERIAL PRIMARY KEY,
    event_id UUID DEFAULT uuid_generate_v4() UNIQUE NOT NULL,
    user_id VARCHAR(255) NOT NULL,
    session_id VARCHAR(255) NOT NULL,
    timestamp TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    event_type VARCHAR(100) NOT NULL,
    domain VARCHAR(100) NOT NULL,
    action VARCHAR(500) NOT NULL,
    resource VARCHAR(500) NOT NULL,
    resource_id VARCHAR(255),
    result VARCHAR(50) NOT NULL,
    description TEXT,
    ip_address INET,
    user_agent TEXT,
    correlation_id VARCHAR(255),
    compliance_type VARCHAR(100),
    risk_score INTEGER DEFAULT 0,
    metadata JSONB,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for performance
CREATE INDEX IF NOT EXISTS idx_audit_events_user_id ON audit.audit_events(user_id);
CREATE INDEX IF NOT EXISTS idx_audit_events_timestamp ON audit.audit_events(timestamp);
CREATE INDEX IF NOT EXISTS idx_audit_events_event_type ON audit.audit_events(event_type);
CREATE INDEX IF NOT EXISTS idx_audit_events_domain ON audit.audit_events(domain);
CREATE INDEX IF NOT EXISTS idx_audit_events_result ON audit.audit_events(result);
CREATE INDEX IF NOT EXISTS idx_audit_events_compliance_type ON audit.audit_events(compliance_type);
CREATE INDEX IF NOT EXISTS idx_audit_events_session_id ON audit.audit_events(session_id);
CREATE INDEX IF NOT EXISTS idx_audit_events_correlation_id ON audit.audit_events(correlation_id);
CREATE INDEX IF NOT EXISTS idx_audit_events_ip_address ON audit.audit_events(ip_address);

-- Create composite indexes for common queries
CREATE INDEX IF NOT EXISTS idx_audit_events_user_timestamp ON audit.audit_events(user_id, timestamp DESC);
CREATE INDEX IF NOT EXISTS idx_audit_events_domain_timestamp ON audit.audit_events(domain, timestamp DESC);
CREATE INDEX IF NOT EXISTS idx_audit_events_type_timestamp ON audit.audit_events(event_type, timestamp DESC);

-- Create GIN index for JSONB metadata search
CREATE INDEX IF NOT EXISTS idx_audit_events_metadata ON audit.audit_events USING GIN(metadata);

-- Create function to update updated_at timestamp
CREATE OR REPLACE FUNCTION audit.update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Create trigger to automatically update updated_at
DROP TRIGGER IF EXISTS update_audit_events_updated_at ON audit.audit_events;
CREATE TRIGGER update_audit_events_updated_at
    BEFORE UPDATE ON audit.audit_events
    FOR EACH ROW
    EXECUTE FUNCTION audit.update_updated_at_column();

-- Create table for audit statistics (for caching)
CREATE TABLE IF NOT EXISTS audit.audit_statistics (
    id BIGSERIAL PRIMARY KEY,
    period_start TIMESTAMP WITH TIME ZONE NOT NULL,
    period_end TIMESTAMP WITH TIME ZONE NOT NULL,
    domain VARCHAR(100),
    event_type VARCHAR(100),
    total_events BIGINT DEFAULT 0,
    successful_events BIGINT DEFAULT 0,
    failed_events BIGINT DEFAULT 0,
    unauthorized_events BIGINT DEFAULT 0,
    high_risk_events BIGINT DEFAULT 0,
    compliance_events BIGINT DEFAULT 0,
    statistics_data JSONB,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for audit statistics
CREATE INDEX IF NOT EXISTS idx_audit_statistics_period ON audit.audit_statistics(period_start, period_end);
CREATE INDEX IF NOT EXISTS idx_audit_statistics_domain ON audit.audit_statistics(domain);
CREATE INDEX IF NOT EXISTS idx_audit_statistics_event_type ON audit.audit_statistics(event_type);

-- Create trigger for audit statistics updated_at
DROP TRIGGER IF EXISTS update_audit_statistics_updated_at ON audit.audit_statistics;
CREATE TRIGGER update_audit_statistics_updated_at
    BEFORE UPDATE ON audit.audit_statistics
    FOR EACH ROW
    EXECUTE FUNCTION audit.update_updated_at_column();

-- Create table for compliance reports
CREATE TABLE IF NOT EXISTS audit.compliance_reports (
    id BIGSERIAL PRIMARY KEY,
    report_id UUID DEFAULT uuid_generate_v4() UNIQUE NOT NULL,
    compliance_type VARCHAR(100) NOT NULL,
    period_start TIMESTAMP WITH TIME ZONE NOT NULL,
    period_end TIMESTAMP WITH TIME ZONE NOT NULL,
    total_events BIGINT DEFAULT 0,
    compliant_events BIGINT DEFAULT 0,
    non_compliant_events BIGINT DEFAULT 0,
    compliance_score DECIMAL(5,2),
    report_data JSONB,
    generated_by VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for compliance reports
CREATE INDEX IF NOT EXISTS idx_compliance_reports_type ON audit.compliance_reports(compliance_type);
CREATE INDEX IF NOT EXISTS idx_compliance_reports_period ON audit.compliance_reports(period_start, period_end);
CREATE INDEX IF NOT EXISTS idx_compliance_reports_generated_by ON audit.compliance_reports(generated_by);

-- Create view for recent audit events (last 24 hours)
CREATE OR REPLACE VIEW audit.recent_audit_events AS
SELECT 
    id,
    event_id,
    user_id,
    session_id,
    timestamp,
    event_type,
    domain,
    action,
    resource,
    resource_id,
    result,
    description,
    ip_address,
    correlation_id,
    compliance_type,
    risk_score,
    metadata
FROM audit.audit_events
WHERE timestamp >= CURRENT_TIMESTAMP - INTERVAL '24 hours'
ORDER BY timestamp DESC;

-- Create view for high-risk events
CREATE OR REPLACE VIEW audit.high_risk_events AS
SELECT 
    id,
    event_id,
    user_id,
    session_id,
    timestamp,
    event_type,
    domain,
    action,
    resource,
    result,
    risk_score,
    metadata
FROM audit.audit_events
WHERE risk_score >= 7 OR result IN ('UNAUTHORIZED', 'FORBIDDEN', 'SUSPICIOUS')
ORDER BY timestamp DESC, risk_score DESC;

-- Create view for compliance events
CREATE OR REPLACE VIEW audit.compliance_events AS
SELECT 
    id,
    event_id,
    user_id,
    timestamp,
    event_type,
    domain,
    action,
    resource,
    result,
    compliance_type,
    metadata
FROM audit.audit_events
WHERE compliance_type IS NOT NULL
ORDER BY timestamp DESC;

-- Grant permissions to the audit user
GRANT USAGE ON SCHEMA audit TO gogidix_audit;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA audit TO gogidix_audit;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA audit TO gogidix_audit;
GRANT ALL PRIVILEGES ON ALL FUNCTIONS IN SCHEMA audit TO gogidix_audit;

-- Set default privileges for future objects
ALTER DEFAULT PRIVILEGES IN SCHEMA audit GRANT ALL ON TABLES TO gogidix_audit;
ALTER DEFAULT PRIVILEGES IN SCHEMA audit GRANT ALL ON SEQUENCES TO gogidix_audit;
ALTER DEFAULT PRIVILEGES IN SCHEMA audit GRANT ALL ON FUNCTIONS TO gogidix_audit;

-- Insert some sample data for development
INSERT INTO audit.audit_events (
    user_id, session_id, event_type, domain, action, resource, result, 
    description, ip_address, compliance_type, risk_score
) VALUES 
    ('system', 'init-session', 'SYSTEM_EVENT', 'INFRASTRUCTURE', 'DATABASE_INIT', 'audit_schema', 'SUCCESS', 
     'Database schema initialized successfully', '127.0.0.1', 'SOX', 1),
    ('admin', 'admin-session-001', 'SYSTEM_EVENT', 'INFRASTRUCTURE', 'SERVICE_START', 'shared-audit', 'SUCCESS',
     'Shared audit service started successfully', '127.0.0.1', 'SOX', 2),
    ('system', 'init-session', 'CONFIGURATION', 'INFRASTRUCTURE', 'CONFIG_LOAD', 'application.yml', 'SUCCESS',
     'Application configuration loaded', '127.0.0.1', 'SOX', 1);

-- Create function for audit event statistics
CREATE OR REPLACE FUNCTION audit.get_audit_statistics(
    start_time TIMESTAMP WITH TIME ZONE,
    end_time TIMESTAMP WITH TIME ZONE,
    event_domain VARCHAR(100) DEFAULT NULL
)
RETURNS TABLE(
    total_events BIGINT,
    successful_events BIGINT,
    failed_events BIGINT,
    unauthorized_events BIGINT,
    high_risk_events BIGINT,
    avg_risk_score DECIMAL,
    compliance_events BIGINT
) AS $$
BEGIN
    RETURN QUERY
    SELECT 
        COUNT(*) AS total_events,
        COUNT(*) FILTER (WHERE result = 'SUCCESS') AS successful_events,
        COUNT(*) FILTER (WHERE result IN ('FAILURE', 'ERROR')) AS failed_events,
        COUNT(*) FILTER (WHERE result IN ('UNAUTHORIZED', 'FORBIDDEN')) AS unauthorized_events,
        COUNT(*) FILTER (WHERE risk_score >= 7) AS high_risk_events,
        ROUND(AVG(risk_score), 2) AS avg_risk_score,
        COUNT(*) FILTER (WHERE compliance_type IS NOT NULL) AS compliance_events
    FROM audit.audit_events
    WHERE timestamp BETWEEN start_time AND end_time
        AND (event_domain IS NULL OR domain = event_domain);
END;
$$ LANGUAGE plpgsql;

-- Create indexes for performance optimization
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_audit_events_timestamp_desc ON audit.audit_events(timestamp DESC);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_audit_events_user_timestamp_desc ON audit.audit_events(user_id, timestamp DESC);

-- Analyze tables for query optimization
ANALYZE audit.audit_events;
ANALYZE audit.audit_statistics;
ANALYZE audit.compliance_reports;

-- Success message
INSERT INTO audit.audit_events (
    user_id, session_id, event_type, domain, action, resource, result, 
    description, ip_address, compliance_type, risk_score
) VALUES 
    ('postgres', 'init-script', 'SYSTEM_EVENT', 'INFRASTRUCTURE', 'SCHEMA_INIT', 'postgres_init_script', 'SUCCESS',
     'PostgreSQL initialization script completed successfully', '127.0.0.1', 'SOX', 1);

COMMIT;