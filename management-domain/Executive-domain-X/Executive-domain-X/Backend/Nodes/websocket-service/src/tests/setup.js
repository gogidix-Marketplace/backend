const config = require('../config');

beforeAll(async () => {
  config.env = 'test';

  console.log('Test environment setup');
});

afterAll(async () => {
  console.log('Test environment teardown');
});

global.console = {
  ...console,
  log: jest.fn(),
  debug: jest.fn(),
  info: jest.fn(),
  warn: jest.fn(),
  error: jest.fn(),
};
