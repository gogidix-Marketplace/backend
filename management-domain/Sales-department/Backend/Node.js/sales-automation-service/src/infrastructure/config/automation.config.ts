import { registerAs } from '@nestjs/config';

export const automationConfig = registerAs('automation', () => ({
  leadScoring: {
    enabled: process.env.LEAD_SCORING_ENABLED === 'true',
    defaultThreshold: parseInt(process.env.DEFAULT_LEAD_SCORE_THRESHOLD, 10) || 50,
  },
  workflow: {
    maxExecutionTime: parseInt(process.env.MAX_WORKFLOW_EXECUTION_TIME, 10) || 300000,
    maxConcurrentWorkflows: parseInt(process.env.MAX_CONCURRENT_WORKFLOWS, 10) || 10,
    retryAttempts: parseInt(process.env.WORKFLOW_RETRY_ATTEMPTS, 10) || 3,
    retryDelay: parseInt(process.env.WORKFLOW_RETRY_DELAY, 10) || 5000,
  },
  email: {
    enabled: process.env.EMAIL_SERVICE_ENABLED === 'true',
    from: process.env.EMAIL_FROM || 'noreply@gogidix.com',
    smtp: {
      host: process.env.SMTP_HOST || 'smtp.gmail.com',
      port: parseInt(process.env.SMTP_PORT, 10) || 587,
      user: process.env.SMTP_USER,
      password: process.env.SMTP_PASSWORD,
    },
  },
  scheduling: {
    enabled: true,
    timezone: process.env.DEFAULT_TIMEZONE || 'UTC',
  },
}));
