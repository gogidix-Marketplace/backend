# Ticket Routing Service

An intelligent ticket routing service for customer support that distributes incoming tickets to available agents using multiple routing strategies.

## Features

- **Multiple Routing Strategies**:
  - Round-robin: Distributes tickets evenly among agents
  - Least-busy: Routes to agents with the lowest current load
  - Skills-based: Matches tickets to agents based on required skills
  - Priority-based: Considers both priority and availability

- **Agent Management**:
  - Create, update, and delete support agents
  - Track agent status (available, busy, offline, away)
  - Skill-based agent profiles with certification levels
  - Agent availability tracking with heartbeat monitoring

- **Queue Management**:
  - Automatic queuing when no agents are available
  - Priority-based queue ordering
  - Queue position tracking
  - Estimated wait time calculation

- **Reassignment Capabilities**:
  - Manual ticket reassignment between agents
  - Automatic reassignment based on timeouts
  - Reassignment history tracking

## Installation

```bash
npm install
```

## Configuration

Create a `.env` file based on `.env.example`:

```bash
cp .env.example .env
```

## Running the Service

### Development

```bash
npm run dev
```

### Production

```bash
npm run build
npm start
```

### Docker

```bash
docker build -t ticket-routing-service .
docker run -p 3001:3001 ticket-routing-service
```

## API Endpoints

### Health Check
- `GET /health` - Service health status

### Agents
- `POST /api/agents` - Create a new agent
- `GET /api/agents` - Get all agents (filter by status or department)
- `GET /api/agents/available` - Get available agents
- `GET /api/agents/:id` - Get agent by ID
- `PUT /api/agents/:id` - Update agent
- `PATCH /api/agents/:id/status` - Update agent status
- `DELETE /api/agents/:id` - Delete agent
- `GET /api/agents/:id/stats` - Get agent statistics
- `GET /api/agents/:id/load` - Get agent current load
- `POST /api/agents/:id/activity` - Record agent heartbeat

### Tickets
- `POST /api/tickets` - Create a new ticket
- `GET /api/tickets/:id` - Get ticket by ID
- `POST /api/tickets/:id/route` - Route ticket to an agent
- `POST /api/tickets/:id/reassign` - Reassign ticket to different agent
- `GET /api/tickets/:id/history` - Get routing history
- `PATCH /api/tickets/:id/status` - Update ticket status

### Queue
- `GET /api/queue` - Get all tickets in queue
- `GET /api/queue/length` - Get queue length
- `GET /api/queue/stats` - Get queue statistics
- `GET /api/queue/position` - Get ticket position in queue
- `POST /api/queue/process` - Process queue manually
- `DELETE /api/queue` - Clear queue
- `POST /api/queue/reprioritize` - Reprioritize ticket in queue

### Routing
- `GET /api/routing/stats` - Get routing statistics
- `POST /api/routing/reassign` - Reassign ticket

## Example Usage

### Create an Agent

```bash
curl -X POST http://localhost:3001/api/agents \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "status": "available",
    "skills": [
      {"name": "technical_support", "level": 4},
      {"name": "billing", "level": 3}
    ],
    "maxConcurrentTickets": 10,
    "departments": ["technical", "billing"]
  }'
```

### Create and Route a Ticket

```bash
curl -X POST http://localhost:3001/api/tickets \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": "cust_123",
    "subject": "Unable to access account",
    "description": "I am unable to log in to my account",
    "priority": "high",
    "department": "technical",
    "requiredSkills": ["technical_support"]
  }'

curl -X POST http://localhost:3001/api/tickets/{ticketId}/route \
  -H "Content-Type: application/json" \
  -d '{"strategy": "skills_based"}'
```

## Technologies

- Node.js 18 LTS
- Express.js
- TypeScript
- Redis (for data persistence and queue management)
- Winston (logging)
- Joi (validation)
