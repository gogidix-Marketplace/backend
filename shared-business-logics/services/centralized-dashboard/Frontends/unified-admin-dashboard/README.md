# Unified Admin Dashboard

Frontend application for the Centralized Dashboard.

## Tech Stack

- React 18
- TypeScript
- Vite
- Tailwind CSS
- React Query
- Recharts
- Zustand

## Getting Started

### Installation

```bash
npm install
```

### Development

```bash
npm run dev
```

The application will be available at http://localhost:3000

### Build

```bash
npm run build
```

### Preview

```bash
npm run preview
```

## Features

- **Dashboard**: Overview of system status and metrics
- **Charts**: Visualize time-series data
- **Real-time**: WebSocket feed for live updates
- **Settings**: Configure dashboard preferences

## Configuration

The API endpoints are configured via Vite proxy:

- `/api/v1/gateway` -> API Gateway (8907)
- `/api/v1/charts` -> Chart Service (8909)
- `/api/v1/websocket` -> WebSocket Gateway (8908)
- `/ws` -> WebSocket connection (8908)

## Environment Variables

Create a `.env` file in the root directory:

```env
VITE_API_GATEWAY_URL=http://localhost:8907
VITE_CHART_SERVICE_URL=http://localhost:8909
VITE_WEBSOCKET_URL=ws://localhost:8908
```
