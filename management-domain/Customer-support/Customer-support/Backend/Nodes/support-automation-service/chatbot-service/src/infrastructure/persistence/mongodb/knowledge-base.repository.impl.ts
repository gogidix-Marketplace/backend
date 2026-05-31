import { Injectable, Logger } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model, Schema } from 'mongoose';
import { KnowledgeBaseRepository } from '@domain/ports/output';

const ArticleSchema = new Schema({
  id: { type: String, required: true, unique: true },
  title: { type: String, required: true },
  content: { type: String, required: true },
  category: { type: String, required: true },
  tags: [{ type: String }],
  locale: { type: String, default: 'en' },
}, { timestamps: true, collection: 'knowledge_articles' });

@Injectable()
export class MongoKnowledgeBaseRepository implements KnowledgeBaseRepository {
  private readonly logger = new Logger(MongoKnowledgeBaseRepository.name);

  constructor(@InjectModel('KnowledgeArticle') private readonly model: Model<any>) {}

  async search(query: string, language?: string): Promise<string | null> {
    const normalizedQuery = query.toLowerCase();
    const articles = await this.model.find({ locale: language || 'en' });
    let bestMatch: any = null;
    let bestScore = 0;
    for (const article of articles) {
      const text = (article.title + ' ' + article.content + ' ' + article.tags.join(' ')).toLowerCase();
      const words = normalizedQuery.split(' ');
      const score = words.filter(w => text.includes(w)).length / words.length;
      if (score > bestScore) { bestScore = score; bestMatch = article; }
    }
    return bestScore > 0.1 ? bestMatch.content : null;
  }

  async getArticle(articleId: string): Promise<any | null> {
    return this.model.findOne({ id: articleId });
  }

  async addArticle(article: any): Promise<any> {
    const doc = new this.model(article);
    await doc.save();
    return article;
  }
}

export { ArticleSchema };
