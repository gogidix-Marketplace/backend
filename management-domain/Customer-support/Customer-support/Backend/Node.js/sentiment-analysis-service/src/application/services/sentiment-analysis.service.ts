import { Injectable, Logger } from '@nestjs/common';
import { v4 as uuidv4 } from 'uuid';
import Sentiment from 'sentiment';
import natural from 'natural';
import { SentimentAnalysisInputPort, AnalyzeTextRequest, SentimentAnalysisResult } from '@domain/ports/input';
import { SentimentAnalysisRepository, SentimentAlertRepository, EventPublisher } from '@domain/ports/output';
import { SentimentAnalysis, SentimentAlert } from '@domain/models';
import { SentimentLabel } from '@domain/enums';
import { SentimentAnalyzedEvent, SentimentAlertTriggeredEvent } from '@domain/events';

@Injectable()
export class SentimentAnalysisService implements SentimentAnalysisInputPort {
  private readonly logger = new Logger(SentimentAnalysisService.name);
  private readonly afinn = new Sentiment();
  private readonly tokenizer = new natural.WordTokenizer();
  private readonly negativityThreshold = 30;
  private readonly alertThreshold = 25;

  constructor(
    private readonly analysisRepository: SentimentAnalysisRepository,
    private readonly alertRepository: SentimentAlertRepository,
    private readonly eventPublisher: EventPublisher,
  ) {}

  async analyzeText(request: AnalyzeTextRequest): Promise<SentimentAnalysisResult> {
    const text = request.text;
    const language = request.language || 'en';

    const tokens = this.tokenizer.tokenize(text) || [];
    const afinnResult = this.afinn.analyze(text);
    const combinedScore = afinnResult.score;
    const normalizedScore = Math.max(0, Math.min(100, ((combinedScore + 5) / 10) * 100));
    const label = this.determineLabel(normalizedScore, afinnResult);
    const emotions = this.analyzeEmotions(text, tokens);
    const keywords = this.extractKeywords(afinnResult);
    const confidence = this.calculateConfidence(tokens.length, afinnResult);

    const result: SentimentAnalysisResult = {
      ticketId: request.ticketId || '',
      text,
      sentiment: { score: combinedScore, normalized: normalizedScore, label, confidence },
      emotions,
      keywords,
      language,
    };

    const analysis = SentimentAnalysis.create({
      id: uuidv4(),
      ticketId: request.ticketId || '',
      text,
      sentiment: result.sentiment,
      emotions,
      keywords,
      language,
      timestamp: new Date(),
      createdAt: new Date(),
      updatedAt: new Date(),
    });

    await this.analysisRepository.save(analysis);

    await this.eventPublisher.publish(new SentimentAnalyzedEvent({
      eventId: uuidv4(),
      ticketId: request.ticketId || '',
      score: normalizedScore,
      label,
    }));

    if (normalizedScore < this.alertThreshold) {
      const severity = this.determineSeverity(normalizedScore, label);
      const alert = SentimentAlert.create({
        id: uuidv4(),
        alertId: `alert_${uuidv4()}`,
        ticketId: request.ticketId || '',
        sentimentScore: normalizedScore,
        sentimentLabel: label,
        severity,
        triggeredBy: 'sentiment_analysis',
        message: `Negative sentiment detected: score ${normalizedScore}`,
        acknowledged: false,
        createdAt: new Date(),
      });

      await this.alertRepository.save(alert);

      await this.eventPublisher.publish(new SentimentAlertTriggeredEvent({
        eventId: uuidv4(),
        alertId: alert.alertId,
        ticketId: request.ticketId || '',
        severity,
      }));
    }

    return result;
  }

  async batchAnalyze(texts: string[], language?: string): Promise<SentimentAnalysisResult[]> {
    const results: SentimentAnalysisResult[] = [];
    for (const text of texts) {
      const result = await this.analyzeText({ text, language });
      results.push(result);
    }
    return results;
  }

  async getAlerts(): Promise<any[]> {
    return this.alertRepository.findActive();
  }

  async getAlertStats(): Promise<Record<string, unknown>> {
    const alerts = await this.alertRepository.findActive();
    const bySeverity: Record<string, number> = { low: 0, medium: 0, high: 0, critical: 0 };
    for (const alert of alerts) {
      bySeverity[alert.severity]++;
    }
    return { total: alerts.length, bySeverity, acknowledged: alerts.filter(a => a.acknowledged).length };
  }

  async acknowledgeAlert(alertId: string, userId: string): Promise<boolean> {
    const alert = await this.alertRepository.findById(alertId);
    if (!alert) return false;
    const updated = alert.acknowledge(userId);
    await this.alertRepository.update(updated);
    return true;
  }

  private determineLabel(score: number, afinnResult: any): SentimentLabel {
    const angerWords = ['furious', 'enraged', 'hate', 'terrible', 'awful', 'disgusting'];
    const hasAnger = angerWords.some(word =>
      afinnResult.calculation?.some((c: any) => c.word.toLowerCase().includes(word))
    );
    if (hasAnger || (score < 20 && afinnResult.negative > Math.abs(afinnResult.positive) * 2)) return SentimentLabel.ANGRY;
    if (score > 85) return SentimentLabel.VERY_POSITIVE;
    if (score > 70) return SentimentLabel.POSITIVE;
    if (score < 20) return SentimentLabel.VERY_NEGATIVE;
    if (score < 30) return SentimentLabel.NEGATIVE;
    return SentimentLabel.NEUTRAL;
  }

  private analyzeEmotions(text: string, tokens: string[]): Record<string, number> {
    const lowerText = text.toLowerCase();
    const joyWords = ['happy', 'glad', 'pleased', 'delighted', 'satisfied', 'excited', 'love'];
    const sadnessWords = ['sad', 'unhappy', 'disappointed', 'upset', 'depressed'];
    const angerWords = ['angry', 'furious', 'mad', 'annoyed', 'irritated', 'hate'];
    const fearWords = ['worried', 'scared', 'afraid', 'anxious', 'concerned', 'nervous'];
    const disgustWords = ['disgusted', 'gross', 'awful', 'terrible'];
    const surpriseWords = ['surprised', 'shocked', 'amazed', 'unexpected'];
    const totalTokens = tokens.length || 1;
    const countEmotion = (words: string[]) => words.reduce((c, w) => c + (lowerText.includes(w) ? 1 : 0), 0);
    return {
      joy: countEmotion(joyWords) / totalTokens,
      sadness: countEmotion(sadnessWords) / totalTokens,
      anger: countEmotion(angerWords) / totalTokens,
      fear: countEmotion(fearWords) / totalTokens,
      disgust: countEmotion(disgustWords) / totalTokens,
      surprise: countEmotion(surpriseWords) / totalTokens,
    };
  }

  private extractKeywords(afinnResult: any): Array<{ word: string; sentiment: string; score: number }> {
    const keywords: Array<{ word: string; sentiment: string; score: number }> = [];
    if (afinnResult.calculation) {
      for (const item of afinnResult.calculation) {
        if (Math.abs(item.score) >= 2) {
          keywords.push({ word: item.word, sentiment: item.score > 0 ? 'positive' : 'negative', score: Math.abs(item.score) });
        }
      }
    }
    return keywords.sort((a, b) => b.score - a.score).slice(0, 10);
  }

  private calculateConfidence(tokenCount: number, afinnResult: any): number {
    const sentimentWords = afinnResult.calculation?.length || 0;
    let confidence = 0.5;
    confidence += Math.min(sentimentWords * 0.1, 0.3);
    if (tokenCount >= 5 && tokenCount <= 100) confidence += 0.1;
    return Math.min(confidence, 1);
  }

  private determineSeverity(score: number, label: SentimentLabel): 'low' | 'medium' | 'high' | 'critical' {
    if (label === SentimentLabel.ANGRY || score < 15) return 'critical';
    if (label === SentimentLabel.VERY_NEGATIVE || score < 25) return 'high';
    if (score < this.negativityThreshold) return 'medium';
    return 'low';
  }
}
