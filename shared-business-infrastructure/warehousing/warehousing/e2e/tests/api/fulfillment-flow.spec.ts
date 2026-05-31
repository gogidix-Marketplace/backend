import { test, expect, APIRequestContext } from '@playwright/test';
import { createClient } from '@playwright/test';

const API_BASE_URL = process.env.FULFILLMENT_API_URL || 'http://localhost:8085/api/v1/fulfillment';
const INVENTORY_API_URL = process.env.INVENTORY_API_URL || 'http://localhost:8081/api/v1/inventory';
const TENANT_ID = process.env.TENANT_ID || 'test-tenant-001';

/**
 * Fulfillment API E2E Tests
 *
 * Tests the complete order fulfillment flow:
 * - Order creation and processing
 * - Picking workflow
 * - Packing workflow
 * - Shipping integration
 * - Returns processing
 */
test.describe('Fulfillment API Flow', () => {
  let apiContext: APIRequestContext;
  let testOrderId: string;
  let testItemId: string;
  let testSKU: string;

  test.beforeAll(async () => {
    apiContext = await createClient();
    testSKU = `FULFILL-SKU-${Date.now()}`;

    // Create test inventory item first
    const itemData = {
      sku: testSKU,
      name: 'Fulfillment Test Item',
      quantity: 500,
      unitOfMeasure: 'EA',
      locationId: 'ZONE-A-AISLE-01-SHELF-01-BIN-01',
      tenantId: TENANT_ID
    };

    const itemResponse = await apiContext.post(`${INVENTORY_API_URL}/items`, {
      data: itemData,
      headers: { 'X-Tenant-ID': TENANT_ID }
    });

    if (itemResponse.ok()) {
      const item = await itemResponse.json();
      testItemId = item.id;
    }
  });

  test.afterAll(async () => {
    // Cleanup test data
    if (testItemId) {
      await apiContext.delete(`${INVENTORY_API_URL}/items/${testItemId}`, {
        headers: { 'X-Tenant-ID': TENANT_ID }
      });
    }
    if (testOrderId) {
      await apiContext.delete(`${API_BASE_URL}/orders/${testOrderId}`, {
        headers: { 'X-Tenant-ID': TENANT_ID }
      });
    }
  });

  test('GET /health - Service health check', async () => {
    const response = await apiContext.get(
      `${API_BASE_URL.replace('/api/v1/fulfillment', '')}/actuator/health`
    );
    expect(response.status()).toBe(200);
    const health = await response.json();
    expect(health.status).toBe('UP');
  });

  test('POST /orders - Create fulfillment order', async () => {
    const orderData = {
      orderNumber: `ORDER-${Date.now()}`,
      customer: {
        id: 'customer-001',
        name: 'Test Customer',
        email: 'test@example.com'
      },
      items: [
        {
          sku: testSKU,
          quantity: 10,
          price: 29.99
        }
      ],
      shipping: {
        method: 'STANDARD',
        address: {
          street: '123 Test St',
          city: 'Test City',
          state: 'TS',
          zip: '12345',
          country: 'Test Country'
        }
      },
      priority: 'NORMAL',
      tenantId: TENANT_ID
    };

    const response = await apiContext.post(`${API_BASE_URL}/orders`, {
      data: orderData,
      headers: {
        'X-Tenant-ID': TENANT_ID
      }
    });

    expect(response.status()).toBe(201);
    const order = await response.json();
    expect(order.orderNumber).toContain('ORDER-');
    expect(order.status).toBe('PENDING');
    testOrderId = order.id;
  });

  test('GET /orders - List orders', async () => {
    const response = await apiContext.get(`${API_BASE_URL}/orders`, {
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
    expect(Array.isArray(result.content || result.orders || result)).toBe(true);
  });

  test('GET /orders/{id} - Get order by ID', async () => {
    const response = await apiContext.get(`${API_BASE_URL}/orders/${testOrderId}`, {
      headers: {
        'X-Tenant-ID': TENANT_ID
      }
    });

    expect(response.status()).toBe(200);
    const order = await response.json();
    expect(order.id).toBe(testOrderId);
    expect(order.status).toBe('PENDING');
  });

  test('POST /orders/{id}/start - Start fulfillment process', async () => {
    const response = await apiContext.post(`${API_BASE_URL}/orders/${testOrderId}/start`, {
      headers: {
        'X-Tenant-ID': TENANT_ID
      }
    });

    expect(response.status()).toBe(200);
    const order = await response.json();
    expect(order.status).toMatch(/PICKING|IN_PROGRESS/);
  });

  test('POST /picking/assign - Assign picking task', async () => {
    const assignData = {
      orderId: testOrderId,
      pickerId: 'PICKER-001',
      zone: 'ZONE-A'
    };

    const response = await apiContext.post(`${API_BASE_URL}/picking/assign`, {
      data: assignData,
      headers: {
        'X-Tenant-ID': TENANT_ID
      }
    });

    expect(response.status()).toBe(200);
    const task = await response.json();
    expect(task.orderId).toBe(testOrderId);
    expect(task.pickerId).toBe('PICKER-001');
  });

  test('POST /picking/complete - Complete picking', async () => {
    // First, assign a task
    await apiContext.post(`${API_BASE_URL}/picking/assign`, {
      data: {
        orderId: testOrderId,
        pickerId: 'PICKER-001',
        zone: 'ZONE-A'
      },
      headers: { 'X-Tenant-ID': TENANT_ID }
    });

    const completeData = {
      orderId: testOrderId,
      pickerId: 'PICKER-001',
      items: [
        {
          sku: testSKU,
          quantityPicked: 10,
          locationId: 'ZONE-A-AISLE-01-SHELF-01-BIN-01'
        }
      ],
      notes: 'All items picked successfully'
    };

    const response = await apiContext.post(`${API_BASE_URL}/picking/complete`, {
      data: completeData,
      headers: {
        'X-Tenant-ID': TENANT_ID
      }
    });

    expect(response.status()).toBe(200);
    const result = await response.json();
    expect(result.orderId).toBe(testOrderId);
  });

  test('POST /packing/start - Start packing process', async () => {
    const response = await apiContext.post(`${API_BASE_URL}/packing/start`, {
      data: {
        orderId: testOrderId,
        packerId: 'PACKER-001'
      },
      headers: {
        'X-Tenant-ID': TENANT_ID
      }
    });

    expect(response.status()).toBe(200);
    const packing = await response.json();
    expect(packing.orderId).toBe(testOrderId);
    expect(packing.packerId).toBe('PACKER-001');
  });

  test('POST /packing/complete - Complete packing', async () => {
    const completeData = {
      orderId: testOrderId,
      packerId: 'PACKER-001',
      boxes: [
        {
          boxType: 'STANDARD',
          weight: 2.5,
          items: [{ sku: testSKU, quantity: 10 }]
        }
      ]
    };

    const response = await apiContext.post(`${API_BASE_URL}/packing/complete`, {
      data: completeData,
      headers: {
        'X-Tenant-ID': TENANT_ID
      }
    });

    expect(response.status()).toBe(200);
    const result = await response.json();
    expect(result.orderId).toBe(testOrderId);
    expect(result.boxes).toBeDefined();
  });

  test('POST /shipping/label - Generate shipping label', async () => {
    const labelData = {
      orderId: testOrderId,
      carrier: 'FEDEX',
      service: 'GROUND'
    };

    const response = await apiContext.post(`${API_BASE_URL}/shipping/label`, {
      data: labelData,
      headers: {
        'X-Tenant-ID': TENANT_ID
      }
    });

    expect(response.status()).toBe(200);
    const label = await response.json();
    expect(label.trackingNumber).toBeDefined();
    expect(label.labelUrl).toBeDefined();
  });

  test('POST /shipping/ship - Mark order as shipped', async () => {
    const shipData = {
      orderId: testOrderId,
      carrier: 'FEDEX',
      trackingNumber: 'TRACK-123456789',
      shippedAt: new Date().toISOString()
    };

    const response = await apiContext.post(`${API_BASE_URL}/shipping/ship`, {
      data: shipData,
      headers: {
        'X-Tenant-ID': TENANT_ID
      }
    });

    expect(response.status()).toBe(200);
    const order = await response.json();
    expect(order.status).toBe('SHIPPED');
    expect(order.trackingNumber).toBe('TRACK-123456789');
  });

  test('GET /orders/{id}/status - Get order status timeline', async () => {
    const response = await apiContext.get(`${API_BASE_URL}/orders/${testOrderId}/status`, {
      headers: {
        'X-Tenant-ID': TENANT_ID
      }
    });

    expect(response.status()).toBe(200);
    const timeline = await response.json();
    expect(Array.isArray(timeline)).toBe(true);
    expect(timeline.length).toBeGreaterThan(0);
  });

  test('POST /returns/initiate - Initiate return', async () => {
    const returnData = {
      orderId: testOrderId,
      reason: 'DAMAGED',
      items: [
        {
          sku: testSKU,
          quantity: 2,
          reason: 'Damaged during shipping'
        }
      ],
      customerNotes: 'Package arrived damaged'
    };

    const response = await apiContext.post(`${API_BASE_URL}/returns/initiate`, {
      data: returnData,
      headers: {
        'X-Tenant-ID': TENANT_ID
      }
    });

    expect(response.status()).toBe(201);
    const returnOrder = await response.json();
    expect(returnOrder.originalOrderId).toBe(testOrderId);
    expect(returnOrder.status).toBe('PENDING_RECEIPT');
  });

  test('GET /wave/pending - Get pending wave picks', async () => {
    // Create another order to test wave picking
    const orderData = {
      orderNumber: `WAVE-ORDER-${Date.now()}`,
      customer: { id: 'customer-002', name: 'Test Customer 2' },
      items: [{ sku: testSKU, quantity: 5, price: 29.99 }],
      shipping: { method: 'STANDARD', address: { street: '456 Test St', city: 'Test City', state: 'TS', zip: '12345' } },
      tenantId: TENANT_ID
    };

    await apiContext.post(`${API_BASE_URL}/orders`, {
      data: orderData,
      headers: { 'X-Tenant-ID': TENANT_ID }
    });

    const response = await apiContext.get(`${API_BASE_URL}/wave/pending`, {
      headers: {
        'X-Tenant-ID': TENANT_ID
      }
    });

    expect(response.status()).toBe(200);
    const wave = await response.json();
    expect(Array.isArray(wave.orders || wave)).toBe(true);
  });

  test('GET /metrics - Fulfillment metrics', async () => {
    const response = await apiContext.get(
      `${API_BASE_URL.replace('/api/v1/fulfillment', '')}/actuator/prometheus`
    );

    expect(response.status()).toBe(200);
    const metrics = await response.text();
    expect(metrics).toContain('fulfillment_');
  });
});
