# Kafka Consumer Service - Architecture Documentation

## Overview
The Kafka Consumer Service processes domain events and updates read models for the query side.

## Technology Stack
- **Runtime**: Node.js 18+
- **Framework**: Express.js
- **Messaging**: Kafka (node-rdkafka)
- **Database**: MongoDB

## Architecture

```
Kafka Topics --> Consumer Groups --> Event Handlers --> Read Model Updates
```

## Key Features
- Event consumption from multiple topics
- Parallel processing with consumer groups
- Error handling and retry logic
- Dead letter queue handling
