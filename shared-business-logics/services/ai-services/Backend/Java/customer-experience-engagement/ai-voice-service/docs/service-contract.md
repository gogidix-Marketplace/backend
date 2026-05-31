# AI Voice Service - Service Contract

## Service Responsibility
Voice synthesis and text-to-speech generation.

## Core Functionality
- Text to speech
- Voice cloning
- Voice customization
- Audio generation

## API Contracts

### 1. Synthesize Speech
**Endpoint:** `POST /api/v1/voice/synthesize`

**Input:**
```json
{
  "text": "string",
  "voiceId": "string (preset voice)",
  "format": "MP3|WAV|OGG",
  "speed": "float (0.5-2.0)",
  "pitch": "float (-10 to +10)"
}
```

**Output:**
```json
{
  "synthesisId": "string (UUID)",
  "audioUrl": "string",
  "duration": "float (seconds)",
  "sampleRate": "integer (Hz)"
}
```

### 2. Get Available Voices
**Endpoint:** `GET /api/v1/voice/voices`

## Business Rules
- Max text length: 5000 characters
- Max audio duration: 10 minutes
- Audio retention: 24 hours

## Error Conditions
- Text too long (400)
- Voice not available (404)
