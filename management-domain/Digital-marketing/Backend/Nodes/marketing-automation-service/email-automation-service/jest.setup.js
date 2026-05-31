// Jest setup file
global.console = {
  ...console,
  // Suppress console output during tests unless needed
  log: jest.fn(),
  debug: jest.fn(),
  info: jest.fn(),
  warn: jest.fn(),
  error: jest.fn(),
};

// Mock environment variables
process.env.NODE_ENV = 'test';
process.env.JWT_SECRET = 'test-secret-key';
process.env.EMAIL_QUEUE_BACKOFF_DELAY = '2000';
