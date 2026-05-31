export class RoutingException extends Error { constructor(message: string, public code: string, public statusCode = 500) { super(message); } }
