const mongoose = require('mongodb');
const { v4: uuidv4 } = require('uuid');

const EventLogSchema = new mongoose.Schema({
  eventId: {
    type: String,
    default: () => uuidv4(),
    unique: true
  },
  eventType: {
    type: String,
    required: true,
    enum: ['EXECUTIVE_CREATED', 'EXECUTIVE_UPDATED', 'KPI_UPDATED', 'STRATEGY_UPDATED', 'APPROVAL_CREATED', 'DECISION_MADE']
  },
  eventVersion: {
    type: String,
    required: true,
    default: '1.0'
  },
  aggregateId: {
    type: String,
    required: true
  },
  aggregateType: {
    type: String,
    required: true,
    enum: ['EXECUTIVE', 'KPI', 'STRATEGY', 'APPROVAL', 'DECISION']
  },
  eventData: {
    type: Object,
    required: true
  },
  timestamp: {
    type: Date,
    default: Date.now,
    index: true
  },
  processedAt: {
    type: Date,
    default: null
  },
  status: {
    type: String,
    default: 'PENDING',
    enum: ['PENDING', 'PROCESSING', 'COMPLETED', 'FAILED']
  },
  error: {
    type: String,
    default: null
  },
  retryCount: {
    type: Number,
    default: 0
  }
}, {
  timestamps: true
});

EventLogSchema.index({ eventType: 1, timestamp: -1 });
EventLogSchema.index({ aggregateId: 1, aggregateType: 1 });
EventLogSchema.index({ status: 1 });
EventLogSchema.index({ processedAt: 1 });

EventLogSchema.statics.createEvent = function (eventData) {
  return this.create({
    eventType: eventData.eventType,
    eventVersion: eventData.eventVersion || '1.0',
    aggregateId: eventData.aggregateId,
    aggregateType: eventData.aggregateType,
    eventData: eventData.payload
  });
};

EventLogSchema.statics.markAsProcessed = function (eventId) {
  return this.findOneAndUpdate(
    { eventId },
    {
      status: 'COMPLETED',
      processedAt: new Date()
    }
  );
};

EventLogSchema.statics.markAsFailed = function (eventId, error) {
  return this.findOneAndUpdate(
    { eventId },
    {
      status: 'FAILED',
      error: error,
      processedAt: new Date(),
      retryCount: { $inc: 1 }
    }
  );
};

const EventLog = mongoose.models.EventLog || mongoose.model('EventLog', EventLogSchema);

module.exports = EventLog;