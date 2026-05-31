/**
 * API Configuration
 * Central configuration for all backend service endpoints
 */

export const API_CONFIG = {
  // Gateway URL (single entry point for all services)
  gatewayUrl: import.meta.env.VITE_API_GATEWAY_URL || 'http://localhost:8080',

  // Executive Backend Services
  executive: {
    analytics: import.meta.env.VITE_EXECUTIVE_ANALYTICS_URL || 'http://localhost:9001',
    approval: import.meta.env.VITE_EXECUTIVE_APPROVAL_URL || 'http://localhost:9002',
    strategy: import.meta.env.VITE_EXECUTIVE_STRATEGY_URL || 'http://localhost:9003',
    financial: import.meta.env.VITE_EXECUTIVE_FINANCIAL_URL || 'http://localhost:9011',
    operations: import.meta.env.VITE_EXECUTIVE_OPERATIONS_URL || 'http://localhost:9021',
    technology: import.meta.env.VITE_EXECUTIVE_TECHNOLOGY_URL || 'http://localhost:9031',
    alerts: import.meta.env.VITE_EXECUTIVE_ALERTS_URL || 'http://localhost:9041',
    audit: import.meta.env.VITE_EXECUTIVE_AUDIT_URL || 'http://localhost:9042',
    report: import.meta.env.VITE_EXECUTIVE_REPORT_URL || 'http://localhost:9043',
    dashboard: import.meta.env.VITE_EXECUTIVE_DASHBOARD_URL || 'http://localhost:9044',
  },

  // Data Aggregation Services
  aggregation: {
    dataAggregation: import.meta.env.VITE_AGGREGATION_DATA_URL || 'http://localhost:9201',
    metricsAggregation: import.meta.env.VITE_AGGREGATION_METRICS_URL || 'http://localhost:9202',
    centralizedData: import.meta.env.VITE_AGGREGATION_CENTRALIZED_URL || 'http://localhost:9203',
  },

  // AI Services
  ai: {
    baseUrl: import.meta.env.VITE_AI_API_URL || 'http://localhost:7001',
    businessIntelligence: {
      churnPrediction: 'http://localhost:7001',
      customerSegmentation: 'http://localhost:7002',
      marketBasketAnalysis: 'http://localhost:7003',
      productRecommendation: 'http://localhost:7004',
      salesForecasting: 'http://localhost:7005',
      userProfiling: 'http://localhost:7006',
      intelligenceAnalysis: 'http://localhost:7007',
      researchIntelligence: 'http://localhost:7008',
    },
    customerExperience: {
      chatbot: 'http://localhost:7011',
      contentGeneration: 'http://localhost:7012',
      customerEngagement: 'http://localhost:7013',
      customerFeedback: 'http://localhost:7014',
      emailOptimization: 'http://localhost:7015',
      notification: 'http://localhost:7016',
      personalization: 'http://localhost:7017',
      recommendationEngine: 'http://localhost:7018',
      search: 'http://localhost:7019',
      sentimentAnalysis: 'http://localhost:7020',
      translation: 'http://localhost:7021',
      voiceAssistant: 'http://localhost:7022',
      voiceRecognition: 'http://localhost:7023',
    },
    dataAnalytics: {
      analyticsDashboard: 'http://localhost:7031',
      dataProcessing: 'http://localhost:7032',
      dataValidation: 'http://localhost:7033',
      prediction: 'http://localhost:7034',
      reporting: 'http://localhost:7035',
      predictiveAnalytics: 'http://localhost:7036',
      timeSeriesForecasting: 'http://localhost:7037',
    },
    contentProcessing: {
      documentClassification: 'http://localhost:7041',
      documentExtraction: 'http://localhost:7042',
      documentProcessing: 'http://localhost:7043',
      ocr: 'http://localhost:7044',
      summarization: 'http://localhost:7045',
      multimodalProcessing: 'http://localhost:7046',
      nlpProcessing: 'http://localhost:7047',
    },
    mlOperations: {
      featureExtraction: 'http://localhost:7051',
      featureStore: 'http://localhost:7052',
      inference: 'http://localhost:7053',
      modelManagement: 'http://localhost:7054',
      modelTraining: 'http://localhost:7055',
      training: 'http://localhost:7056',
    },
    security: {
      authentication: 'http://localhost:7061',
      fraudDetection: 'http://localhost:7062',
      securityAnalysis: 'http://localhost:7063',
    },
  },

  // Shared Business Infrastructure
  business: {
    ecommerce: {
      baseUrl: import.meta.env.VITE_ECOMMERCE_API_URL || 'http://localhost:8001',
      services: [
        'analytics', 'cart', 'catalog', 'communication', 'customer',
        'discount', 'fulfillment', 'influencer', 'integration',
        'inventory', 'loyalty', 'marketplace', 'notification',
        'order', 'payment', 'pricing', 'procurement', 'promotion',
        'publicAPI', 'search', 'tenant', 'tracking', 'vendor',
        'wholesaler', 'wishlist'
      ],
    },
    admin: {
      baseUrl: import.meta.env.VITE_ADMIN_API_URL || 'http://localhost:8501',
      services: [
        'partner-registration', // 8501
        'partner-kyc',          // 8502
        'partner-management',   // 8503
        'partner-domain',       // 8504
        'commission-engine',    // 8505
        'settlement',           // 8506
        'country-analytics',    // 8507
        'cross-domain-analytics',// 8508
        'regional-hub',         // 8509
        'service-monitoring',   // 8510
        'hq-oversight',         // 8511
      ],
    },
    courier: {
      baseUrl: import.meta.env.VITE_COURIER_API_URL || 'http://localhost:8101',
    },
    warehousing: {
      baseUrl: import.meta.env.VITE_WAREHOUSE_API_URL || 'http://localhost:8201',
    },
  },

  // Foundation Services
  foundation: {
    auth: import.meta.env.VITE_AUTH_ISSUER || 'http://localhost:8080/auth',
    discovery: import.meta.env.VITE_DISCOVERY_URL || 'http://localhost:8761',
    config: import.meta.env.VITE_CONFIG_URL || 'http://localhost:8888',
  },

  // WebSocket
  websocket: import.meta.env.VITE_WS_URL || 'ws://localhost:9090',

  // Timeouts
  timeout: {
    default: 30000,     // 30 seconds
    upload: 300000,     // 5 minutes
    download: 600000,   // 10 minutes
    stream: 900000,     // 15 minutes
  },

  // Retry configuration
  retry: {
    maxRetries: 3,
    retryDelay: 1000,
    retryCondition: (error: any) => {
      // Retry on network errors and 5xx server errors
      return !error.response || (error.response.status >= 500 && error.response.status < 600)
    },
  },

  // Feature flags
  features: {
    useMockData: import.meta.env.VITE_USE_MOCK_DATA === 'true',
    enableRealtime: import.meta.env.VITE_ENABLE_REALTIME !== 'false',
    enableAI: import.meta.env.VITE_ENABLE_AI !== 'false',
    enableWebsocket: import.meta.env.VITE_ENABLE_WEBSOCKET !== 'false',
  },
} as const

export type ApiConfig = typeof API_CONFIG

/**
 * Get service URL by role and service name
 */
export function getServiceUrl(role: 'CEO' | 'CFO' | 'COO' | 'CTO', service: string): string {
  const roleServices: Record<string, string> = {
    CEO: API_CONFIG.executive.analytics,
    CFO: API_CONFIG.executive.financial,
    COO: API_CONFIG.executive.operations,
    CTO: API_CONFIG.executive.technology,
  }
  return roleServices[role] || API_CONFIG.executive.analytics
}

/**
 * Get AI service URL
 */
export function getAIServiceUrl(category: string, service: string): string {
  const categoryMap: Record<string, any> = API_CONFIG.ai
  if (categoryMap[category] && categoryMap[category][service]) {
    return categoryMap[category][service]
  }
  return API_CONFIG.ai.baseUrl
}
