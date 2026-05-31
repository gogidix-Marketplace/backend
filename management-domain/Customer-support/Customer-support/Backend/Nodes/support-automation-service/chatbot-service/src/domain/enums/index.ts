export enum Intent {
  GREETING = 'greeting',
  GOODBYE = 'goodbye',
  FAQ = 'faq',
  SUPPORT_REQUEST = 'support_request',
  BILLING_INQUIRY = 'billing_inquiry',
  TECHNICAL_ISSUE = 'technical_issue',
  ACCOUNT_ACCESS = 'account_access',
  ORDER_STATUS = 'order_status',
  REFUND_REQUEST = 'refund_request',
  COMPLAINT = 'complaint',
  FEEDBACK = 'feedback',
  UNKNOWN = 'unknown',
}

export enum MessageType {
  USER = 'user',
  BOT = 'bot',
  SYSTEM = 'system',
}

export enum HandoffReason {
  LOW_CONFIDENCE = 'low_confidence',
  COMPLEX_QUERY = 'complex_query',
  ESCALATION_REQUEST = 'escalation_request',
  NEGATIVE_SENTIMENT = 'negative_sentiment',
  AUTHENTICATION_REQUIRED = 'authentication_required',
}
