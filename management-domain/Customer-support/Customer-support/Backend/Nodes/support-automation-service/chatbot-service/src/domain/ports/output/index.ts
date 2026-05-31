import { ConversationContext } from '../../models';

export interface ContextRepository {
  save(context: ConversationContext): Promise<ConversationContext>;
  findBySessionId(sessionId: string): Promise<ConversationContext | null>;
  delete(sessionId: string): Promise<boolean>;
}

export interface KnowledgeBaseRepository {
  search(query: string, language?: string): Promise<string | null>;
  getArticle(articleId: string): Promise<any | null>;
  addArticle(article: any): Promise<any>;
}

export interface EventPublisher {
  publish(event: import('../../events').DomainEvent): Promise<void>;
  publishAll(events: import('../../events').DomainEvent[]): Promise<void>;
}
