-- Subscriptions table
CREATE TABLE IF NOT EXISTS subscriptions (
    subscription_id VARCHAR(50) PRIMARY KEY,
    tenant_id VARCHAR(50) NOT NULL,
    plan VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    billing_period VARCHAR(50) NOT NULL,
    monthly_price DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    yearly_price DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP,
    trial_end_date TIMESTAMP,
    currency VARCHAR(3) NOT NULL DEFAULT 'USD',
    max_users INTEGER NOT NULL,
    max_storage_gb BIGINT NOT NULL,
    auto_renew BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    payment_method_id VARCHAR(100),
    last_billing_date TIMESTAMP,
    next_billing_date TIMESTAMP,
    current_balance DECIMAL(10, 2) NOT NULL DEFAULT 0.00
);

CREATE INDEX idx_subscriptions_tenant_id ON subscriptions(tenant_id);
CREATE INDEX idx_subscriptions_status ON subscriptions(status);
CREATE INDEX idx_subscriptions_plan ON subscriptions(plan);

-- Add comments
COMMENT ON TABLE subscriptions IS 'Stores subscription information for each tenant';
COMMENT ON COLUMN subscriptions.plan IS 'Subscription plan: FREE, STARTER, PROFESSIONAL, ENTERPRISE, CUSTOM';
COMMENT ON COLUMN subscriptions.status IS 'Subscription status: ACTIVE, TRIAL, PAST_DUE, CANCELLED, SUSPENDED, PENDING';
COMMENT ON COLUMN subscriptions.billing_period IS 'Billing period: MONTHLY, YEARLY, CUSTOM';
