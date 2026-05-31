-- =====================================================
-- Usage Metering Service Database Migration
-- Version: 1.0.0
-- =====================================================

-- Create metric_definitions table
CREATE TABLE IF NOT EXISTS metric_definitions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    metric_name VARCHAR(255) NOT NULL UNIQUE,
    metric_display_name VARCHAR(255) NOT NULL,
    description TEXT,
    metric_type VARCHAR(50) NOT NULL CHECK (metric_type IN ('COUNTER', 'GAUGE', 'HISTOGRAM')),
    metric_category VARCHAR(100),
    unit VARCHAR(50),
    aggregation_type VARCHAR(50) CHECK (aggregation_type IN ('SUM', 'AVG', 'MAX', 'MIN', 'COUNT')),
    retention_days INTEGER DEFAULT 90,
    billable BOOLEAN DEFAULT FALSE,
    unit_price NUMERIC(10,4),
    dimensions JSONB DEFAULT '{}',
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE INDEX idx_metric_definitions_name ON metric_definitions(metric_name);
CREATE INDEX idx_metric_definitions_category ON metric_definitions(metric_category);

-- Create usage_records table (partitioned by month in production)
CREATE TABLE IF NOT EXISTS usage_records (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(100) NOT NULL,
    metric_name VARCHAR(255) NOT NULL,
    metric_type VARCHAR(50) NOT NULL CHECK (metric_type IN ('COUNTER', 'GAUGE', 'HISTOGRAM')),
    quantity NUMERIC(15,4) NOT NULL,
    unit VARCHAR(50),
    event_time TIMESTAMP WITH TIME ZONE NOT NULL,
    received_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    dimensions JSONB DEFAULT '{}',
    service_name VARCHAR(255),
    resource_id VARCHAR(255),
    user_id VARCHAR(255),
    correlation_id VARCHAR(255),
    metadata JSONB DEFAULT '{}',
    CONSTRAINT chk_quantity CHECK (quantity >= 0)
);

CREATE INDEX idx_usage_records_tenant_time ON usage_records(tenant_id, event_time);
CREATE INDEX idx_usage_records_metric_time ON usage_records(metric_name, event_time);
CREATE INDEX idx_usage_records_service_time ON usage_records(service_name, event_time);

-- Create usage_aggregates table
CREATE TABLE IF NOT EXISTS usage_aggregates (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(100) NOT NULL,
    metric_name VARCHAR(255) NOT NULL,
    aggregation_type VARCHAR(50) NOT NULL CHECK (aggregation_type IN ('HOURLY', 'DAILY', 'MONTHLY')),
    period_start TIMESTAMP WITH TIME ZONE NOT NULL,
    period_end TIMESTAMP WITH TIME ZONE NOT NULL,
    total_quantity NUMERIC(20,4) NOT NULL,
    avg_quantity NUMERIC(20,4),
    max_quantity NUMERIC(20,4),
    min_quantity NUMERIC(20,4),
    count INTEGER NOT NULL,
    dimensions JSONB DEFAULT '{}',
    quota_limit NUMERIC(20,4),
    quota_usage_percentage NUMERIC(5,2),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE INDEX idx_usage_aggregates_tenant_period ON usage_aggregates(tenant_id, period_start, period_end);
CREATE INDEX idx_usage_aggregates_metric_period ON usage_aggregates(metric_name, period_start);
CREATE INDEX idx_usage_aggregates_type_period ON usage_aggregates(aggregation_type, period_start);

-- Create quota_definitions table
CREATE TABLE IF NOT EXISTS quota_definitions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(100) NOT NULL,
    quota_name VARCHAR(255) NOT NULL,
    quota_display_name VARCHAR(255) NOT NULL,
    metric_name VARCHAR(255) NOT NULL,
    soft_limit NUMERIC(20,4),
    hard_limit NUMERIC(20,4) NOT NULL,
    quota_period VARCHAR(50) NOT NULL CHECK (quota_period IN ('HOURLY', 'DAILY', 'MONTHLY', 'BILLING_CYCLE')),
    soft_limit_action VARCHAR(50) CHECK (soft_limit_action IN ('ALERT', 'THROTTLE', 'NONE')),
    hard_limit_action VARCHAR(50) NOT NULL CHECK (hard_limit_action IN ('BLOCK', 'ALERT', 'CHARGE_OVERAGE')),
    notification_thresholds JSONB DEFAULT '[80,90,100]',
    notification_channels TEXT[],
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE INDEX idx_quota_definitions_tenant ON quota_definitions(tenant_id);
CREATE INDEX idx_quota_definitions_metric ON quota_definitions(metric_name);

-- Create quota_usage table
CREATE TABLE IF NOT EXISTS quota_usage (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(100) NOT NULL,
    quota_id UUID NOT NULL,
    current_usage NUMERIC(20,4) NOT NULL,
    soft_limit NUMERIC(20,4),
    hard_limit NUMERIC(20,4) NOT NULL,
    period_start TIMESTAMP WITH TIME ZONE NOT NULL,
    period_end TIMESTAMP WITH TIME ZONE NOT NULL,
    soft_limit_exceeded BOOLEAN DEFAULT FALSE,
    hard_limit_exceeded BOOLEAN DEFAULT FALSE,
    soft_limit_percentage NUMERIC(5,2),
    hard_limit_percentage NUMERIC(5,2),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    CONSTRAINT uq_quota_usage_period UNIQUE (tenant_id, quota_id, period_start)
);

CREATE INDEX idx_quota_usage_tenant_quota ON quota_usage(tenant_id, quota_id);
CREATE INDEX idx_quota_usage_period ON quota_usage(period_start, period_end);
CREATE INDEX idx_quota_usage_exceeded ON quota_usage(hard_limit_exceeded) WHERE hard_limit_exceeded = TRUE;

-- Create quota_alerts table
CREATE TABLE IF NOT EXISTS quota_alerts (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(100) NOT NULL,
    quota_id UUID NOT NULL,
    alert_type VARCHAR(50) NOT NULL CHECK (alert_type IN ('SOFT_LIMIT', 'HARD_LIMIT', 'FORECAST')),
    severity VARCHAR(50) NOT NULL CHECK (severity IN ('INFO', 'WARNING', 'CRITICAL')),
    current_usage NUMERIC(20,4) NOT NULL,
    limit_value NUMERIC(20,4) NOT NULL,
    percentage NUMERIC(5,2) NOT NULL,
    message TEXT NOT NULL,
    recommended_action TEXT,
    is_acknowledged BOOLEAN DEFAULT FALSE,
    acknowledged_by VARCHAR(255),
    acknowledged_at TIMESTAMP WITH TIME ZONE,
    notification_sent BOOLEAN DEFAULT FALSE,
    notification_channels TEXT[],
    period_start TIMESTAMP WITH TIME ZONE NOT NULL,
    period_end TIMESTAMP WITH TIME ZONE NOT NULL,
    metadata JSONB DEFAULT '{}',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE INDEX idx_quota_alerts_tenant ON quota_alerts(tenant_id);
CREATE INDEX idx_quota_alerts_quota ON quota_alerts(quota_id);
CREATE INDEX idx_quota_alerts_created ON quota_alerts(created_at);
CREATE INDEX idx_quota_alerts_acknowledged ON quota_alerts(is_acknowledged) WHERE is_acknowledged = FALSE;

-- Create update triggers
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_metric_definitions_updated_at BEFORE UPDATE ON metric_definitions
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_quota_definitions_updated_at BEFORE UPDATE ON quota_definitions
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_quota_usage_updated_at BEFORE UPDATE ON quota_usage
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Enable Row-Level Security
ALTER TABLE usage_aggregates ENABLE ROW LEVEL SECURITY;
CREATE POLICY usage_aggregates_tenant_isolation ON usage_aggregates
    USING (tenant_id = current_setting('app.current_tenant_id', true)::VARCHAR);

ALTER TABLE quota_definitions ENABLE ROW LEVEL SECURITY;
CREATE POLICY quota_definitions_tenant_isolation ON quota_definitions
    USING (tenant_id = current_setting('app.current_tenant_id', true)::VARCHAR);

ALTER TABLE quota_usage ENABLE ROW LEVEL SECURITY;
CREATE POLICY quota_usage_tenant_isolation ON quota_usage
    USING (tenant_id = current_setting('app.current_tenant_id', true)::VARCHAR);

ALTER TABLE quota_alerts ENABLE ROW LEVEL SECURITY;
CREATE POLICY quota_alerts_tenant_isolation ON quota_alerts
    USING (tenant_id = current_setting('app.current_tenant_id', true)::VARCHAR);
