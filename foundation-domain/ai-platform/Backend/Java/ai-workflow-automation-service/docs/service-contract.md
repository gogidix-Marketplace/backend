# AI Workflow Automation Service - Service Contract

## Service Responsibility
Automates repetitive workflows and business processes.

## Core Functionality
- Workflow templates
- Automation triggers
- Process scheduling
- Workflow monitoring

## API Contracts

### 1. Create Automation
**Endpoint:** `POST /api/v1/automation/workflows`

**Input:**
```json
{
  "name": "string",
  "trigger": {"type": "SCHEDULE|EVENT|WEBHOOK"},
  "actions": [{"service": "string", "operation": "string"}],
  "schedule": "cron-expression"
}
```

**Output:**
```json
{
  "automationId": "string (UUID)",
  "status": "ACTIVE|PAUSED",
  "nextRun": "datetime"
}
```

### 2. Trigger Automation
**Endpoint:** `POST /api/v1/automation/workflows/{automationId}/trigger`

## Business Rules
- Max actions per workflow: 20
- Min schedule interval: 1 minute
- Max execution time: 30 minutes

## Error Conditions
- Invalid cron expression (400)
- Automation not found (404)
