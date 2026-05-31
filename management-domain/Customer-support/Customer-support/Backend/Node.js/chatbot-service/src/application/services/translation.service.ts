import { Injectable, Logger } from '@nestjs/common';
import { ITranslationQueryPort } from '@domain/ports/input';
import { TranslationRequestProps, TranslationResponseProps } from '@domain/models';
import { ConfigService } from '@nestjs/config';

@Injectable()
export class TranslationApplicationService implements ITranslationQueryPort {
  private readonly logger = new Logger(TranslationApplicationService.name);
  private supportedLanguages: Set<string>;

  constructor(private configService: ConfigService) {
    const langs = this.configService.get<string>('SUPPORTED_LANGUAGES', 'en,es,fr,de,pt,zh,ja,ar');
    this.supportedLanguages = new Set(langs.split(','));
  }

  async translate(request: TranslationRequestProps): Promise<TranslationResponseProps> {
    const { text, targetLanguage, sourceLanguage } = request;

    if (!this.supportedLanguages.has(targetLanguage)) {
      throw new Error(`Unsupported target language: ${targetLanguage}`);
    }

    const detectedSource = sourceLanguage || this.detectLanguage(text);

    if (detectedSource === targetLanguage) {
      return { translatedText: text, sourceLanguage: detectedSource, targetLanguage, confidence: 1 };
    }

    const translatedText = await this.performTranslation(text, detectedSource, targetLanguage);
    return { translatedText, sourceLanguage: detectedSource, targetLanguage, confidence: 0.9 };
  }

  async batchTranslate(texts: string[], targetLanguage: string, sourceLanguage?: string): Promise<TranslationResponseProps[]> {
    const results: TranslationResponseProps[] = [];
    for (const text of texts) {
      try {
        results.push(await this.translate({ text, targetLanguage, sourceLanguage }));
      } catch {
        results.push({
          translatedText: text,
          sourceLanguage: sourceLanguage || 'en',
          targetLanguage,
          confidence: 0,
        });
      }
    }
    return results;
  }

  getSupportedLanguages(): string[] {
    return Array.from(this.supportedLanguages);
  }

  isLanguageSupported(language: string): boolean {
    return this.supportedLanguages.has(language);
  }

  private detectLanguage(text: string): string {
    const patterns: Record<string, RegExp> = {
      es: /\b(hola|gracias|por favor|buenos días|buenas noches|adiós)\b/i,
      fr: /\b(bonjour|merci|au revoir)\b/i,
      de: /\b(guten tag|danke|bitte|auf wiedersehen)\b/i,
      pt: /\b(olá|obrigado|por favor|bom dia|boa noite)\b/i,
      zh: /[\u4e00-\u9fff]/,
      ja: /[\u3040-\u309f\u30a0-\u30ff]/,
      ar: /[\u0600-\u06ff]/,
    };
    for (const [lang, pattern] of Object.entries(patterns)) {
      if (pattern.test(text)) return lang;
    }
    return 'en';
  }

  private async performTranslation(text: string, sourceLanguage: string, targetLanguage: string): Promise<string> {
    const dictionary: Record<string, Record<string, string>> = {
      'en->es': { 'hello': 'hola', 'goodbye': 'adiós', 'thank you': 'gracias', 'please': 'por favor', 'yes': 'sí', 'no': 'no', 'help': 'ayuda', 'sorry': 'lo siento' },
      'en->fr': { 'hello': 'bonjour', 'goodbye': 'au revoir', 'thank you': 'merci', 'please': "s'il vous plaît", 'yes': 'oui', 'no': 'non', 'help': 'aide' },
      'en->de': { 'hello': 'guten tag', 'goodbye': 'auf wiedersehen', 'thank you': 'danke', 'please': 'bitte', 'yes': 'ja', 'no': 'nein', 'help': 'hilfe' },
    };

    const key = `${sourceLanguage}->${targetLanguage}`;
    const langDict = dictionary[key];
    if (langDict) {
      const lowerText = text.toLowerCase().trim();
      if (langDict[lowerText]) return langDict[lowerText];
    }

    this.logger.warn(`Translation not available for "${text}" from ${sourceLanguage} to ${targetLanguage}`);
    return `[${targetLanguage.toUpperCase()}] ${text}`;
  }
}
