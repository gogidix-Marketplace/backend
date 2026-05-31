require('dotenv').config();

const app = require('./app');
const logger = require('./config/logger');

async function startServer() {
  try {
    await app.initialize();
    await app.listen();

    logger.info('WebSocket service started successfully');
  } catch (error) {
    logger.error('Failed to start WebSocket service:', error);
    process.exit(1);
  }
}

if (require.main === module) {
  startServer();
}

module.exports = app;
