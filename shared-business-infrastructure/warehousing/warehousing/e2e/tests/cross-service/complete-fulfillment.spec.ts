import { test, expect, APIRequestContext } from '@playwright/test';
import { createClient } from '@playwright/test';

const INVENTORY_API = process.env.INVENTORY_API_URL || 'http://localhost:8081/api/v1/inventory';
const FULFILLMENT_API = process.env.FULFILLMENT_API_URL || 'http://localhost:8085/api/v1/fulfillment';
const SHIPPING_API = process.env.SHIPPING_API_URL || 'http://localhost:8088/api/v1/shipping';
const TENANT_ID = process.env.TENANT_ID || 'test-tenant-001';

/**
 * Complete Fulfillment Flow E2E Tests
 *
 * Tests the end-to-end fulfillment process across multiple services:
 * 1. Inventory setup
 * 2. Order creation
 * 3. Picking
 * 4. Packing
 * 5. Shipping
 * 6. Tracking
 */
test.describe('Complete Fulfillment Flow - Cross Service', () => {
  let apiContext: APIRequestContext;
  let orderId: string;
  let itemId: string;
  let trackingNumber: string;
  const testSKU = `E2E-SKU-${Date.now()}`;

  test.beforeAll(async () => {
    apiContext = await createClient();
  });

  test.afterAll(async () => {
    // Cleanup
    if (orderId) {
      await apiContext.delete(`${FULFILLMENT_API}/orders/${orderId}`, {
        headers: { 'X-Tenant-ID': TENANT_ID }
      });
    }
    if (itemId) {
      await apiContext.delete(`${INVENTORY_API}/items/${itemId}`, {
        headers: { 'X-Tenant-ID': TENANT_ID }
      });
    }
  });

  test('Step 1: Setup inventory for fulfillment', async ({ }) => {
    const itemData = {
      sku: testSKU,
      name: 'E2E Test Product',
      description: 'Product for end-to-end testing',
      quantity: 1000,
      unitOfMeasure: 'EA',
      locationId: 'ZONE-A-AISLE-01-SHELF-01-BIN-01',
      reorderThreshold: 100,
      tenantId: TENANT_ID,
      attributes: {
        category: 'TEST',
        brand: 'E2E-Brand',
        weight: 1.5,
        weightUnit: 'KG'
      }
    };

    const response = await apiContext.post(`${INVENTORY_API}/items`, {
      data: itemData,
      headers: { 'X-Tenant-ID': TENANT_ID }
    });

    expect(response.status()).toBe(201);
    const item = await response.json();
    itemId = item.id;
    expect(item.quantity).toBe(1000);
  });

  test('Step 2: Create fulfillment order', async ({ }) => {
    const orderData = {
      orderNumber: `E2E-ORDER-${Date.now()}`,
      customer: {
        id: 'e2e-customer-001',
        name: 'E2E Test Customer',
        email: 'e2e@example.com',
        phone: '+1234567890'
      },
      items: [
        {
          sku: testSKU,
          quantity: 50,
          price: 29.99
        }
      ],
      shipping: {
        method: 'EXPRESS',
        carrier: 'FEDEX',
        address: {
          name: 'E2E Test Customer',
          street: '123 Test Street',
          city: 'Test City',
          state: 'TS',
          zip: '12345',
          country: 'Test Country'
        }
      },
      priority: 'EXPRESS',
      notes: 'E2E test order',
      tenantId: TENANT_ID
    };

    const response = await apiContext.post(`${FULFILLMENT_API}/orders`, {
      data: orderData,
      headers: { 'X-Tenant-ID': TENANT_ID }
    });

    expect(response.status()).toBe(201);
    const order = await response.json();
    orderId = order.id;
    expect(order.status).toBe('PENDING');
    expect(order.items.length).toBe(1);
  });

  test('Step 3: Start order processing', async ({ }) => {
    const response = await apiContext.post(`${FULFILLMENT_API}/orders/${orderId}/start`, {
      headers: { 'X-Tenant-ID': TENANT_ID }
    });

    expect(response.status()).toBe(200);
    const order = await response.json();
    expect(order.status).toBe('PICKING');
  });

  test('Step 4: Assign and execute picking', async ({ }) => {
    // Assign picker
    const assignResponse = await apiContext.post(`${FULFILLMENT_API}/picking/assign`, {
      data: {
        orderId: orderId,
        pickerId: 'E2E-PICKER',
        zone: 'ZONE-A'
      },
      headers: { 'X-Tenant-ID': TENANT_ID }
    });

    expect(assignResponse.status()).toBe(200);
    const task = await assignResponse.json();
    expect(task.status).toBe('ASSIGNED');

    // Complete picking
    const completeResponse = await apiContext.post(`${FULFILLMENT_API}/picking/complete`, {
      data: {
        orderId: orderId,
        pickerId: 'E2E-PICKER',
        items: [
          {
            sku: testSKU,
            quantityPicked: 50,
            locationId: 'ZONE-A-AISLE-01-SHELF-01-BIN-01',
            barcodeScanned: true
          }
        ],
        duration: 300,
        notes: 'Picking completed successfully'
      },
      headers: { 'X-Tenant-ID': TENANT_ID }
    });

    expect(completeResponse.status()).toBe(200);
    const result = await completeResponse.json();
    expect(result.orderId).toBe(orderId);
  });

  test('Step 5: Verify inventory updated after picking', async ({ }) => {
    const response = await apiContext.get(`${INVENTORY_API}/items/${itemId}`, {
      headers: { 'X-Tenant-ID': TENANT_ID }
    });

    expect(response.status()).toBe(200);
    const item = await response.json();
    expect(item.quantity).toBe(950); // 1000 - 50
  });

  test('Step 6: Execute packing process', async ({ }) => {
    // Start packing
    const startResponse = await apiContext.post(`${FULFILLMENT_API}/packing/start`, {
      data: {
        orderId: orderId,
        packerId: 'E2E-PACKER'
      },
      headers: { 'X-Tenant-ID': TENANT_ID }
    });

    expect(startResponse.status()).toBe(200);

    // Complete packing
    const completeResponse = await apiContext.post(`${FULFILLMENT_API}/packing/complete`, {
      data: {
        orderId: orderId,
        packerId: 'E2E-PACKER',
        boxes: [
          {
            boxType: 'STANDARD',
            weight: 75.0,
            dimensions: {
              length: 40,
              width: 30,
              height: 20,
              unit: 'CM'
            },
            items: [
              {
                sku: testSKU,
                quantity: 50
              }
            ]
          }
        ],
        packingMaterials: ['BUBBLE_WRAP', 'PACKING_PEANUTS'],
        duration: 180
      },
      headers: { 'X-Tenant-ID': TENANT_ID }
    });

    expect(completeResponse.status()).toBe(200);
    const result = await completeResponse.json();
    expect(result.boxes.length).toBe(1);
  });

  test('Step 7: Quality check', async ({ }) => {
    const qcResponse = await apiContext.post(`${FULFILLMENT_API}/quality/check`, {
      data: {
        orderId: orderId,
        inspectorId: 'E2E-QC',
        checks: [
          {
            type: 'VISUAL_INSPECTION',
            result: 'PASS',
            notes: 'No visual defects found'
          },
          {
            type: 'QUANTITY_VERIFICATION',
            result: 'PASS',
            notes: 'Quantity matches order'
          },
          {
            type: 'PACKAGING_CHECK',
            result: 'PASS',
            notes: 'Properly packaged'
          }
        ],
        overallResult: 'PASS'
      },
      headers: { 'X-Tenant-ID': TENANT_ID }
    });

    expect(qcResponse.status()).toBe(200);
    const qc = await qcResponse.json();
    expect(qc.overallResult).toBe('PASS');
  });

  test('Step 8: Generate shipping label', async ({ }) => {
    const labelResponse = await apiContext.post(`${SHIPPING_API}/labels`, {
      data: {
        orderId: orderId,
        carrier: 'FEDEX',
        service: 'EXPRESS',
        shipmentDetails: {
          weight: 75.0,
          weightUnit: 'KG',
          dimensions: {
            length: 40,
            width: 30,
            height: 20,
            unit: 'CM'
          }
        }
      },
      headers: { 'X-Tenant-ID': TENANT_ID }
    });

    expect(labelResponse.status()).toBe(200);
    const label = await labelResponse.json();
    expect(label.labelUrl).toBeDefined();
    expect(label.trackingNumber).toBeDefined();
    trackingNumber = label.trackingNumber;
  });

  test('Step 9: Ship order', async ({ }) => {
    const shipResponse = await apiContext.post(`${SHIPPING_API}/ship`, {
      data: {
        orderId: orderId,
        trackingNumber: trackingNumber,
        carrier: 'FEDEX',
        shippedAt: new Date().toISOString(),
        estimatedDelivery: new Date(Date.now() + 2 * 24 * 60 * 60 * 1000).toISOString()
      },
      headers: { 'X-Tenant-ID': TENANT_ID }
    });

    expect(shipResponse.status()).toBe(200);
    const shipment = await shipResponse.json();
    expect(shipment.status).toBe('SHIPPED');
    expect(shipment.trackingNumber).toBe(trackingNumber);
  });

  test('Step 10: Verify order status', async ({ }) => {
    const response = await apiContext.get(`${FULFILLMENT_API}/orders/${orderId}`, {
      headers: { 'X-Tenant-ID': TENANT_ID }
    });

    expect(response.status()).toBe(200);
    const order = await response.json();
    expect(order.status).toBe('SHIPPED');
    expect(order.trackingNumber).toBe(trackingNumber);
  });

  test('Step 11: Get order timeline', async ({ }) => {
    const response = await apiContext.get(`${FULFILLMENT_API}/orders/${orderId}/timeline`, {
      headers: { 'X-Tenant-ID': TENANT_ID }
    });

    expect(response.status()).toBe(200);
    const timeline = await response.json();

    const statuses = timeline.map((t: any) => t.status);
    expect(statuses).toContain('PENDING');
    expect(statuses).toContain('PICKING');
    expect(statuses).toContain('SHIPPED');
  });

  test('Step 12: Track shipment', async ({ }) => {
    const response = await apiContext.get(`${SHIPPING_API}/tracking/${trackingNumber}`, {
      headers: { 'X-Tenant-ID': TENANT_ID }
    });

    expect(response.status()).toBe(200);
    const tracking = await response.json();
    expect(tracking.trackingNumber).toBe(trackingNumber);
    expect(tracking.status).toBe('SHIPPED');
    expect(tracking.events).toBeDefined();
    expect(Array.isArray(tracking.events)).toBe(true);
  });

  test('Step 13: Verify fulfillment metrics', async ({ }) => {
    const metricsResponse = await apiContext.get(`${FULFILLMENT_API}/metrics`, {
      headers: { 'X-Tenant-ID': TENANT_ID },
      params: {
        startDate: new Date(Date.now() - 24 * 60 * 60 * 1000).toISOString(),
        endDate: new Date().toISOString()
      }
    });

    expect(metricsResponse.status()).toBe(200);
    const metrics = await metricsResponse.json();
    expect(metrics.totalOrders).toBeGreaterThan(0);
    expect(metrics.fulfillmentRate).toBeDefined();
  });

  test('Cross-service: Verify data consistency', async ({ }) => {
    // Get order from fulfillment service
    const orderResponse = await apiContext.get(`${FULFILLMENT_API}/orders/${orderId}`, {
      headers: { 'X-Tenant-ID': TENANT_ID }
    });
    const order = await orderResponse.json();

    // Get inventory item
    const itemResponse = await apiContext.get(`${INVENTORY_API}/items/${itemId}`, {
      headers: { 'X-Tenant-ID': TENANT_ID }
    });
    const item = await itemResponse.json();

    // Verify quantities match
    const totalPicked = order.items.reduce((sum: number, i: any) => sum + i.quantity, 0);
    const expectedInventory = 1000 - totalPicked;
    expect(item.quantity).toBe(expectedInventory);

    // Get shipping info
    const shippingResponse = await apiContext.get(`${SHIPPING_API}/shipment/${orderId}`, {
      headers: { 'X-Tenant-ID': TENANT_ID }
    });
    const shipping = await shippingResponse.json();

    // Verify order ID matches across services
    expect(shipping.orderId).toBe(orderId);
    expect(order.trackingNumber).toBe(shipping.trackingNumber);
  });

  test('Cross-service: Test failure compensation', async ({ }) => {
    // Create a new order that will fail during picking
    const failSKU = `FAIL-SKU-${Date.now()}`;

    // Create item with insufficient stock
    const itemResponse = await apiContext.post(`${INVENTORY_API}/items`, {
      data: {
        sku: failSKU,
        name: 'Fail Test Item',
        quantity: 5,
        unitOfMeasure: 'EA',
        locationId: 'ZONE-A-AISLE-01-SHELF-01-BIN-01',
        tenantId: TENANT_ID
      },
      headers: { 'X-Tenant-ID': TENANT_ID }
    });
    const failItem = await itemResponse.json();

    // Create order for more than available
    const orderResponse = await apiContext.post(`${FULFILLMENT_API}/orders`, {
      data: {
        orderNumber: `FAIL-ORDER-${Date.now()}`,
        customer: { id: 'e2e-customer-002', name: 'Test Customer' },
        items: [{ sku: failSKU, quantity: 10, price: 10.00 }],
        shipping: {
          method: 'STANDARD',
          address: { street: '456 Test St', city: 'Test City', state: 'TS', zip: '12345', country: 'Test' }
        },
        tenantId: TENANT_ID
      },
      headers: { 'X-Tenant-ID': TENANT_ID }
    });
    const failOrder = await orderResponse.json();

    // Try to start picking
    const pickResponse = await apiContext.post(`${FULFILLMENT_API}/picking/complete`, {
      data: {
        orderId: failOrder.id,
        pickerId: 'E2E-PICKER',
        items: [{ sku: failSKU, quantityPicked: 10, locationId: 'ZONE-A-AISLE-01-SHELF-01-BIN-01' }]
      },
      headers: { 'X-Tenant-ID': TENANT_ID }
    });

    // Should fail or be compensated
    expect([200, 400, 409]).toContain(pickResponse.status());

    // Cleanup
    await apiContext.delete(`${INVENTORY_API}/items/${failItem.id}`, {
      headers: { 'X-Tenant-ID': TENANT_ID }
    });
    await apiContext.delete(`${FULFILLMENT_API}/orders/${failOrder.id}`, {
      headers: { 'X-Tenant-ID': TENANT_ID }
    });
  });

  test('Cross-service: Test concurrent order processing', async ({ }) => {
    // Create multiple orders concurrently
    const concurrentSKUs = Array.from({ length: 5 }, (_, i) => `CONCURRENT-SKU-${Date.now()}-${i}`);

    // Create items
    const itemPromises = concurrentSKUs.map(sku =>
      apiContext.post(`${INVENTORY_API}/items`, {
        data: {
          sku,
          name: `Concurrent Test Item ${sku}`,
          quantity: 100,
          unitOfMeasure: 'EA',
          locationId: 'ZONE-A-AISLE-01-SHELF-01-BIN-01',
          tenantId: TENANT_ID
        },
        headers: { 'X-Tenant-ID': TENANT_ID }
      })
    );
    const itemResponses = await Promise.all(itemPromises);

    // Create orders concurrently
    const orderPromises = concurrentSKUs.map((sku, index) =>
      apiContext.post(`${FULFILLMENT_API}/orders`, {
        data: {
          orderNumber: `CONCURRENT-ORDER-${Date.now()}-${index}`,
          customer: { id: `e2e-customer-${index}`, name: `Customer ${index}` },
          items: [{ sku, quantity: 10, price: 10.00 }],
          shipping: {
            method: 'STANDARD',
            address: { street: `${index} Test St`, city: 'Test City', state: 'TS', zip: '12345', country: 'Test' }
          },
          tenantId: TENANT_ID
        },
        headers: { 'X-Tenant-ID': TENANT_ID }
      })
    );
    const orderResponses = await Promise.all(orderPromises);

    // All orders should be created successfully
    for (const response of orderResponses) {
      expect(response.status()).toBe(201);
    }

    // Cleanup
    const items = await Promise.all(itemResponses.map(r => r.json()));
    const orders = await Promise.all(orderResponses.map(r => r.json()));

    await Promise.all(items.map((item: any) =>
      apiContext.delete(`${INVENTORY_API}/items/${item.id}`, { headers: { 'X-Tenant-ID': TENANT_ID } })
    ));
    await Promise.all(orders.map((order: any) =>
      apiContext.delete(`${FULFILLMENT_API}/orders/${order.id}`, { headers: { 'X-Tenant-ID': TENANT_ID } })
    ));
  });
});
