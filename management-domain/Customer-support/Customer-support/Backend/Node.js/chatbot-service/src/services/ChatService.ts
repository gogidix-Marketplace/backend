/**
 * Chat Service
 * Handles conversation logic, context management, and response generation
 */

import axios from 'axios';
import OpenAI from 'openai';
import { v4 as uuidv4 } from 'uuid';
import { ChatSessionModel } from '../models/ChatSession';
import { IMessage, IBotResponse, ResponseType, IChatSession } from '../types';
import { intentDetectionService } from './IntentDetectionService';
import { knowledgeBaseService } from './KnowledgeBaseService';
import { handoffService } from './HandoffService';
import { translationService } from './TranslationService';
import { logger } from '../utils/logger';
import { redisClient } from '../utils/redis';
import config from '../config';

// Initialize OpenAI client
const openai = config.openai.apiKey
  ? new OpenAI({ apiKey: config.openai.apiKey })
  : null;

interface ConversationContext {
  previousIntents: string[];
  entityHistory: Record<string, string>;
  topic: string | null;
  turnsWithoutResolution: number;
  lastHandoffSuggestion: number;
}

export class ChatService {
  private readonly CACHE_TTL = 3600; // 1 hour
  private readonly MAX_CONTEXT_HISTORY = 10;

  /**
   * Create a new chat session
   */
  async createSession(
    customerId?: string,
    language: string = 'en',
    metadata: Record<string, unknown> = {}
  ): Promise<IChatSession> {
    try {
      const sessionId = `session_${uuidv4()}`;

      const session = new ChatSessionModel({
        sessionId,
        customerId,
        userId: customerId,
        status: 'active',
        language,
        startedAt: new Date(),
        lastActivityAt: new Date(),
        context: {
          customerId,
          locale: language,
          timezone: metadata.timezone as string || 'UTC',
          metadata,
          accumulatedContext: {},
          turnCount: 0,
          lastActivity: new Date(),
        },
        messages: [],
        tags: [],
        metadata,
      });

      await session.save();

      // Cache in Redis
      await redisClient.set(
        `session:${sessionId}`,
        JSON.stringify(session.toJSON()),
        config.session.timeoutMs / 1000
      );

      logger.info(`Created new session ${sessionId} for customer ${customerId}`);

      return session;
    } catch (error) {
      logger.error('Error creating chat session:', error);
      throw error;
    }
  }

  /**
   * Send a message and get a response
   */
  async sendMessage(
    sessionId: string,
    message: string,
    customerId?: string,
    language: string = 'en',
    metadata: Record<string, unknown> = {}
  ): Promise<IBotResponse> {
    try {
      // Get or create session
      let session = await ChatSessionModel.findOne({ sessionId });

      if (!session) {
        session = await this.createSession(customerId, language, metadata);
      }

      // Add user message to session
      const userMessage: IMessage = {
        id: `msg_${uuidv4()}`,
        sessionId,
        content: message,
        sender: 'user',
        timestamp: new Date(),
        language,
        metadata,
      };

      session.messages.push(userMessage);
      session.context.turnCount += 1;
      session.lastActivityAt = new Date();

      // Detect intent
      const intentDetection = await intentDetectionService.detectIntent(message, language);

      // Update session tags based on intent
      if (!session.tags.includes(intentDetection.intent)) {
        session.tags.push(intentDetection.intent);
      }

      // Update sentiment
      session.updateSentiment(
        intentDetection.sentiment.score,
        intentDetection.sentiment.label
      );

      // Get conversation context
      const conversationContext = await this.getConversationContext(sessionId);

      // Check if handoff is needed
      if (intentDetection.requiresHandoff) {
        await this.scheduleHandoff(session, intentDetection);
      }

      // Generate response
      let response: IBotResponse;

      if (this.shouldUseGPT(message, intentDetection, conversationContext)) {
        response = await this.generateGPTResponse(
          message,
          session,
          intentDetection,
          conversationContext
        );
      } else {
        response = await this.generateRuleBasedResponse(
          message,
          session,
          intentDetection,
          conversationContext
        );
      }

      // Add bot response to session
      const botMessage: IMessage = {
        id: `msg_${uuidv4()}`,
        sessionId,
        content: response.text || '',
        sender: 'bot',
        timestamp: new Date(),
        language: response.language,
        confidence: response.confidence,
      };

      session.messages.push(botMessage);
      await session.save();

      // Update cache
      await redisClient.set(
        `session:${sessionId}`,
        JSON.stringify(session.toJSON()),
        config.session.timeoutMs / 1000
      );

      // Update conversation context
      await this.updateConversationContext(sessionId, intentDetection, conversationContext);

      return response;
    } catch (error) {
      logger.error(`Error sending message for session ${sessionId}:`, error);
      return {
        type: ResponseType.ERROR,
        text: this.getErrorMessage(language),
        metadata: { error: error instanceof Error ? error.message : 'Unknown error' },
        confidence: 0,
        language,
      };
    }
  }

  /**
   * Determine if we should use GPT for response generation
   */
  private shouldUseGPT(
    message: string,
    intentDetection: any,
    context: ConversationContext
  ): boolean {
    // Use GPT for complex queries or low confidence
    if (intentDetection.confidence < 0.5) return true;

    // Use GPT if we've had multiple turns without resolution
    if (context.turnsWithoutResolution > 3) return true;

    // Use GPT for general or undefined intents
    if (intentDetection.intent === 'general') return true;

    // Use GPT for longer, more complex messages
    if (message.split(' ').length > 20) return true;

    return false;
  }

  /**
   * Generate response using GPT
   */
  private async generateGPTResponse(
    message: string,
    session: IChatSession,
    intentDetection: any,
    context: ConversationContext
  ): Promise<IBotResponse> {
    if (!openai) {
      return this.generateRuleBasedResponse(message, session, intentDetection, context);
    }

    try {
      // Build conversation history for context
      const recentMessages = session.messages
        .slice(-this.MAX_CONTEXT_HISTORY)
        .map(msg => ({
          role: msg.sender === 'user' ? 'user' : 'assistant',
          content: msg.content,
        }));

      // System prompt
      const systemPrompt = this.buildSystemPrompt(session, intentDetection);

      const completion = await openai.chat.completions.create({
        model: config.openai.model,
        messages: [
          { role: 'system', content: systemPrompt },
          ...recentMessages,
          { role: 'user', content: message },
        ],
        max_tokens: config.openai.maxTokens,
        temperature: config.openai.temperature,
      });

      const responseText = completion.choices[0]?.message?.content || this.getFallbackResponse(session.language);

      return {
        type: ResponseType.TEXT,
        text: responseText,
        confidence: 0.8,
        intent: intentDetection.intent,
        language: session.language,
        metadata: {
          model: config.openai.model,
          usage: completion.usage,
        },
      };
    } catch (error) {
      logger.error('GPT generation error:', error);
      return this.generateRuleBasedResponse(message, session, intentDetection, context);
    }
  }

  /**
   * Build system prompt for GPT
   */
  private buildSystemPrompt(session: IChatSession, intentDetection: any): string {
    const basePrompt = `You are a helpful customer support chatbot for Gogidix. Your role is to assist customers with their inquiries in a friendly and professional manner.

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

    return basePrompt;
  }

  /**
   * Generate rule-based response
   */
  private async generateRuleBasedResponse(
    message: string,
    session: IChatSession,
    intentDetection: any,
    context: ConversationContext
  ): Promise<IBotResponse> {
    const { intent, category, suggestedResponses, entities } = intentDetection;

    // Check if we need knowledge base search
    const needsKB = this.needsKnowledgeBase(category, intent);
    let kbResponse = null;

    if (needsKB) {
      kbResponse = await knowledgeBaseService.search(message, session.language);
    }

    // Build response
    let responseText: string;

    if (kbResponse && kbResponse.articles.length > 0) {
      responseText = this.formatKBResponse(kResponse, session.language);
    } else if (suggestedResponses.length > 0) {
      responseText = suggestedResponses[Math.floor(Math.random() * suggestedResponses.length)];
    } else {
      responseText = this.getFallbackResponse(session.language);
    }

    // Personalize with entities
    responseText = this.personalizeResponse(responseText, entities);

    // Add quick replies if appropriate
    const quickReplies = this.generateQuickReplies(intent, category, session.language);

    return {
      type: quickReplies.length > 0 ? ResponseType.QUICK_REPLIES : ResponseType.TEXT,
      text: responseText,
      quickReplies: quickReplies.length > 0 ? quickReplies : undefined,
      confidence: intentDetection.confidence,
      intent,
      language: session.language,
    };
  }

  /**
   * Check if knowledge base search is needed
   */
  private needsKnowledgeBase(category: string, intent: string): boolean {
    const kbCategories = ['faq', 'product_info', 'support', 'technical', 'shipping', 'returns'];
    return kbCategories.includes(category);
  }

  /**
   * Format knowledge base response
   */
  private formatKBResponse(kbResponse: any, language: string): string {
    const topArticle = kbResponse.articles[0];

    if (language !== 'en' && topArticle.content) {
      // Translate if needed (in real implementation)
    }

    return `${topArticle.content}

Was this helpful? Let me know if you need more information or I can connect you with a human agent.`;
  }

  /**
   * Personalize response with entities
   */
  private personalizeResponse(response: string, entities: any[]): string {
    let personalized = response;

    entities.forEach(entity => {
      if (entity.type === 'email') {
        personalized = personalized.replace('{email}', entity.value);
      } else if (entity.type === 'order_number') {
        personalized = personalized.replace('{order}', entity.value);
      } else if (entity.type === 'phone') {
        personalized = personalized.replace('{phone}', entity.value);
      }
    });

    return personalized;
  }

  /**
   * Generate quick replies
   */
  private generateQuickReplies(intent: string, category: string, language: string): any[] {
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

  /**
   * Get conversation context from cache
   */
  private async getConversationContext(sessionId: string): Promise<ConversationContext> {
    const cached = await redisClient.get(`context:${sessionId}`);

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

  /**
   * Update conversation context
   */
  private async updateConversationContext(
    sessionId: string,
    intentDetection: any,
    context: ConversationContext
  ): Promise<void> {
    context.previousIntents.push(intentDetection.intent);
    if (context.previousIntents.length > 10) {
      context.previousIntents.shift();
    }

    // Track entities
    intentDetection.entities.forEach((entity: any) => {
      if (entity.resolved) {
        context.entityHistory[entity.type] = entity.value;
      }
    });

    // Update topic based on intent category
    context.topic = intentDetection.category;

    // Reset resolution counter on new intent
    if (context.previousIntents.length <= 1 ||
        context.previousIntents[context.previousIntents.length - 2] !== intentDetection.intent) {
      context.turnsWithoutResolution = 0;
    } else {
      context.turnsWithoutResolution += 1;
    }

    await redisClient.set(
      `context:${sessionId}`,
      JSON.stringify(context),
      config.session.timeoutMs / 1000
    );
  }

  /**
   * Schedule handoff to human agent
   */
  private async scheduleHandoff(session: IChatSession, intentDetection: any): Promise<void> {
    const timeSinceLastSuggestion = Date.now() - intentDetection.lastHandoffSuggestion;

    if (timeSinceLastSuggestion < 60000) {
      return; // Don't suggest handoff too frequently
    }

    try {
      await handoffService.requestHandoff(session.sessionId, {
        reason: `Intent ${intentDetection.intent} requires human assistance`,
        priority: this.calculateHandoffPriority(session, intentDetection),
        requiredSkills: intentDetection.requiredSkills || [],
      });

      session.updateStatus('waiting_for_agent');
      await session.save();
    } catch (error) {
      logger.error('Error scheduling handoff:', error);
    }
  }

  /**
   * Calculate handoff priority
   */
  private calculateHandoffPriority(session: IChatSession, intentDetection: any): 'low' | 'medium' | 'high' | 'urgent' {
    if (intentDetection.sentiment.label === 'negative' && intentDetection.sentiment.score < 30) {
      return 'urgent';
    }

    if (intentDetection.category === 'complaint' || intentDetection.category === 'refund') {
      return 'high';
    }

    if (session.context.turnCount > config.handoff.maxAutomatedTurns) {
      return 'high';
    }

    return 'medium';
  }

  /**
   * Get error message
   */
  private getErrorMessage(language: string): string {
    const messages: Record<string, string> = {
      en: 'Sorry, I encountered an error. Please try again or contact support.',
      es: 'Lo siento, encontré un error. Por favor, inténtelo de nuevo o contacte soporte.',
      fr: 'Désolé, j\'ai rencontré une erreur. Veuillez réessayer ou contacter le support.',
      de: 'Entschuldigung, es ist ein Fehler aufgetreten. Bitte versuchen Sie es erneut oder kontaktieren Sie den Support.',
      pt: 'Desculpe, encontrei um erro. Por favor, tente novamente ou contate o suporte.',
      zh: '抱歉，遇到错误。请重试或联系支持。',
      ja: 'エラーが発生しました。もう一度お試しいただくか、サポートにお問い合わせください。',
      ar: 'عذراً، حدث خطأ. يرجى المحاولة مرة أخرى أو الاتصال بالدعم.',
    };

    return messages[language] || messages.en;
  }

  /**
   * Get fallback response
   */
  private getFallbackResponse(language: string): string {
    const messages: Record<string, string> = {
      en: "I'm here to help! Could you please provide more details about what you need assistance with?",
      es: '¡Estoy aquí para ayudar! ¿Podría proporcionar más detalles sobre qué necesita asistencia?',
      fr: 'Je suis là pour aider ! Pourriez-vous fournir plus de détails sur ce dont vous avez besoin ?',
      de: 'Ich bin hier, um zu helfen! Könnten Sie mehr Details dazu angeben, was Sie benötigen?',
      pt: 'Estou aqui para ajudar! Você poderia fornecer mais detalhes sobre o que precisa de assistência?',
      zh: '我在这里提供帮助！您能提供更多关于需要什么帮助的详细信息吗？',
      ja: 'お手伝いします！どのようなサポートが必要ですか？詳しく教えていただけますか？',
      ar: 'أنا هنا للمساعدة! هل يمكنك تقديم المزيد من التفاصيل حول ما تحتاج إلى مساعدة فيه؟',
    };

    return messages[language] || messages.en;
  }

  /**
   * Get session by ID
   */
  async getSession(sessionId: string): Promise<IChatSession | null> {
    return ChatSessionModel.findOne({ sessionId });
  }

  /**
   * Update session status
   */
  async updateSessionStatus(
    sessionId: string,
    status: 'active' | 'waiting_for_agent' | 'with_agent' | 'closed' | 'timeout'
  ): Promise<void> {
    await ChatSessionModel.findOneAndUpdate({ sessionId }, { status });
    await redisClient.del(`session:${sessionId}`);
  }

  /**
   * Close session
   */
  async closeSession(sessionId: string): Promise<void> {
    const session = await ChatSessionModel.findOne({ sessionId });
    if (session) {
      session.updateStatus('closed');
      await session.save();
      await redisClient.del(`session:${sessionId}`);
      await redisClient.del(`context:${sessionId}`);
      logger.info(`Closed session ${sessionId}`);
    }
  }

  /**
   * Get active sessions for a customer
   */
  async getActiveSessions(customerId: string): Promise<IChatSession[]> {
    return ChatSessionModel.findActiveByCustomerId(customerId);
  }

  /**
   * Get session history
   */
  async getSessionHistory(sessionId: string): Promise<IMessage[]> {
    const session = await ChatSessionModel.findOne({ sessionId });
    return session?.messages || [];
  }
}

export const chatService = new ChatService();
