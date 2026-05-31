require('dotenv').config();

const config = {
  env: process.env.NODE_ENV || 'development',

  server: {
    port: parseInt(process.env.PORT, 10) || 4000,
    host: process.env.HOST || 'localhost',
    apiPrefix: process.env.API_PREFIX || '/api/v1'
  },

  websocket: {
    port: parseInt(process.env.WS_PORT, 10) || 4001,
    path: process.env.WS_PATH || '/socket.io',
    corsOrigin: process.env.WS_CORS_ORIGIN?.split(',') || ['http://localhost:3000'],
    pingInterval: parseInt(process.env.WS_PING_INTERVAL, 10) || 25000,
    pingTimeout: parseInt(process.env.WS_PING_TIMEOUT, 10) || 20000,
    maxHttpBufferSize: parseInt(process.env.WS_MAX_HTTP_BUFFER_SIZE, 10) || 1e8
  },

  redis: {
    host: process.env.REDIS_HOST || 'localhost',
    port: parseInt(process.env.REDIS_PORT, 10) || 6379,
    password: process.env.REDIS_PASSWORD || undefined,
    db: parseInt(process.env.REDIS_DB, 10) || 0,
    tls: process.env.REDIS_TLS === 'true',
    clusterMode: process.env.REDIS_CLUSTER_MODE === 'true',
    clusterNodes: process.env.REDIS_CLUSTER_NODES?.split(',') || [],
    sentinelMode: process.env.REDIS_SENTINEL_MODE === 'true',
    sentinelName: process.env.REDIS_SENTINEL_NAME || 'mymaster',
    sentinelNodes: process.env.REDIS_SENTINEL_NODES?.split(',') || [],
    username: process.env.REDIS_USERNAME || undefined
  },

  mongodb: {
    uri: process.env.MONGODB_URI || 'mongodb://localhost:27017',
    dbName: process.env.DB_NAME || 'gogidix_executive',
    poolSize: parseInt(process.env.MONGODB_POOL_SIZE, 10) || 10,
    minPoolSize: parseInt(process.env.MONGODB_MIN_POOL_SIZE, 10) || 2
  },

  kafka: {
    brokers: process.env.KAFKA_BROKERS?.split(',') || ['localhost:9092'],
    clientId: process.env.KAFKA_CLIENT_ID || 'websocket-service',
    consumerGroupId: process.env.KAFKA_CONSUMER_GROUP_ID || 'websocket-service-group',
    producer: {
      acks: process.env.KAFKA_PRODUCER_ACKS || 'all',
      maxRetries: parseInt(process.env.KAFKA_PRODUCER_MAX_RETRIES, 10) || 3
    },
    consumer: {
      sessionTimeout: parseInt(process.env.KAFKA_CONSUMER_SESSION_TIMEOUT, 10) || 30000,
      heartbeatInterval: parseInt(process.env.KAFKA_CONSUMER_HEARTBEAT_INTERVAL, 10) || 3000
    },
    topics: {
      executiveUpdates: process.env.KAFKA_TOPICS_EXECUTIVE_UPDATES || 'executive.updates',
      kpiUpdates: process.env.KAFKA_TOPICS_KPI_UPDATES || 'kpi.updates',
      dashboardUpdates: process.env.KAFKA_TOPICS_DASHBOARD_UPDATES || 'dashboard.updates',
      notifications: process.env.KAFKA_TOPICS_NOTIFICATIONS || 'notifications',
      presence: process.env.KAFKA_TOPICS_PRESENCE || 'presence.events'
    }
  },

  jwt: {
    secret: process.env.JWT_SECRET || 'your-super-secret-jwt-key',
    expiresIn: process.env.JWT_EXPIRES_IN || '24h',
    refreshExpiresIn: process.env.JWT_REFRESH_EXPIRES_IN || '7d',
    issuer: process.env.JWT_ISSUER || 'gogidix-websocket-service',
    audience: process.env.JWT_AUDIENCE || 'gogidix-executive-domain'
  },

  rateLimit: {
    windowMs: parseInt(process.env.RATE_LIMIT_WINDOW_MS, 10) || 60000,
    maxRequests: parseInt(process.env.RATE_LIMIT_MAX_REQUESTS, 10) || 100,
    skipFailedRequests: process.env.RATE_LIMIT_SKIP_FAILED_REQUESTS === 'true',
    skipSuccessfulRequests: process.env.RATE_LIMIT_SKIP_SUCCESSFUL_REQUESTS === 'true'
  },

  connections: {
    maxPerTenant: parseInt(process.env.MAX_CONNECTIONS_PER_TENANT, 10) || 100,
    maxPerUser: parseInt(process.env.MAX_CONNECTIONS_PER_USER, 10) || 5,
    maxRoomsPerTenant: parseInt(process.env.MAX_ROOMS_PER_TENANT, 10) || 50,
    maxUsersPerRoom: parseInt(process.env.MAX_USERS_PER_ROOM, 10) || 100
  },

  presence: {
    ttl: parseInt(process.env.PRESENCE_TTL, 10) || 120,
    syncInterval: parseInt(process.env.PRESENCE_SYNC_INTERVAL, 10) || 30000,
    typingTimeout: parseInt(process.env.PRESENCE_TYPING_TIMEOUT, 10) || 5000
  },

  logging: {
    level: process.env.LOG_LEVEL || 'info',
    fileEnabled: process.env.LOG_FILE_ENABLED !== 'false',
    filePath: process.env.LOG_FILE_PATH || './logs',
    maxSize: process.env.LOG_MAX_SIZE || '20m',
    maxFiles: process.env.LOG_MAX_FILES || '14d'
  },

  cors: {
    origin: process.env.CORS_ORIGIN?.split(',') || ['http://localhost:3000'],
    credentials: process.env.CORS_CREDENTIALS === 'true',
    methods: process.env.CORS_METHODS?.split(',') || ['GET', 'POST', 'PUT', 'DELETE', 'OPTIONS'],
    headers: process.env.CORS_HEADERS?.split(',') || ['Content-Type', 'Authorization']
  },

  healthCheck: {
    interval: parseInt(process.env.HEALTH_CHECK_INTERVAL, 10) || 30000,
    timeout: parseInt(process.env.HEALTH_CHECK_TIMEOUT, 10) || 5000
  },

  multiTenant: {
    enabled: process.env.MULTI_TENANT_ENABLED !== 'false',
    defaultTenantId: process.env.DEFAULT_TENANT_ID || 'default'
  },

  metrics: {
    enabled: process.env.METRICS_ENABLED === 'true',
    port: parseInt(process.env.METRICS_PORT, 10) || 9090,
    path: process.env.METRICS_PATH || '/metrics'
  }
};

module.exports = config;
