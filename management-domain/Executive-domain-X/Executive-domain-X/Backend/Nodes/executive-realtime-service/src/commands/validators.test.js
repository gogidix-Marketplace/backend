/**
 * Command Validators Tests
 */

const { validateCommand, commandMetadataSchema } = require('./validators');
const Joi = require('joi');

describe('Command Validators', () => {
  describe('commandMetadataSchema', () => {
    it('should validate valid metadata', () => {
      const metadata = {
        id: 'cmd123',
        tenantId: 'tenant1',
        userId: 'user1',
        executiveLevel: 'C-LEVEL',
        timestamp: Date.now(),
        correlationId: 'corr123',
      };

      const { error } = commandMetadataSchema.validate(metadata);
      expect(error).toBeUndefined();
    });

    it('should require tenantId', () => {
      const metadata = {
        userId: 'user1',
      };

      const { error } = commandMetadataSchema.validate(metadata);
      expect(error).toBeDefined();
      expect(error.details[0].path).toContain('tenantId');
    });
  });

  describe('validateCommand', () => {
    it('should validate update_kpi_threshold command', () => {
      const command = {
        id: 'cmd123',
        type: 'update_kpi_threshold',
        payload: {
          kpiId: 'kpi123',
          threshold: {
            warning: 80,
            critical: 90,
            operator: 'gt',
          },
        },
        metadata: {
          tenantId: 'tenant1',
        },
      };

      const { error, value } = validateCommand(command);
      expect(error).toBeUndefined();
      expect(value).toEqual(command);
    });

    it('should validate create_kpi_alert command', () => {
      const command = {
        id: 'cmd124',
        type: 'create_kpi_alert',
        payload: {
          kpiId: 'kpi123',
          level: 'WARNING',
          message: 'KPI exceeded threshold',
        },
        metadata: {
          tenantId: 'tenant1',
        },
      };

      const { error } = validateCommand(command);
      expect(error).toBeUndefined();
    });

    it('should validate broadcast_to_tenant command', () => {
      const command = {
        id: 'cmd125',
        type: 'broadcast_to_tenant',
        payload: {
          tenantId: 'tenant1',
          message: { type: 'test', data: 'hello' },
        },
        metadata: {
          tenantId: 'tenant1',
        },
      };

      const { error } = validateCommand(command);
      expect(error).toBeUndefined();
    });

    it('should validate broadcast_to_room command', () => {
      const command = {
        id: 'cmd126',
        type: 'broadcast_to_room',
        payload: {
          roomId: 'tenant:tenant1:dashboard:main',
          message: { type: 'test', data: 'hello' },
        },
        metadata: {
          tenantId: 'tenant1',
        },
      };

      const { error } = validateCommand(command);
      expect(error).toBeUndefined();
    });

    it('should reject command without required fields', () => {
      const command = {
        id: 'cmd127',
        type: 'update_kpi_threshold',
        payload: {
          kpiId: 'kpi123',
        },
        metadata: {
          tenantId: 'tenant1',
        },
      };

      const { error } = validateCommand(command);
      expect(error).toBeDefined();
      expect(error.details.some(d => d.path.includes('threshold'))).toBe(true);
    });

    it('should reject invalid operator', () => {
      const command = {
        id: 'cmd128',
        type: 'update_kpi_threshold',
        payload: {
          kpiId: 'kpi123',
          threshold: {
            warning: 80,
            critical: 90,
            operator: 'invalid',
          },
        },
        metadata: {
          tenantId: 'tenant1',
        },
      };

      const { error } = validateCommand(command);
      expect(error).toBeDefined();
    });

    it('should reject invalid alert level', () => {
      const command = {
        id: 'cmd129',
        type: 'create_kpi_alert',
        payload: {
          kpiId: 'kpi123',
          level: 'INVALID',
          message: 'Test',
        },
        metadata: {
          tenantId: 'tenant1',
        },
      };

      const { error } = validateCommand(command);
      expect(error).toBeDefined();
    });
  });
});
