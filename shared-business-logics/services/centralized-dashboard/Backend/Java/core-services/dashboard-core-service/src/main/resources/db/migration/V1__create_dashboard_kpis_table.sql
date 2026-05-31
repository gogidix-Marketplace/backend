-- Dashboard KPI table
CREATE TABLE IF NOT EXISTS dashboard_kpis (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    description TEXT,
    code VARCHAR(100) NOT NULL UNIQUE,
    category VARCHAR(100) NOT NULL,
    tenant_id VARCHAR(50) NOT NULL,
    source_domain VARCHAR(50) NOT NULL,
    unit VARCHAR(50),
    data_type VARCHAR(50) NOT NULL,
    aggregation_type VARCHAR(50),
    formula TEXT,
    is_active BOOLEAN NOT NULL DEFAULT true,
    is_real_time BOOLEAN NOT NULL DEFAULT false,
    refresh_interval_seconds INTEGER,
    threshold_warning DOUBLE PRECISION,
    threshold_critical DOUBLE PRECISION NOT NULL,
    target_value DOUBLE PRECISION,
    current_value DOUBLE PRECISION,
    previous_value DOUBLE PRECISION,
    trend VARCHAR(20),
    last_calculated_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100)
);

-- Create indexes
CREATE INDEX IF NOT EXISTS idx_kpi_tenant_id ON dashboard_kpis(tenant_id);
CREATE INDEX IF NOT EXISTS idx_kpi_category ON dashboard_kpis(category);
CREATE INDEX IF NOT EXISTS idx_kpi_source_domain ON dashboard_kpis(source_domain);
CREATE INDEX IF NOT EXISTS idx_kpi_is_active ON dashboard_kpis(is_active);
CREATE INDEX IF NOT EXISTS idx_kpi_code ON dashboard_kpis(code);
CREATE INDEX IF NOT EXISTS idx_kpi_is_real_time ON dashboard_kpis(is_real_time);

-- KPI Values table
CREATE TABLE IF NOT EXISTS kpi_values (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    kpi_id UUID NOT NULL,
    value DOUBLE PRECISION NOT NULL,
    recorded_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    metadata TEXT,
    FOREIGN KEY (kpi_id) REFERENCES dashboard_kpis(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_kpi_value_kpi_id ON kpi_values(kpi_id);
CREATE INDEX IF NOT EXISTS idx_kpi_value_recorded_at ON kpi_values(recorded_at);

-- KPI Targets table
CREATE TABLE IF NOT EXISTS kpi_targets (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    kpi_id UUID NOT NULL,
    target_value DOUBLE PRECISION NOT NULL,
    target_period VARCHAR(50),
    target_date DATE,
    start_date DATE,
    end_date DATE,
    minimum_acceptable DOUBLE PRECISION,
    stretch_target DOUBLE PRECISION,
    owner VARCHAR(100),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (kpi_id) REFERENCES dashboard_kpis(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_kpi_target_kpi_id ON kpi_targets(kpi_id);
CREATE INDEX IF NOT EXISTS idx_kpi_target_period ON kpi_targets(target_period);
CREATE INDEX IF NOT EXISTS idx_kpi_target_date ON kpi_targets(target_date);

-- Dashboard Widgets table
CREATE TABLE IF NOT EXISTS dashboard_widgets (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    dashboard_id UUID NOT NULL,
    tenant_id VARCHAR(50) NOT NULL,
    widget_type VARCHAR(50) NOT NULL,
    position_x INTEGER NOT NULL DEFAULT 0,
    position_y INTEGER NOT NULL DEFAULT 0,
    width INTEGER NOT NULL DEFAULT 4,
    height INTEGER NOT NULL DEFAULT 3,
    kpi_ids TEXT,
    config TEXT,
    is_refreshable BOOLEAN NOT NULL DEFAULT true,
    refresh_interval_seconds INTEGER,
    is_visible BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_widget_tenant_id ON dashboard_widgets(tenant_id);
CREATE INDEX IF NOT EXISTS idx_widget_dashboard_id ON dashboard_widgets(dashboard_id);
