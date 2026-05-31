import { MongooseModule } from '@nestjs/mongoose';
import { Module } from '@nestjs/common';
import { KnowledgeBaseRepository } from '@domain/ports/output';
import { MongoKnowledgeBaseRepository, ArticleSchema } from './knowledge-base.repository.impl';
import { RedisContextRepository } from './context.repository.impl';
import { ContextRepository } from '@domain/ports/output';

@Module({
  imports: [MongooseModule.forFeature([{ name: 'KnowledgeArticle', schema: ArticleSchema }])],
  providers: [
    { provide: 'KnowledgeBaseRepository', useClass: MongoKnowledgeBaseRepository },
    { provide: 'ContextRepository', useClass: RedisContextRepository },
  ],
  exports: ['KnowledgeBaseRepository', 'ContextRepository'],
})
export class PersistenceModule {}
