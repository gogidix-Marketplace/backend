/**
 * Command Publisher for Kafka
 *
 * Publishes commands to Kafka topics for distributed processing.
 */

const { kafkaProducer, connectKafkaProducer } = require('../kafka/producer');
const logger = require('../config/logger');

/**
 * Kafka topics for commands
 */
const CommandTopics = {
  KPI_COMMANDS: 'executive.kpi.commands',
  DASHBOARD_COMMANDS: 'executive.dashboard.commands',
  ALERT_COMMANDS: 'executive.alert.commands',
  BROADCAST_COMMANDS: 'executive.broadcast.commands',
};

/**
 * Map command type to topic
 */
function getTopicForCommand(commandType) {
  const topicMap = {
    update_kpi_threshold: CommandTopics.KPI_COMMANDS,
    create_kpi_alert: CommandTopics.ALERT_COMMANDS,
    dismiss_kpi_alert: CommandTopics.ALERT_COMMANDS,
    create_dashboard: CommandTopics.DASHBOARD_COMMANDS,
    update_dashboard_layout: CommandTopics.DASHBOARD_COMMANDS,
    broadcast_to_tenant: CommandTopics.BROADCAST_COMMANDS,
    broadcast_to_room: CommandTopics.BROADCAST_COMMANDS,
  };

  return topicMap[commandType] || CommandTopics.KPI_COMMANDS;
}

/**
 * Publish command to Kafka
 */
async function publishCommand(command) {
  try {
    // Ensure producer is connected
    if (!kafkaProducer || !kafkaProducer.isConnected()) {
      await connectKafkaProducer();
    }

    const topic = getTopicForCommand(command.type);

    const message = {
      key: command.metadata.tenantId,
      value: JSON.stringify(command),
      headers: {
        commandId: command.id,
        correlationId: command.metadata.correlationId,
        tenantId: command.metadata.tenantId,
        timestamp: command.metadata.timestamp.toString(),
      },
    };

    await kafkaProducer.send({
      topic,
      messages: [message],
    });

    logger.debug(`Command published to ${topic}:`, { commandId: command.id });

    return {
      success: true,
      topic,
      commandId: command.id,
    };

  } catch (error) {
    logger.error('Failed to publish command:', error);
    throw error;
  }
}

module.exports = {
  publishCommand,
  CommandTopics,
};
