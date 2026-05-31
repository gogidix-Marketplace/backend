# Sentiment Analysis Service

Real-time sentiment analysis for customer support tickets.

## Features

- **Real-time Analysis**: Instant sentiment scoring of ticket content
- **Emotion Detection**: Joy, sadness, anger, fear, disgust, surprise
- **Customer Mood Detection**: Positive, negative, neutral, angry, frustrated, happy
- **Sentiment Scoring**: 0-100 scale with confidence levels
- **Trend Analysis**: Track sentiment changes over time
- **Alert System**: Automatic alerts on negative sentiment spikes
- **Batch Processing**: Analyze multiple texts at once
- **Keyword Extraction**: Identify sentiment-bearing words

## Quick Start

```bash
npm install
npm run dev
```

## API Endpoints

- `POST /api/v1/sentiment/analyze` - Analyze text sentiment
- `POST /api/v1/sentiment/batch` - Batch analyze texts
- `GET /api/v1/sentiment/trend/:ticketId` - Get sentiment trend for ticket
- `GET /api/v1/alerts` - Get active alerts
- `POST /api/v1/alerts/:alertId/acknowledge` - Acknowledge alert

## Environment Variables

```env
PORT=8114
MONGODB_URI=mongodb://localhost:27017/sentiment-analysis
REDIS_HOST=localhost
REDIS_PORT=6379
NEGATIVITY_THRESHOLD=30
POSITIVITY_THRESHOLD=70
ALERT_THRESHOLD=25
```

## Docker

```bash
docker-compose up -d
```

## License

MIT
