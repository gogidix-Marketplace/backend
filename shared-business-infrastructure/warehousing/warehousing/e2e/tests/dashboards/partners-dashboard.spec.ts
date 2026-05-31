import { test, expect } from '@playwright/test';

const DASHBOARD_URL = process.env.PARTNERS_DASHBOARD_URL || 'http://localhost:3001';

/**
 * Partners Dashboard E2E Tests
 *
 * Tests the warehouse partners dashboard functionality:
 * - Login and authentication
 * - Inventory overview
 * - Fulfillment management
 * - Storage space management
 * - Analytics and reports
 */
test.describe('Partners Dashboard', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto(DASHBOARD_URL);
  });

  test('should load dashboard homepage', async ({ page }) => {
    await expect(page).toHaveTitle(/Partners Dashboard/);
    await expect(page.locator('nav')).toBeVisible();
  });

  test('should redirect to login for unauthenticated users', async ({ page }) => {
    await page.goto(`${DASHBOARD_URL}/dashboard`);
    await expect(page).toHaveURL(/\/login/);
  });

  test('should login with valid credentials', async ({ page }) => {
    await page.goto(`${DASHBOARD_URL}/login`);

    await page.fill('[data-testid="email-input"]', 'partner@example.com');
    await page.fill('[data-testid="password-input"]', 'test-password');
    await page.click('[data-testid="login-button"]');

    // Should redirect to dashboard after successful login
    await expect(page).toHaveURL(/\/dashboard/, { timeout: 5000 });
  });

  test('should show login error with invalid credentials', async ({ page }) => {
    await page.goto(`${DASHBOARD_URL}/login`);

    await page.fill('[data-testid="email-input"]', 'invalid@example.com');
    await page.fill('[data-testid="password-input"]', 'wrong-password');
    await page.click('[data-testid="login-button"]');

    await expect(page.locator('[data-testid="error-message"]')).toBeVisible();
    await expect(page.locator('[data-testid="error-message"]')).toContainText('Invalid credentials');
  });

  test('should display inventory overview', async ({ page }) => {
    // First login
    await page.goto(`${DASHBOARD_URL}/login`);
    await page.fill('[data-testid="email-input"]', 'partner@example.com');
    await page.fill('[data-testid="password-input"]', 'test-password');
    await page.click('[data-testid="login-button"]');

    // Navigate to inventory
    await page.click('[data-testid="nav-inventory"]');
    await expect(page).toHaveURL(/\/inventory/);

    // Verify inventory metrics are displayed
    await expect(page.locator('[data-testid="total-items"]')).toBeVisible();
    await expect(page.locator('[data-testid="low-stock-count"]')).toBeVisible();
    await expect(page.locator('[data-testid="stock-value"]')).toBeVisible();
  });

  test('should search for inventory items', async ({ page }) => {
    await page.goto(`${DASHBOARD_URL}/login`);
    await page.fill('[data-testid="email-input"]', 'partner@example.com');
    await page.fill('[data-testid="password-input"]', 'test-password');
    await page.click('[data-testid="login-button"]');

    await page.click('[data-testid="nav-inventory"]');

    // Use search functionality
    await page.fill('[data-testid="inventory-search"]', 'TEST-SKU');
    await page.press('[data-testid="inventory-search"]', 'Enter');

    // Wait for search results
    await expect(page.locator('[data-testid="inventory-results"]')).toBeVisible({ timeout: 5000 });
  });

  test('should display fulfillment orders', async ({ page }) => {
    await page.goto(`${DASHBOARD_URL}/login`);
    await page.fill('[data-testid="email-input"]', 'partner@example.com');
    await page.fill('[data-testid="password-input"]', 'test-password');
    await page.click('[data-testid="login-button"]');

    await page.click('[data-testid="nav-fulfillment"]');

    // Verify fulfillment metrics
    await expect(page.locator('[data-testid="pending-orders"]')).toBeVisible();
    await expect(page.locator('[data-testid="orders-picked-today"]')).toBeVisible();
    await expect(page.locator('[data-testid="orders-shipped-today"]')).toBeVisible();
  });

  test('should create new fulfillment order', async ({ page }) => {
    await page.goto(`${DASHBOARD_URL}/login`);
    await page.fill('[data-testid="email-input"]', 'partner@example.com');
    await page.fill('[data-testid="password-input"]', 'test-password');
    await page.click('[data-testid="login-button"]');

    await page.click('[data-testid="nav-fulfillment"]');
    await page.click('[data-testid="create-order-button"]');

    // Fill order form
    await page.fill('[data-testid="customer-name"]', 'Test Customer');
    await page.fill('[data-testid="customer-email"]', 'test@example.com');
    await page.selectOption('[data-testid="shipping-method"]', 'STANDARD');

    // Add line item
    await page.click('[data-testid="add-line-item"]');
    await page.fill('[data-testid="item-sku-0"]', 'TEST-SKU-001');
    await page.fill('[data-testid="item-quantity-0"]', '10');

    // Submit order
    await page.click('[data-testid="submit-order"]');

    // Verify success message
    await expect(page.locator('[data-testid="success-message"]')).toBeVisible();
  });

  test('should display storage spaces', async ({ page }) => {
    await page.goto(`${DASHBOARD_URL}/login`);
    await page.fill('[data-testid="email-input"]', 'partner@example.com');
    await page.fill('[data-testid="password-input"]', 'test-password');
    await page.click('[data-testid="login-button"]');

    await page.click('[data-testid="nav-storage"]');

    // Verify storage metrics
    await expect(page.locator('[data-testid="total-capacity"]')).toBeVisible();
    await expect(page.locator('[data-testid="utilization-percent"]')).toBeVisible();
    await expect(page.locator('[data-testid="available-spaces"]')).toBeVisible();
  });

  test('should create storage booking', async ({ page }) => {
    await page.goto(`${DASHBOARD_URL}/login`);
    await page.fill('[data-testid="email-input"]', 'partner@example.com');
    await page.fill('[data-testid="password-input"]', 'test-password');
    await page.click('[data-testid="login-button"]');

    await page.click('[data-testid="nav-storage"]');
    await page.click('[data-testid="new-booking-button"]');

    // Fill booking form
    await page.selectOption('[data-testid="space-type"]', 'CLIMATE_CONTROLLED');
    await page.selectOption('[data-testid="size-category"]', 'MEDIUM');
    await page.fill('[data-testid="duration"]', '90');
    await page.fill('[data-testid="customer-name"]', 'Test Storage Customer');

    // Submit booking
    await page.click('[data-testid="submit-booking"]');

    // Verify success
    await expect(page.locator('[data-testid="booking-confirmation"]')).toBeVisible();
  });

  test('should display analytics dashboard', async ({ page }) => {
    await page.goto(`${DASHBOARD_URL}/login`);
    await page.fill('[data-testid="email-input"]', 'partner@example.com');
    await page.fill('[data-testid="password-input"]', 'test-password');
    await page.click('[data-testid="login-button"]');

    await page.click('[data-testid="nav-analytics"]');

    // Verify analytics components
    await expect(page.locator('[data-testid="chart-inventory-trend"]')).toBeVisible();
    await expect(page.locator('[data-testid="chart-fulfillment-rate"]')).toBeVisible();
    await expect(page.locator('[data-testid="kpi-cards"]')).toBeVisible();
  });

  test('should export inventory report', async ({ page }) => {
    await page.goto(`${DASHBOARD_URL}/login`);
    await page.fill('[data-testid="email-input"]', 'partner@example.com');
    await page.fill('[data-testid="password-input"]', 'test-password');
    await page.click('[data-testid="login-button"]');

    await page.click('[data-testid="nav-reports"]');
    await page.selectOption('[data-testid="report-type"]', 'INVENTORY_SUMMARY');
    await page.click('[data-testid="export-button"]');

    // Verify download started (check for download event)
    const downloadPromise = page.waitForEvent('download');
    await page.click('[data-testid="confirm-export"]');
    const download = await downloadPromise;
    expect(download.suggestedFilename()).toMatch(/\.(xlsx|csv)$/);
  });

  test('should handle real-time updates', async ({ page }) => {
    await page.goto(`${DASHBOARD_URL}/login`);
    await page.fill('[data-testid="email-input"]', 'partner@example.com');
    await page.fill('[data-testid="password-input"]', 'test-password');
    await page.click('[data-testid="login-button"]');

    await page.click('[data-testid="nav-inventory"]');

    // Get initial count
    const initialCount = await page.textContent('[data-testid="total-items"]');

    // Simulate real-time update notification
    await page.evaluate(() => {
      window.dispatchEvent(new CustomEvent('inventory-update', {
        detail: { type: 'STOCK_CHANGE', sku: 'TEST-SKU-001', quantity: 50 }
      }));
    });

    // Wait for update indicator
    await expect(page.locator('[data-testid="update-indicator"]')).toBeVisible({ timeout: 3000 });
  });

  test('should logout successfully', async ({ page }) => {
    await page.goto(`${DASHBOARD_URL}/login`);
    await page.fill('[data-testid="email-input"]', 'partner@example.com');
    await page.fill('[data-testid="password-input"]', 'test-password');
    await page.click('[data-testid="login-button"]');

    // Click user menu and logout
    await page.click('[data-testid="user-menu"]');
    await page.click('[data-testid="logout-button"]');

    // Verify redirected to login
    await expect(page).toHaveURL(/\/login/);
  });

  test('should display notifications', async ({ page }) => {
    await page.goto(`${DASHBOARD_URL}/login`);
    await page.fill('[data-testid="email-input"]', 'partner@example.com');
    await page.fill('[data-testid="password-input"]', 'test-password');
    await page.click('[data-testid="login-button"]');

    // Click notification bell
    await page.click('[data-testid="notification-bell"]');

    // Verify notification panel opens
    await expect(page.locator('[data-testid="notification-panel"]')).toBeVisible();
  });

  test('should navigate between sections using breadcrumbs', async ({ page }) => {
    await page.goto(`${DASHBOARD_URL}/login`);
    await page.fill('[data-testid="email-input"]', 'partner@example.com');
    await page.fill('[data-testid="password-input"]', 'test-password');
    await page.click('[data-testid="login-button"]');

    await page.click('[data-testid="nav-inventory"]');
    await expect(page.locator('[data-testid="breadcrumb-inventory"]')).toBeVisible();

    await page.click('[data-testid="nav-fulfillment"]');
    await expect(page.locator('[data-testid="breadcrumb-fulfillment"]')).toBeVisible();
  });
});
