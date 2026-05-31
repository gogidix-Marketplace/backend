/**
 * JWT Authentication Tests
 */

const { verifyToken, signToken } = require('./jwt');

describe('JWT Authentication', () => {
  const validPayload = {
    tenantId: 'tenant-123',
    userId: 'user-456',
    executiveLevel: 'CEO',
    email: 'ceo@example.com',
  };

  describe('signToken', () => {
    test('should sign a valid token', () => {
      const token = signToken(validPayload);

      expect(token).toBeDefined();
      expect(typeof token).toBe('string');
      expect(token.split('.')).toHaveLength(3); // JWT has 3 parts
    });

    test('should include payload in token', () => {
      const token = signToken(validPayload);
      const decoded = Buffer.from(token.split('.')[1], 'base64').toString();
      const data = JSON.parse(decoded);

      expect(data.tenantId).toBe(validPayload.tenantId);
      expect(data.userId).toBe(validPayload.userId);
      expect(data.executiveLevel).toBe(validPayload.executiveLevel);
    });
  });

  describe('verifyToken', () => {
    test('should verify a valid token', async () => {
      const token = signToken(validPayload);
      const decoded = await verifyToken(token);

      expect(decoded).toBeDefined();
      expect(decoded.tenantId).toBe(validPayload.tenantId);
      expect(decoded.userId).toBe(validPayload.userId);
      expect(decoded.executiveLevel).toBe(validPayload.executiveLevel);
    });

    test('should reject an invalid token', async () => {
      await expect(verifyToken('invalid-token')).rejects.toThrow();
    });

    test('should reject a tampered token', async () => {
      const token = signToken(validPayload);
      const tamperedToken = token + 'tamper';

      await expect(verifyToken(tamperedToken)).rejects.toThrow();
    });

    test('should reject expired token', async () => {
      // This would require a very short expiry in signToken
      // For now, we use 1h expiry so this is conceptual
      const token = signToken({ ...validPayload, exp: Math.floor(Date.now() / 1000) - 3600 });

      await expect(verifyToken(token)).rejects.toThrow();
    }, 10000);
  });

  describe('Integration', () => {
    test('should sign and verify token successfully', async () => {
      const token = signToken(validPayload);
      const decoded = await verifyToken(token);

      expect(decoded.tenantId).toBe(validPayload.tenantId);
      expect(decoded.userId).toBe(validPayload.userId);
    });
  });
});
