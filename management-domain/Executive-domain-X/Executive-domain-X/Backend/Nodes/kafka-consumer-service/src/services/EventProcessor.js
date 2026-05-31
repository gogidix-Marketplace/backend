const EventController = require('../controllers/EventController');
const EventLog = require('../models/EventLog');
const logger = require('../config/logger');

class EventProcessor {
  constructor() {
    this.eventController = new EventController();
    this.setupEventHandlers();
  }

  setupEventHandlers() {
    // Executive events
    this.eventController.registerEventHandler('EXECUTIVE_CREATED', async (data) => {
      logger.info(`Processing EXECUTIVE_CREATED event for executive: ${data.aggregateId}`);
      return this.processExecutiveCreated(data);
    });

    this.eventController.registerEventHandler('EXECUTIVE_UPDATED', async (data) => {
      logger.info(`Processing EXECUTIVE_UPDATED event for executive: ${data.aggregateId}`);
      return this.processExecutiveUpdated(data);
    });

    // KPI events
    this.eventController.registerEventHandler('KPI_UPDATED', async (data) => {
      logger.info(`Processing KPI_UPDATED event for KPI: ${data.aggregateId}`);
      return this.processKpiUpdated(data);
    });

    // Strategy events
    this.eventController.registerEventHandler('STRATEGY_UPDATED', async (data) => {
      logger.info(`Processing STRATEGY_UPDATED event for strategy: ${data.aggregateId}`);
      return this.processStrategyUpdated(data);
    });

    // Approval events
    this.eventController.registerEventHandler('APPROVAL_CREATED', async (data) => {
      logger.info(`Processing APPROVAL_CREATED event for approval: ${data.aggregateId}`);
      return this.processApprovalCreated(data);
    });

    // Decision events
    this.eventController.registerEventHandler('DECISION_MADE', async (data) => {
      logger.info(`Processing DECISION_MADE event for decision: ${data.aggregateId}`);
      return this.processDecisionMade(data);
    });
  }

  async processExecutiveCreated(data) {
    // Process new executive creation
    // This could include creating initial KPIs, setting up notifications, etc.
    logger.info(`New executive created: ${data.payload.name || data.aggregateId}`);

    // Example: Cache executive data in Redis
    // await redis.getClient().setex(`executive:${data.aggregateId}`, 3600, JSON.stringify(data.payload));

    return { success: true, message: 'Executive created successfully' };
  }

  async processExecutiveUpdated(data) {
    // Process executive updates
    logger.info(`Executive updated: ${data.aggregateId}, changes: ${Object.keys(data.payload).join(', ')}`);

    // Example: Update executive cache
    // await redis.getClient().setex(`executive:${data.aggregateId}`, 3600, JSON.stringify(data.payload));

    return { success: true, message: 'Executive updated successfully' };
  }

  async processKpiUpdated(data) {
    // Process KPI updates
    logger.info(`KPI updated: ${data.aggregateId}, new value: ${data.payload.value}`);

    // Example: Update KPI cache and trigger notifications
    // await redis.getClient().setex(`kpi:${data.aggregateId}`, 3600, JSON.stringify(data.payload));

    return { success: true, message: 'KPI updated successfully' };
  }

  async processStrategyUpdated(data) {
    // Process strategy updates
    logger.info(`Strategy updated: ${data.aggregateId}`);

    // Example: Update strategy cache
    // await redis.getClient().setex(`strategy:${data.aggregateId}`, 3600, JSON.stringify(data.payload));

    return { success: true, message: 'Strategy updated successfully' };
  }

  async processApprovalCreated(data) {
    // Process new approval
    logger.info(`New approval created: ${data.aggregateId} for executive: ${data.payload.executiveId}`);

    // Example: Cache approval data
    // await redis.getClient().setex(`approval:${data.aggregateId}`, 3600, JSON.stringify(data.payload));

    return { success: true, message: 'Approval created successfully' };
  }

  async processDecisionMade(data) {
    // Process decision
    logger.info(`Decision made: ${data.aggregateId}, result: ${data.payload.approved ? 'Approved' : 'Rejected'}`);

    // Example: Update decision cache
    // await redis.getClient().setex(`decision:${data.aggregateId}`, 3600, JSON.stringify(data.payload));

    return { success: true, message: 'Decision processed successfully' };
  }

  async getProcessingStats() {
    return await this.eventController.getEventStats();
  }
}

module.exports = EventProcessor;