import { Test, TestingModule } from '@nestjs/testing';
import { INestApplication, ValidationPipe } from '@nestjs/common';
import * as request from 'supertest';
import { AppModule } from '../src/app.module';

describe('InventoryController (e2e)', () => {
  let app: INestApplication;

  beforeAll(async () => {
    const moduleFixture: TestingModule = await Test.createTestingModule({
      imports: [AppModule],
    }).compile();

    app = moduleFixture.createNestApplication();
    app.useGlobalPipes(new ValidationPipe({ whitelist: true, transform: true }));
    await app.init();
  });

  afterAll(async () => {
    await app.close();
  });

  it('/inventory/items (POST)', () => {
    return request(app.getHttpServer())
      .post('/api/inventory/items')
      .send({
        tenantId: 'tenant-1',
        warehouseId: 'warehouse-1',
        sku: 'TEST-SKU-001',
        name: 'Test Product',
        quantity: 100,
      })
      .expect(201);
  });

  it('/inventory/items (GET)', () => {
    return request(app.getHttpServer())
      .get('/api/inventory/items')
      .query({ tenantId: 'tenant-1' })
      .expect(200);
  });

  it('/inventory/items/:itemId (GET)', () => {
    return request(app.getHttpServer())
      .get('/api/inventory/items/item-id-1')
      .query({ tenantId: 'tenant-1' })
      .expect(200);
  });

  it('/inventory/warehouses/:warehouseId/low-stock (GET)', () => {
    return request(app.getHttpServer())
      .get('/api/inventory/warehouses/warehouse-1/low-stock')
      .query({ tenantId: 'tenant-1' })
      .expect(200);
  });
});
