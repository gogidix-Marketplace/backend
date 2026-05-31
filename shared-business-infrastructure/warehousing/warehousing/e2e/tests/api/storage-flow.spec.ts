import { test, expect, APIRequestContext } from '@playwright/test';
import { createClient } from '@playwright/test';

const STORAGE_API_URL = process.env.STORAGE_API_URL || 'http://localhost:8083/api/v1/storage';
const PRICING_API_URL = process.env.PRICING_API_URL || 'http://localhost:8084/api/v1/pricing';
const TENANT_ID = process.env.TENANT_ID || 'test-tenant-001';

/**
 * Storage API E2E Tests
 *
 * Tests the complete storage management flow:
 * - Space allocation
 * - Availability checking
 * - Access control
 * - Pricing calculations
 */
test.describe('Storage API Flow', () => {
  let apiContext: APIRequestContext;
  let testSpaceId: string;
  let testBookingId: string;

  test.beforeAll(async () => {
    apiContext = await createClient();
  });

  test.afterAll(async () => {
    // Cleanup test data
    if (testBookingId) {
      await apiContext.delete(`${STORAGE_API_URL}/bookings/${testBookingId}`, {
        headers: { 'X-Tenant-ID': TENANT_ID }
      });
    }
  });

  test('GET /health - Service health check', async () => {
    const response = await apiContext.get(
      `${STORAGE_API_URL.replace('/api/v1/storage', '')}/actuator/health`
    );
    expect(response.status()).toBe(200);
    const health = await response.json();
    expect(health.status).toBe('UP');
  });

  test('POST /spaces - Create storage space', async () => {
    const spaceData = {
      spaceCode: `SPACE-${Date.now()}`,
      spaceType: 'CLIMATE_CONTROLLED',
      sizeCategory: 'MEDIUM',
      dimensions: {
        length: 10,
        width: 8,
        height: 8,
        unit: 'FT'
      },
      capacity: 640,
      warehouseId: 'WAREHOUSE-001',
      zoneId: 'ZONE-CC-A',
      features: ['CLIMATE_CONTROLLED', 'SECURITY_24_7', 'ACCESS_CONTROLLED'],
      tenantId: TENANT_ID
    };

    const response = await apiContext.post(`${STORAGE_API_URL}/spaces`, {
      data: spaceData,
      headers: {
        'X-Tenant-ID': TENANT_ID
      }
    });

    expect(response.status()).toBe(201);
    const space = await response.json();
    expect(space.spaceCode).toContain('SPACE-');
    expect(space.spaceType).toBe('CLIMATE_CONTROLLED');
    testSpaceId = space.id;
  });

  test('GET /spaces - List available spaces', async () => {
    const response = await apiContext.get(`${STORAGE_API_URL}/spaces`, {
      headers: {
        'X-Tenant-ID': TENANT_ID
      },
      params: {
        available: true,
        spaceType: 'CLIMATE_CONTROLLED',
        page: 0,
        size: 20
      }
    });

    expect(response.status()).toBe(200);
    const result = await response.json();
    expect(Array.isArray(result.content || result.spaces || result)).toBe(true);
  });

  test('GET /spaces/{id} - Get space details', async () => {
    const response = await apiContext.get(`${STORAGE_API_URL}/spaces/${testSpaceId}`, {
      headers: {
        'X-Tenant-ID': TENANT_ID
      }
    });

    expect(response.status()).toBe(200);
    const space = await response.json();
    expect(space.id).toBe(testSpaceId);
    expect(space.availableCapacity).toBeDefined();
  });

  test('POST /availability/check - Check space availability', async () => {
    const availabilityRequest = {
      spaceType: 'STANDARD',
      sizeCategory: 'SMALL',
      startDate: new Date().toISOString(),
      duration: 30,
      unit: 'DAYS'
    };

    const response = await apiContext.post(`${STORAGE_API_URL}/availability/check`, {
      data: availabilityRequest,
      headers: {
        'X-Tenant-ID': TENANT_ID
      }
    });

    expect(response.status()).toBe(200);
    const availability = await response.json();
    expect(Array.isArray(availability.availableSpaces || availability)).toBe(true);
    expect(availability.totalAvailable).toBeDefined();
  });

  test('POST /allocate - Allocate space to customer', async () => {
    const allocationData = {
      spaceId: testSpaceId,
      customerId: 'customer-001',
      startDate: new Date().toISOString(),
      duration: 90,
      durationUnit: 'DAYS',
      tenantId: TENANT_ID
    };

    const response = await apiContext.post(`${STORAGE_API_URL}/allocate`, {
      data: allocationData,
      headers: {
        'X-Tenant-ID': TENANT_ID
      }
    });

    expect(response.status()).toBe(200);
    const allocation = await response.json();
    expect(allocation.spaceId).toBe(testSpaceId);
    expect(allocation.customerId).toBe('customer-001');
    testBookingId = allocation.bookingId;
  });

  test('POST /access/generate - Generate access code', async () => {
    const accessRequest = {
      bookingId: testBookingId,
      accessType: 'TEMPORARY',
      validFrom: new Date().toISOString(),
      validUntil: new Date(Date.now() + 24 * 60 * 60 * 1000).toISOString(), // 24 hours
      pinType: '4_DIGIT'
    };

    const response = await apiContext.post(`${STORAGE_API_URL}/access/generate`, {
      data: accessRequest,
      headers: {
        'X-Tenant-ID': TENANT_ID
      }
    });

    expect(response.status()).toBe(200);
    const access = await response.json();
    expect(access.accessCode).toBeDefined();
    expect(access.accessCode.length).toBe(4);
    expect(access.validUntil).toBeDefined();
  });

  test('POST /access/validate - Validate access code', async () => {
    // First generate a code
    const generateResponse = await apiContext.post(`${STORAGE_API_URL}/access/generate`, {
      data: {
        bookingId: testBookingId,
        accessType: 'TEMPORARY',
        validFrom: new Date().toISOString(),
        validUntil: new Date(Date.now() + 24 * 60 * 60 * 1000).toISOString(),
        pinType: '4_DIGIT'
      },
      headers: { 'X-Tenant-ID': TENANT_ID }
    });

    const accessData = await generateResponse.json();

    const validateRequest = {
      bookingId: testBookingId,
      accessCode: accessData.accessCode,
      timestamp: new Date().toISOString()
    };

    const response = await apiContext.post(`${STORAGE_API_URL}/access/validate`, {
      data: validateRequest,
      headers: {
        'X-Tenant-ID': TENANT_ID
      }
    });

    expect(response.status()).toBe(200);
    const validation = await response.json();
    expect(validation.valid).toBe(true);
    expect(validation.grantAccess).toBe(true);
  });

  test('POST /access/log - Log access event', async () => {
    const logData = {
      bookingId: testBookingId,
      eventType: 'ENTRY',
      timestamp: new Date().toISOString(),
      method: 'PIN_CODE',
      success: true
    };

    const response = await apiContext.post(`${STORAGE_API_URL}/access/log`, {
      data: logData,
      headers: {
        'X-Tenant-ID': TENANT_ID
      }
    });

    expect(response.status()).toBe(201);
    const log = await response.json();
    expect(log.eventType).toBe('ENTRY');
    expect(log.success).toBe(true);
  });

  test('GET /access/history/{bookingId} - Get access history', async () => {
    const response = await apiContext.get(`${STORAGE_API_URL}/access/history/${testBookingId}`, {
      headers: {
        'X-Tenant-ID': TENANT_ID
      }
    });

    expect(response.status()).toBe(200);
    const history = await response.json();
    expect(Array.isArray(history.events || history)).toBe(true);
  });

  test('Pricing API - Calculate storage price', async () => {
    const pricingRequest = {
      spaceType: 'CLIMATE_CONTROLLED',
      sizeCategory: 'MEDIUM',
      duration: 90,
      durationUnit: 'DAYS',
      features: ['CLIMATE_CONTROLLED', 'SECURITY_24_7']
    };

    const response = await apiContext.post(`${PRICING_API_URL}/calculate`, {
      data: pricingRequest,
      headers: {
        'X-Tenant-ID': TENANT_ID
      }
    });

    expect(response.status()).toBe(200);
    const pricing = await response.json();
    expect(pricing.basePrice).toBeDefined();
    expect(pricing.totalPrice).toBeDefined();
    expect(pricing.currency).toBe('USD');
    expect(pricing.breakdown).toBeDefined();
  });

  test('Pricing API - Get price quote', async () => {
    const quoteRequest = {
      spaceType: 'STANDARD',
      sizeCategory: 'LARGE',
      duration: 180,
      durationUnit: 'DAYS',
      customerTier: 'PREMIUM'
    };

    const response = await apiContext.post(`${PRICING_API_URL}/quote`, {
      data: quoteRequest,
      headers: {
        'X-Tenant-ID': TENANT_ID
      }
    });

    expect(response.status()).toBe(200);
    const quote = await response.json();
    expect(quote.quoteId).toBeDefined();
    expect(quote.validUntil).toBeDefined();
    expect(quote.totalPrice).toBeDefined();
    expect(quote.discounts).toBeDefined();
  });

  test('GET /utilization - Get storage utilization', async () => {
    const response = await apiContext.get(`${STORAGE_API_URL}/utilization`, {
      headers: {
        'X-Tenant-ID': TENANT_ID
      }
    });

    expect(response.status()).toBe(200);
    const utilization = await response.json();
    expect(utilization.overallPercent).toBeDefined();
    expect(utilization.bySpaceType).toBeDefined();
    expect(utilization.byZone).toBeDefined();
  });

  test('POST /spaces/{id}/optimize - Optimize space utilization', async () => {
    const response = await apiContext.post(`${STORAGE_API_URL}/spaces/${testSpaceId}/optimize`, {
      data: {
        targetUtilization: 85
      },
      headers: {
        'X-Tenant-ID': TENANT_ID
      }
    });

    expect(response.status()).toBe(200);
    const optimization = await response.json();
    expect(optimization.currentUtilization).toBeDefined();
    expect(optimization.suggestions).toBeDefined();
  });

  test('PUT /bookings/{id}/extend - Extend booking', async () => {
    const extendData = {
      additionalDuration: 30,
      durationUnit: 'DAYS',
      reason: 'Customer request'
    };

    const response = await apiContext.put(`${STORAGE_API_URL}/bookings/${testBookingId}/extend`, {
      data: extendData,
      headers: {
        'X-Tenant-ID': TENANT_ID
      }
    });

    expect(response.status()).toBe(200);
    const booking = await response.json();
    expect(booking.id).toBe(testBookingId);
    expect(booking.newEndDate).toBeDefined();
  });

  test('POST /bookings/{id}/terminate - Terminate booking early', async () => {
    // Create a temporary booking for termination test
    const tempSpace = await apiContext.post(`${STORAGE_API_URL}/spaces`, {
      data: {
        spaceCode: `TEMP-SPACE-${Date.now()}`,
        spaceType: 'STANDARD',
        sizeCategory: 'SMALL',
        dimensions: { length: 5, width: 5, height: 8, unit: 'FT' },
        capacity: 200,
        warehouseId: 'WAREHOUSE-001',
        zoneId: 'ZONE-S-A',
        tenantId: TENANT_ID
      },
      headers: { 'X-Tenant-ID': TENANT_ID }
    });

    const space = await tempSpace.json();

    const allocation = await apiContext.post(`${STORAGE_API_URL}/allocate`, {
      data: {
        spaceId: space.id,
        customerId: 'customer-temp',
        startDate: new Date().toISOString(),
        duration: 30,
        durationUnit: 'DAYS',
        tenantId: TENANT_ID
      },
      headers: { 'X-Tenant-ID': TENANT_ID }
    });

    const booking = await allocation.json();

    const terminateData = {
      reason: 'Customer request',
      terminationDate: new Date().toISOString(),
      refundEligible: true
    };

    const response = await apiContext.post(`${STORAGE_API_URL}/bookings/${booking.bookingId}/terminate`, {
      data: terminateData,
      headers: {
        'X-Tenant-ID': TENANT_ID
      }
    });

    expect(response.status()).toBe(200);
    const termination = await response.json();
    expect(termination.bookingId).toBe(booking.bookingId);
    expect(termination.status).toBe('TERMINATED');
    expect(termination.refundAmount).toBeDefined();
  });
});
