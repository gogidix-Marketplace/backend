-- Payment methods table (saved payment methods)
CREATE TABLE IF NOT EXISTS payment_methods (
    payment_method_id VARCHAR(50) PRIMARY KEY,
    tenant_id VARCHAR(50) NOT NULL,
    method_type VARCHAR(50) NOT NULL,
    provider VARCHAR(50) NOT NULL,
    provider_token VARCHAR(500) NOT NULL,
    is_default BOOLEAN NOT NULL DEFAULT false,
    card_last_four VARCHAR(4),
    card_brand VARCHAR(50),
    card_expiry_month INTEGER,
    card_expiry_year INTEGER,
    account_holder_name VARCHAR(200),
    is_verified BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_used_at TIMESTAMP
);

CREATE INDEX idx_payment_methods_tenant_id ON payment_methods(tenant_id);
CREATE INDEX idx_payment_methods_is_default ON payment_methods(is_default);

COMMENT ON TABLE payment_methods IS 'Stores saved payment methods for tenants';
COMMENT ON COLUMN payment_methods.method_type IS 'Payment method type: CARD, BANK_ACCOUNT, PAYPAL, etc.';
