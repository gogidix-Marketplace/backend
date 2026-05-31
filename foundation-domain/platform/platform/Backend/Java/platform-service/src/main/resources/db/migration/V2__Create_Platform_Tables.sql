-- =====================================================
-- Platform Service Database Migration
-- Version: 1.0.0
-- =====================================================

-- Create platform_configurations table
CREATE TABLE IF NOT EXISTS platform_configurations (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(100) NOT NULL,
    config_key VARCHAR(255) NOT NULL UNIQUE,
    config_value TEXT NOT NULL,
    config_type VARCHAR(50) NOT NULL CHECK (config_type IN ('STRING', 'JSON', 'NUMBER', 'BOOLEAN')),
    description TEXT,
    is_sensitive BOOLEAN DEFAULT FALSE,
    is_encrypted BOOLEAN DEFAULT FALSE,
    environment VARCHAR(50) CHECK (environment IN ('DEV', 'STAGING', 'PROD', 'ALL')),
    version INTEGER NOT NULL DEFAULT 1,
    effective_from TIMESTAMP WITH TIME ZONE,
    effective_until TIMESTAMP WITH TIME ZONE,
    tags TEXT[],
    metadata JSONB DEFAULT '{}',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE', 'INACTIVE', 'ARCHIVED')),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    created_by VARCHAR(255),
    updated_by VARCHAR(255)
);

CREATE INDEX idx_platform_config_tenant ON platform_configurations(tenant_id);
CREATE INDEX idx_platform_config_key ON platform_configurations(config_key);
CREATE INDEX idx_platform_config_env ON platform_configurations(environment);
CREATE INDEX idx_platform_config_tags ON platform_configurations USING GIN(tags);
CREATE INDEX idx_platform_config_status ON platform_configurations(status);
CREATE INDEX idx_platform_config_tenant_key ON platform_configurations(tenant_id, config_key);

-- Create feature_flags table
CREATE TABLE IF NOT EXISTS feature_flags (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(100) NOT NULL,
    flag_key VARCHAR(255) NOT NULL UNIQUE,
    flag_name VARCHAR(255) NOT NULL,
    description TEXT,
    is_enabled BOOLEAN DEFAULT FALSE,
    rollout_percentage INTEGER DEFAULT 0 CHECK (rollout_percentage BETWEEN 0 AND 100),
    target_segments TEXT[],
    whitelist_users TEXT[],
    blacklist_users TEXT[],
    dependencies TEXT[],
    metadata JSONB DEFAULT '{}',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE', 'INACTIVE', 'ARCHIVED')),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    created_by VARCHAR(255),
    updated_by VARCHAR(255)
);

CREATE INDEX idx_feature_flags_tenant ON feature_flags(tenant_id);
CREATE INDEX idx_feature_flags_key ON feature_flags(flag_key);
CREATE INDEX idx_feature_flags_enabled ON feature_flags(is_enabled);
CREATE INDEX idx_feature_flags_tenant_key ON feature_flags(tenant_id, flag_key);

-- Create service_health_status table
CREATE TABLE IF NOT EXISTS service_health_status (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(100) NOT NULL,
    service_name VARCHAR(255) NOT NULL,
    service_instance VARCHAR(255),
    status VARCHAR(50) NOT NULL CHECK (status IN ('UP', 'DOWN', 'DEGRADED', 'UNKNOWN')),
    last_health_check TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    response_time_ms INTEGER,
    error_message TEXT,
    error_count INTEGER DEFAULT 0,
    consecutive_failures INTEGER DEFAULT 0,
    last_recovery TIMESTAMP WITH TIME ZONE,
    metadata JSONB DEFAULT '{}',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE INDEX idx_service_health_tenant ON service_health_status(tenant_id);
CREATE INDEX idx_service_health_name ON service_health_status(service_name);
CREATE INDEX idx_service_health_status ON service_health_status(status);
CREATE INDEX idx_service_health_last_check ON service_health_status(last_health_check);
CREATE INDEX idx_service_health_tenant_service ON service_health_status(tenant_id, service_name);

-- Create platform_announcements table
CREATE TABLE IF NOT EXISTS platform_announcements (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(100) NOT NULL,
    title VARCHAR(500) NOT NULL,
    content TEXT NOT NULL,
    announcement_type VARCHAR(50) NOT NULL CHECK (announcement_type IN ('INFO', 'WARNING', 'MAINTENANCE', 'CRITICAL')),
    priority VARCHAR(50) DEFAULT 'NORMAL' CHECK (priority IN ('LOW', 'NORMAL', 'HIGH', 'URGENT')),
    target_audience TEXT[],
    is_pinned BOOLEAN DEFAULT FALSE,
    is_dismissible BOOLEAN DEFAULT TRUE,
    scheduled_from TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    scheduled_until TIMESTAMP WITH TIME ZONE,
    dismiss_count INTEGER DEFAULT 0,
    view_count INTEGER DEFAULT 0,
    click_count INTEGER DEFAULT 0,
    metadata JSONB DEFAULT '{}',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE', 'SCHEDULED', 'ARCHIVED')),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    created_by VARCHAR(255),
    updated_by VARCHAR(255)
);

CREATE INDEX idx_announcements_tenant ON platform_announcements(tenant_id);
CREATE INDEX idx_announcements_type ON platform_announcements(announcement_type);
CREATE INDEX idx_announcements_priority ON platform_announcements(priority);
CREATE INDEX idx_announcements_schedule ON platform_announcements(scheduled_from, scheduled_until);
CREATE INDEX idx_announcements_status ON platform_announcements(status);
CREATE INDEX idx_announcements_tenant_active ON platform_announcements(tenant_id, status);

-- Create maintenance_windows table
CREATE TABLE IF NOT EXISTS maintenance_windows (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(100) NOT NULL,
    window_name VARCHAR(255) NOT NULL,
    description TEXT,
    affected_services TEXT[],
    scheduled_start TIMESTAMP WITH TIME ZONE NOT NULL,
    scheduled_end TIMESTAMP WITH TIME ZONE NOT NULL,
    actual_start TIMESTAMP WITH TIME ZONE,
    actual_end TIMESTAMP WITH TIME ZONE,
    status VARCHAR(50) NOT NULL DEFAULT 'SCHEDULED' CHECK (status IN ('SCHEDULED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED')),
    impact_level VARCHAR(50) DEFAULT 'MEDIUM' CHECK (impact_level IN ('LOW', 'MEDIUM', 'HIGH', 'CRITICAL')),
    notification_sent BOOLEAN DEFAULT FALSE,
    notification_lead_time_minutes INTEGER DEFAULT 60,
    post_maintenance_summary TEXT,
    metadata JSONB DEFAULT '{}',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    CONSTRAINT chk_window_times CHECK (scheduled_end > scheduled_start)
);

CREATE INDEX idx_maintenance_tenant ON maintenance_windows(tenant_id);
CREATE INDEX idx_maintenance_schedule ON maintenance_windows(scheduled_start, scheduled_end);
CREATE INDEX idx_maintenance_status ON maintenance_windows(status);
CREATE INDEX idx_maintenance_impact ON maintenance_windows(impact_level);

-- Create update triggers
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_platform_config_updated_at BEFORE UPDATE ON platform_configurations
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_feature_flags_updated_at BEFORE UPDATE ON feature_flags
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_service_health_updated_at BEFORE UPDATE ON service_health_status
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_announcements_updated_at BEFORE UPDATE ON platform_announcements
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_maintenance_updated_at BEFORE UPDATE ON maintenance_windows
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Enable Row-Level Security for tenant isolation
ALTER TABLE platform_configurations ENABLE ROW LEVEL SECURITY;
CREATE POLICY platform_config_tenant_isolation ON platform_configurations
    USING (tenant_id = current_setting('app.current_tenant_id', true)::VARCHAR);

ALTER TABLE feature_flags ENABLE ROW LEVEL SECURITY;
CREATE POLICY feature_flags_tenant_isolation ON feature_flags
    USING (tenant_id = current_setting('app.current_tenant_id', true)::VARCHAR);

ALTER TABLE service_health_status ENABLE ROW LEVEL SECURITY;
CREATE POLICY service_health_tenant_isolation ON service_health_status
    USING (tenant_id = current_setting('app.current_tenant_id', true)::VARCHAR);

ALTER TABLE platform_announcements ENABLE ROW LEVEL SECURITY;
CREATE POLICY announcements_tenant_isolation ON platform_announcements
    USING (tenant_id = current_setting('app.current_tenant_id', true)::VARCHAR);

ALTER TABLE maintenance_windows ENABLE ROW LEVEL SECURITY;
CREATE POLICY maintenance_tenant_isolation ON maintenance_windows
    USING (tenant_id = current_setting('app.current_tenant_id', true)::VARCHAR);
