import { Injectable, Logger } from '@nestjs/common';
import { v4 as uuidv4 } from 'uuid';
import Sentiment from 'sentiment';
import natural from 'natural';
import { SentimentAnalysisInputPort } from '@domain/ports/input';
import { EventPublisher } from '@domain/ports/output';
import { SentimentResult, EmotionResult, AnalysisResult } from '@domain/models';
import { SentimentType, Emotion } from '@domain/enums';
import { SentimentAnalyzedEvent } from '@domain/events';

const sentimentAnalyzer = new Sentiment();
const tokenizer = new natural.WordTokenizer();

const emotionLexicon: Record<Emotion, string[]> = {
  [Emotion.JOY]: ['happy', 'joy', 'delighted', 'pleased', 'excited', 'thrilled', 'cheerful', 'glad', 'satisfied', 'love', 'enjoy', 'fun', 'smile'],
  [Emotion.SADNESS]: ['sad', 'unhappy', 'miserable', 'depressed', 'down', 'disappointed', 'heartbroken', 'cry', 'tears', 'hurt', 'pain', 'lonely', 'loss'],
  [Emotion.ANGER]: ['angry', 'furious', 'rage', 'mad', 'outraged', 'annoyed', 'irritated', 'frustrated', 'hostile', 'bitter', 'hate'],
  [Emotion.FEAR]: ['afraid', 'fear', 'scared', 'frightened', 'terrified', 'anxious', 'worried', 'nervous', 'dread', 'panic', 'concerned'],
  [Emotion.DISGUST]: ['disgusted', 'revolting', 'repulsive', 'sick', 'nauseating', 'gross', 'appalled', 'horrible', 'terrible', 'nasty'],
  [Emotion.SURPRISE]: ['surprised', 'shocked', 'astonished', 'amazed', 'stunned', 'startled', 'unexpected', 'wow', 'incredible'],
  [Emotion.NEUTRAL]: ['okay', 'ok', 'fine', 'normal', 'average', 'typical', 'standard'],
};

const intensifiers = ['very', 'really', 'extremely', 'absolutely', 'completely', 'totally'];
const negators = ['not', 'no', 'never', 'none', 'nobody', 'nothing', 'neither', 'barely', 'hardly'];

@Injectable()
export class SentimentService implements SentimentAnalysisInputPort {
  private readonly logger = new Logger(SentimentService.name);

  constructor(private readonly eventPublisher: EventPublisher) {}

  async analyze(text: string, includeEmotions = true): Promise<AnalysisResult> {
    const sentimentResult = await this.analyzeSentiment(text);
    let emotions: EmotionResult | undefined;
    if (includeEmotions) emotions = await this.analyzeEmotions(text);

    const result = AnalysisResult.create({
      id: uuidv4(), text, sentiment: sentimentResult.toPlainObject(),
      emotions: emotions?.toPlainObject(), timestamp: new Date(), createdAt: new Date(),
    });

    await this.eventPublisher.publish(new SentimentAnalyzedEvent({
      eventId: uuidv4(), text, score: sentimentResult.score, sentiment: sentimentResult.sentiment,
    }));

    return result;
  }

  async analyzeSentiment(text: string): Promise<SentimentResult> {
    const result = sentimentAnalyzer.analyze(text);
    let sentiment: SentimentType;
    if (result.score > 0) sentiment = SentimentType.POSITIVE;
    else if (result.score < 0) sentiment = SentimentType.NEGATIVE;
    else sentiment = SentimentType.NEUTRAL;

    const maxScore = result.tokens.length * 5;
    const confidence = maxScore > 0 ? Math.min(Math.abs(result.score) / (maxScore * 0.3), 1) : 0;
    const keywords = [...(result.positive || []), ...(result.negative || [])];

    return SentimentResult.create({ text, sentiment, score: result.score, confidence: Math.round(confidence * 100) / 100, keywords });
  }

  async analyzeEmotions(text: string): Promise<EmotionResult> {
    const tokens = tokenizer.tokenize(text.toLowerCase()) || [];
    const emotionScores: Record<Emotion, number> = { [Emotion.JOY]: 0, [Emotion.SADNESS]: 0, [Emotion.ANGER]: 0, [Emotion.FEAR]: 0, [Emotion.DISGUST]: 0, [Emotion.SURPRISE]: 0, [Emotion.NEUTRAL]: 0 };
    const keywords: Record<Emotion, string[]> = { [Emotion.JOY]: [], [Emotion.SADNESS]: [], [Emotion.ANGER]: [], [Emotion.FEAR]: [], [Emotion.DISGUST]: [], [Emotion.SURPRISE]: [], [Emotion.NEUTRAL]: [] };

    for (let i = 0; i < tokens.length; i++) {
      const token = tokens[i];
      const emotion = this.getWordEmotion(token);
      if (emotion) {
        let score = 1;
        let hasNegator = false;
        for (let j = Math.max(0, i - 3); j < i; j++) {
          if (negators.includes(tokens[j])) { hasNegator = true; break; }
          if (intensifiers.includes(tokens[j])) score *= 1.5;
        }
        if (hasNegator) { emotionScores[this.getOppositeEmotion(emotion)] += score * 0.5; }
        else { emotionScores[emotion] += score; keywords[emotion].push(token); }
      }
    }

    const totalScore = Object.values(emotionScores).reduce((s, v) => s + v, 0) || 1;
    for (const e of Object.keys(emotionScores)) emotionScores[e as Emotion] = Math.round((emotionScores[e as Emotion] / totalScore) * 100) / 100;

    let primaryEmotion = Emotion.NEUTRAL;
    let maxScore = 0;
    for (const [e, s] of Object.entries(emotionScores)) { if (s > maxScore) { maxScore = s; primaryEmotion = e as Emotion; } }

    return EmotionResult.create({ text, primaryEmotion, emotions: emotionScores, confidence: maxScore > 0.3 ? maxScore : 0, keywords });
  }

  async batchAnalyze(texts: string[], includeEmotions = false): Promise<{ results: AnalysisResult[]; summary: any }> {
    const results: AnalysisResult[] = [];
    for (const text of texts) results.push(await this.analyze(text, includeEmotions));
    let positive = 0, negative = 0, neutral = 0, totalScore = 0;
    for (const r of results) {
      if (r.sentiment.sentiment === SentimentType.POSITIVE) positive++;
      else if (r.sentiment.sentiment === SentimentType.NEGATIVE) negative++;
      else neutral++;
      totalScore += r.sentiment.score;
    }
    return { results, summary: { total: results.length, positive, negative, neutral, averageScore: results.length > 0 ? Math.round((totalScore / results.length) * 100) / 100 : 0 } };
  }

  async getStats(): Promise<Record<string, unknown>> {
    return { cacheSize: 0, averageConfidence: 0.75 };
  }

  async clearCache(): Promise<number> { return 0; }

  private getWordEmotion(word: string): Emotion | null {
    const lower = word.toLowerCase().replace(/[^a-z]/g, '');
    for (const [emotion, words] of Object.entries(emotionLexicon)) {
      if (words.includes(lower)) return emotion as Emotion;
    }
    return null;
  }

  private getOppositeEmotion(emotion: Emotion): Emotion {
    const opposites: Record<Emotion, Emotion> = { [Emotion.JOY]: Emotion.SADNESS, [Emotion.SADNESS]: Emotion.JOY, [Emotion.ANGER]: Emotion.NEUTRAL, [Emotion.FEAR]: Emotion.NEUTRAL, [Emotion.DISGUST]: Emotion.JOY, [Emotion.SURPRISE]: Emotion.NEUTRAL, [Emotion.NEUTRAL]: Emotion.NEUTRAL };
    return opposites[emotion] || Emotion.NEUTRAL;
  }
}
