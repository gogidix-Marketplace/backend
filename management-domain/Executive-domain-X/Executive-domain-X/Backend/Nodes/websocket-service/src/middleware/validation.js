const Joi = require('joi');
const logger = require('../config/logger');

class ValidationMiddleware {
  constructor() {
    this.schemas = {
      authenticate: Joi.object({
        token: Joi.string().required()
      }),

      joinRoom: Joi.object({
        room: Joi.string().required(),
        tenantId: Joi.string().optional(),
        metadata: Joi.object().optional()
      }),

      leaveRoom: Joi.object({
        room: Joi.string().required()
      }),

      sendMessage: Joi.object({
        room: Joi.string().optional(),
        to: Joi.string().optional(),
        event: Joi.string().required(),
        data: Joi.object().required(),
        ephemeral: Joi.boolean().optional()
      }),

      broadcast: Joi.object({
        room: Joi.string().optional(),
        event: Joi.string().required(),
        data: Joi.object().required(),
        exclude: Joi.array().items(Joi.string()).optional()
      }),

        room: Joi.string().required(),
        state: Joi.string().valid('online', 'offline', 'away', 'busy').required(),
        metadata: Joi.object().optional()
      }),

      typingIndicator: Joi.object({
        room: Joi.string().required(),
        isTyping: Joi.boolean().required()
      }),

      createRoom: Joi.object({
        name: Joi.string().required(),
        type: Joi.string().valid('public', 'private', 'presence').required(),
        maxUsers: Joi.number().integer().min(1).max(1000).optional(),
        metadata: Joi.object().optional()
      }),

      subscribeToEvents: Joi.object({
        events: Joi.array().items(Joi.string()).required()
      }),

      updateRoomMetadata: Joi.object({
        room: Joi.string().required(),
        metadata: Joi.object().required()
      })
    };
  }

  validate(schemaName) {
    return async (socket, data, callback) => {
      try {
        const schema = this.schemas[schemaName];
        if (!schema) {
          throw new Error(`Schema '${schemaName}' not found`);
        }

        const { error, value } = schema.validate(data, {
          abortEarly: false,
          stripUnknown: true
        });

        if (error) {
          const errors = error.details.map(detail => ({
            field: detail.path.join('.'),
            message: detail.message
          }));

          logger.debug(`Validation failed for ${schemaName}:`, errors);

          return callback?.({
            success: false,
            errors,
            message: 'Validation failed'
          });
        }

        return callback?.({ success: true, data: value });
      } catch (error) {
        logger.error(`Validation error for ${schemaName}:`, error);
        return callback?.({
          success: false,
          message: error.message
        });
      }
    };
  }

  validateSync(schemaName, data) {
    const schema = this.schemas[schemaName];
    if (!schema) {
      throw new Error(`Schema '${schemaName}' not found`);
    }

    const { error, value } = schema.validate(data, {
      abortEarly: false,
      stripUnknown: true
    });

    if (error) {
      const errors = error.details.map(detail => ({
        field: detail.path.join('.'),
        message: detail.message
      }));

      return {
        success: false,
        errors,
        message: 'Validation failed'
      };
    }

    return {
      success: true,
      data: value
    };
  }

  addSchema(name, schema) {
    this.schemas[name] = schema;
  }

  addCustomSchema(name, schemaObject) {
    this.schemas[name] = Joi.object(schemaObject);
  }
}

module.exports = new ValidationMiddleware();
