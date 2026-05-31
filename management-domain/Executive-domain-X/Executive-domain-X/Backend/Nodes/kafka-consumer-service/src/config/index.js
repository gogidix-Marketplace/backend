// Central configuration export
const logger = require('./logger');
const kafka = require('./kafka');
const mongodb = require('./mongodb');
const redis = require('./redis');
const metrics = require('./metrics');

module.exports = {
  logger,
  kafka,
  mongodb,
  redis,
  metrics
};
