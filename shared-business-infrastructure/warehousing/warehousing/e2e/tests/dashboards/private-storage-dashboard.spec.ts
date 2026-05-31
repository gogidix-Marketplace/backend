import { test, expect } from '@playwright/test';

const DASHBOARD_URL = process.env.PRIVATE_STORAGE_DASHBOARD_URL || 'http://localhost:3002';

/**
 * Private Storage Dashboard E2E Tests
 *
 * Tests the private storage customer dashboard functionality:
 * - Customer portal login
 * - Storage unit management
 * - Access control
 * - Billing and payments
 * - Support requests
 */
test.describe('Private Storage Dashboard', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto(DASHBOARD_URL);
  });

  test('should load customer portal homepage', async ({ page }) => {
    await expect(page).toHaveTitle(/Private Storage/);
    await expect(page.locator('nav')).toBeVisible();
  });

  test('should redirect to login for unauthenticated customers', async ({ page }) => {
    await page.goto(`${DASHBOARD_URL}/my-storage`);
    await expect(page).toHaveURL(/\/login/);
  });

  test('should login as customer', async ({ page }) => {
    await page.goto(`${DASHBOARD_URL}/login`);

    await page.fill('[data-testid="email-input"]', 'customer@example.com');
    await page.fill('[data-testid="password-input"]', 'customer-password');
    await page.click('[data-testid="login-button"]');

    await expect(page).toHaveURL(/\/my-storage/, { timeout: 5000 });
  });

  test('should display customer storage units', async ({ page }) => {
    await page.goto(`${DASHBOARD_URL}/login`);
    await page.fill('[data-testid="email-input"]', 'customer@example.com');
    await page.fill('[data-testid="password-input"]', 'customer-password');
    await page.click('[data-testid="login-button"]');

    // Verify storage units are displayed
    await expect(page.locator('[data-testid="storage-units-list"]')).toBeVisible();
    const unitCards = page.locator('[data-testid="storage-unit-card"]');
    await expect(unitCards).toHaveCount(await unitCards.count());
  });

  test('should view storage unit details', async ({ page }) => {
    await page.goto(`${DASHBOARD_URL}/login`);
    await page.fill('[data-testid="email-input"]', 'customer@example.com');
    await page.fill('[data-testid="password-input"]', 'customer-password');
    await page.click('[data-testid="login-button"]');

    // Click on first storage unit
    await page.click('[data-testid="storage-unit-card"]:first-child');

    // Verify unit details page
    await expect(page).toHaveURL(/\/units\/[a-zA-Z0-9-]+/);
    await expect(page.locator('[data-testid="unit-size"]')).toBeVisible();
    await expect(page.locator('[data-testid="unit-type"]')).toBeVisible();
    await expect(page.locator('[data-testid="unit-features"]')).toBeVisible();
  });

  test('should generate access code', async ({ page }) => {
    await page.goto(`${DASHBOARD_URL}/login`);
    await page.fill('[data-testid="email-input"]', 'customer@example.com');
    await page.fill('[data-testid="password-input"]', 'customer-password');
    await page.click('[data-testid="login-button"]');

    await page.click('[data-testid="storage-unit-card"]:first-child');
    await page.click('[data-testid="generate-access-button"]');

    // Verify access code is displayed
    await expect(page.locator('[data-testid="access-code-display"]')).toBeVisible();
    const accessCode = await page.textContent('[data-testid="access-code-display"]');
    expect(accessCode).toMatch(/^\d{4}$/);
  });

  test('should view access history', async ({ page }) => {
    await page.goto(`${DASHBOARD_URL}/login`);
    await page.fill('[data-testid="email-input"]', 'customer@example.com');
    await page.fill('[data-testid="password-input"]', 'customer-password');
    await page.click('[data-testid="login-button"]');

    await page.click('[data-testid="storage-unit-card"]:first-child');
    await page.click('[data-testid="access-history-tab"]');

    // Verify access history is displayed
    await expect(page.locator('[data-testid="access-history-list"]')).toBeVisible();
    const historyEntries = page.locator('[data-testid="access-history-entry"]');
    await expect(historyEntries).toHaveCount(await historyEntries.count());
  });

  test('should view billing summary', async ({ page }) => {
    await page.goto(`${DASHBOARD_URL}/login`);
    await page.fill('[data-testid="email-input"]', 'customer@example.com');
    await page.fill('[data-testid="password-input"]', 'customer-password');
    await page.click('[data-testid="login-button"]');

    await page.click('[data-testid="nav-billing"]');

    // Verify billing information
    await expect(page.locator('[data-testid="current-balance"]')).toBeVisible();
    await expect(page.locator('[data-testid="next-payment-due"]')).toBeVisible();
    await expect(page.locator('[data-testid="payment-history"]')).toBeVisible();
  });

  test('should make payment', async ({ page }) => {
    await page.goto(`${DASHBOARD_URL}/login`);
    await page.fill('[data-testid="email-input"]', 'customer@example.com');
    await page.fill('[data-testid="password-input"]', 'customer-password');
    await page.click('[data-testid="login-button"]');

    await page.click('[data-testid="nav-billing"]');
    await page.click('[data-testid="make-payment-button"]');

    // Fill payment form
    await page.fill('[data-testid="payment-amount"]', '100.00');
    await page.selectOption('[data-testid="payment-method"]', 'CREDIT_CARD');

    await page.click('[data-testid="submit-payment"]');

    // Verify payment confirmation
    await expect(page.locator('[data-testid="payment-confirmation"]')).toBeVisible();
  });

  test('should add payment method', async ({ page }) => {
    await page.goto(`${DASHBOARD_URL}/login`);
    await page.fill('[data-testid="email-input"]', 'customer@example.com');
    await page.fill('[data-testid="password-input"]', 'customer-password');
    await page.click('[data-testid="login-button"]');

    await page.click('[data-testid="nav-billing"]');
    await page.click('[data-testid="manage-payment-methods"]');
    await page.click('[data-testid="add-payment-method"]');

    // Fill payment method form
    await page.fill('[data-testid="card-number"]', '4111111111111111');
    await page.fill('[data-testid="card-name"]', 'Test Customer');
    await page.fill('[data-testid="card-expiry"]', '12/25');
    await page.fill('[data-testid="card-cvv"]', '123');

    await page.click('[data-testid="save-payment-method"]');

    // Verify success message
    await expect(page.locator('[data-testid="success-message"]')).toBeVisible();
  });

  test('should create support request', async ({ page }) => {
    await page.goto(`${DASHBOARD_URL}/login`);
    await page.fill('[data-testid="email-input"]', 'customer@example.com');
    await page.fill('[data-testid="password-input"]', 'customer-password');
    await page.click('[data-testid="login-button"]');

    await page.click('[data-testid="nav-support"]');
    await page.click('[data-testid="new-request-button"]');

    // Fill support request form
    await page.selectOption('[data-testid="request-type"]', 'MAINTENANCE');
    await page.fill('[data-testid="request-subject"]', 'Light not working');
    await page.fill('[data-testid="request-description"]', 'The light in my storage unit is not working.');
    await page.selectOption('[data-testid="priority"]', 'NORMAL');

    await page.click('[data-testid="submit-request"]');

    // Verify request confirmation
    await expect(page.locator('[data-testid="request-confirmation"]')).toBeVisible();
    await expect(page.locator('[data-testid="request-id"]')).toBeVisible();
  });

  test('should view support request status', async ({ page }) => {
    await page.goto(`${DASHBOARD_URL}/login`);
    await page.fill('[data-testid="email-input"]', 'customer@example.com');
    await page.fill('[data-testid="password-input"]', 'customer-password');
    await page.click('[data-testid="login-button"]');

    await page.click('[data-testid="nav-support"]');

    // Verify support requests list
    await expect(page.locator('[data-testid="support-requests-list"]')).toBeVisible();

    // Click on a request
    await page.click('[data-testid="support-request-card"]:first-child');

    // Verify request details
    await expect(page.locator('[data-testid="request-status"]')).toBeVisible();
    await expect(page.locator('[data-testid="request-history"]')).toBeVisible();
  });

  test('should update customer profile', async ({ page }) => {
    await page.goto(`${DASHBOARD_URL}/login`);
    await page.fill('[data-testid="email-input"]', 'customer@example.com');
    await page.fill('[data-testid="password-input"]', 'customer-password');
    await page.click('[data-testid="login-button"]');

    await page.click('[data-testid="user-menu"]');
    await page.click('[data-testid="profile-settings"]');

    // Update profile
    await page.fill('[data-testid="phone-number"]', '+1234567890');
    await page.fill('[data-testid="emergency-contact"]', 'Emergency Contact Name');
    await page.fill('[data-testid="emergency-phone"]', '+1987654321');

    await page.click('[data-testid="save-profile"]');

    // Verify success message
    await expect(page.locator('[data-testid="profile-saved"]')).toBeVisible();
  });

  test('should view storage utilization analytics', async ({ page }) => {
    await page.goto(`${DASHBOARD_URL}/login`);
    await page.fill('[data-testid="email-input"]', 'customer@example.com');
    await page.fill('[data-testid="password-input"]', 'customer-password');
    await page.click('[data-testid="login-button"]');

    await page.click('[data-testid="storage-unit-card"]:first-child');
    await page.click('[data-testid="analytics-tab"]');

    // Verify analytics are displayed
    await expect(page.locator('[data-testid="utilization-chart"]')).toBeVisible();
    await expect(page.locator('[data-testid="item-breakdown"]')).toBeVisible();
  });

  test('should extend storage rental', async ({ page }) => {
    await page.goto(`${DASHBOARD_URL}/login');
    await page.fill('[data-testid="email-input"]', 'customer@example.com');
    await page.fill('[data-testid="password-input"]', 'customer-password');
    await page.click('[data-testid="login-button"]');

    await page.click('[data-testid="storage-unit-card"]:first-child');
    await page.click('[data-testid="extend-rental-button"]');

    // Select extension duration
    await page.selectOption('[data-testid="extension-duration"]', '30');
    await page.click('[data-testid="confirm-extension"]');

    // Verify extension confirmation
    await expect(page.locator('[data-testid="extension-confirmed"]')).toBeVisible();
  });

  test('should request rental termination', async ({ page }) => {
    await page.goto(`${DASHBOARD_URL}/login`);
    await page.fill('[data-testid="email-input"]', 'customer@example.com');
    await page.fill('[data-testid="password-input"]', 'customer-password');
    await page.click('[data-testid="login-button"]');

    await page.click('[data-testid="storage-unit-card"]:first-child');
    await page.click('[data-testid="more-options"]');
    await page.click('[data-testid="terminate-rental-button"]');

    // Fill termination form
    await page.fill('[data-testid="termination-date"]', '2024-12-31');
    await page.fill('[data-testid="termination-reason"]', 'Moving to a new location');

    await page.click('[data-testid="submit-termination"]');

    // Verify termination request submitted
    await expect(page.locator('[data-testid="termination-requested"]')).toBeVisible();
  });

  test('should handle session timeout', async ({ page }) => {
    // Set short timeout for testing
    await page.goto(`${DASHBOARD_URL}/login`);
    await page.fill('[data-testid="email-input"]', 'customer@example.com');
    await page.fill('[data-testid="password-input"]', 'customer-password');
    await page.click('[data-testid="login-button"]');

    // Simulate session timeout
    await page.evaluate(() => {
      localStorage.clear();
      sessionStorage.clear();
    });

    // Try to navigate
    await page.click('[data-testid="nav-billing"]');

    // Should redirect to login
    await expect(page).toHaveURL(/\/login/);
  });
});
