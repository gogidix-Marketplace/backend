-- =====================================================
-- Subscription Service Database Migration
-- Version: 1.0.0
-- =====================================================

-- Create subscription_plans table
CREATE TABLE IF NOT EXISTS subscription_plans (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(100) NOT NULL,
    plan_code VARCHAR(100) NOT NULL UNIQUE,
    plan_name VARCHAR(255) NOT NULL,
    description TEXT,
    plan_type VARCHAR(50) NOT NULL CHECK (plan_type IN ('FIXED', 'USAGE_BASED', 'TIERED')),
    billing_cycle VARCHAR(50) NOT NULL CHECK (billing_cycle IN ('MONTHLY', 'QUARTERLY', 'ANNUAL')),
    base_price NUMERIC(10,2) NOT NULL,
    currency VARCHAR(3) DEFAULT 'USD',
    trial_days INTEGER DEFAULT 0,
    max_users INTEGER,
    max_storage_gb INTEGER,
    features JSONB DEFAULT '{}',
    limits JSONB DEFAULT '{}',
    discounts JSONB DEFAULT '{}',
    metadata JSONB DEFAULT '{}',
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE', 'INACTIVE', 'ARCHIVED')),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    CONSTRAINT chk_base_price CHECK (base_price >= 0)
);

CREATE INDEX idx_subscription_plans_tenant ON subscription_plans(tenant_id);
CREATE INDEX idx_subscription_plans_code ON subscription_plans(plan_code);
CREATE INDEX idx_subscription_plans_status ON subscription_plans(status);
CREATE INDEX idx_subscription_plans_type ON subscription_plans(plan_type);

-- Create subscriptions table
CREATE TABLE IF NOT EXISTS subscriptions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(100) NOT NULL,
    subscription_number VARCHAR(100) NOT NULL UNIQUE,
    customer_id VARCHAR(255) NOT NULL,
    plan_id UUID NOT NULL,
    status VARCHAR(50) NOT NULL CHECK (status IN ('TRIAL', 'ACTIVE', 'SUSPENDED', 'CANCELLED', 'EXPIRED', 'PENDING')),
    trial_start DATE,
    trial_end DATE,
    current_period_start TIMESTAMP WITH TIME ZONE NOT NULL,
    current_period_end TIMESTAMP WITH TIME ZONE NOT NULL,
    billing_cycle VARCHAR(50) NOT NULL CHECK (billing_cycle IN ('MONTHLY', 'QUARTERLY', 'ANNUAL')),
    base_price NUMERIC(10,2) NOT NULL,
    discount_percentage NUMERIC(5,2) DEFAULT 0,
    tax_rate NUMERIC(5,2) DEFAULT 0,
    total_amount NUMERIC(10,2) NOT NULL,
    currency VARCHAR(3) DEFAULT 'USD',
    current_users INTEGER DEFAULT 0,
    current_storage_gb NUMERIC(10,2) DEFAULT 0,
    usage_json JSONB DEFAULT '{}',
    is_trial BOOLEAN DEFAULT FALSE,
    trial_days_remaining INTEGER,
    auto_renew BOOLEAN DEFAULT TRUE,
    cancel_at_period_end BOOLEAN DEFAULT FALSE,
    cancelled_at TIMESTAMP WITH TIME ZONE,
    cancellation_reason TEXT,
    metadata JSONB DEFAULT '{}',
    stripe_subscription_id VARCHAR(255),
    stripe_customer_id VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    CONSTRAINT chk_total_amount CHECK (total_amount >= 0)
);

CREATE INDEX idx_subscriptions_tenant ON subscriptions(tenant_id);
CREATE INDEX idx_subscriptions_customer ON subscriptions(customer_id);
CREATE INDEX idx_subscriptions_plan ON subscriptions(plan_id);
CREATE INDEX idx_subscriptions_status ON subscriptions(status);
CREATE INDEX idx_subscriptions_period ON subscriptions(current_period_start, current_period_end);
CREATE INDEX idx_subscriptions_number ON subscriptions(subscription_number);

-- Create invoices table
CREATE TABLE IF NOT EXISTS invoices (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(100) NOT NULL,
    invoice_number VARCHAR(100) NOT NULL UNIQUE,
    subscription_id UUID NOT NULL,
    customer_id VARCHAR(255) NOT NULL,
    invoice_date TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    due_date TIMESTAMP WITH TIME ZONE NOT NULL,
    period_start TIMESTAMP WITH TIME ZONE NOT NULL,
    period_end TIMESTAMP WITH TIME ZONE NOT NULL,
    subtotal NUMERIC(10,2) NOT NULL,
    discount_amount NUMERIC(10,2) DEFAULT 0,
    tax_amount NUMERIC(10,2) DEFAULT 0,
    total_amount NUMERIC(10,2) NOT NULL,
    currency VARCHAR(3) DEFAULT 'USD',
    status VARCHAR(50) NOT NULL CHECK (status IN ('DRAFT', 'PENDING', 'PAID', 'OVERDUE', 'VOID', 'PARTIALLY_PAID')),
    paid_amount NUMERIC(10,2) DEFAULT 0,
    payment_method VARCHAR(255),
    payment_date TIMESTAMP WITH TIME ZONE,
    payment_reference VARCHAR(255),
    reminders_sent INTEGER DEFAULT 0,
    last_reminder_sent TIMESTAMP WITH TIME ZONE,
    pdf_url TEXT,
    line_items JSONB,
    metadata JSONB,
    stripe_invoice_id VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    created_by VARCHAR(255),
    updated_by VARCHAR(255)
);

CREATE INDEX idx_invoices_tenant ON invoices(tenant_id);
CREATE INDEX idx_invoices_subscription ON invoices(subscription_id);
CREATE INDEX idx_invoices_customer ON invoices(customer_id);
CREATE INDEX idx_invoices_status ON invoices(status);
CREATE INDEX idx_invoices_due_date ON invoices(due_date);
CREATE INDEX idx_invoices_number ON invoices(invoice_number);

-- Create payment_methods table
CREATE TABLE IF NOT EXISTS payment_methods (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(100) NOT NULL,
    customer_id VARCHAR(255) NOT NULL,
    method_type VARCHAR(50) NOT NULL CHECK (method_type IN ('CREDIT_CARD', 'DEBIT_CARD', 'BANK_TRANSFER', 'PAYPAL')),
    provider VARCHAR(100),
    provider_method_id VARCHAR(255),
    card_last4 VARCHAR(4),
    card_brand VARCHAR(50),
    card_expiry_month INTEGER,
    card_expiry_year INTEGER,
    cardholder_name VARCHAR(255),
    bank_name VARCHAR(255),
    bank_account_last4 VARCHAR(4),
    is_default BOOLEAN DEFAULT FALSE,
    is_verified BOOLEAN DEFAULT FALSE,
    status VARCHAR(50) DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE', 'INACTIVE', 'EXPIRED')),
    billing_address JSONB,
    metadata JSONB,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE INDEX idx_payment_methods_tenant ON payment_methods(tenant_id);
CREATE INDEX idx_payment_methods_customer ON payment_methods(customer_id);
CREATE INDEX idx_payment_methods_default ON payment_methods(customer_id, is_default);

-- Create payment_transactions table
CREATE TABLE IF NOT EXISTS payment_transactions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(100) NOT NULL,
    transaction_number VARCHAR(100) NOT NULL UNIQUE,
    invoice_id UUID,
    customer_id VARCHAR(255) NOT NULL,
    payment_method_id UUID,
    amount NUMERIC(10,2) NOT NULL,
    currency VARCHAR(3) DEFAULT 'USD',
    transaction_type VARCHAR(50) NOT NULL CHECK (transaction_type IN ('CHARGE', 'REFUND', 'PARTIAL_REFUND')),
    provider VARCHAR(100),
    provider_transaction_id VARCHAR(255),
    provider_response JSONB,
    status VARCHAR(50) NOT NULL CHECK (status IN ('PENDING', 'COMPLETED', 'FAILED', 'CANCELLED')),
    failure_reason TEXT,
    original_transaction_id UUID,
    refund_reason TEXT,
    processed_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE INDEX idx_payment_transactions_tenant ON payment_transactions(tenant_id);
CREATE INDEX idx_payment_transactions_invoice ON payment_transactions(invoice_id);
CREATE INDEX idx_payment_transactions_customer ON payment_transactions(customer_id);
CREATE INDEX idx_payment_transactions_status ON payment_transactions(status);
CREATE INDEX idx_payment_transactions_number ON payment_transactions(transaction_number);

-- Create usage_records table
CREATE TABLE IF NOT EXISTS usage_records (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(100) NOT NULL,
    subscription_id UUID NOT NULL,
    customer_id VARCHAR(255) NOT NULL,
    metric_name VARCHAR(255) NOT NULL,
    metric_type VARCHAR(50) NOT NULL CHECK (metric_type IN ('COUNTER', 'GAUGE')),
    quantity NUMERIC(15,4) NOT NULL,
    unit VARCHAR(50),
    period_start TIMESTAMP WITH TIME ZONE NOT NULL,
    period_end TIMESTAMP WITH TIME ZONE NOT NULL,
    unit_price NUMERIC(10,4),
    total_cost NUMERIC(10,2),
    description TEXT,
    metadata JSONB,
    recorded_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    CONSTRAINT chk_quantity CHECK (quantity >= 0)
);

CREATE INDEX idx_usage_records_tenant ON usage_records(tenant_id);
CREATE INDEX idx_usage_records_subscription ON usage_records(subscription_id);
CREATE INDEX idx_usage_records_customer ON usage_records(customer_id);
CREATE INDEX idx_usage_records_metric ON usage_records(metric_name);
CREATE INDEX idx_usage_records_period ON usage_records(period_start, period_end);

-- Create update triggers
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_subscription_plans_updated_at BEFORE UPDATE ON subscription_plans
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_subscriptions_updated_at BEFORE UPDATE ON subscriptions
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_invoices_updated_at BEFORE UPDATE ON invoices
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_payment_methods_updated_at BEFORE UPDATE ON payment_methods
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_payment_transactions_updated_at BEFORE UPDATE ON payment_transactions
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Enable Row-Level Security
ALTER TABLE subscription_plans ENABLE ROW LEVEL SECURITY;
CREATE POLICY subscription_plans_tenant_isolation ON subscription_plans
    USING (tenant_id = current_setting('app.current_tenant_id', true)::VARCHAR);

ALTER TABLE subscriptions ENABLE ROW LEVEL SECURITY;
CREATE POLICY subscriptions_tenant_isolation ON subscriptions
    USING (tenant_id = current_setting('app.current_tenant_id', true)::VARCHAR);

ALTER TABLE invoices ENABLE ROW LEVEL SECURITY;
CREATE POLICY invoices_tenant_isolation ON invoices
    USING (tenant_id = current_setting('app.current_tenant_id', true)::VARCHAR);

ALTER TABLE payment_methods ENABLE ROW LEVEL SECURITY;
CREATE POLICY payment_methods_tenant_isolation ON payment_methods
    USING (tenant_id = current_setting('app.current_tenant_id', true)::VARCHAR);

ALTER TABLE payment_transactions ENABLE ROW LEVEL SECURITY;
CREATE POLICY payment_transactions_tenant_isolation ON payment_transactions
    USING (tenant_id = current_setting('app.current_tenant_id', true)::VARCHAR);

ALTER TABLE usage_records ENABLE ROW LEVEL SECURITY;
CREATE POLICY usage_records_tenant_isolation ON usage_records
    USING (tenant_id = current_setting('app.current_tenant_id', true)::VARCHAR);
