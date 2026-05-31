export class ChatbotException extends Error {
  constructor(
    message: string,
    public readonly code: string,
    public readonly statusCode: number = 500,
    public readonly details?: unknown,
  ) {
    super(message);
    this.name = 'ChatbotException';
  }
}

export class IntentDetectionException extends ChatbotException {
  constructor(message: string, details?: unknown) {
    super(message, 'INTENT_DETECTION_ERROR', 500, details);
    this.name = 'IntentDetectionException';
  }
}

export class SessionNotFoundException extends ChatbotException {
  constructor(sessionId: string) {
    super(`Session ${sessionId} not found`, 'SESSION_NOT_FOUND', 404);
    this.name = 'SessionNotFoundException';
  }
}

export class HandoffException extends ChatbotException {
  constructor(message: string, details?: unknown) {
    super(message, 'HANDOFF_ERROR', 500, details);
    this.name = 'HandoffException';
  }
}

export class TranslationException extends ChatbotException {
  constructor(message: string, details?: unknown) {
    super(message, 'TRANSLATION_ERROR', 500, details);
    this.name = 'TranslationException';
  }
}

export class IntentNotFoundException extends ChatbotException {
  constructor(intentId: string) {
    super(`Intent ${intentId} not found`, 'INTENT_NOT_FOUND', 404);
    this.name = 'IntentNotFoundException';
  }
}
