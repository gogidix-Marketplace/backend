const config = require('../../config');

describe('Config', () => {
  test('should have default values', () => {
    expect(config).toBeDefined();
    expect(config.env).toBeDefined();
    expect(config.server).toBeDefined();
    expect(config.websocket).toBeDefined();
    expect(config.redis).toBeDefined();
    expect(config.mongodb).toBeDefined();
    expect(config.kafka).toBeDefined();
    expect(config.jwt).toBeDefined();
  });

  test('should have server config with port and host', () => {
    expect(config.server.port).toBeGreaterThanOrEqual(1);
    expect(config.server.port).toBeLessThanOrEqual(65535);
    expect(config.server.host).toBeDefined();
  });

  test('should have websocket config', () => {
    expect(config.websocket.port).toBeDefined();
    expect(config.websocket.path).toBeDefined();
    expect(config.websocket.corsOrigin).toBeDefined();
    expect(Array.isArray(config.websocket.corsOrigin)).toBe(true);
  });

  test('should have kafka topics defined', () => {
    expect(config.kafka.topics).toBeDefined();
    expect(config.kafka.topics.executiveUpdates).toBeDefined();
    expect(config.kafka.topics.kpiUpdates).toBeDefined();
    expect(config.kafka.topics.dashboardUpdates).toBeDefined();
    expect(config.kafka.topics.notifications).toBeDefined();
    expect(config.kafka.topics.presence).toBeDefined();
  });

  test('should have connection limits', () => {
    expect(config.connections.maxPerTenant).toBeGreaterThan(0);
    expect(config.connections.maxPerUser).toBeGreaterThan(0);
    expect(config.connections.maxRoomsPerTenant).toBeGreaterThan(0);
    expect(config.connections.maxUsersPerRoom).toBeGreaterThan(0);
  });
});
