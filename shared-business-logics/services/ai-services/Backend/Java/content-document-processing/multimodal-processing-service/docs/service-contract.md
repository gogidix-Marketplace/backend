# Multimodal Processing Service - Service Contract

## Service Responsibility
Processes multimodal content (text, image, audio, video).

## Core Functionality
- Multimodal embedding generation
- Cross-modal search
- Content understanding
- Feature fusion

## API Contracts

### 1. Process Multimodal Content
**Endpoint:** `POST /api/v1/multimodal/process`

**Input:**
```json
{
  "contentItems": [{
    "type": "TEXT|IMAGE|AUDIO|VIDEO",
    "url": "string",
    "metadata": {}
  }],
  "outputFormat": "EMBEDDING|SUMMARY|TAGS"
}
```

**Output:**
```json
{
  "processingId": "string (UUID)",
  "embeddings": [{"modality": "string", "vector": "number[]"}],
  "fusedEmbedding": "number[]",
  "summary": "string",
  "tags": ["string"]
}
```

### 2. Search Similar Content
**Endpoint:** `POST /api/v1/multimodal/search`

## Business Rules
- Max content items: 10
- Embedding dimension: 768
- Max video length: 10 minutes

## Error Conditions
- Invalid content URL (400)
