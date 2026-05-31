import { TranslationRequestProps, TranslationResponseProps } from '../../models';

export interface ITranslationQueryPort {
  translate(request: TranslationRequestProps): Promise<TranslationResponseProps>;
  batchTranslate(texts: string[], targetLanguage: string, sourceLanguage?: string): Promise<TranslationResponseProps[]>;
  getSupportedLanguages(): string[];
  isLanguageSupported(language: string): boolean;
}
