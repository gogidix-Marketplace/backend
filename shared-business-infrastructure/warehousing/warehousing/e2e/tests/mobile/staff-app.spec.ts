import { test, expect } from '@playwright/test';

/**
 * Warehouse Staff App E2E Tests (Mobile)
 *
 * Tests the warehouse staff mobile app functionality:
 * - Staff authentication
 * - Picking workflows
 * - Packing workflows
 * - Barcode scanning
 * - Task management
 */
test.describe('Warehouse Staff App', () => {
  const APP_URL = process.env.STAFF_APP_URL || 'http://localhost:19007';

  test.use({
    ...devices['iPhone 12'],
    locale: 'en-US',
    timezoneId: 'America/New_York',
  });

  test.beforeEach(async ({ page }) => {
    await page.goto(APP_URL);
  });

  test('should load staff app homepage', async ({ page }) => {
    await expect(page).toHaveTitle(/Warehouse Staff/);
  });

  test('should login as warehouse staff', async ({ page }) => {
    await page.fill('[data-testid="staff-id-input"]', 'STAFF-001');
    await page.fill('[data-testid="password-input"]', 'staff-password');
    await page.tap('[data-testid="login-button"]');

    // Should navigate to home screen
    await expect(page.locator('[data-testid="staff-home"]')).toBeVisible({ timeout: 10000 });
  });

  test('should display assigned tasks', async ({ page }) => {
    await page.fill('[data-testid="staff-id-input"]', 'STAFF-001');
    await page.fill('[data-testid="password-input"]', 'staff-password');
    await page.tap('[data-testid="login-button"]');

    // Verify tasks are displayed
    await expect(page.locator('[data-testid="tasks-list"]')).toBeVisible();
    const tasks = page.locator('[data-testid="task-card"]');
    expect(await tasks.count()).toBeGreaterThan(0);
  });

  test('should start picking task', async ({ page }) => {
    await page.fill('[data-testid="staff-id-input"]', 'STAFF-001');
    await page.fill('[data-testid="password-input"]', 'staff-password');
    await page.tap('[data-testid="login-button"]');

    // Find first picking task
    await page.tap('[data-testid="task-card"][data-task-type="PICK"]:first-child');

    // View task details
    await expect(page.locator('[data-testid="task-details"]')).toBeVisible();
    await expect(page.locator('[data-testid="order-number"]')).toBeVisible();
    await expect(page.locator('[data-testid="items-to-pick"]')).toBeVisible();

    // Start task
    await page.tap('[data-testid="start-task-button"]');

    // Verify task started
    await expect(page.locator('[data-testid="task-in-progress"]')).toBeVisible();
  });

  test('should scan item during picking', async ({ page }) => {
    await page.fill('[data-testid="staff-id-input"]', 'STAFF-001');
    await page.fill('[data-testid="password-input"]', 'staff-password');
    await page.tap('[data-testid="login-button"]');

    await page.tap('[data-testid="task-card"][data-task-type="PICK"]:first-child');
    await page.tap('[data-testid="start-task-button"]');

    // Request camera permission
    await page.tap('[data-testid="scan-barcode-button"]');
    await page.tap('[data-testid="allow-camera"]');

    // Simulate barcode scan
    await page.evaluate(() => {
      window.dispatchEvent(new CustomEvent('barcode-scanned', {
        detail: { barcode: 'ITEM-001', type: 'CODE128' }
      }));
    });

    // Verify item recognized
    await expect(page.locator('[data-testid="item-verified"]')).toBeVisible();

    // Enter quantity
    await page.fill('[data-testid="quantity-picked"]', '10');
    await page.tap('[data-testid="confirm-pick"]');

    // Verify pick recorded
    await expect(page.locator('[data-testid="pick-recorded"]')).toBeVisible();
  });

  test('should complete picking task', async ({ page }) => {
    await page.fill('[data-testid="staff-id-input"]', 'STAFF-001');
    await page.fill('[data-testid="password-input"]', 'staff-password');
    await page.tap('[data-testid="login-button"]');

    await page.tap('[data-testid="task-card"][data-task-type="PICK"]:first-child');
    await page.tap('[data-testid="start-task-button"]');

    // Pick all items (simulation)
    await page.tap('[data-testid="complete-task-button"]');

    // Confirm completion
    await page.tap('[data-testid="confirm-complete-button"]');

    // Verify task completed
    await expect(page.locator('[data-testid="task-completed"]')).toBeVisible();
  });

  test('should handle packing workflow', async ({ page }) => {
    await page.fill('[data-testid="staff-id-input"]', 'STAFF-001');
    await page.fill('[data-testid="password-input"]', 'staff-password');
    await page.tap('[data-testid="login-button"]');

    await page.tap('[data-testid="task-card"][data-task-type="PACK"]:first-child');

    // View packing details
    await expect(page.locator('[data-testid="packing-list"]')).toBeVisible();

    // Start packing
    await page.tap('[data-testid="start-packing-button"]');

    // Select box type
    await page.selectOption('[data-testid="box-type"]', 'STANDARD');

    // Scan item to pack
    await page.tap('[data-testid="scan-to-pack-button"]');

    await page.evaluate(() => {
      window.dispatchEvent(new CustomEvent('barcode-scanned', {
        detail: { barcode: 'ITEM-001', type: 'CODE128' }
      }));
    });

    // Verify item added to box
    await expect(page.locator('[data-testid="box-contents"]')).toBeVisible();

    // Complete packing
    await page.tap('[data-testid="complete-packing-button"]');
    await page.tap('[data-testid="confirm-packing"]');

    // Verify packing completed
    await expect(page.locator('[data-testid="packing-completed"]')).toBeVisible();
  });

  test('should print shipping label', async ({ page }) => {
    await page.fill('[data-testid="staff-id-input"]', 'STAFF-001');
    await page.fill('[data-testid="password-input"]', 'staff-password');
    await page.tap('[data-testid="login-button"]');

    await page.tap('[data-testid="task-card"][data-task-type="PACK"]:first-child');

    // Complete packing first
    await page.tap('[data-testid="start-packing-button"]');
    await page.selectOption('[data-testid="box-type"]', 'STANDARD');
    await page.tap('[data-testid="complete-packing-button"]');
    await page.tap('[data-testid="confirm-packing"]');

    // Print label
    await page.tap('[data-testid="print-label-button"]');

    // Verify print dialog
    await expect(page.locator('[data-testid="label-preview"]')).toBeVisible();
    await page.tap('[data-testid="confirm-print"]');

    // Verify label printed
    await expect(page.locator('[data-testid="label-printed"]')).toBeVisible();
  });

  test('should report quality issue', async ({ page }) => {
    await page.fill('[data-testid="staff-id-input"]', 'STAFF-001');
    await page.fill('[data-testid="password-input"]', 'staff-password');
    await page.tap('[data-testid="login-button"]');

    await page.tap('[data-testid="task-card"][data-task-type="PICK"]:first-child');
    await page.tap('[data-testid="start-task-button"]');

    // Report issue with item
    await page.tap('[data-testid="report-issue-button"]');
    await page.selectOption('[data-testid="issue-type"]', 'DAMAGED');

    // Scan damaged item
    await page.tap('[data-testid="scan-item-button"]');

    await page.evaluate(() => {
      window.dispatchEvent(new CustomEvent('barcode-scanned', {
        detail: { barcode: 'ITEM-001', type: 'CODE128' }
      }));
    });

    // Add notes
    await page.fill('[data-testid="issue-notes"]', 'Item packaging is torn');

    // Take photo (simulate)
    await page.tap('[data-testid="take-photo-button"]');
    await page.tap('[data-testid="use-photo"]');

    // Submit report
    await page.tap('[data-testid="submit-issue"]');

    // Verify issue reported
    await expect(page.locator('[data-testid="issue-reported"]')).toBeVisible();
  });

  test('should view productivity stats', async ({ page }) => {
    await page.fill('[data-testid="staff-id-input"]', 'STAFF-001');
    await page.fill('[data-testid="password-input"]', 'staff-password');
    await page.tap('[data-testid="login-button"]');

    await page.tap('[data-testid="stats-tab"]');

    // Verify stats display
    await expect(page.locator('[data-testid="picks-today"]')).toBeVisible();
    await expect(page.locator('[data-testid="packs-today"]')).toBeVisible();
    await expect(page.locator('[data-testid="accuracy-rate"]')).toBeVisible();
    await expect(page.locator('[data-testid="tasks-completed"]')).toBeVisible();
  });

  test('should handle task reassignment', async ({ page }) => {
    await page.fill('[data-testid="staff-id-input"]', 'STAFF-001');
    await page.fill('[data-testid="password-input"]', 'staff-password');
    await page.tap('[data-testid="login-button"]');

    await page.tap('[data-testid="task-card"]:first-child');

    // Request reassignment
    await page.tap('[data-testid="more-options"]');
    await page.tap('[data-testid="request-reassignment-button"]');

    // Select reason
    await page.selectOption('[data-testid="reassignment-reason"]', 'EQUIPMENT_ISSUE');
    await page.fill('[data-testid="reassignment-notes"]', 'Scanner not working');

    await page.tap('[data-testid="submit-reassignment"]');

    // Verify request submitted
    await expect(page.locator('[data-testid="reassignment-requested"]')).toBeVisible();
  });

  test('should sync data when connection restored', async ({ page }) => {
    // Login while online
    await page.fill('[data-testid="staff-id-input"]', 'STAFF-001');
    await page.fill('[data-testid="password-input"]', 'staff-password');
    await page.tap('[data-testid="login-button"]');

    // Go offline
    await page.context().setOffline(true);

    // Complete a task (should queue)
    await page.tap('[data-testid="task-card"]:first-child');
    await page.tap('[data-testid="start-task-button"]');
    await page.tap('[data-testid="complete-task-button"]');
    await page.tap('[data-testid="confirm-complete-button"]');

    // Verify offline indicator
    await expect(page.locator('[data-testid="offline-indicator"]')).toBeVisible();

    // Go back online
    await page.context().setOffline(false);

    // Verify sync
    await expect(page.locator('[data-testid="syncing-indicator"]')).toBeVisible();
    await expect(page.locator('[data-testid="sync-complete"]')).toBeVisible({ timeout: 10000 });
  });

  test('should use voice commands', async ({ page }) => {
    await page.fill('[data-testid="staff-id-input"]', 'STAFF-001');
    await page.fill('[data-testid="password-input"]', 'staff-password');
    await page.tap('[data-testid="login-button"]');

    // Enable voice (simulate permission)
    await page.tap('[data-testid="voice-button"]');

    // Simulate voice command result
    await page.evaluate(() => {
      window.dispatchEvent(new CustomEvent('voice-command', {
        detail: { command: 'next task', confidence: 0.95 }
      }));
    });

    // Verify navigation to next task
    await expect(page.locator('[data-testid="task-details"]')).toBeVisible();
  });

  test('should view shift schedule', async ({ page }) => {
    await page.fill('[data-testid="staff-id-input"]', 'STAFF-001');
    await page.fill('[data-testid="password-input"]', 'staff-password');
    await page.tap('[data-testid="login-button"]');

    await page.tap('[data-testid="schedule-tab"]');

    // Verify schedule displayed
    await expect(page.locator('[data-testid="shift-calendar"]')).toBeVisible();
    await expect(page.locator('[data-testid="today-shift"]')).toBeVisible();
  });

  test('should clock in/out', async ({ page }) => {
    await page.fill('[data-testid="staff-id-input"]', 'STAFF-001');
    await page.fill('[data-testid="password-input"]', 'staff-password');
    await page.tap('[data-testid="login-button"]');

    // Clock in
    await page.tap('[data-testid="clock-in-button"]');

    // Verify clocked in state
    await expect(page.locator('[data-testid="clocked-in-indicator"]')).toBeVisible();

    // Clock out
    await page.tap('[data-testid="clock-out-button"]');

    // Confirm
    await page.tap('[data-testid="confirm-clock-out"]');

    // Verify clocked out
    await expect(page.locator('[data-testid="clocked-out-indicator"]')).toBeVisible();
  });

  test('should take break', async ({ page }) => {
    await page.fill('[data-testid="staff-id-input"]', 'STAFF-001');
    await page.fill('[data-testid="password-input"]', 'staff-password');
    await page.tap('[data-testid="login-button"]');

    // Clock in first
    await page.tap('[data-testid="clock-in-button"]');

    // Start break
    await page.tap('[data-testid="start-break-button"]');
    await page.selectOption('[data-testid="break-type"]', 'LUNCH');

    await page.tap('[data-testid="confirm-break"]');

    // Verify break started
    await expect(page.locator('[data-testid="on-break-indicator"]')).toBeVisible();

    // End break
    await page.tap('[data-testid="end-break-button"]');

    // Verify break ended
    await expect(page.locator('[data-testid="back-to-work-indicator"]')).toBeVisible();
  });
});
