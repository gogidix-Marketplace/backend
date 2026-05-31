import { Injectable, Inject, Logger, OnModuleInit } from '@nestjs/common';
import { IIntentCommandPort, IIntentQueryPort } from '@domain/ports/input';
import { IIntentRepository } from '@domain/ports/output';
import { IntentDetectionProps, IntentProps } from '@domain/models';
import { IntentCategory, IntentConfidence } from '@domain/enums';
import { IntentCreatedEvent } from '@domain/events';
import { IEventPublisher } from '@domain/ports/output';
import natural from 'natural';
import stopword from 'stopword';

const tokenizer = new natural.WordTokenizer();
const stemmer = natural.PorterStemmer;
const classifier = new natural.BayesClassifier();

@Injectable()
export class IntentApplicationService implements IIntentCommandPort, IIntentQueryPort, OnModuleInit {
  private readonly logger = new Logger(IntentApplicationService.name);
  private intents: Map<string, IntentProps> = new Map();
  private initialized = false;

  constructor(
    @Inject('IIntentRepository')
    private readonly intentRepo: IIntentRepository,
    @Inject('IEventPublisher')
    private readonly eventPublisher: IEventPublisher,
  ) {}

  async onModuleInit() {
    await this.initialize();
  }

  private async initialize(): Promise<void> {
    try {
      await this.loadIntents();
      await this.trainClassifier();
      this.initialized = true;
      this.logger.log('Intent Detection Service initialized');
    } catch (error) {
      this.logger.error('Failed to initialize Intent Detection Service: ' + error);
    }
  }

  private async loadIntents(): Promise<void> {
    const intents = await this.intentRepo.find({ isActive: true });
    this.intents.clear();
    for (const intent of intents) {
      this.intents.set(intent.name, intent);
    }
    this.logger.log(`Loaded ${this.intents.size} intents`);
  }

  private async trainClassifier(): Promise<void> {
    for (const [name, intent] of this.intents) {
      for (const phrase of intent.trainingPhrases) {
        const processedPhrase = this.preprocessText(phrase);
        classifier.addDocument(processedPhrase, name);
      }
    }
    classifier.train();
  }

  private preprocessText(text: string): string {
    let processed = text.toLowerCase();
    processed = processed.replace(/[^\w\s]/g, ' ');
    const tokens = tokenizer.tokenize(processed) || [];
    const filteredTokens = stopword.removeStopwords(tokens);
    const stemmedTokens = filteredTokens.map(token => stemmer.stem(token));
    return stemmedTokens.join(' ');
  }

  async detectIntent(message: string, language: string = 'en'): Promise<IntentDetectionProps> {
    if (!this.initialized) await this.initialize();

    const processedMessage = this.preprocessText(message);
    const classifications = classifier.getClassifications(processedMessage);
    const topClassification = classifications[0];

    if (!topClassification || topClassification.value < 0.3) {
      return this.createDefaultDetection(message, language);
    }

    const intentName = topClassification.label;
    const confidence = topClassification.value;
    const intent = this.intents.get(intentName);

    if (!intent) return this.createDefaultDetection(message, language);

    const entities = this.extractEntities(message);
    const confidenceLevel = this.getConfidenceLevel(confidence);
    const sentiment = this.detectSentiment(message);

    return {
      intent: intentName,
      category: intent.category,
      confidence,
      confidenceLevel,
      entities,
      sentiment,
      language,
      requiresHandoff: intent.requiresHandoff,
      suggestedResponses: intent.responses.slice(0, 3),
    };
  }

  async batchDetect(messages: string[], language: string = 'en'): Promise<IntentDetectionProps[]> {
    const results: IntentDetectionProps[] = [];
    for (const message of messages) {
      try {
        results.push(await this.detectIntent(message, language));
      } catch {
        results.push(this.createDefaultDetection(message, language));
      }
    }
    return results;
  }

  async getIntent(intentId: string): Promise<IntentProps | null> {
    return this.intentRepo.findById(intentId);
  }

  async getIntents(filters?: { language?: string; category?: string; isActive?: boolean }): Promise<IntentProps[]> {
    const query: any = {};
    if (filters?.language) query.language = filters.language;
    if (filters?.category) query.category = filters.category;
    if (filters?.isActive !== undefined) query.isActive = filters.isActive;
    return this.intentRepo.find(query);
  }

  getIntentCategories(): Array<{ value: string; label: string }> {
    return [
      { value: 'greeting', label: 'Greeting' },
      { value: 'faq', label: 'FAQ' },
      { value: 'order_status', label: 'Order Status' },
      { value: 'product_info', label: 'Product Information' },
      { value: 'support', label: 'Support' },
      { value: 'complaint', label: 'Complaint' },
      { value: 'refund', label: 'Refund' },
      { value: 'billing', label: 'Billing' },
      { value: 'technical', label: 'Technical' },
      { value: 'shipping', label: 'Shipping' },
      { value: 'returns', label: 'Returns' },
      { value: 'account', label: 'Account' },
      { value: 'payment', label: 'Payment' },
      { value: 'general', label: 'General' },
      { value: 'escalation', label: 'Escalation' },
    ];
  }

  async createIntent(data: any): Promise<IntentProps> {
    const existing = await this.intentRepo.findOne({ name: data.name, language: data.language || 'en' });
    if (existing) throw new Error('Intent with this name already exists for this language');

    const saved = await this.intentRepo.save(data);
    await this.reloadIntents();
    await this.eventPublisher.publish(new IntentCreatedEvent(data.name, data.category));
    return saved;
  }

  async updateIntent(intentId: string, data: any): Promise<IntentProps | null> {
    const result = await this.intentRepo.findByIdAndUpdate(intentId, { $set: data }, { new: true, runValidators: true });
    if (result) await this.reloadIntents();
    return result;
  }

  async deleteIntent(intentId: string): Promise<void> {
    await this.intentRepo.findByIdAndDelete(intentId);
    await this.reloadIntents();
  }

  async addTrainingPhrase(intentId: string, phrase: string): Promise<IntentProps | null> {
    const intent = await this.intentRepo.findById(intentId);
    if (!intent) return null;
    if (!intent.trainingPhrases.includes(phrase)) {
      intent.trainingPhrases.push(phrase);
    }
    const saved = await this.intentRepo.save(intent);
    await this.reloadIntents();
    return saved;
  }

  async addResponse(intentId: string, response: string): Promise<IntentProps | null> {
    const intent = await this.intentRepo.findById(intentId);
    if (!intent) return null;
    if (!intent.responses.includes(response)) {
      intent.responses.push(response);
    }
    const saved = await this.intentRepo.save(intent);
    return saved;
  }

  async toggleIntent(intentId: string): Promise<IntentProps | null> {
    const intent = await this.intentRepo.findById(intentId);
    if (!intent) return null;
    intent.isActive = !intent.isActive;
    const saved = await this.intentRepo.save(intent);
    await this.reloadIntents();
    return saved;
  }

  private async reloadIntents(): Promise<void> {
    await this.loadIntents();
    classifier.events.removeAllListeners('trainedWithDocument');
    await this.trainClassifier();
  }

  private createDefaultDetection(message: string, language: string): IntentDetectionProps {
    return {
      intent: 'general',
      category: IntentCategory.GENERAL,
      confidence: 0,
      confidenceLevel: IntentConfidence.LOW,
      entities: this.extractEntities(message),
      sentiment: this.detectSentiment(message),
      language,
      requiresHandoff: false,
      suggestedResponses: [
        "I'm not sure I understand. Could you please rephrase your question?",
        "I want to make sure I help you correctly. Can you provide more details?",
        "Let me connect you with someone who can better assist you with this.",
      ],
    };
  }

  private extractEntities(message: string): any[] {
    const entities: any[] = [];
    const emailRegex = /[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}/g;
    const emailMatches = message.match(emailRegex);
    if (emailMatches) {
      entities.push({ type: 'email', value: emailMatches[0], confidence: 0.95, resolved: true });
    }

    const phoneRegex = /(?:\+?1[-.\s]?)?\(?\d{3}\)?[-.\s]?\d{3}[-.\s]?\d{4}/g;
    const phoneMatches = message.match(phoneRegex);
    if (phoneMatches) {
      entities.push({ type: 'phone', value: phoneMatches[0], confidence: 0.9, resolved: true });
    }

    const orderRegex = /(?:order\s*(?:#|number|num|no)?\s*[:#]?\s*)?([A-Z0-9]{4,15})/gi;
    const orderMatch = message.match(orderRegex);
    if (orderMatch) {
      const m = /([A-Z0-9]{4,15})/.exec(orderMatch[0]);
      if (m) entities.push({ type: 'order_number', value: m[1], confidence: 0.85, resolved: true });
    }

    return entities;
  }

  private detectSentiment(message: string): { score: number; label: 'positive' | 'neutral' | 'negative' } {
    const analyzer = new natural.SentimentAnalyzer('English', stemmer, 'afinn');
    const tokens = tokenizer.tokenize(message.toLowerCase()) || [];
    const score = analyzer.getSentiment(tokens);
    const normalizedScore = Math.round(((score + 1) / 2) * 100);

    let label: 'positive' | 'neutral' | 'negative';
    if (score > 0.1) label = 'positive';
    else if (score < -0.1) label = 'negative';
    else label = 'neutral';

    return { score: normalizedScore, label };
  }

  private getConfidenceLevel(confidence: number): IntentConfidence {
    if (confidence >= 0.7) return IntentConfidence.HIGH;
    if (confidence >= 0.4) return IntentConfidence.MEDIUM;
    return IntentConfidence.LOW;
  }
}
