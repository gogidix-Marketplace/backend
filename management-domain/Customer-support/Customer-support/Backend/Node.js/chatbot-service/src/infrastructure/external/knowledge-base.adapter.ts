import { Injectable, Logger } from '@nestjs/common';
import { IKnowledgeBasePort } from '@domain/ports/output';
import { SearchResultProps, KnowledgeArticleProps } from '@domain/models';
import { ConfigService } from '@nestjs/config';
import axios, { AxiosInstance } from 'axios';

@Injectable()
export class KnowledgeBaseAdapter implements IKnowledgeBasePort {
  private readonly logger = new Logger(KnowledgeBaseAdapter.name);
  private client: AxiosInstance;

  constructor(private configService: ConfigService) {
    this.client = axios.create({
      baseURL: this.configService.get<string>('config.services.knowledgeBase') || 'http://localhost:8115',
      timeout: 10000,
      headers: { 'Content-Type': 'application/json' },
    });
  }

  async search(query: string, language: string = 'en', limit: number = 5, category?: string): Promise<SearchResultProps> {
    try {
      const startTime = Date.now();
      const response = await this.client.post('/api/v1/search', { query, language, limit, category });
      const searchTime = Date.now() - startTime;

      return {
        articles: response.data.articles || [],
        totalResults: response.data.total || response.data.articles?.length || 0,
        searchTime,
      };
    } catch (error) {
      this.logger.error('Knowledge base search error: ' + error);
      return { articles: [], totalResults: 0, searchTime: 0 };
    }
  }

  async getArticle(articleId: string, language: string = 'en'): Promise<KnowledgeArticleProps | null> {
    try {
      const response = await this.client.get(`/api/v1/articles/${articleId}`, { params: { language } });
      return response.data;
    } catch (error) {
      this.logger.error('Get article error: ' + error);
      return null;
    }
  }

  async getArticlesByCategory(category: string, language: string = 'en', limit: number = 10): Promise<KnowledgeArticleProps[]> {
    try {
      const response = await this.client.get('/api/v1/articles', { params: { category, language, limit } });
      return response.data.articles || [];
    } catch (error) {
      this.logger.error('Get articles by category error: ' + error);
      return [];
    }
  }

  async healthCheck(): Promise<boolean> {
    try {
      const response = await this.client.get('/health', { timeout: 5000 });
      return response.status === 200;
    } catch {
      return false;
    }
  }
}
