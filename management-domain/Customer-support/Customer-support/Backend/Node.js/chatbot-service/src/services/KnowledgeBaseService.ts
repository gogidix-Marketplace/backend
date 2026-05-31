/**
 * Knowledge Base Service
 * Integrates with external knowledge base for retrieving relevant articles
 */

import axios, { AxiosInstance } from 'axios';
import { IKnowledgeArticle, ISearchResult } from '../types';
import { logger } from '../utils/logger';
import config from '../config';
import { redisClient } from '../utils/redis';

export class KnowledgeBaseService {
  private client: AxiosInstance;
  private cacheEnabled: boolean = true;
  private readonly CACHE_TTL = 1800; // 30 minutes

  constructor() {
    this.client = axios.create({
      baseURL: config.services.knowledgeBase,
      timeout: 10000,
      headers: {
        'Content-Type': 'application/json',
      },
    });
  }

  /**
   * Search knowledge base for relevant articles
   */
  async search(
    query: string,
    language: string = 'en',
    limit: number = 5,
    category?: string
  ): Promise<ISearchResult> {
    try {
      const cacheKey = `kb:${language}:${category || 'all'}:${Buffer.from(query).toString('base64')}`;

      if (this.cacheEnabled) {
        const cached = await redisClient.get(cacheKey);
        if (cached) {
          logger.debug(`KB cache hit for query: ${query}`);
          return JSON.parse(cached);
        }
      }

      const startTime = Date.now();

      const response = await this.client.post('/api/v1/search', {
        query,
        language,
        limit,
        category,
      });

      const searchTime = Date.now() - startTime;
      const result: ISearchResult = {
        articles: response.data.articles || [],
        totalResults: response.data.total || response.data.articles?.length || 0,
        searchTime,
      };

      // Cache the results
      if (this.cacheEnabled && result.articles.length > 0) {
        await redisClient.set(cacheKey, JSON.stringify(result), this.CACHE_TTL);
      }

      return result;
    } catch (error) {
      if (axios.isAxiosError(error)) {
        logger.error('Knowledge base search error:', {
          message: error.message,
          status: error.response?.status,
          data: error.response?.data,
        });

        // Return empty result on failure
        return {
          articles: [],
          totalResults: 0,
          searchTime: 0,
        };
      }

      logger.error('Knowledge base search error:', error);
      return {
        articles: [],
        totalResults: 0,
        searchTime: 0,
      };
    }
  }

  /**
   * Get article by ID
   */
  async getArticle(articleId: string, language: string = 'en'): Promise<IKnowledgeArticle | null> {
    try {
      const cacheKey = `kb:article:${articleId}:${language}`;

      if (this.cacheEnabled) {
        const cached = await redisClient.get(cacheKey);
        if (cached) {
          return JSON.parse(cached);
        }
      }

      const response = await this.client.get(`/api/v1/articles/${articleId}`, {
        params: { language },
      });

      const article = response.data;

      if (this.cacheEnabled && article) {
        await redisClient.set(cacheKey, JSON.stringify(article), this.CACHE_TTL);
      }

      return article;
    } catch (error) {
      if (axios.isAxiosError(error)) {
        logger.error('Get article error:', {
          message: error.message,
          status: error.response?.status,
        });
      } else {
        logger.error('Get article error:', error);
      }
      return null;
    }
  }

  /**
   * Get articles by category
   */
  async getArticlesByCategory(
    category: string,
    language: string = 'en',
    limit: number = 10
  ): Promise<IKnowledgeArticle[]> {
    try {
      const cacheKey = `kb:category:${category}:${language}`;

      if (this.cacheEnabled) {
        const cached = await redisClient.get(cacheKey);
        if (cached) {
          return JSON.parse(cached);
        }
      }

      const response = await this.client.get('/api/v1/articles', {
        params: { category, language, limit },
      });

      const articles = response.data.articles || [];

      if (this.cacheEnabled) {
        await redisClient.set(cacheKey, JSON.stringify(articles), this.CACHE_TTL);
      }

      return articles;
    } catch (error) {
      logger.error('Get articles by category error:', error);
      return [];
    }
  }

  /**
   * Get featured/popular articles
   */
  async getFeaturedArticles(language: string = 'en', limit: number = 5): Promise<IKnowledgeArticle[]> {
    try {
      const cacheKey = `kb:featured:${language}`;

      if (this.cacheEnabled) {
        const cached = await redisClient.get(cacheKey);
        if (cached) {
          return JSON.parse(cached);
        }
      }

      const response = await this.client.get('/api/v1/articles/featured', {
        params: { language, limit },
      });

      const articles = response.data.articles || [];

      if (this.cacheEnabled) {
        await redisClient.set(cacheKey, JSON.stringify(articles), this.CACHE_TTL * 2); // Longer cache for featured
      }

      return articles;
    } catch (error) {
      logger.error('Get featured articles error:', error);
      return [];
    }
  }

  /**
   * Get suggested articles based on previous queries
   */
  async getSuggestedArticles(
    previousQueries: string[],
    language: string = 'en',
    limit: number = 3
  ): Promise<IKnowledgeArticle[]> {
    try {
      if (previousQueries.length === 0) {
        return this.getFeaturedArticles(language, limit);
      }

      // Use the most recent query
      const lastQuery = previousQueries[previousQueries.length - 1];
      const result = await this.search(lastQuery, language, limit);

      return result.articles;
    } catch (error) {
      logger.error('Get suggested articles error:', error);
      return [];
    }
  }

  /**
   * Check if knowledge base is available
   */
  async healthCheck(): Promise<boolean> {
    try {
      const response = await this.client.get('/health', { timeout: 5000 });
      return response.status === 200;
    } catch {
      return false;
    }
  }

  /**
   * Clear cache for a specific query
   */
  async clearCache(query: string, language: string = 'en', category?: string): Promise<void> {
    const cacheKey = `kb:${language}:${category || 'all'}:${Buffer.from(query).toString('base64')}`;
    await redisClient.del(cacheKey);
  }

  /**
   * Clear all KB cache
   */
  async clearAllCache(): Promise<void> {
    // In production, you might want to use Redis SCAN to avoid blocking
    const keys = await redisClient.getClient().keys('kb:*');
    if (keys.length > 0) {
      await redisClient.getClient().del(keys);
    }
    logger.info(`Cleared ${keys.length} KB cache entries`);
  }

  /**
   * Enable or disable caching
   */
  setCacheEnabled(enabled: boolean): void {
    this.cacheEnabled = enabled;
  }
}

export const knowledgeBaseService = new KnowledgeBaseService();
