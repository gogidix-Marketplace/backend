/**
 * Command Handler for CQRS Pattern
 *
 * Commands represent intents to change state in the system.
 * All commands are validated and processed asynchronously.
 */

const { validateCommand } = require('./validators');
const { publishCommand } = require('./publisher');
const logger = require('../config/logger');

/**
 * Command types
 */
const CommandTypes = {
  // KPI Commands
  UPDATE_KPI_THRESHOLD: 'update_kpi_threshold',
  CREATE_KPI_ALERT: 'create_kpi_alert',
  DISMISS_KPI_ALERT: 'dismiss_kpi_alert',

  // Dashboard Commands
  CREATE_DASHBOARD: 'create_dashboard',
  UPDATE_DASHBOARD_LAYOUT: 'update_dashboard_layout',
  PIN_WIDGET: 'pin_widget',
  UNPIN_WIDGET: 'unpin_widget',

  // Subscription Commands
  BROADCAST_TO_TENANT: 'broadcast_to_tenant',
  BROADCAST_TO_ROOM: 'broadcast_to_room',

  // Session Commands
  UPDATE_USER_PRESENCE: 'update_user_presence',
  SET_USER_AWAY: 'set_user_away',
};

/**
 * Handle incoming command
 */
async function handleCommand(command) {
  try {
    // Validate command structure
    const { error, value } = validateCommand(command);
    if (error) {
      throw new Error(`Command validation failed: ${error.details[0].message}`);
    }

    logger.debug(`Processing command: ${value.type}`, { commandId: value.id });

    // Publish command to Kafka for processing
    await publishCommand(value);

    return {
      success: true,
      commandId: value.id,
      status: 'queued',
    };

  } catch (error) {
    logger.error('Command handling failed:', error);
    throw error;
  }
}

/**
 * Create a command object
 */
function createCommand(type, payload, metadata = {}) {
  return {
    id: metadata.id || generateCommandId(),
    type,
    payload,
    metadata: {
      ...metadata,
      timestamp: Date.now(),
      correlationId: metadata.correlationId || generateCorrelationId(),
    },
  };
}

/**
 * Generate unique command ID
 */
function generateCommandId() {
  return `cmd_${Date.now()}_${Math.random().toString(36).substr(2, 9)}`;
}

/**
 * Generate correlation ID for tracking
 */
function generateCorrelationId() {
  return `corr_${Date.now()}_${Math.random().toString(36).substr(2, 9)}`;
}

module.exports = {
  handleCommand,
  createCommand,
  generateCommandId,
  generateCorrelationId,
  CommandTypes,
};
