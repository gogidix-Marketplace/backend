export interface TranslationRequestProps {
  text: string;
  targetLanguage: string;
  sourceLanguage?: string;
}

export interface TranslationResponseProps {
  translatedText: string;
  sourceLanguage: string;
  targetLanguage: string;
  confidence: number;
}
