/**
 * Command Handler Tests
 */

const {
  handleCommand,
  createCommand,
  generateCommandId,
  generateCorrelationId,
  CommandTypes,
} = require('./index');

const { publishCommand } = require('./publisher');

// Mock publisher
jest.mock('./publisher');

describe('Command Handler', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  describe('createCommand', () => {
    it('should create a command with required fields', () => {
      const payload = { kpiId: 'kpi123', value: 100 };
      const metadata = {
        tenantId: 'tenant1',
        userId: 'user1',
      };

      const command = createCommand(CommandTypes.UPDATE_KPI_THRESHOLD, payload, metadata);

      expect(command).toHaveProperty('id');
      expect(command.type).toBe(CommandTypes.UPDATE_KPI_THRESHOLD);
      expect(command.payload).toEqual(payload);
      expect(command.metadata).toMatchObject(metadata);
      expect(command.metadata).toHaveProperty('timestamp');
      expect(command.metadata).toHaveProperty('correlationId');
    });

    it('should use provided ID if given', () => {
      const payload = { kpiId: 'kpi123' };
      const metadata = {
        tenantId: 'tenant1',
        id: 'custom-id',
      };

      const command = createCommand(CommandTypes.UPDATE_KPI_THRESHOLD, payload, metadata);

      expect(command.id).toBe('custom-id');
    });
  });

  describe('generateCommandId', () => {
    it('should generate unique command IDs', () => {
      const id1 = generateCommandId();
      const id2 = generateCommandId();

      expect(id1).toMatch(/^cmd_/);
      expect(id2).toMatch(/^cmd_/);
      expect(id1).not.toBe(id2);
    });
  });

  describe('generateCorrelationId', () => {
    it('should generate unique correlation IDs', () => {
      const id1 = generateCorrelationId();
      const id2 = generateCorrelationId();

      expect(id1).toMatch(/^corr_/);
      expect(id2).toMatch(/^corr_/);
      expect(id1).not.toBe(id2);
    });
  });

  describe('handleCommand', () => {
    it('should handle valid command successfully', async () => {
      const command = createCommand(
        CommandTypes.UPDATE_KPI_THRESHOLD,
        { kpiId: 'kpi123', threshold: { warning: 80, critical: 90, operator: 'gt' } },
        { tenantId: 'tenant1' }
      );

      publishCommand.mockResolvedValue({ success: true, commandId: command.id });

      const result = await handleCommand(command);

      expect(result).toHaveProperty('success', true);
      expect(result).toHaveProperty('commandId', command.id);
      expect(result).toHaveProperty('status', 'queued');
      expect(publishCommand).toHaveBeenCalledWith(command);
    });

    it('should reject invalid command', async () => {
      const invalidCommand = {
        id: 'cmd123',
        type: 'invalid_type',
        payload: {},
        metadata: { tenantId: 'tenant1' },
      };

      await expect(handleCommand(invalidCommand)).rejects.toThrow();
    });

    it('should handle publisher errors', async () => {
      const command = createCommand(
        CommandTypes.UPDATE_KPI_THRESHOLD,
        { kpiId: 'kpi123' },
        { tenantId: 'tenant1' }
      );

      publishCommand.mockRejectedValue(new Error('Publisher error'));

      await expect(handleCommand(command)).rejects.toThrow('Publisher error');
    });
  });

  describe('CommandTypes', () => {
    it('should have all expected command types', () => {
      expect(CommandTypes).toHaveProperty('UPDATE_KPI_THRESHOLD');
      expect(CommandTypes).toHaveProperty('CREATE_KPI_ALERT');
      expect(CommandTypes).toHaveProperty('DISMISS_KPI_ALERT');
      expect(CommandTypes).toHaveProperty('CREATE_DASHBOARD');
      expect(CommandTypes).toHaveProperty('UPDATE_DASHBOARD_LAYOUT');
      expect(CommandTypes).toHaveProperty('BROADCAST_TO_TENANT');
      expect(CommandTypes).toHaveProperty('BROADCAST_TO_ROOM');
    });
  });
});
