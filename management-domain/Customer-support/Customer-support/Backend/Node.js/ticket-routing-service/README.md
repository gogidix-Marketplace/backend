# Ticket Routing Service

Intelligent ticket assignment and routing service with skills-based matching and load balancing.

## Features

- **Multiple Routing Strategies**: Round-robin, least-busy, skills-based, weighted, priority-based
- **Skills-Based Matching**: Match tickets to agents with the right expertise
- **Load Balancing**: Distribute workload evenly across agents
- **Priority Queuing**: Support for urgent and high-priority tickets
- **Escalation Rules**: Automatic escalation based on configurable rules
- **Real-time Queue Management**: Track queue positions and wait times
- **Agent Utilization Tracking**: Monitor agent workload and performance

## Quick Start

```bash
npm install
npm run dev
```

## API Endpoints

- `POST /api/v1/routing/route` - Route ticket to agent
- `GET /api/v1/routing/decision/:ticketId` - Get routing decision
- `GET /api/v1/agents` - List all agents
- `GET /api/v1/queues` - List all queues
- `GET /api/v1/analytics` - Get routing analytics

## Environment Variables

```env
PORT=8113
MONGODB_URI=mongodb://localhost:27017/ticket-routing
REDIS_HOST=localhost
REDIS_PORT=6379
LOAD_BALANCING_STRATEGY=least-busy
SKILL_MATCH_THRESHOLD=0.6
MAX_QUEUE_SIZE=1000
```

## Docker

```bash
docker-compose up -d
```

## License

MIT
