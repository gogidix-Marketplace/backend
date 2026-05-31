/**
 * Authentication Middleware Tests
 */

const {
  authenticate,
  optionalAuthenticate,
  hasRole,
  hasExecutiveLevel,
  verifyTenantAccess,
} = require('./auth');

const { verifyToken } = require('../auth/jwt');

// Mock JWT verification
jest.mock('../auth/jwt');

describe('Authentication Middleware', () => {
  let mockReq;
  let mockRes;
  let mockNext;

  beforeEach(() => {
    mockReq = {
      headers: {},
    };
    mockRes = {
      status: jest.fn().mockReturnThis(),
      json: jest.fn().mockReturnThis(),
    };
    mockNext = jest.fn();
    jest.clearAllMocks();
  });

  describe('authenticate', () => {
    it('should reject missing authorization header', async () => {
      await authenticate(mockReq, mockRes, mockNext);

      expect(mockRes.status).toHaveBeenCalledWith(401);
      expect(mockRes.json).toHaveBeenCalledWith(
        expect.objectContaining({
          error: 'No authorization header',
        })
      );
      expect(mockNext).not.toHaveBeenCalled();
    });

    it('should reject invalid authorization format', async () => {
      mockReq.headers.authorization = 'InvalidFormat token';

      await authenticate(mockReq, mockRes, mockNext);

      expect(mockRes.status).toHaveBeenCalledWith(401);
      expect(mockNext).not.toHaveBeenCalled();
    });

    it('should accept valid Bearer token', async () => {
      const decodedToken = {
        tenantId: 'tenant1',
        userId: 'user1',
        executiveLevel: 'C-LEVEL',
        roles: ['admin'],
      };

      verifyToken.mockResolvedValue(decodedToken);
      mockReq.headers.authorization = 'Bearer valid-token';

      await authenticate(mockReq, mockRes, mockNext);

      expect(mockReq.user).toEqual(decodedToken);
      expect(mockNext).toHaveBeenCalled();
    });

    it('should reject invalid token', async () => {
      verifyToken.mockRejectedValue(new Error('Invalid token'));
      mockReq.headers.authorization = 'Bearer invalid-token';

      await authenticate(mockReq, mockRes, mockNext);

      expect(mockRes.status).toHaveBeenCalledWith(401);
      expect(mockNext).not.toHaveBeenCalled();
    });
  });

  describe('optionalAuthenticate', () => {
    it('should proceed without authentication if no token', async () => {
      await optionalAuthenticate(mockReq, mockRes, mockNext);

      expect(mockReq.user).toBeNull();
      expect(mockNext).toHaveBeenCalled();
    });

    it('should authenticate if valid token provided', async () => {
      const decodedToken = {
        tenantId: 'tenant1',
        userId: 'user1',
        executiveLevel: 'C-LEVEL',
        roles: ['admin'],
      };

      verifyToken.mockResolvedValue(decodedToken);
      mockReq.headers.authorization = 'Bearer valid-token';

      await optionalAuthenticate(mockReq, mockRes, mockNext);

      expect(mockReq.user).toEqual(decodedToken);
      expect(mockNext).toHaveBeenCalled();
    });

    it('should proceed on auth error when optional', async () => {
      verifyToken.mockRejectedValue(new Error('Invalid token'));
      mockReq.headers.authorization = 'Bearer invalid-token';

      await optionalAuthenticate(mockReq, mockRes, mockNext);

      expect(mockReq.user).toBeNull();
      expect(mockNext).toHaveBeenCalled();
    });
  });

  describe('hasRole', () => {
    it('should allow user with required role', () => {
      mockReq.user = {
        tenantId: 'tenant1',
        userId: 'user1',
        roles: ['admin', 'executive'],
      };

      const middleware = hasRole('admin');
      middleware(mockReq, mockRes, mockNext);

      expect(mockNext).toHaveBeenCalled();
    });

    it('should allow user with any of the required roles', () => {
      mockReq.user = {
        tenantId: 'tenant1',
        userId: 'user1',
        roles: ['executive'],
      };

      const middleware = hasRole('admin', 'executive');
      middleware(mockReq, mockRes, mockNext);

      expect(mockNext).toHaveBeenCalled();
    });

    it('should reject unauthenticated user', () => {
      const middleware = hasRole('admin');
      middleware(mockReq, mockRes, mockNext);

      expect(mockRes.status).toHaveBeenCalledWith(401);
      expect(mockNext).not.toHaveBeenCalled();
    });

    it('should reject user without required role', () => {
      mockReq.user = {
        tenantId: 'tenant1',
        userId: 'user1',
        roles: ['viewer'],
      };

      const middleware = hasRole('admin');
      middleware(mockReq, mockRes, mockNext);

      expect(mockRes.status).toHaveBeenCalledWith(403);
      expect(mockNext).not.toHaveBeenCalled();
    });
  });

  describe('hasExecutiveLevel', () => {
    it('should allow user with ALL level', () => {
      mockReq.user = {
        tenantId: 'tenant1',
        userId: 'user1',
        executiveLevel: 'ALL',
      };

      const middleware = hasExecutiveLevel('C-LEVEL');
      middleware(mockReq, mockRes, mockNext);

      expect(mockNext).toHaveBeenCalled();
    });

    it('should allow user with matching level', () => {
      mockReq.user = {
        tenantId: 'tenant1',
        userId: 'user1',
        executiveLevel: 'C-LEVEL',
      };

      const middleware = hasExecutiveLevel('C-LEVEL', 'VP');
      middleware(mockReq, mockRes, mockNext);

      expect(mockNext).toHaveBeenCalled();
    });

    it('should reject user without matching level', () => {
      mockReq.user = {
        tenantId: 'tenant1',
        userId: 'user1',
        executiveLevel: 'DIRECTOR',
      };

      const middleware = hasExecutiveLevel('C-LEVEL', 'VP');
      middleware(mockReq, mockRes, mockNext);

      expect(mockRes.status).toHaveBeenCalledWith(403);
      expect(mockNext).not.toHaveBeenCalled();
    });
  });

  describe('verifyTenantAccess', () => {
    it('should allow access to own tenant', () => {
      mockReq.user = {
        tenantId: 'tenant1',
        userId: 'user1',
      };
      mockReq.params = {
        tenantId: 'tenant1',
      };

      verifyTenantAccess(mockReq, mockRes, mockNext);

      expect(mockNext).toHaveBeenCalled();
    });

    it('should reject access to different tenant', () => {
      mockReq.user = {
        tenantId: 'tenant1',
        userId: 'user1',
      };
      mockReq.params = {
        tenantId: 'tenant2',
      };

      verifyTenantAccess(mockReq, mockRes, mockNext);

      expect(mockRes.status).toHaveBeenCalledWith(403);
      expect(mockNext).not.toHaveBeenCalled();
    });

    it('should reject unauthenticated user', () => {
      mockReq.params = {
        tenantId: 'tenant1',
      };

      verifyTenantAccess(mockReq, mockRes, mockNext);

      expect(mockRes.status).toHaveBeenCalledWith(401);
      expect(mockNext).not.toHaveBeenCalled();
    });

    it('should reject missing tenant ID', () => {
      mockReq.user = {
        tenantId: 'tenant1',
        userId: 'user1',
      };

      verifyTenantAccess(mockReq, mockRes, mockNext);

      expect(mockRes.status).toHaveBeenCalledWith(400);
      expect(mockNext).not.toHaveBeenCalled();
    });
  });
});
