# Sentiment Analysis Service

Real-time sentiment analysis and emotion detection service for customer support messages.

## Features

- **Sentiment Analysis**: Detects positive, negative, and neutral sentiment
- **Emotion Detection**: Identifies 7 emotions:
  - Joy
  - Sadness
  - Anger
  - Fear
  - Disgust
  - Surprise
  - Neutral
- **Multi-language Support**: Analyzes sentiment in multiple languages
- **Batch Analysis**: Process multiple texts efficiently
- **Confidence Scoring**: Provides confidence levels for predictions
- **Keyword Extraction**: Identifies sentiment-bearing words
- **Result Caching**: Redis-based caching for performance

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
docker build -t sentiment-analysis-service .
docker run -p 3003:3003 sentiment-analysis-service
```

## API Endpoints

### Sentiment Analysis
- `POST /api/sentiment/analyze` - Full analysis (sentiment + emotions)
- `POST /api/sentiment/sentiment-only` - Sentiment analysis only
- `POST /api/sentiment/emotions-only` - Emotion detection only
- `POST /api/sentiment/batch` - Batch analyze multiple texts

### Management
- `GET /api/sentiment/stats` - Get service statistics
- `DELETE /api/sentiment/cache` - Clear analysis cache
- `GET /health` - Health check

## Example Usage

### Analyze Text

```bash
curl -X POST http://localhost:3003/api/sentiment/analyze \
  -H "Content-Type: application/json" \
  -d '{
    "text": "I am absolutely thrilled with the excellent service!",
    "includeEmotions": true
  }'
```

**Response:**
```json
{
  "success": true,
  "data": {
    "text": "I am absolutely thrilled with the excellent service!",
    "sentiment": "positive",
    "score": 7,
    "confidence": 0.93,
    "keywords": ["thrilled", "excellent", "service"],
    "emotions": {
      "primaryEmotion": "joy",
      "emotions": {
        "joy": 0.67,
        "surprise": 0.22,
        "neutral": 0.11
      },
      "confidence": 0.67
    }
  }
}
```

### Batch Analysis

```bash
curl -X POST http://localhost:3003/api/sentiment/batch \
  -H "Content-Type: application/json" \
  -d '{
    "texts": [
      "Great service!",
      "Very disappointed",
      "It was okay"
    ],
    "includeEmotions": false
  }'
```

### Sentiment Only

```bash
curl -X POST http://localhost:3003/api/sentiment/sentiment-only \
  -H "Content-Type: application/json" \
  -d '{
    "text": "This product is terrible and I hate it"
  }'
```

**Response:**
```json
{
  "success": true,
  "data": {
    "text": "This product is terrible and I hate it",
    "sentiment": "negative",
    "score": -8,
    "confidence": 0.89,
    "keywords": ["terrible", "hate"]
  }
}
```

## Sentiment Scoring

- **Positive**: Score > 0
- **Negative**: Score < 0
- **Neutral**: Score = 0

Score magnitude indicates intensity:
- **1-2**: Mild sentiment
- **3-5**: Moderate sentiment
- **6+**: Strong sentiment

## Technologies

- Node.js 18 LTS
- Express.js
- TypeScript
- Sentiment (AFINN-based library)
- Natural (NLP toolkit)
- Redis (caching)
- Winston (logging)
