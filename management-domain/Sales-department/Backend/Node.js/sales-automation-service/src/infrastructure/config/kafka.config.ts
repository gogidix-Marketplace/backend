import { registerAs } from '@nestjs/config';

export const kafkaConfig = registerAs('kafka', () => ({
  brokers: (process.env.KAFKA_BROKERS || 'localhost:9092').split(','),
  clientId: process.env.KAFKA_CLIENT_ID || 'sales-automation-service',
  consumer: {
    groupId: process.env.KAFKA_CONSUMER_GROUP || 'sales-automation-group',
    allowAutoTopicCreation: true,
    sessionTimeout: parseInt(process.env.KAFKA_SESSION_TIMEOUT_MS, 10) || 30000,
    heartbeatInterval: parseInt(process.env.KAFKA_HEARTBEAT_INTERVAL_MS, 10) || 3000,
  },
  producer: {
    maxWait: parseInt(process.env.KAFKA_PRODUCER_MAX_WAIT_MS, 10) || 1000,
    retries: parseInt(process.env.KAFKA_PRODUCER_RETRIES, 10) || 5,
  },
  topics: {
    automationTriggered: process.env.KAFKA_TOPIC_AUTOMATION_TRIGGERED || 'sales.automation.triggered',
    workflowExecuted: process.env.KAFKA_TOPIC_WORKFLOW_EXECUTED || 'sales.workflow.executed',
    workflowFailed: process.env.KAFKA_TOPIC_WORKFLOW_FAILED || 'sales.workflow.failed',
    ruleCreated: process.env.KAFKA_TOPIC_RULE_CREATED || 'sales.automation.rule.created',
    ruleUpdated: process.env.KAFKA_TOPIC_RULE_UPDATED || 'sales.automation.rule.updated',
    ruleDeleted: process.env.KAFKA_TOPIC_RULE_DELETED || 'sales.automation.rule.deleted',
    leadScored: process.env.KAFKA_TOPIC_LEAD_SCORED || 'sales.lead.scored',
    dealStageChanged: process.env.KAFKA_TOPIC_DEAL_STAGE_CHANGED || 'sales.deal.stage.changed',
  },
}));
