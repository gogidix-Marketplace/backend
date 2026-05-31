import { test, expect, APIRequestContext, APIResponse } from '@playwright/test';
import { createClient } from '@playwright/test';

const API_BASE_URL = process.env.API_BASE_URL || 'http://localhost:8081/api/v1/inventory';
const TENANT_ID = process.env.TENANT_ID || 'test-tenant-001';

/**
 * Helper function to create authenticated API context
 */
async function createAuthenticatedContext(): Promise<APIRequestContext> {
  const context = await createClient();
  return context;
}

/**
 * Inventory API E2E Tests
 *
 * Tests the complete inventory management flow:
 * - Item creation and management
 * - Stock level tracking
 * - Batch/lot management
 * - Expiration tracking
 * - Multi-tenancy isolation
 */
test.describe('Inventory API Flow', () => {
  let apiContext: APIRequestContext;
  let testItemId: string;
  let testSKU: string;

  test.beforeAll(async () => {
    apiContext = await createAuthenticatedContext();
    testSKU = `TEST-SKU-${Date.now()}`;
  });

  test.afterAll(async () => {
    // Cleanup test data
    if (testItemId) {
      await apiContext.delete(`${API_BASE_URL}/items/${testItemId}`);
    }
  });

  test('GET /health - Service health check', async () => {
    const response = await apiContext.get(`${API_BASE_URL.replace('/api/v1/inventory', '')}/actuator/health`);
    expect(response.status()).toBe(200);
    const health = await response.json();
    expect(health.status).toBe('UP');
  });

  test('POST /items - Create inventory item', async () => {
    const itemData = {
      sku: testSKU,
      name: 'Test Inventory Item',
      description: 'Automated test item',
      quantity: 100,
      unitOfMeasure: 'EA',
      locationId: 'ZONE-A-AISLE-01-SHELF-01-BIN-01',
      tenantId: TENANT_ID,
      attributes: {
        category: 'ELECTRONICS',
        brand: 'TestBrand',
        model: 'TestModel'
      }
    };

    const response = await apiContext.post(`${API_BASE_URL}/items`, {
      data: itemData,
      headers: {
        'X-Tenant-ID': TENANT_ID,
        'Content-Type': 'application/json'
      }
    });

    expect(response.status()).toBe(201);
    const item = await response.json();
    expect(item).toMatchObject({
      sku: testSKU,
      name: 'Test Inventory Item',
      quantity: 100
    });
    testItemId = item.id;
  });

  test('GET /items - List items with tenant isolation', async () => {
    const response = await apiContext.get(`${API_BASE_URL}/items`, {
      headers: {
        'X-Tenant-ID': TENANT_ID
      },
      params: {
        page: 0,
        size: 20
      }
    });

    expect(response.status()).toBe(200);
    const result = await response.json();
    expect(Array.isArray(result.content || result.items || result)).toBe(true);
    // Verify our test item exists
    const items = result.content || result.items || result;
    const testItem = items.find((item: any) => item.sku === testSKU);
    expect(testItem).toBeDefined();
  });

  test('GET /items/{id} - Get item by ID', async () => {
    const response = await apiContext.get(`${API_BASE_URL}/items/${testItemId}`, {
      headers: {
        'X-Tenant-ID': TENANT_ID
      }
    });

    expect(response.status()).toBe(200);
    const item = await response.json();
    expect(item.id).toBe(testItemId);
    expect(item.sku).toBe(testSKU);
  });

  test('POST /adjust - Adjust inventory quantity', async () => {
    const adjustmentData = {
      itemId: testItemId,
      quantity: 50,
      reason: 'STOCK_IN',
      reference: 'INITIAL_STOCK',
      notes: 'Initial stock adjustment'
    };

    const response = await apiContext.post(`${API_BASE_URL}/adjust`, {
      data: adjustmentData,
      headers: {
        'X-Tenant-ID': TENANT_ID
      }
    });

    expect(response.status()).toBe(200);
    const result = await response.json();
    expect(result.newQuantity).toBe(150); // 100 + 50
  });

  test('GET /items/low-stock - Get low stock items', async () => {
    // First, create a low stock item
    const lowStockSKU = `LOW-STOCK-${Date.now()}`;
    const lowStockItem = {
      sku: lowStockSKU,
      name: 'Low Stock Test Item',
      quantity: 5,
      reorderThreshold: 10,
      unitOfMeasure: 'EA',
      tenantId: TENANT_ID
    };

    await apiContext.post(`${API_BASE_URL}/items`, {
      data: lowStockItem,
      headers: { 'X-Tenant-ID': TENANT_ID }
    });

    const response = await apiContext.get(`${API_BASE_URL}/items/low-stock`, {
      headers: {
        'X-Tenant-ID': TENANT_ID
      },
      params: {
        threshold: 10
      }
    });

    expect(response.status()).toBe(200);
    const items = await response.json();
    expect(Array.isArray(items)).toBe(true);
    expect(items.some((item: any) => item.quantity < 10)).toBe(true);

    // Cleanup
    const createdItem = await apiContext.get(`${API_BASE_URL}/items/sku/${lowStockSKU}`, {
      headers: { 'X-Tenant-ID': TENANT_ID }
    });
    const itemData = await createdItem.json();
    await apiContext.delete(`${API_BASE_URL}/items/${itemData.id}`);
  });

  test('PUT /items/{id} - Update item', async () => {
    const updateData = {
      name: 'Updated Test Inventory Item',
      description: 'Updated description'
    };

    const response = await apiContext.put(`${API_BASE_URL}/items/${testItemId}`, {
      data: updateData,
      headers: {
        'X-Tenant-ID': TENANT_ID
      }
    });

    expect(response.status()).toBe(200);
    const updated = await response.json();
    expect(updated.name).toBe('Updated Test Inventory Item');
    expect(updated.description).toBe('Updated description');
  });

  test('POST /adjust - Negative adjustment (stock out)', async () => {
    const adjustmentData = {
      itemId: testItemId,
      quantity: -160,
      reason: 'SALE',
      reference: 'TEST-SALE-001',
      notes: 'Test sale'
    };

    const response = await apiContext.post(`${API_BASE_URL}/adjust`, {
      data: adjustmentData,
      headers: {
        'X-Tenant-ID': TENANT_ID
      }
    });

    expect(response.status()).toBe(200);
    const result = await response.json();
    expect(result.newQuantity).toBe(-10); // 150 - 160 = -10
  });

  test('GET /audit - Get audit trail', async () => {
    const response = await apiContext.get(`${API_BASE_URL}/audit`, {
      headers: {
        'X-Tenant-ID': TENANT_ID
      },
      params: {
        itemId: testItemId,
        page: 0,
        size: 10
      }
    });

    expect(response.status()).toBe(200);
    const audit = await response.json();
    expect(Array.isArray(audit.content || audit.records || audit)).toBe(true);
  });

  test('Multi-tenancy isolation - Cannot access other tenant data', async () => {
    const otherTenantId = 'other-tenant-999';

    const response = await apiContext.get(`${API_BASE_URL}/items`, {
      headers: {
        'X-Tenant-ID': otherTenantId
      }
    });

    expect(response.status()).toBe(200);
    const result = await response.json();
    const items = result.content || result.items || result;
    // Should not find our test item in different tenant
    expect(items.some((item: any) => item.sku === testSKU)).toBe(false);
  });

  test('GET /metrics - Prometheus metrics endpoint', async () => {
    const response = await apiContext.get(
      `${API_BASE_URL.replace('/api/v1/inventory', '')}/actuator/prometheus`,
      {
        headers: {
          'Accept': 'text/plain'
        }
      }
    );

    expect(response.status()).toBe(200);
    const metrics = await response.text();
    expect(metrics).toContain('jvm_');
    expect(metrics).toContain('http_server_requests');
  });
});
