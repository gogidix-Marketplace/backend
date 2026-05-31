/**
 * Command Validators using Joi
 */

const Joi = require('joi');

/**
 * Command metadata schema
 */
const commandMetadataSchema = Joi.object({
  id: Joi.string().optional(),
  timestamp: Joi.number().optional(),
  correlationId: Joi.string().optional(),
  tenantId: Joi.string().required(),
  userId: Joi.string().optional(),
  executiveLevel: Joi.string().optional(),
});

/**
 * Base command schema
 */
const baseCommandSchema = Joi.object({
  id: Joi.string().required(),
  type: Joi.string().required(),
  payload: Joi.object().required(),
  metadata: commandMetadataSchema.required(),
});

/**
 * KPI threshold update command schema
 */
const updateKPIThresholdSchema = Joi.object({
  id: Joi.string().required(),
  type: Joi.string().valid('update_kpi_threshold').required(),
  payload: Joi.object({
    kpiId: Joi.string().required(),
    threshold: Joi.object({
      warning: Joi.number().required(),
      critical: Joi.number().required(),
      operator: Joi.string().valid('gt', 'lt', 'eq', 'gte', 'lte').required(),
    }).required(),
  }).required(),
  metadata: commandMetadataSchema.required(),
});

/**
 * Create KPI alert command schema
 */
const createKPIAlertSchema = Joi.object({
  id: Joi.string().required(),
  type: Joi.string().valid('create_kpi_alert').required(),
  payload: Joi.object({
    kpiId: Joi.string().required(),
    level: Joi.string().valid('WARNING', 'CRITICAL').required(),
    message: Joi.string().required(),
    metadata: Joi.object().optional(),
  }).required(),
  metadata: commandMetadataSchema.required(),
});

/**
 * Broadcast to tenant command schema
 */
const broadcastToTenantSchema = Joi.object({
  id: Joi.string().required(),
  type: Joi.string().valid('broadcast_to_tenant').required(),
  payload: Joi.object({
    tenantId: Joi.string().required(),
    message: Joi.object().required(),
    excludeUserId: Joi.string().optional(),
  }).required(),
  metadata: commandMetadataSchema.required(),
});

/**
 * Broadcast to room command schema
 */
const broadcastToRoomSchema = Joi.object({
  id: Joi.string().required(),
  type: Joi.string().valid('broadcast_to_room').required(),
  payload: Joi.object({
    roomId: Joi.string().required(),
    message: Joi.object().required(),
    excludeUserId: Joi.string().optional(),
  }).required(),
  metadata: commandMetadataSchema.required(),
});

/**
 * Validate command based on type
 */
function validateCommand(command) {
  let schema = baseCommandSchema;

  // Use specific schema based on command type
  switch (command.type) {
    case 'update_kpi_threshold':
      schema = updateKPIThresholdSchema;
      break;
    case 'create_kpi_alert':
      schema = createKPIAlertSchema;
      break;
    case 'broadcast_to_tenant':
      schema = broadcastToTenantSchema;
      break;
    case 'broadcast_to_room':
      schema = broadcastToRoomSchema;
      break;
    default:
      schema = baseCommandSchema;
  }

  return schema.validate(command, { abortEarly: false });
}

module.exports = {
  validateCommand,
  commandMetadataSchema,
};
