export interface KnowledgeArticleProps {
  articleId: string;
  title: string;
  content: string;
  category: string;
  tags: string[];
  language: string;
  relevanceScore: number;
}

export interface SearchResultProps {
  articles: KnowledgeArticleProps[];
  totalResults: number;
  searchTime: number;
}
