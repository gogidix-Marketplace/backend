import { Controller, Post, Get, Body, Query, UseInterceptors, Inject} from '@nestjs/common';
import { KnowledgeBaseRepository } from '@domain/ports/output';
import { SearchKnowledgeDto } from '@application/dto';
import { TransformInterceptor } from '@shared/interceptors';

@Controller('api/knowledge')
@UseInterceptors(TransformInterceptor)
export class KnowledgeController {
  constructor(@Inject('KnowledgeBaseRepository') private readonly knowledgeBase: KnowledgeBaseRepository) {}

  @Post('search')
  async search(@Body() dto: SearchKnowledgeDto) { return this.knowledgeBase.search(dto.query, dto.language); }

  @Get('articles')
  async getArticles(@Query('category') category?: string) { return []; }
}
