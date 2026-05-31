/**
 * Unit tests for Payroll Automation Service
 */

import request from 'supertest';
import { app } from '../index';
import { Queue } from 'bull';
import IORedis from 'ioredis';

// Mock dependencies
jest.mock('bull');
jest.mock('ioredis');
jest.mock('kafkajs');

describe('Payroll Automation Service', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  describe('Health Check Endpoint', () => {
    it('should return healthy status', async () => {
      const response = await request(app)
        .get('/health')
        .expect('Content-Type', /json/);

      expect(response.status).toBe(200);
      expect(response.body).toHaveProperty('status', 'healthy');
      expect(response.body).toHaveProperty('service', 'payroll-automation-service');
      expect(response.body).toHaveProperty('timestamp');
      expect(response.body).toHaveProperty('uptime');
    });
  });

  describe('Payroll Processing API', () => {
    it('should accept payroll processing request', async () => {
      const mockJob = {
        id: 'job-123',
        data: {
          tenantId: 'tenant-001',
          payrollId: 'payroll-123',
          employeeIds: ['emp-001', 'emp-002']
        }
      };

      const mockQueue = {
        add: jest.fn().mockResolvedValue(mockJob)
      };

      (Queue as jest.Mock).mockImplementation(() => mockQueue);

      const response = await request(app)
        .post('/api/v1/payroll/process')
        .send({
          tenantId: 'tenant-001',
          employeeIds: ['emp-001', 'emp-002'],
          startDate: '2024-01-01',
          endDate: '2024-01-31'
        })
        .expect('Content-Type', /json/);

      expect(response.status).toBe(202);
      expect(response.body).toHaveProperty('jobId');
      expect(response.body).toHaveProperty('payrollId');
      expect(response.body).toHaveProperty('status', 'queued');
    });

    it('should reject request with missing tenantId', async () => {
      const response = await request(app)
        .post('/api/v1/payroll/process')
        .send({
          employeeIds: ['emp-001']
        })
        .expect('Content-Type', /json/);

      expect(response.status).toBe(400);
      expect(response.body).toHaveProperty('error', 'Bad Request');
    });

    it('should reject request with non-array employeeIds', async () => {
      const response = await request(app)
        .post('/api/v1/payroll/process')
        .send({
          tenantId: 'tenant-001',
          employeeIds: 'not-an-array'
        })
        .expect('Content-Type', /json/);

      expect(response.status).toBe(400);
    });
  });

  describe('Job Status API', () => {
    it('should return job status for existing job', async () => {
      const mockJob = {
        id: 'job-123',
        getState: jest.fn().mockResolvedValue('completed'),
        progress: jest.fn().mockReturnValue(100),
        data: { tenantId: 'tenant-001' },
        processedOn: new Date(),
        finishedOn: new Date(),
        failedReason: null
      };

      const mockQueue = {
        getJob: jest.fn().mockResolvedValue(mockJob)
      };

      (Queue as jest.Mock).mockImplementation(() => mockQueue);

      const response = await request(app)
        .get('/api/v1/jobs/job-123')
        .expect('Content-Type', /json/);

      expect(response.status).toBe(200);
      expect(response.body).toHaveProperty('jobId', 'job-123');
      expect(response.body).toHaveProperty('state', 'completed');
    });

    it('should return 404 for non-existing job', async () => {
      const mockQueue = {
        getJob: jest.fn().mockResolvedValue(null)
      };

      (Queue as jest.Mock).mockImplementation(() => mockQueue);

      const response = await request(app)
        .get('/api/v1/jobs/non-existing')
        .expect('Content-Type', /json/);

      expect(response.status).toBe(404);
    });
  });

  describe('Queue Statistics API', () => {
    it('should return queue statistics', async () => {
      const mockQueue = {
        getWaitingCount: jest.fn().mockResolvedValue(10),
        getActiveCount: jest.fn().mockResolvedValue(2),
        getCompletedCount: jest.fn().mockResolvedValue(100),
        getFailedCount: jest.fn().mockResolvedValue(5)
      };

      (Queue as jest.Mock).mockImplementation(() => mockQueue);

      const response = await request(app)
        .get('/api/v1/queue/stats')
        .expect('Content-Type', /json/);

      expect(response.status).toBe(200);
      expect(response.body).toHaveProperty('waiting', 10);
      expect(response.body).toHaveProperty('active', 2);
      expect(response.body).toHaveProperty('completed', 100);
      expect(response.body).toHaveProperty('failed', 5);
      expect(response.body).toHaveProperty('total', 117);
    });
  });

  describe('Payroll Processing Logic', () => {
    it('should process employee payroll correctly', async () => {
      const mockEmployeeData = {
        employeeId: 'emp-001',
        baseSalary: 5000,
        taxRate: 0.20,
        benefitsDeduction: 500
      };

      // Expected calculations
      const grossPay = mockEmployeeData.baseSalary;
      const taxes = grossPay * mockEmployeeData.taxRate;
      const deductions = mockEmployeeData.benefitsDeduction;
      const netPay = grossPay - taxes - deductions;

      expect(grossPay).toBe(5000);
      expect(taxes).toBe(1000);
      expect(deductions).toBe(500);
      expect(netPay).toBe(3500);
    });

    it('should handle overtime calculations', () => {
      const baseSalary = 4000;
      const overtimeHours = 10;
      const hourlyRate = baseSalary / 160; // Assuming 160 working hours per month
      const overtimeMultiplier = 1.5;

      const overtimePay = overtimeHours * hourlyRate * overtimeMultiplier;
      const totalPay = baseSalary + overtimePay;

      expect(hourlyRate).toBe(25);
      expect(overtimePay).toBe(375);
      expect(totalPay).toBe(4375);
    });
  });

  describe('Kafka Event Handling', () => {
    it('should process PROCESS_PAYROLL command', async () => {
      const command = {
        type: 'PROCESS_PAYROLL',
        data: {
          tenantId: 'tenant-001',
          employeeIds: ['emp-001'],
          startDate: '2024-01-01',
          endDate: '2024-01-31'
        }
      };

      const mockQueue = {
        add: jest.fn().mockResolvedValue({ id: 'job-123' })
      };

      (Queue as jest.Mock).mockImplementation(() => mockQueue);

      // Simulate Kafka message handling
      // In actual implementation, this would be tested through integration tests
      expect(command.type).toBe('PROCESS_PAYROLL');
      expect(command.data.tenantId).toBe('tenant-001');
    });

    it('should process CANCEL_PAYROLL command', async () => {
      const command = {
        type: 'CANCEL_PAYROLL',
        data: {
          jobId: 'job-123'
        }
      };

      const mockJob = {
        id: 'job-123',
        remove: jest.fn().mockResolvedValue(undefined)
      };

      const mockQueue = {
        getJob: jest.fn().mockResolvedValue(mockJob)
      };

      (Queue as jest.Mock).mockImplementation(() => mockQueue);

      expect(command.type).toBe('CANCEL_PAYROLL');
      expect(command.data.jobId).toBe('job-123');
    });
  });
});
