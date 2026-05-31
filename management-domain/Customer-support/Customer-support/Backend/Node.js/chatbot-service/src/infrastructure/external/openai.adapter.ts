import { Injectable, Logger, OnModuleInit } from '@nestjs/common';
import { IOpenAIPort } from '@domain/ports/output';
import OpenAI from 'openai';
import { ConfigService } from '@nestjs/config';

@Injectable()
export class OpenAIAdapter implements IOpenAIPort, OnModuleInit {
  private readonly logger = new Logger(OpenAIAdapter.name);
  private client: OpenAI | null = null;

  constructor(private configService: ConfigService) {}

  onModuleInit() {
    const apiKey = this.configService.get<string>('config.openai.apiKey');
    if (apiKey) {
      this.client = new OpenAI({ apiKey });
    }
  }

  async chatCompletion(
    systemPrompt: string,
    messages: Array<{ role: string; content: string }>,
    maxTokens?: number,
    temperature?: number,
  ): Promise<{ text: string; usage?: any }> {
    if (!this.client) {
      throw new Error('OpenAI client not initialized');
    }

    try {
      const completion = await this.client.chat.completions.create({
        model: this.configService.get<string>('config.openai.model') || 'gpt-4',
        messages: [
          { role: 'system', content: systemPrompt },
          ...messages,
        ],
        max_tokens: maxTokens || this.configService.get<number>('config.openai.maxTokens') || 500,
        temperature: temperature || this.configService.get<number>('config.openai.temperature') || 0.7,
      });

      return {
        text: completion.choices[0]?.message?.content || '',
        usage: completion.usage,
      };
    } catch (error) {
      this.logger.error('OpenAI completion error: ' + error);
      throw error;
    }
  }
}
