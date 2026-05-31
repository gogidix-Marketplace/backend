import { defineConfig, devices } from '@playwright/test';

/**
 * Playwright Configuration for Warehousing Domain E2E Tests
 *
 * This configuration supports testing:
 * - REST API endpoints for all services
 * - Web dashboards (Partners Dashboard, Private Storage Dashboard)
 * - Mobile applications (Warehouse Staff App, Vendor Self Storage App)
 * - Cross-service integration scenarios
 */
export default defineConfig({
  testDir: './tests',
  fullyParallel: true,
  forbidOnly: !!process.env.CI,
  retries: process.env.CI ? 2 : 0,
  workers: process.env.CI ? 4 : undefined,
  reporter: [
    ['html', { outputFolder: 'playwright-report', open: 'never' }],
    ['json', { outputFile: 'test-results.json' }],
    ['junit', { outputFile: 'test-results-junit.xml' }],
    ['list'],
  ],

  use: {
    baseURL: process.env.BASE_URL || 'http://localhost:3000',
    trace: 'on-first-retry',
    screenshot: 'only-on-failure',
    video: 'retain-on-failure',
    headless: true,
    actionTimeout: 10000,
    navigationTimeout: 30000,
  },

  projects: [
    {
      name: 'api-tests',
      testMatch: /tests\/api\/.*\.spec\.ts/,
      use: {
        // API tests don't need a browser
      },
    },

    {
      name: 'chromium-web',
      testMatch: /tests\/(dashboards|web)\/.*\.spec\.ts/,
      use: { ...devices['Desktop Chrome'] },
    },

    {
      name: 'firefox-web',
      testMatch: /tests\/(dashboards|web)\/.*\.spec\.ts/,
      use: { ...devices['Desktop Firefox'] },
    },

    {
      name: 'webkit-web',
      testMatch: /tests\/(dashboards|web)\/.*\.spec\.ts/,
      use: { ...devices['Desktop Safari'] },
    },

    {
      name: 'mobile-chrome',
      testMatch: /tests\/mobile\/.*\.spec\.ts/,
      use: { ...devices['Pixel 5'] },
    },

    {
      name: 'mobile-ios',
      testMatch: /tests\/mobile\/.*\.spec\.ts/,
      use: { ...devices['iPhone 12'] },
    },

    {
      name: 'cross-service-tests',
      testMatch: /tests\/cross-service\/.*\.spec\.ts/,
      use: { ...devices['Desktop Chrome'] },
      fullyParallel: false, // Cross-service tests should run sequentially
    },
  ],

  // Test server configuration for local development
  webServer: process.env.CI
    ? undefined
    : {
        command: 'npm run dev',
        url: 'http://localhost:3000',
        reuseExistingServer: !process.env.CI,
        timeout: 120000,
      },
});
