/**
 * Translation Service
 * Handles multi-language support and translation
 */

import axios from 'axios';
import { ITranslationRequest, ITranslationResponse, TranslationError } from '../types';
import { logger } from '../utils/logger';
import { redisClient } from './KnowledgeBaseService';

interface TranslationCache {
  [key: string]: {
    text: string;
    timestamp: number;
  };
}

export class TranslationService {
  private cache: TranslationCache = {};
  private readonly CACHE_TTL = 3600000; // 1 hour
  private supportedLanguages: Set<string>;

  constructor() {
    this.supportedLanguages = new Set(config.i18n.supportedLanguages);
    this.cleanupCache();
  }

  /**
   * Detect language from text
   */
  async detectLanguage(text: string): Promise<string> {
    try {
      // Simple keyword-based detection (in production, use a proper language detection API)
      const patterns: Record<string, RegExp[]> = {
        es: [/\\b(hola|gracias|por favor|buenos días|buenas noches|adiós)\\b/i],
        fr: [/\\b(bonjour|merci|s'il vous plaît|au revoir)\\b/i],
        de: [/\\b(guten tag|danke|bitte|auf wiedersehen)\\b/i],
        pt: [/\\b(olá|obrigado|por favor|bom dia|boa noite)\\b/i],
        zh: [/[\u4e00-\u9fff]/],
        ja: [/[\u3040-\u309f\u30a0-\u30ff]/],
        ar: [/[\u0600-\u06ff]/],
      };

      for (const [lang, regexList] of Object.entries(patterns)) {
        for (const regex of regexList) {
          if (regex.test(text)) {
            return lang;
          }
        }
      }

      return config.i18n.defaultLanguage;
    } catch (error) {
      logger.error('Language detection error:', error);
      return config.i18n.defaultLanguage;
    }
  }

  /**
   * Translate text
   */
  async translate(request: ITranslationRequest): Promise<ITranslationResponse> {
    try {
      const { text, targetLanguage, sourceLanguage } = request;

      // Validate target language
      if (!this.supportedLanguages.has(targetLanguage)) {
        throw new TranslationError(`Unsupported target language: ${targetLanguage}`);
      }

      // Detect source language if not provided
      const detectedSource = sourceLanguage || await this.detectLanguage(text);

      // If already in target language, return as-is
      if (detectedSource === targetLanguage) {
        return {
          translatedText: text,
          sourceLanguage: detectedSource,
          targetLanguage,
          confidence: 1,
        };
      }

      // Check cache
      const cacheKey = `${detectedSource}:${targetLanguage}:${Buffer.from(text).toString('base64')}`;
      const cached = await this.getFromCache(cacheKey);
      if (cached) {
        return {
          translatedText: cached,
          sourceLanguage: detectedSource,
          targetLanguage,
          confidence: 0.95,
        };
      }

      // Perform translation
      const translatedText = await this.performTranslation(text, detectedSource, targetLanguage);

      // Cache the result
      this.setToCache(cacheKey, translatedText);

      return {
        translatedText,
        sourceLanguage: detectedSource,
        targetLanguage,
        confidence: 0.9,
      };
    } catch (error) {
      logger.error('Translation error:', error);
      throw new TranslationError(
        error instanceof Error ? error.message : 'Translation failed',
        error
      );
    }
  }

  /**
   * Perform actual translation (placeholder for integration)
   * In production, integrate with Google Translate, DeepL, or similar
   */
  private async performTranslation(
    text: string,
    sourceLanguage: string,
    targetLanguage: string
  ): Promise<string> {
    // Simple dictionary-based translation for common phrases
    const dictionary: Record<string, Record<string, string>> = {
      'en->es': {
        'hello': 'hola',
        'goodbye': 'adiós',
        'thank you': 'gracias',
        'please': 'por favor',
        'yes': 'sí',
        'no': 'no',
        'help': 'ayuda',
        'welcome': 'bienvenido',
        'sorry': 'lo siento',
      },
      'en->fr': {
        'hello': 'bonjour',
        'goodbye': 'au revoir',
        'thank you': 'merci',
        'please': "s'il vous plaît",
        'yes': 'oui',
        'no': 'non',
        'help': 'aide',
        'welcome': 'bienvenue',
        'sorry': 'désolé',
      },
      'en->de': {
        'hello': 'guten tag',
        'goodbye': 'auf wiedersehen',
        'thank you': 'danke',
        'please': 'bitte',
        'yes': 'ja',
        'no': 'nein',
        'help': 'hilfe',
        'welcome': 'willkommen',
        'sorry': 'entschuldigung',
      },
    };

    const key = `${sourceLanguage}->${targetLanguage}`;
    const langDict = dictionary[key];

    if (langDict) {
      const lowerText = text.toLowerCase().trim();
      if (langDict[lowerText]) {
        return langDict[lowerText];
      }
    }

    // Fallback: Return original text with language indicator
    // In production, integrate with a real translation service
    logger.warn(`Translation not available for "${text}" from ${sourceLanguage} to ${targetLanguage}`);

    // Return a placeholder indicating translation needed
    return `[${targetLanguage.toUpperCase()}] ${text}`;
  }

  /**
   * Batch translate multiple texts
   */
  async batchTranslate(
    texts: string[],
    targetLanguage: string,
    sourceLanguage?: string
  ): Promise<ITranslationResponse[]> {
    const results: ITranslationResponse[] = [];

    for (const text of texts) {
      try {
        const result = await this.translate({
          text,
          targetLanguage,
          sourceLanguage,
        });
        results.push(result);
      } catch (error) {
        logger.error(`Batch translation error for text: ${text}`, error);
        results.push({
          translatedText: text,
          sourceLanguage: sourceLanguage || config.i18n.defaultLanguage,
          targetLanguage,
          confidence: 0,
        });
      }
    }

    return results;
  }

  /**
   * Get localized message
   */
  getLocalizedMessage(key: string, language: string = config.i18n.defaultLanguage): string {
    const messages: Record<string, Record<string, string>> = {
      welcome: {
        en: 'Welcome! How can I help you today?',
        es: '¡Bienvenido! ¿Cómo puedo ayudarte hoy?',
        fr: 'Bienvenue! Comment puis-je vous aider aujourd\'hui?',
        de: 'Willkommen! Wie kann ich Ihnen heute helfen?',
        pt: 'Bem-vindo! Como posso ajudá-lo hoje?',
        zh: '欢迎！今天我能为您做什么？',
        ja: 'いらっしゃいませ！今日はどのようにお手伝いできますか？',
        ar: 'مرحبا! كيف يمكنني مساعدتك اليوم؟',
      },
      goodbye: {
        en: 'Goodbye! Have a great day!',
        es: '¡Adiós! ¡Que tengas un gran día!',
        fr: 'Au revoir! Passez une excellente journée!',
        de: 'Auf Wiedersehen! Haben Sie einen great Tag!',
        pt: 'Adeus! Tenha um ótimo dia!',
        zh: '再见！祝您有美好的一天！',
        ja: 'さようなら！素敵な一日を！',
        ar: 'وداعا! أتمنى لك يوما رائعا!',
      },
      thanks: {
        en: 'Thank you for contacting us!',
        es: '¡Gracias por contactarnos!',
        fr: 'Merci de nous avoir contactés!',
        de: 'Danke, dass Sie uns kontaktiert haben!',
        pt: 'Obrigado por entrar em contato!',
        zh: '感谢您联系我们！',
        ja: 'お問い合わせいただきありがとうございます！',
        ar: 'شكرا لتواصلك معنا!',
      },
      error: {
        en: 'Sorry, something went wrong. Please try again.',
        es: 'Lo siento, algo salió mal. Por favor, inténtelo de nuevo.',
        fr: 'Désolé, quelque chose s\'est mal passé. Veuillez réessayer.',
        de: 'Entschuldigung, etwas ist schiefgelaufen. Bitte versuchen Sie es erneut.',
        pt: 'Desculpe, algo deu errado. Por favor, tente novamente.',
        zh: '抱歉，出了点问题。请重试。',
        ja: '申し訳ありませんが、何かが間違っていました。もう一度お試しください。',
        ar: 'عذرا، حدث خطأ ما. يرجى المحاولة مرة أخرى.',
      },
      handoff: {
        en: 'Let me connect you with a human agent who can better assist you.',
        es: 'Déjame conectarle con un agente humano que pueda ayudarle mejor.',
        fr: 'Laissez-moi vous connecter avec un agent humain qui pourra mieux vous aider.',
        de: 'Lassen Sie mich Sie mit einem menschlichen Agenten verbinden, der Ihnen besser helfen kann.',
        pt: 'Deixe-me conectá-lo com um agente humano que pode ajudá-lo melhor.',
        zh: '让我为您连接一位能更好地为您提供帮助的人工代理。',
        ja: 'よりよくお手伝いできる人間のエージェントにあなたを繋がせましょう。',
        ar: 'دعني أوصلك بوكيل بشري يمكنه مساعدتك بشكل أفضل.',
      },
      searching: {
        en: 'Let me look that up for you...',
        es: 'Déjame buscar eso para ti...',
        fr: 'Laissez-moi chercher cela pour vous...',
        de: 'Lassen Sie mich das für Sie nachschlagen...',
        pt: 'Deixe-me procurar isso para você...',
        zh: '让我为您查一下...',
        ja: 'それを調べてみましょう...',
        ar: 'دعني أبحث عن ذلك لك...',
      },
    };

    return messages[key]?.[language] || messages[key]?.[config.i18n.defaultLanguage] || key;
  }

  /**
   * Check if language is supported
   */
  isLanguageSupported(language: string): boolean {
    return this.supportedLanguages.has(language);
  }

  /**
   * Get supported languages
   */
  getSupportedLanguages(): string[] {
    return Array.from(this.supportedLanguages);
  }

  /**
   * Cache management
   */
  private async getFromCache(key: string): Promise<string | null> {
    try {
      const cached = await redisClient.get(`trans:${key}`);
      return cached;
    } catch (error) {
      logger.error('Get translation from cache error:', error);
      return null;
    }
  }

  private async setToCache(key: string, value: string): Promise<void> {
    try {
      await redisClient.set(`trans:${key}`, value, this.CACHE_TTL / 1000);
    } catch (error) {
      logger.error('Set translation to cache error:', error);
    }
  }

  /**
   * Clean up expired cache entries
   */
  private cleanupCache(): void {
    setInterval(() => {
      const now = Date.now();
      for (const key in this.cache) {
        if (now - this.cache[key].timestamp > this.CACHE_TTL) {
          delete this.cache[key];
        }
      }
    }, 300000); // Every 5 minutes
  }

  /**
   * Clear translation cache
   */
  async clearCache(): Promise<void> {
    try {
      const keys = await redisClient.getClient().keys('trans:*');
      if (keys.length > 0) {
        await redisClient.getClient().del(keys);
      }
      this.cache = {};
      logger.info(`Cleared ${keys.length} translation cache entries`);
    } catch (error) {
      logger.error('Clear translation cache error:', error);
    }
  }
}

export const translationService = new TranslationService();
