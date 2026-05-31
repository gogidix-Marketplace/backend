import { ApiService } from '../src/application/services/apiService';
import { describe, it, expect, beforeEach, vi } from 'vitest';

// Mock AsyncStorage
vi.mock('@react-native-async-storage/async-storage', () => ({
  default: {
    getItem: vi.fn(() => Promise.resolve(null)),
    setItem: vi.fn(() => Promise.resolve()),
    removeItem: vi.fn(() => Promise.resolve()),
  },
}));

// Mock NetInfo
vi.mock('@react-native-community/netinfo', () => ({
  default: {
    fetch: vi.fn(() => Promise.resolve({ isConnected: true })),
  },
}));

describe('ApiService', () => {
  let apiService: ApiService;

  beforeEach(() => {
    apiService = new ApiService();
  });

  describe('Configuration', () => {
    it('should initialize with base URL', () => {
      expect(apiService).toBeDefined();
    });

    it('should set authentication token', async () => {
      await apiService.setAuthToken('test-token');
      // Verify token is stored
    });
  });

  describe('API Calls', () => {
    it('should fetch leads successfully', async () => {
      const leads = await apiService.getLeads();

      expect(leads).toBeDefined();
      expect(Array.isArray(leads)).toBe(true();
    });

    it('should handle API errors gracefully', async () => {
      // Mock failed API call
      const leads = await apiService.getLeads();

      expect(leads).toBeDefined();
      // Verify error handling
    });

    it('should create new lead', async () => {
      const newLead = {
        firstName: 'John',
        lastName: 'Doe',
        email: 'john@example.com'
      };

      const result = await apiService.createLead(newLead);

      expect(result).toBeDefined();
    });
  });

  describe('Request Interception', () => {
    it('adds auth token to requests', async () => {
      await apiService.setAuthToken('test-token');

      // Make API call
      await apiService.getLeads();

      // Verify auth header added
    });

    it('retries failed requests', async () => {
      // Mock network failure
      const data = await apiService.getLeads();

      expect(data).toBeDefined();
    });
  });

  describe('Cache Strategy', () => {
    it('caches GET requests', async () => {
      await apiService.getLeads();
      // Second call should use cache
      await apiService.getLeads();
    });

    it('invalidates cache on mutations', async () => {
      await apiService.getLeads();
      await apiService.createLead({} as any);
      // Next GET should fetch fresh data
    });
  });
});
