import { SearchResultProps, KnowledgeArticleProps } from '../../models';

export interface IKnowledgeBasePort {
  search(query: string, language?: string, limit?: number, category?: string): Promise<SearchResultProps>;
  getArticle(articleId: string, language?: string): Promise<KnowledgeArticleProps | null>;
  getArticlesByCategory(category: string, language?: string, limit?: number): Promise<KnowledgeArticleProps[]>;
  healthCheck(): Promise<boolean>;
}
