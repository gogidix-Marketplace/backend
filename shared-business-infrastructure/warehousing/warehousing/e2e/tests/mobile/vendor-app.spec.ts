import { test, expect } from '@playwright/test';

/**
 * Vendor Self Storage App E2E Tests (Mobile)
 *
 * Tests the vendor self-storage mobile app functionality:
 * - Vendor authentication
 * - Inventory sync
 * - Location management
 * - Barcode scanning
 * - Performance tracking
 */
test.describe('Vendor Self Storage App', () => {
  const APP_URL = process.env.VENDOR_APP_URL || 'http://localhost:19006';

  test.use({
    ...devices['Pixel 5'],
    locale: 'en-US',
    timezoneId: 'America/New_York',
  });

  test.beforeEach(async ({ page }) => {
    await page.goto(APP_URL);
  });

  test('should load vendor app homepage', async ({ page }) => {
    await expect(page).toHaveTitle(/Vendor Storage/);
  });

  test('should login as vendor', async ({ page }) => {
    await page.fill('[data-testid="vendor-id-input"]', 'VENDOR-001');
    await page.fill('[data-testid="password-input"]', 'vendor-password');
    await page.tap('[data-testid="login-button"]');

    // Should navigate to dashboard
    await expect(page.locator('[data-testid="vendor-dashboard"]')).toBeVisible({ timeout: 10000 });
  });

  test('should display vendor inventory', async ({ page }) => {
    await page.fill('[data-testid="vendor-id-input"]', 'VENDOR-001');
    await page.fill('[data-testid="password-input"]', 'vendor-password');
    await page.tap('[data-testid="login-button"]');

    await page.tap('[data-testid="inventory-tab"]');

    // Verify inventory list
    await expect(page.locator('[data-testid="inventory-list"]')).toBeVisible();
    const items = page.locator('[data-testid="inventory-item"]');
    expect(await items.count()).toBeGreaterThan(0);
  });

  test('should add inventory item', async ({ page }) => {
    await page.fill('[data-testid="vendor-id-input"]', 'VENDOR-001');
    await page.fill('[data-testid="password-input"]', 'vendor-password');
    await page.tap('[data-testid="login-button"]');

    await page.tap('[data-testid="inventory-tab"]');
    await page.tap('[data-testid="add-item-button"]');

    // Fill item details
    await page.fill('[data-testid="item-sku"]', `VENDOR-SKU-${Date.now()}`);
    await page.fill('[data-testid="item-name"]', 'Test Vendor Item');
    await page.fill('[data-testid="item-quantity"]', '50');
    await page.selectOption('[data-testid="item-category"]', 'ELECTRONICS');

    await page.tap('[data-testid="save-item-button"]');

    // Verify success
    await expect(page.locator('[data-testid="success-toast"]')).toBeVisible();
  });

  test('should sync inventory with server', async ({ page }) => {
    await page.fill('[data-testid="vendor-id-input"]', 'VENDOR-001');
    await page.fill('[data-testid="password-input"]', 'vendor-password');
    await page.tap('[data-testid="login-button"]');

    await page.tap('[data-testid="inventory-tab"]');

    // Pull to refresh (simulate swipe down)
    await page.touchscreen.tap(0, 0);

    // Wait for sync indicator
    await expect(page.locator('[data-testid="sync-indicator"]')).toBeVisible({ timeout: 5000 });
    await expect(page.locator('[data-testid="sync-complete"]')).toBeVisible({ timeout: 10000 });
  });

  test('should scan barcode', async ({ page }) => {
    await page.fill('[data-testid="vendor-id-input"]', 'VENDOR-001');
    await page.fill('[data-testid="password-input"]', 'vendor-password');
    await page.tap('[data-testid="login-button"]');

    await page.tap('[data-testid="inventory-tab"]');
    await page.tap('[data-testid="scan-barcode-button"]');

    // Camera permission should be requested
    await expect(page.locator('[data-testid="camera-permission-dialog"]')).toBeVisible();
    await page.tap('[data-testid="allow-camera"]');

    // Simulate barcode scan result
    await page.evaluate(() => {
      window.dispatchEvent(new CustomEvent('barcode-scanned', {
        detail: { barcode: '1234567890123', type: 'EAN13' }
      }));
    });

    // Verify item lookup
    await expect(page.locator('[data-testid="item-details"]')).toBeVisible();
  });

  test('should manage vendor locations', async ({ page }) => {
    await page.fill('[data-testid="vendor-id-input"]', 'VENDOR-001');
    await page.fill('[data-testid="password-input"]', 'vendor-password');
    await page.tap('[data-testid="login-button"]');

    await page.tap('[data-testid="locations-tab"]');

    // Verify locations list
    await expect(page.locator('[data-testid="locations-list"]')).toBeVisible();

    // Add new location
    await page.tap('[data-testid="add-location-button"]');

    await page.fill('[data-testid="location-name"]', 'Test Warehouse Location');
    await page.fill('[data-testid="location-address"]', '123 Vendor St, Test City');
    await page.fill('[data-testid="location-capacity"]', '1000');

    await page.tap('[data-testid="save-location-button"]');

    // Verify location added
    await expect(page.locator('[data-testid="location-added-toast"]')).toBeVisible();
  });

  test('should view performance metrics', async ({ page }) => {
    await page.fill('[data-testid="vendor-id-input"]', 'VENDOR-001');
    await page.fill('[data-testid="password-input"]', 'vendor-password');
    await page.tap('[data-testid="login-button"]');

    await page.tap('[data-testid="performance-tab"]');

    // Verify metrics display
    await expect(page.locator('[data-testid="fulfillment-rate"]')).toBeVisible();
    await expect(page.locator('[data-testid="capacity-utilization"]')).toBeVisible();
    await expect(page.locator('[data-testid="quality-score"]')).toBeVisible();
  });

  test('should receive push notifications', async ({ page }) => {
    await page.fill('[data-testid="vendor-id-input"]', 'VENDOR-001');
    await page.fill('[data-testid="password-input"]', 'vendor-password');
    await page.tap('[data-testid="login-button"]');

    // Grant notification permission
    await page.evaluate(() => {
      return Notification.requestPermission();
    });

    // Simulate incoming notification
    await page.evaluate(() => {
      new Notification('New Order', {
        body: 'You have a new order to fulfill',
        icon: '/notification-icon.png'
      });
    });

    // Verify notification display
    await expect(page.locator('[data-testid="notification-banner"]')).toBeVisible();
  });

  test('should work offline', async ({ page }) => {
    // Login while online
    await page.fill('[data-testid="vendor-id-input"]', 'VENDOR-001');
    await page.fill('[data-testid="password-input"]', 'vendor-password');
    await page.tap('[data-testid="login-button"]');

    // Go offline
    await page.context().setOffline(true);

    // Navigate to inventory
    await page.tap('[data-testid="inventory-tab"]');

    // Verify cached data is displayed
    await expect(page.locator('[data-testid="inventory-list"]')).toBeVisible();

    // Try to add item (should queue for sync)
    await page.tap('[data-testid="add-item-button"]');

    await page.fill('[data-testid="item-sku"]', 'OFFLINE-ITEM-001');
    await page.fill('[data-testid="item-name"]', 'Offline Test Item');
    await page.fill('[data-testid="item-quantity"]', '25');

    await page.tap('[data-testid="save-item-button"]');

    // Verify queued for sync indicator
    await expect(page.locator('[data-testid="pending-sync-indicator"]')).toBeVisible();

    // Go back online
    await page.context().setOffline(false);

    // Verify sync starts
    await expect(page.locator('[data-testid="syncing-indicator"]')).toBeVisible();
  });

  test('should handle biometric authentication', async ({ page }) => {
    // First login with credentials
    await page.fill('[data-testid="vendor-id-input"]', 'VENDOR-001');
    await page.fill('[data-testid="password-input"]', 'vendor-password');
    await page.tap('[data-testid="login-button"]');

    // Enable biometric
    await page.tap('[data-testid="user-menu"]');
    await page.tap('[data-testid="settings-button"]');
    await page.tap('[data-testid="enable-biometric-toggle"]');

    await page.tap('[data-testid="logout-button"]');

    // Login with biometric
    await page.fill('[data-testid="vendor-id-input"]', 'VENDOR-001');
    await page.tap('[data-testid="biometric-login-button"]');

    // Simulate biometric success
    await page.evaluate(() => {
      window.dispatchEvent(new CustomEvent('biometric-auth-success'));
    });

    // Verify logged in
    await expect(page.locator('[data-testid="vendor-dashboard"]')).toBeVisible();
  });

  test('should adjust inventory quantity', async ({ page }) => {
    await page.fill('[data-testid="vendor-id-input"]', 'VENDOR-001');
    await page.fill('[data-testid="password-input"]', 'vendor-password');
    await page.tap('[data-testid="login-button"]');

    await page.tap('[data-testid="inventory-tab"]');

    // Tap on first item
    await page.tap('[data-testid="inventory-item"]:first-child');

    // Adjust quantity
    await page.tap('[data-testid="adjust-quantity-button"]');
    await page.fill('[data-testid="new-quantity"]', '100');
    await page.selectOption('[data-testid="adjustment-reason"]', 'STOCK_COUNT');
    await page.tap('[data-testid="confirm-adjustment"]');

    // Verify success
    await expect(page.locator('[data-testid="adjustment-success-toast"]')).toBeVisible();
  });

  test('should view order history', async ({ page }) => {
    await page.fill('[data-testid="vendor-id-input"]', 'VENDOR-001');
    await page.fill('[data-testid="password-input"]', 'vendor-password');
    await page.tap('[data-testid="login-button"]');

    await page.tap('[data-testid="orders-tab"]');

    // Verify orders list
    await expect(page.locator('[data-testid="orders-list"]')).toBeVisible();

    // Tap on order
    await page.tap('[data-testid="order-card"]:first-child');

    // Verify order details
    await expect(page.locator('[data-testid="order-details"]')).toBeVisible();
    await expect(page.locator('[data-testid="order-items"]')).toBeVisible();
  });

  test('should report issue', async ({ page }) => {
    await page.fill('[data-testid="vendor-id-input"]', 'VENDOR-001');
    await page.fill('[data-testid="password-input"]', 'vendor-password');
    await page.tap('[data-testid="login-button"]');

    await page.tap('[data-testid="support-tab"]');
    await page.tap('[data-testid="report-issue-button"]');

    await page.selectOption('[data-testid="issue-category"]', 'TECHNICAL');
    await page.fill('[data-testid="issue-description"]', 'App is crashing when I try to scan barcodes');
    await page.tap('[data-testid="submit-issue"]');

    // Verify submission
    await expect(page.locator('[data-testid="issue-submitted-toast"]')).toBeVisible();
  });
});
