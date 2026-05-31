export class SentimentException extends Error { constructor(message: string, public code: string, public statusCode = 500) { super(message); } }
