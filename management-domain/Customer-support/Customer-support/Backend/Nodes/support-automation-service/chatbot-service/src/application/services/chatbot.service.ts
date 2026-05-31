import { Injectable, Logger } from '@nestjs/common';
import { v4 as uuidv4 } from 'uuid';
import natural from 'natural';
import { ChatbotInputPort, IntentRecognitionInputPort, IntentRecognitionResult } from '@domain/ports/input';
import { ContextRepository, KnowledgeBaseRepository, EventPublisher } from '@domain/ports/output';
import { ConversationContext, Message, BotResponse } from '@domain/models';
import { Intent, MessageType, HandoffReason } from '@domain/enums';
import { ConversationStartedEvent, MessageProcessedEvent, HandoffRequestedEvent } from '@domain/events';

const trainingData: Record<string, { utterances: string[]; responses: string[] }> = {
  [Intent.GREETING]: { utterances: ['hello', 'hi', 'hey', 'good morning', 'greetings'], responses: ['Hello! How can I help you today?', 'Hi there! What can I assist you with?'] },
  [Intent.GOODBYE]: { utterances: ['bye', 'goodbye', 'see you', 'farewell', 'end chat'], responses: ['Goodbye! Feel free to come back if you need any help.', 'Take care!'] },
  [Intent.FAQ]: { utterances: ['what are your hours', 'when are you open', 'business hours', 'contact info'], responses: ['I can help with general questions. What would you like to know?'] },
  [Intent.SUPPORT_REQUEST]: { utterances: ['i need help', 'help me', 'i have a problem', 'not working', 'broken'], responses: ["I'm here to help. Could you please describe the issue you're experiencing?"] },
  [Intent.BILLING_INQUIRY]: { utterances: ['billing question', 'about my bill', 'invoice', 'charge'], responses: ['I can help with billing questions. Could you please specify?'] },
  [Intent.TECHNICAL_ISSUE]: { utterances: ["can't login", 'forgot password', 'page not loading', 'error'], responses: ["I understand you're experiencing a technical issue. Let me help troubleshoot."] },
  [Intent.ACCOUNT_ACCESS]: { utterances: ['access my account', 'sign in', 'locked out'], responses: ['For account-related requests, I need to verify your identity first.'] },
  [Intent.ORDER_STATUS]: { utterances: ['where is my order', 'order status', 'track order', 'delivery'], responses: ['I can help check your order status. Please provide your order number.'] },
  [Intent.REFUND_REQUEST]: { utterances: ['i want a refund', 'request refund', 'get my money back'], responses: ['I understand you want a refund. Let me help you with that.'] },
  [Intent.COMPLAINT]: { utterances: ['i want to complain', 'terrible service', 'worst service'], responses: ["I'm truly sorry to hear about your experience."] },
  [Intent.FEEDBACK]: { utterances: ['i have feedback', 'share my experience', 'suggestion'], responses: ['Thank you for your feedback!'] },
  [Intent.UNKNOWN]: { utterances: [], responses: ["I'm not sure I understood. Could you please rephrase?", "Let me connect you with a human agent."] },
};

const entityPatterns = {
  orderNumber: /\b(?:order\s*#?\s*)?[A-Z0-9]{2,10}[-_]?\d{4,10}\b/gi,
  email: /\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Z|a-z]{2,}\b/g,
};

@Injectable()
export class ChatbotService implements ChatbotInputPort {
  private readonly logger = new Logger(ChatbotService.name);
  private readonly handoffThreshold = parseFloat(process.env.HANDOFF_THRESHOLD || '0.3');
  private readonly classifier: natural.LogisticRegressionClassifier;
  private readonly tokenizer = new natural.WordTokenizer();
  private readonly confidenceThreshold = parseFloat(process.env.CONFIDENCE_THRESHOLD || '0.7');

  constructor(
    private readonly contextRepository: ContextRepository,
    private readonly knowledgeBase: KnowledgeBaseRepository,
    private readonly eventPublisher: EventPublisher,
  ) {
    this.classifier = new natural.LogisticRegressionClassifier();
    this.trainClassifier();
  }

  async startConversation(customerId?: string, language = 'en'): Promise<{ sessionId: string; message: string }> {
    const sessionId = uuidv4();
    const context = ConversationContext.create({
      sessionId, customerId, language,
      messages: [], currentIntent: Intent.UNKNOWN,
      intentHistory: [], entities: {}, handoffRequested: false,
      startedAt: new Date(), lastActivityAt: new Date(),
    });
    await this.contextRepository.save(context);

    const greeting = this.getResponseForIntent(Intent.GREETING);
    await this.eventPublisher.publish(new ConversationStartedEvent({ eventId: uuidv4(), sessionId, customerId }));

    return { sessionId, message: greeting };
  }

  async processMessage(sessionId: string, message: string, customerId?: string, language = 'en'): Promise<BotResponse> {
    let context = await this.contextRepository.findBySessionId(sessionId);
    if (!context) {
      const result = await this.startConversation(customerId, language);
      context = await this.contextRepository.findBySessionId(result.sessionId);
    }

    const userMsg = Message.create({ id: uuidv4(), sessionId, type: MessageType.USER, content: message, timestamp: new Date() });
    context = context!.addMessage(userMsg.toPlainObject());

    const intentResult = this.recognizeIntent(message);
    const isEscalation = this.isEscalation(message);

    if (Object.keys(intentResult.entities).length > 0) {
      context = context!.updateEntities(intentResult.entities);
    }

    const handoff = this.shouldHandoff(intentResult.intent, intentResult.confidence, isEscalation);

    if (handoff && handoff.recommended) {
      context = context!.requestHandoff();
      await this.contextRepository.save(context!);
      await this.eventPublisher.publish(new HandoffRequestedEvent({
        eventId: uuidv4(), sessionId, reason: handoff.reason!,
      }));

      return BotResponse.create({
        message: this.getHandoffMessage(handoff.reason!),
        intent: intentResult.intent, confidence: intentResult.confidence, handoff,
      });
    }

    let botMessage = this.getResponseForIntent(intentResult.intent);
    if ([Intent.FAQ, Intent.TECHNICAL_ISSUE, Intent.BILLING_INQUIRY].includes(intentResult.intent)) {
      const kbResponse = await this.knowledgeBase.search(message, context!.language);
      if (kbResponse) botMessage = kbResponse;
    }

    const botMsg = Message.create({
      id: uuidv4(), sessionId, type: MessageType.BOT,
      content: botMessage, timestamp: new Date(),
      intent: intentResult.intent, confidence: intentResult.confidence,
    });
    context = context!.addMessage(botMsg.toPlainObject());
    await this.contextRepository.save(context!);

    await this.eventPublisher.publish(new MessageProcessedEvent({
      eventId: uuidv4(), sessionId,
      intent: intentResult.intent, confidence: intentResult.confidence,
    }));

    return BotResponse.create({
      message: botMessage, intent: intentResult.intent,
      confidence: intentResult.confidence,
      suggestedResponses: this.getSuggestedResponses(intentResult.intent),
    });
  }

  async endConversation(sessionId: string): Promise<{ success: boolean; summary?: any }> {
    const context = await this.contextRepository.findBySessionId(sessionId);
    if (!context) return { success: false };
    const summary = {
      sessionId, messageCount: context.messages.length,
      primaryIntent: context.currentIntent, entities: context.entities,
    };
    await this.contextRepository.delete(sessionId);
    return { success: true, summary };
  }

  async getConversationHistory(sessionId: string): Promise<any[]> {
    const context = await this.contextRepository.findBySessionId(sessionId);
    return context ? context.messages : [];
  }

  async getStats(): Promise<Record<string, unknown>> {
    return { activeConversations: 0, totalMessages: 0 };
  }

  recognizeIntent(message: string): IntentRecognitionResult {
    const tokens = this.tokenize(message);
    if (tokens.length === 0) return { intent: Intent.UNKNOWN, confidence: 0, entities: {}, alternativeIntents: [] };
    const classifications = this.classifier.getClassifications(tokens);
    if (classifications.length === 0) return { intent: Intent.UNKNOWN, confidence: 0, entities: {}, alternativeIntents: [] };
    const top = classifications[0];
    const entities = this.extractEntities(message);
    const intent = top.value >= this.confidenceThreshold ? top.label as Intent : Intent.UNKNOWN;
    return {
      intent, confidence: top.value, entities,
      alternativeIntents: classifications.slice(1, 4).filter(c => c.value > 0.1).map(c => ({ intent: c.label as Intent, confidence: c.value })),
    };
  }

  getResponseForIntent(intent: Intent): string {
    const data = trainingData[intent];
    if (!data || data.responses.length === 0) return trainingData[Intent.UNKNOWN].responses[0];
    return data.responses[Math.floor(Math.random() * data.responses.length)];
  }

  isEscalation(message: string): boolean {
    const keywords = ['manager', 'supervisor', 'escalate', 'speak to human', 'real person', 'agent', 'terrible', 'horrible', 'worst', 'angry', 'furious'];
    return keywords.some(kw => message.toLowerCase().includes(kw));
  }

  private trainClassifier(): void {
    for (const [intent, data] of Object.entries(trainingData)) {
      if (intent === Intent.UNKNOWN) continue;
      for (const utterance of data.utterances) {
        this.classifier.addDocument(this.tokenize(utterance), intent);
      }
    }
    this.classifier.train();
  }

  private tokenize(text: string): string[] {
    const tokens = this.tokenizer.tokenize(text.toLowerCase()) || [];
    const stopWords = new Set(['a', 'an', 'the', 'is', 'are', 'was', 'i', 'you', 'me', 'my', 'your', 'this', 'that', 'please', 'just']);
    return tokens.filter(t => !stopWords.has(t)).map(t => natural.PorterStemmer.stem(t));
  }

  private extractEntities(message: string): Record<string, any> {
    const entities: Record<string, any> = {};
    const orderMatch = message.match(entityPatterns.orderNumber);
    if (orderMatch) entities.orderNumber = orderMatch[0];
    const emailMatch = message.match(entityPatterns.email);
    if (emailMatch) entities.email = emailMatch[0];
    return entities;
  }

  private shouldHandoff(intent: Intent, confidence: number, isEscalation: boolean): BotResponse['handoff'] extends infer R ? R : any {
    if (isEscalation) return { recommended: true, reason: HandoffReason.ESCALATION_REQUEST };
    if (intent === Intent.UNKNOWN || confidence < this.handoffThreshold) return { recommended: true, reason: HandoffReason.LOW_CONFIDENCE };
    if ([Intent.COMPLAINT, Intent.REFUND_REQUEST].includes(intent)) return { recommended: true, reason: HandoffReason.COMPLEX_QUERY };
    if (intent === Intent.ACCOUNT_ACCESS) return { recommended: true, reason: HandoffReason.AUTHENTICATION_REQUIRED };
    return { recommended: false };
  }

  private getHandoffMessage(reason: HandoffReason): string {
    const messages: Record<HandoffReason, string> = {
      [HandoffReason.LOW_CONFIDENCE]: "I'm not sure I understood correctly. Let me connect you with a human agent.",
      [HandoffReason.COMPLEX_QUERY]: 'This requires detailed assistance. Let me connect you with a specialist.',
      [HandoffReason.ESCALATION_REQUEST]: "I'll connect you with a human agent.",
      [HandoffReason.AUTHENTICATION_REQUIRED]: 'For security purposes, I need to transfer you to a human agent.',
      [HandoffReason.NEGATIVE_SENTIMENT]: "I'll connect you with a human agent.",
    };
    return messages[reason] || "I'll connect you with a human agent.";
  }

  private getSuggestedResponses(intent: Intent): string[] {
    const suggestions: Partial<Record<Intent, string[]>> = {
      [Intent.GREETING]: ['I need help with my account', 'Check my order status', 'Billing question'],
      [Intent.FAQ]: ['Business hours?', 'Contact info?', 'Return policy?'],
      [Intent.SUPPORT_REQUEST]: ['Technical issue', 'Billing problem', 'Account access'],
      [Intent.UNKNOWN]: ['Speak to an agent', 'Common questions', 'Start over'],
    };
    return suggestions[intent] || [];
  }
}
