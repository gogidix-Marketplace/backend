-- Payments table
CREATE TABLE IF NOT EXISTS payments (
    payment_id VARCHAR(50) PRIMARY KEY,
    tenant_id VARCHAR(50) NOT NULL,
    invoice_id VARCHAR(50),
    subscription_id VARCHAR(50),
    status VARCHAR(50) NOT NULL,
    method VARCHAR(50) NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    currency VARCHAR(3) NOT NULL DEFAULT 'USD',
    transaction_date TIMESTAMP NOT NULL,
    processed_date TIMESTAMP,
    transaction_reference VARCHAR(100) UNIQUE,
    gateway VARCHAR(50),
    gateway_transaction_id VARCHAR(200),
    gateway_response TEXT,
    failure_reason TEXT,
    payment_method_id VARCHAR(100),
    card_last_four VARCHAR(4),
    card_brand VARCHAR(50),
    description TEXT,
    metadata JSONB,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_payment_invoice FOREIGN KEY (invoice_id) 
        REFERENCES invoices(invoice_id) ON DELETE SET NULL
);

CREATE INDEX idx_payments_tenant_id ON payments(tenant_id);
CREATE INDEX idx_payments_invoice_id ON payments(invoice_id);
CREATE INDEX idx_payments_status ON payments(status);
CREATE INDEX idx_payments_transaction_date ON payments(transaction_date);
CREATE INDEX idx_payments_gateway_transaction_id ON payments(gateway_transaction_id);

COMMENT ON TABLE payments IS 'Stores payment transactions for invoices';
COMMENT ON COLUMN payments.status IS 'Payment status: PENDING, PROCESSING, COMPLETED, FAILED, REFUNDED, PARTIALLY_REFUNDED, CANCELLED, EXPIRED';
COMMENT ON COLUMN payments.method IS 'Payment method: CREDIT_CARD, DEBIT_CARD, BANK_TRANSFER, PAYPAL, STRIPE, CRYPTO, CHECK, WIRE_TRANSFER, OTHER';
