-- Invoice line items table
CREATE TABLE IF NOT EXISTS invoice_line_items (
    item_id VARCHAR(50) PRIMARY KEY,
    invoice_id VARCHAR(50) NOT NULL,
    description TEXT NOT NULL,
    quantity DECIMAL(10, 2) NOT NULL DEFAULT 1.00,
    unit_price DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    discount DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    tax_code VARCHAR(50),
    tax_rate DECIMAL(5, 2) NOT NULL DEFAULT 0.00,
    total DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    sort_order INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_line_item_invoice FOREIGN KEY (invoice_id) 
        REFERENCES invoices(invoice_id) ON DELETE CASCADE
);

CREATE INDEX idx_line_items_invoice_id ON invoice_line_items(invoice_id);

COMMENT ON TABLE invoice_line_items IS 'Stores individual line items for each invoice';
