import { Injectable, Inject, Logger } from '@nestjs/common';
import { IChatCommandPort, IChatQueryPort } from '@domain/ports/input';
import {
  IChatSessionRepository,
  IEventPublisher,
  ICachePort,
  IOpenAIPort,
  IKnowledgeBasePort,
} from '@domain/ports/output';
import { ChatSession, Message, BotResponse, ChatSessionProps, BotResponseProps } from '@domain/models';
import { SessionStatus, ResponseType } from '@domain/enums';
import { SessionCreatedEvent, SessionClosedEvent, MessageSentEvent } from '@domain/events';
import { IIntentQueryPort } from '@domain/ports/input';
import { IHandoffCommandPort } from '@domain/ports/input';
import { v4 as uuidv4 } from 'uuid';

interface ConversationContext {
  previousIntents: string[];
  entityHistory: Record<string, string>;
  topic: string | null;
  turnsWithoutResolution: number;
  lastHandoffSuggestion: number;
}

@Injectable()
export class ChatApplicationService implements IChatCommandPort, IChatQueryPort {
  private readonly logger = new Logger(ChatApplicationService.name);
  private readonly MAX_CONTEXT_HISTORY = 10;

  constructor(
    @Inject('IChatSessionRepository')
    private readonly sessionRepo: IChatSessionRepository,
    @Inject('IEventPublisher')
    private readonly eventPublisher: IEventPublisher,
    @Inject('ICachePort')
    private readonly cachePort: ICachePort,
    @Inject('IOpenAIPort')
    private readonly openaiPort: IOpenAIPort,
    @Inject('IKnowledgeBasePort')
    private readonly knowledgeBasePort: IKnowledgeBasePort,
    @Inject('IIntentQueryPort')
    private readonly intentQueryPort: IIntentQueryPort,
    @Inject('IHandoffCommandPort')
    private readonly handoffCommandPort: IHandoffCommandPort,
  ) {}

  async createSession(customerId?: string, language: string = 'en', metadata: Record<string, unknown> = {}): Promise<ChatSessionProps> {
    const sessionId = `session_${uuidv4()}`;
    const session = ChatSession.create({
      sessionId,
      customerId,
      userId: customerId,
      language,
      metadata,
      context: {
        customerId,
        locale: language,
        timezone: (metadata.timezone as string) || 'UTC',
        metadata,
        accumulatedContext: {},
        turnCount: 0,
        lastActivity: new Date(),
      },
    });

    const saved = await this.sessionRepo.save(session.toPlainObject());

    await this.cachePort.set(
      `session:${sessionId}`,
      JSON.stringify(saved),
      1800,
    );

    await this.eventPublisher.publish(new SessionCreatedEvent(sessionId, customerId, language));

    this.logger.log(`Created session ${sessionId}`);
    return saved;
  }

  async sendMessage(sessionId: string, message: string, customerId?: string, language: string = 'en', metadata: Record<string, unknown> = {}): Promise<BotResponseProps> {
    try {
      let sessionProps = await this.sessionRepo.findOne(sessionId);
      if (!sessionProps) {
        sessionProps = await this.createSession(customerId, language, metadata);
      }

      const session = new ChatSession(sessionProps.sessionId, sessionProps);

      const userMessage = Message.create({
        id: `msg_${uuidv4()}`,
        sessionId,
        content: message,
        sender: 'user',
        timestamp: new Date(),
        language,
        metadata,
      });

      session.addMessage(userMessage);

      const intentDetection = await this.intentQueryPort.detectIntent(message, language);

      session.addTag(intentDetection.intent);
      session.updateSentiment(intentDetection.sentiment.score, intentDetection.sentiment.label);

      const conversationContext = await this.getConversationContext(sessionId);

      if (intentDetection.requiresHandoff) {
        await this.scheduleHandoff(session, intentDetection);
      }

      let response: BotResponse;

      if (this.shouldUseGPT(message, intentDetection, conversationContext)) {
        response = await this.generateGPTResponse(message, session, intentDetection, conversationContext);
      } else {
        response = await this.generateRuleBasedResponse(message, session, intentDetection, conversationContext);
      }

      const botMessage = Message.create({
        id: `msg_${uuidv4()}`,
        sessionId,
        content: response.text || '',
        sender: 'bot',
        timestamp: new Date(),
        language: response.language,
        confidence: response.confidence,
      });

      session.addMessage(botMessage);
      await this.sessionRepo.save(session.toPlainObject());

      await this.cachePort.set(
        `session:${sessionId}`,
        JSON.stringify(session.toPlainObject()),
        1800,
      );

      await this.updateConversationContext(sessionId, intentDetection, conversationContext);

      await this.eventPublisher.publish(new MessageSentEvent(sessionId, 'bot', intentDetection.intent));

      return response.toPlainObject();
    } catch (error) {
      this.logger.error(`Error sending message for session ${sessionId}: ${error}`);
      return BotResponse.error(this.getErrorMessage(language), language).toPlainObject();
    }
  }

  async closeSession(sessionId: string): Promise<void> {
    const sessionProps = await this.sessionRepo.findOne(sessionId);
    if (sessionProps) {
      const session = new ChatSession(sessionProps.sessionId, sessionProps);
      session.updateStatus(SessionStatus.CLOSED);
      await this.sessionRepo.save(session.toPlainObject());
      await this.cachePort.del(`session:${sessionId}`);
      await this.cachePort.del(`context:${sessionId}`);
      await this.eventPublisher.publish(new SessionClosedEvent(sessionId, session.duration, session.messages.length));
      this.logger.log(`Closed session ${sessionId}`);
    }
  }

  async submitFeedback(sessionId: string, rating: number, comment?: string, resolved?: boolean): Promise<void> {
    const sessionProps = await this.sessionRepo.findOne(sessionId);
    if (!sessionProps) return;

    const session = new ChatSession(sessionProps.sessionId, sessionProps);
    session.setMetadata('feedback', { rating, comment, resolved });
    session.setMetadata('feedbackSubmittedAt', new Date());
    await this.sessionRepo.save(session.toPlainObject());
  }

  async getSession(sessionId: string): Promise<ChatSessionProps | null> {
    return this.sessionRepo.findOne(sessionId);
  }

  async getSessionHistory(sessionId: string): Promise<any[]> {
    const session = await this.sessionRepo.findOne(sessionId);
    return session?.messages || [];
  }

  async getActiveSessions(customerId: string): Promise<ChatSessionProps[]> {
    return this.sessionRepo.findActiveByCustomerId(customerId);
  }

  async getCustomerSessions(customerId: string, status?: string, limit: number = 20): Promise<ChatSessionProps[]> {
    return this.sessionRepo.findByCustomerId(customerId, status, limit);
  }

  private shouldUseGPT(message: string, intentDetection: any, context: ConversationContext): boolean {
    if (intentDetection.confidence < 0.5) return true;
    if (context.turnsWithoutResolution > 3) return true;
    if (intentDetection.intent === 'general') return true;
    if (message.split(' ').length > 20) return true;
    return false;
  }

  private async generateGPTResponse(message: string, session: ChatSession, intentDetection: any, context: ConversationContext): Promise<BotResponse> {
    try {
      const recentMessages = session.messages
        .slice(-this.MAX_CONTEXT_HISTORY)
        .map(msg => ({ role: msg.sender === 'user' ? 'user' : 'assistant', content: msg.content }));

      const systemPrompt = this.buildSystemPrompt(session, intentDetection);

      const result = await this.openaiPort.chatCompletion(systemPrompt, [...recentMessages, { role: 'user', content: message }]);

      return BotResponse.create({
        type: ResponseType.TEXT,
        text: result.text || this.getFallbackResponse(session.language),
        confidence: 0.8,
        intent: intentDetection.intent,
        language: session.language,
        metadata: { usage: result.usage },
      });
    } catch (error) {
      this.logger.error('GPT generation error: ' + error);
      return this.generateRuleBasedResponse(message, session, intentDetection, context);
    }
  }

  private buildSystemPrompt(session: ChatSession, intentDetection: any): string {
    return `You are a helpful customer support chatbot for Gogidix. Your role is to assist customers with their inquiries in a friendly and professional manner.

Current session context:
- Language: ${session.language}
- Customer ID: ${session.customerId || 'guest'}
- Previous intents: ${session.tags.join(', ')}
- Current intent: ${intentDetection.intent} (${intentDetection.category})
- Sentiment: ${intentDetection.sentiment.label}

Guidelines:
1. Be concise and helpful
2. Use the customer's language
3. If you don't know something, offer to connect them with a human agent
4. For negative sentiment, be extra empathetic
5. Detect and handle escalation topics (refund, complaint, legal, etc.) appropriately`;
  }

  private async generateRuleBasedResponse(message: string, session: ChatSession, intentDetection: any, context: ConversationContext): Promise<BotResponse> {
    const { intent, category, suggestedResponses, entities } = intentDetection;

    const kbCategories = ['faq', 'product_info', 'support', 'technical', 'shipping', 'returns'];
    let kbResponse = null;

    if (kbCategories.includes(category)) {
      kbResponse = await this.knowledgeBasePort.search(message, session.language);
    }

    let responseText: string;

    if (kbResponse && kbResponse.articles.length > 0) {
      const topArticle = kbResponse.articles[0];
      responseText = `${topArticle.content}\n\nWas this helpful? Let me know if you need more information or I can connect you with a human agent.`;
    } else if (suggestedResponses.length > 0) {
      responseText = suggestedResponses[Math.floor(Math.random() * suggestedResponses.length)];
    } else {
      responseText = this.getFallbackResponse(session.language);
    }

    if (entities) {
      responseText = this.personalizeResponse(responseText, entities);
    }

    const quickReplies = this.generateQuickReplies(category);

    return BotResponse.create({
      type: quickReplies.length > 0 ? ResponseType.QUICK_REPLIES : ResponseType.TEXT,
      text: responseText,
      quickReplies: quickReplies.length > 0 ? quickReplies : undefined,
      confidence: intentDetection.confidence,
      intent,
      language: session.language,
    });
  }

  private personalizeResponse(response: string, entities: any[]): string {
    let personalized = response;
    entities.forEach(entity => {
      if (entity.type === 'email') personalized = personalized.replace('{email}', entity.value);
      else if (entity.type === 'order_number') personalized = personalized.replace('{order}', entity.value);
      else if (entity.type === 'phone') personalized = personalized.replace('{phone}', entity.value);
    });
    return personalized;
  }

  private generateQuickReplies(category: string): Array<{ title: string; payload: string }> {
    const quickRepliesMap: Record<string, string[]> = {
      greeting: ['Track my order', 'Product info', 'Talk to agent'],
      order_status: ['Where is my order?', 'Track package', 'Order details'],
      product_info: ['See specifications', 'Compare products', 'Pricing info'],
      support: ['Technical help', 'Billing question', 'Account issues'],
      general: ['Contact support', 'FAQ', 'Main menu'],
    };
    const replies = quickRepliesMap[category] || quickRepliesMap.general;
    return replies.slice(0, 4).map(title => ({
      title,
      payload: title.toLowerCase().replace(/\s+/g, '_'),
    }));
  }

  private async getConversationContext(sessionId: string): Promise<ConversationContext> {
    const cached = await this.cachePort.get(`context:${sessionId}`);
    if (cached) {
      return JSON.parse(cached);
    }
    return {
      previousIntents: [],
      entityHistory: {},
      topic: null,
      turnsWithoutResolution: 0,
      lastHandoffSuggestion: 0,
    };
  }

  private async updateConversationContext(sessionId: string, intentDetection: any, context: ConversationContext): Promise<void> {
    context.previousIntents.push(intentDetection.intent);
    if (context.previousIntents.length > 10) context.previousIntents.shift();

    if (intentDetection.entities) {
      intentDetection.entities.forEach((entity: any) => {
        if (entity.resolved) context.entityHistory[entity.type] = entity.value;
      });
    }

    context.topic = intentDetection.category;

    if (context.previousIntents.length <= 1 || context.previousIntents[context.previousIntents.length - 2] !== intentDetection.intent) {
      context.turnsWithoutResolution = 0;
    } else {
      context.turnsWithoutResolution += 1;
    }

    await this.cachePort.set(`context:${sessionId}`, JSON.stringify(context), 1800);
  }

  private async scheduleHandoff(session: ChatSession, intentDetection: any): Promise<void> {
    const timeSinceLastSuggestion = Date.now() - intentDetection.lastHandoffSuggestion;
    if (timeSinceLastSuggestion < 60000) return;

    try {
      await this.handoffCommandPort.requestHandoff(session.sessionId, {
        reason: `Intent ${intentDetection.intent} requires human assistance`,
        priority: this.calculateHandoffPriority(session, intentDetection),
        requiredSkills: intentDetection.requiredSkills || [],
      });

      session.updateStatus(SessionStatus.WAITING_FOR_AGENT);
      await this.sessionRepo.save(session.toPlainObject());
    } catch (error) {
      this.logger.error('Error scheduling handoff: ' + error);
    }
  }

  private calculateHandoffPriority(session: ChatSession, intentDetection: any): 'low' | 'medium' | 'high' | 'urgent' {
    if (intentDetection.sentiment.label === 'negative' && intentDetection.sentiment.score < 30) return 'urgent';
    if (intentDetection.category === 'complaint' || intentDetection.category === 'refund') return 'high';
    if (session.context.turnCount > 5) return 'high';
    return 'medium';
  }

  private getErrorMessage(language: string): string {
    const messages: Record<string, string> = {
      en: 'Sorry, I encountered an error. Please try again or contact support.',
      es: 'Lo siento, encontré un error. Por favor, inténtelo de nuevo o contacte soporte.',
      fr: "Désolé, j'ai rencontré une erreur. Veuillez réessayer ou contacter le support.",
      de: 'Entschuldigung, es ist ein Fehler aufgetreten. Bitte versuchen Sie es erneut.',
      pt: 'Desculpe, encontrei um erro. Por favor, tente novamente ou contate o suporte.',
      zh: '抱歉，遇到错误。请重试或联系支持。',
      ja: 'エラーが発生しました。もう一度お試しください。',
      ar: 'عذراً، حدث خطأ. يرجى المحاولة مرة أخرى.',
    };
    return messages[language] || messages.en;
  }

  private getFallbackResponse(language: string): string {
    const messages: Record<string, string> = {
      en: "I'm here to help! Could you please provide more details about what you need assistance with?",
      es: '¡Estoy aquí para ayudar! ¿Podría proporcionar más detalles?',
      fr: "Je suis là pour aider ! Pourriez-vous fournir plus de détails ?",
      de: 'Ich bin hier, um zu helfen! Könnten Sie mehr Details angeben?',
      pt: 'Estou aqui para ajudar! Você poderia fornecer mais detalhes?',
      zh: '我在这里提供帮助！您能提供更多详细信息吗？',
      ja: 'お手伝いします！詳しく教えていただけますか？',
      ar: 'أنا هنا للمساعدة! هل يمكنك تقديم المزيد من التفاصيل؟',
    };
    return messages[language] || messages.en;
  }
}
