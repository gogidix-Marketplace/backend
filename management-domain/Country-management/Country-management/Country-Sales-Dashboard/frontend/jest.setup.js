import '@testing-library/jest-dom'

// Mock environment variables
process.env.NEXT_PUBLIC_API_URL = 'http://localhost:8080/api/sales/v1'
process.env.NEXT_PUBLIC_COUNTRY_CODE = 'US'

// Mock fetch globally
global.fetch = jest.fn()

beforeEach(() => {
  // Clear all mocks before each test
  jest.clearAllMocks()
})
