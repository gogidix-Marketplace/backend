/**
 * Intent Detection Service
 * Handles natural language processing, intent classification, and entity extraction
 */

import natural from 'natural';
import stopword from 'stopword';
import { IntentModel } from '../models/Intent';
import {
  IIntentDetection,
  IEntity,
  IntentCategory,
  IntentConfidence,
  IntentDetectionError,
} from '../types';
import { logger } from '../utils/logger';
import { detectLanguage, extractEmail, extractOrderNumber, extractPhone } from '../utils/helpers';
import config from '../config';

const tokenizer = new natural.WordTokenizer();
const stemmer = natural.PorterStemmer;
const classifier = new natural.BayesClassifier();

export class IntentDetectionService {
  private intents: Map<string, any> = new Map();
  private initialized: boolean = false;

  constructor() {
    this.initialize();
  }

  /**
   * Initialize the intent detection service by loading and training
   */
  private async initialize(): Promise<void> {
    try {
      await this.loadIntents();
      await this.trainClassifier();
      this.initialized = true;
      logger.info('Intent Detection Service initialized successfully');
    } catch (error) {
      logger.error('Failed to initialize Intent Detection Service:', error);
    }
  }

  /**
   * Load intents from database
   */
  private async loadIntents(): Promise<void> {
    try {
      const intents = await IntentModel.find({ isActive: true });
      this.intents.clear();

      for (const intent of intents) {
        this.intents.set(intent.name, intent);
      }

      logger.info(`Loaded ${this.intents.size} intents`);
    } catch (error) {
      logger.error('Error loading intents:', error);
      throw new IntentDetectionError('Failed to load intents', error);
    }
  }

  /**
   * Train the classifier with loaded intents
   */
  private async trainClassifier(): Promise<void> {
    try {
      classifier.events.on('trainedWithDocument', (document, label) => {
        logger.debug(`Trained with document: "${document}" as "${label}"`);
      });

      for (const [name, intent] of this.intents) {
        for (const phrase of intent.trainingPhrases) {
          const processedPhrase = this.preprocessText(phrase);
          classifier.addDocument(processedPhrase, name);
        }
      }

      classifier.train();
      logger.info('Classifier trained successfully');
    } catch (error) {
      logger.error('Error training classifier:', error);
      throw new IntentDetectionError('Failed to train classifier', error);
    }
  }

  /**
   * Preprocess text for NLP
   */
  private preprocessText(text: string): string {
    // Convert to lowercase
    let processed = text.toLowerCase();

    // Remove special characters but keep spaces and alphanumeric
    processed = processed.replace(/[^\w\s]/g, ' ');

    // Tokenize
    const tokens = tokenizer.tokenize(processed) || [];

    // Remove stop words
    const filteredTokens = stopword.removeStopwords(tokens);

    // Apply stemming
    const stemmedTokens = filteredTokens.map(token => stemmer.stem(token));

    return stemmedTokens.join(' ');
  }

  /**
   * Detect intent from user message
   */
  async detectIntent(
    message: string,
    language: string = 'en'
  ): Promise<IIntentDetection> {
    try {
      if (!this.initialized) {
        await this.initialize();
      }

      const detectedLanguage = language || detectLanguage(message);
      const processedMessage = this.preprocessText(message);

      // Get classification results
      const classifications = classifier.getClassifications(processedMessage);
      const topClassification = classifications[0];

      if (!topClassification || topClassification.value < 0.3) {
        return this.createDefaultDetection(message, detectedLanguage);
      }

      const intentName = topClassification.label;
      const confidence = topClassification.value;
      const intent = this.intents.get(intentName);

      if (!intent) {
        return this.createDefaultDetection(message, detectedLanguage);
      }

      // Extract entities
      const entities = this.extractEntities(message);

      // Determine confidence level
      const confidenceLevel = this.getConfidenceLevel(confidence);

      // Detect sentiment
      const sentiment = this.detectSentiment(message);

      // Check if handoff is required
      const requiresHandoff = this.requiresHandoff(
        message,
        intent,
        confidence,
        entities
      );

      // Generate suggested responses
      const suggestedResponses = this.generateResponses(intent, message);

      return {
        intent: intentName,
        category: intent.category,
        confidence,
        confidenceLevel,
        entities,
        sentiment,
        language: detectedLanguage,
        requiresHandoff,
        suggestedResponses,
      };
    } catch (error) {
      logger.error('Error detecting intent:', error);
      throw new IntentDetectionError('Failed to detect intent', error);
    }
  }

  /**
   * Create a default detection when intent is unclear
   */
  private createDefaultDetection(message: string, language: string): IIntentDetection {
    return {
      intent: 'general',
      category: IntentCategory.GENERAL,
      confidence: 0,
      confidenceLevel: IntentConfidence.LOW,
      entities: this.extractEntities(message),
      sentiment: this.detectSentiment(message),
      language,
      requiresHandoff: false,
      suggestedResponses: this.getGeneralResponses(),
    };
  }

  /**
   * Extract entities from message
   */
  private extractEntities(message: string): IEntity[] {
    const entities: IEntity[] = [];

    // Extract email
    const email = extractEmail(message);
    if (email) {
      const emailMatch = message.match(new RegExp(email, 'i'));
      if (emailMatch?.index !== undefined) {
        entities.push({
          type: 'email',
          value: email,
          confidence: 0.95,
          start: emailMatch.index,
          end: emailMatch.index + email.length,
          resolved: true,
        });
      }
    }

    // Extract phone
    const phone = extractPhone(message);
    if (phone) {
      const phoneMatch = message.match(new RegExp(phone.replace(/[()]/g, '\\$&'), 'i'));
      if (phoneMatch?.index !== undefined) {
        entities.push({
          type: 'phone',
          value: phone,
          confidence: 0.9,
          start: phoneMatch.index,
          end: phoneMatch.index + phone.length,
          resolved: true,
        });
      }
    }

    // Extract order number
    const orderNumber = extractOrderNumber(message);
    if (orderNumber) {
      const orderMatch = message.match(new RegExp(orderNumber, 'i'));
      if (orderMatch?.index !== undefined) {
        entities.push({
          type: 'order_number',
          value: orderNumber,
          confidence: 0.85,
          start: orderMatch.index,
          end: orderMatch.index + orderNumber.length,
          resolved: true,
        });
      }
    }

    // Extract numbers
    const numbers = message.match(/\b\d+(\.\d+)?\b/g);
    if (numbers) {
      numbers.forEach(num => {
        const numMatch = message.indexOf(num);
        entities.push({
          type: 'number',
          value: num,
          confidence: 0.8,
          start: numMatch,
          end: numMatch + num.length,
          resolved: false,
        });
      });
    }

    // Extract dates
    const datePatterns = [
      /\b(\d{1,2}[/-]\d{1,2}[/-]\d{2,4})\b/g,
      /\b(\d{4}[/-]\d{1,2}[/-]\d{1,2})\b/g,
    ];

    datePatterns.forEach(pattern => {
      const matches = message.match(pattern);
      if (matches) {
        matches.forEach(match => {
          const matchIndex = message.indexOf(match);
          entities.push({
            type: 'date',
            value: match,
            confidence: 0.75,
            start: matchIndex,
            end: matchIndex + match.length,
            resolved: false,
          });
        });
      }
    });

    return entities;
  }

  /**
   * Detect sentiment from message
   */
  private detectSentiment(message: string): { score: number; label: 'positive' | 'neutral' | 'negative' } {
    const analyzer = new natural.SentimentAnalyzer('English', stemmer, 'afinn');
    const tokens = tokenizer.tokenize(message.toLowerCase()) || [];
    const score = analyzer.getSentiment(tokens);

    // Convert -1 to 1 range to 0-100 scale
    const normalizedScore = Math.round(((score + 1) / 2) * 100);

    let label: 'positive' | 'neutral' | 'negative';
    if (score > 0.1) {
      label = 'positive';
    } else if (score < -0.1) {
      label = 'negative';
    } else {
      label = 'neutral';
    }

    return { score: normalizedScore, label };
  }

  /**
   * Get confidence level from numeric confidence
   */
  private getConfidenceLevel(confidence: number): IntentConfidence {
    if (confidence >= 0.7) return IntentConfidence.HIGH;
    if (confidence >= 0.4) return IntentConfidence.MEDIUM;
    return IntentConfidence.LOW;
  }

  /**
   * Check if handoff is required
   */
  private requiresHandoff(
    message: string,
    intent: any,
    confidence: number,
    entities: IEntity[]
  ): boolean {
    // Check if intent explicitly requires handoff
    if (intent.requiresHandoff) {
      return true;
    }

    // Check for escalation keywords
    const escalationKeywords = config.handoff.escalationTopics;
    const lowerMessage = message.toLowerCase();

    for (const keyword of escalationKeywords) {
      if (lowerMessage.includes(keyword)) {
        return true;
      }
    }

    // Check for angry customer sentiment
    const sentiment = this.detectSentiment(message);
    if (sentiment.label === 'negative' && sentiment.score < 30) {
      return true;
    }

    // Low confidence for critical intents
    const criticalCategories = [
      IntentCategory.COMPLAINT,
      IntentCategory.REFUND,
      IntentCategory.BILLING,
      IntentCategory.ESCALATION,
    ];

    if (
      criticalCategories.includes(intent.category) &&
      confidence < config.handoff.threshold
    ) {
      return true;
    }

    return false;
  }

  /**
   * Generate responses based on intent
   */
  private generateResponses(intent: any, message: string): string[] {
    const responses = [...intent.responses];

    // Personalize responses based on extracted entities
    if (message.toLowerCase().includes('thank')) {
      responses.push("You're welcome! Is there anything else I can help you with?");
    }

    return responses.slice(0, 3);
  }

  /**
   * Get general responses for unknown intents
   */
  private getGeneralResponses(): string[] {
    return [
      "I'm not sure I understand. Could you please rephrase your question?",
      "I want to make sure I help you correctly. Can you provide more details?",
      "Let me connect you with someone who can better assist you with this.",
    ];
  }

  /**
   * Reload intents from database (for admin updates)
   */
  async reloadIntents(): Promise<void> {
    await this.loadIntents();
    classifier.events.removeAllListeners('trainedWithDocument');
    await this.trainClassifier();
    logger.info('Intents reloaded successfully');
  }

  /**
   * Get intent by name
   */
  getIntent(name: string): any | undefined {
    return this.intents.get(name);
  }

  /**
   * Get all intents
   */
  getAllIntents(): any[] {
    return Array.from(this.intents.values());
  }

  /**
   * Get intents by category
   */
  getIntentsByCategory(category: IntentCategory): any[] {
    return Array.from(this.intents.values()).filter(
      intent => intent.category === category
    );
  }

  /**
   * Add training example
   */
  async addTrainingExample(intentName: string, phrase: string): Promise<void> {
    const intent = await IntentModel.findOne({ name: intentName });
    if (!intent) {
      throw new IntentDetectionError(`Intent ${intentName} not found`);
    }

    intent.addTrainingPhrase(phrase);
    await intent.save();

    // Reload to update classifier
    await this.reloadIntents();

    logger.info(`Added training example for intent ${intentName}`);
  }

  /**
   * Batch detect intents (for analytics)
   */
  async batchDetect(messages: string[], language: string = 'en'): Promise<IIntentDetection[]> {
    const results: IIntentDetection[] = [];

    for (const message of messages) {
      try {
        const detection = await this.detectIntent(message, language);
        results.push(detection);
      } catch (error) {
        logger.error(`Error detecting intent for message: ${message}`, error);
        results.push(this.createDefaultDetection(message, language));
      }
    }

    return results;
  }
}

export const intentDetectionService = new IntentDetectionService();
