# Voice Recognition Service - Service Contract

## Service Responsibility
Speech-to-text and voice command processing.

## Core Functionality
- Speech to text
- Speaker identification
- Voice commands
- Transcription services

## API Contracts

### 1. Transcribe Audio
**Endpoint:** `POST /api/v1/voice/transcribe`

**Input:**
```json
{
  "audioUrl": "string",
  "language": "AUTO|EN|ES|FR|DE|ZH",
  "enableSpeakerDiarization": "boolean",
  "enablePunctuation": "boolean"
}
```

**Output:**
```json
{
  "transcriptionId": "string (UUID)",
  "transcript": "string",
  "confidence": "float (0-1)",
  "speakers": [{"id": "string", "segments": []}],
  "duration": "float (seconds)"
}
```

### 2. Process Voice Command
**Endpoint:** `POST /api/v1/voice/command`

## Business Rules
- Max audio duration: 30 minutes
- Supported formats: MP3, WAV, FLAC
- Min confidence threshold: 0.7

## Error Conditions
- Audio not found (404)
- Unsupported format (400)
