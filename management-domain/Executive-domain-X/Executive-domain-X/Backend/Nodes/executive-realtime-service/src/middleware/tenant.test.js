/**
 * Tenant Middleware Tests
 */

const {
  extractTenant,
  validateTenantAccess,
  validateTenantSocket,
  getTenantRoom,
  getCategoryRoom,
  getExecutiveLevelRoom,
  getDashboardRoom,
} = require('./tenant');

describe('Tenant Middleware', () => {
  let mockReq;
  let mockRes;
  let mockNext;

  beforeEach(() => {
    mockReq = {
      headers: {},
      query: {},
      params: {},
      user: null,
    };
    mockRes = {
      status: jest.fn().mockReturnThis(),
      json: jest.fn().mockReturnThis(),
    };
    mockNext = jest.fn();
  });

  describe('extractTenant', () => {
    it('should extract tenant from header', () => {
      mockReq.headers['x-tenant-id'] = 'tenant1';

      extractTenant(mockReq, mockRes, mockNext);

      expect(mockReq.tenantId).toBe('tenant1');
      expect(mockNext).toHaveBeenCalled();
    });

    it('should extract tenant from query parameter', () => {
      mockReq.query.tenantId = 'tenant1';

      extractTenant(mockReq, mockRes, mockNext);

      expect(mockReq.tenantId).toBe('tenant1');
      expect(mockNext).toHaveBeenCalled();
    });

    it('should extract tenant from authenticated user', () => {
      mockReq.user = { tenantId: 'tenant1' };

      extractTenant(mockReq, mockRes, mockNext);

      expect(mockReq.tenantId).toBe('tenant1');
      expect(mockNext).toHaveBeenCalled();
    });

    it('should prioritize header over other sources', () => {
      mockReq.headers['x-tenant-id'] = 'tenant-from-header';
      mockReq.query.tenantId = 'tenant-from-query';
      mockReq.user = { tenantId: 'tenant-from-user' };

      extractTenant(mockReq, mockRes, mockNext);

      expect(mockReq.tenantId).toBe('tenant-from-header');
    });

    it('should reject missing tenant ID', () => {
      extractTenant(mockReq, mockRes, mockNext);

      expect(mockRes.status).toHaveBeenCalledWith(400);
      expect(mockRes.json).toHaveBeenCalledWith(
        expect.objectContaining({
          error: 'Tenant ID required',
        })
      );
      expect(mockNext).not.toHaveBeenCalled();
    });
  });

  describe('validateTenantAccess', () => {
    it('should allow access to own tenant', () => {
      mockReq.user = { tenantId: 'tenant1' };
      mockReq.tenantId = 'tenant1';

      validateTenantAccess(mockReq, mockRes, mockNext);

      expect(mockNext).toHaveBeenCalled();
    });

    it('should reject access to different tenant', () => {
      mockReq.user = { tenantId: 'tenant1' };
      mockReq.tenantId = 'tenant2';

      validateTenantAccess(mockReq, mockRes, mockNext);

      expect(mockRes.status).toHaveBeenCalledWith(403);
      expect(mockNext).not.toHaveBeenCalled();
    });

    it('should reject unauthenticated user', () => {
      validateTenantAccess(mockReq, mockRes, mockNext);

      expect(mockRes.status).toHaveBeenCalledWith(401);
      expect(mockNext).not.toHaveBeenCalled();
    });

    it('should use params tenantId if available', () => {
      mockReq.user = { tenantId: 'tenant1' };
      mockReq.params.tenantId = 'tenant1';

      validateTenantAccess(mockReq, mockRes, mockNext);

      expect(mockReq.tenantId).toBe('tenant1');
      expect(mockNext).toHaveBeenCalled();
    });
  });

  describe('validateTenantSocket', () => {
    it('should validate tenant socket access', () => {
      const socket = {
        tenantId: 'tenant1',
        user: { tenantId: 'tenant1' },
      };

      const result = validateTenantSocket(socket, 'tenant:tenant1:dashboard:main');

      expect(result).toBe(true);
    });

    it('should reject cross-tenant socket access', () => {
      const socket = {
        tenantId: 'tenant1',
        user: { tenantId: 'tenant1' },
      };

      const result = validateTenantSocket(socket, 'tenant:tenant2:dashboard:main');

      expect(result).toBe(false);
    });

    it('should reject socket without tenant ID', () => {
      const socket = {
        tenantId: null,
      };

      const result = validateTenantSocket(socket, 'tenant:tenant1:dashboard:main');

      expect(result).toBe(false);
    });
  });

  describe('getTenantRoom', () => {
    it('should return tenant room name', () => {
      const room = getTenantRoom('tenant1');

      expect(room).toBe('tenant:tenant1');
    });

    it('should return tenant room with suffix', () => {
      const room = getTenantRoom('tenant1', 'dashboard:main');

      expect(room).toBe('tenant:tenant1:dashboard:main');
    });
  });

  describe('getCategoryRoom', () => {
    it('should return category room name', () => {
      const room = getCategoryRoom('tenant1', 'sales');

      expect(room).toBe('tenant:tenant1:sales');
    });
  });

  describe('getExecutiveLevelRoom', () => {
    it('should return executive level room name', () => {
      const room = getExecutiveLevelRoom('tenant1', 'C-LEVEL');

      expect(room).toBe('tenant:tenant1:C-LEVEL');
    });
  });

  describe('getDashboardRoom', () => {
    it('should return dashboard room name', () => {
      const room = getDashboardRoom('tenant1', 'main');

      expect(room).toBe('tenant:tenant1:dashboard:main');
    });
  });
});
