import { salesApi } from '@/services/api'
import axios from 'axios'

// Mock axios
jest.mock('axios')
const mockedAxios = axios as jest.Mocked<typeof axios>

describe('SalesApiService', () => {
  beforeEach(() => {
    jest.clearAllMocks()
    // Reset axios mock
    mockedAxios.create.mockReturnValue({
      get: jest.fn().mockResolvedValue({ data: {} }),
      post: jest.fn().mockResolvedValue({ data: {} }),
      put: jest.fn().mockResolvedValue({ data: {} }),
      delete: jest.fn().mockResolvedValue({ data: {} }),
      interceptors: {
        request: { use: jest.fn() },
        response: { use: jest.fn() },
      },
    } as any)
  })

  describe('getDashboardSummary', () => {
    it('should call the correct endpoint', async () => {
      const mockClient = {
        get: jest.fn().mockResolvedValue({ data: { success: true, data: {} } }),
        interceptors: {
          request: { use: jest.fn() },
          response: { use: jest.fn() },
        },
      }
      mockedAxios.create.mockReturnValue(mockClient as any)

      await salesApi.getDashboardSummary('US')

      expect(mockClient.get).toHaveBeenCalledWith('/dashboard/summary', {
        headers: { 'X-Country': 'US' },
      })
    })
  })

  describe('getSalesMetrics', () => {
    it('should call the correct endpoint with date parameter', async () => {
      const mockClient = {
        get: jest.fn().mockResolvedValue({ data: { success: true, data: {} } }),
        interceptors: {
          request: { use: jest.fn() },
          response: { use: jest.fn() },
        },
      }
      mockedAxios.create.mockReturnValue(mockClient as any)

      await salesApi.getSalesMetrics('US', '2024-01-15')

      expect(mockClient.get).toHaveBeenCalledWith('/dashboard/metrics', {
        params: { asOfDate: '2024-01-15' },
        headers: { 'X-Country': 'US' },
      })
    })
  })

  describe('getDeals', () => {
    it('should call the correct endpoint with query parameters', async () => {
      const mockClient = {
        get: jest.fn().mockResolvedValue({ data: { success: true, data: {} } }),
        interceptors: {
          request: { use: jest.fn() },
          response: { use: jest.fn() },
        },
      }
      mockedAxios.create.mockReturnValue(mockClient as any)

      await salesApi.getDeals({ page: 1, size: 20, sortBy: 'createdAt', sortDirection: 'desc' })

      expect(mockClient.get).toHaveBeenCalledWith('/deals', {
        params: { page: 1, size: 20, sortBy: 'createdAt', sortDirection: 'desc' },
      })
    })
  })

  describe('createDeal', () => {
    it('should POST deal data to the correct endpoint', async () => {
      const mockClient = {
        post: jest.fn().mockResolvedValue({ data: { success: true, data: {} } }),
        interceptors: {
          request: { use: jest.fn() },
          response: { use: jest.fn() },
        },
      }
      mockedAxios.create.mockReturnValue(mockClient as any)

      const dealData = {
        title: 'Test Deal',
        value: 100000,
        customerId: 'customer1',
      }

      await salesApi.createDeal(dealData)

      expect(mockClient.post).toHaveBeenCalledWith('/deals', dealData)
    })
  })

  describe('getCustomers', () => {
    it('should call the correct endpoint with default parameters', async () => {
      const mockClient = {
        get: jest.fn().mockResolvedValue({ data: { success: true, data: {} } }),
        interceptors: {
          request: { use: jest.fn() },
          response: { use: jest.fn() },
        },
      }
      mockedAxios.create.mockReturnValue(mockClient as any)

      await salesApi.getCustomers()

      expect(mockClient.get).toHaveBeenCalledWith('/customers', {
        params: undefined,
      })
    })
  })
})
