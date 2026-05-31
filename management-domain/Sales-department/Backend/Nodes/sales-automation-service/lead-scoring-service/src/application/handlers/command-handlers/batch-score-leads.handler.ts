import { Injectable } from '@nestjs/common';
import { ICommandHandler } from './interfaces/command-handler.interface';
import { BatchScoreLeadsCommand } from '../../commands/batch-score-leads.command';
import { ScoreLeadCommand } from '../../commands/score-lead.command';
import { ScoreLeadHandler } from './score-lead.handler';
import { LeadScore } from '../../../domain/entities/lead-score.entity';

export interface BatchScoreResult {
  leadId: string;
  scoreId: string;
  score: number;
  grade: string;
}

export interface BatchScoreError {
  leadId: string;
  error: string;
}

export interface BatchScoreResponse {
  successful: BatchScoreResult[];
  failed: BatchScoreError[];
  totalProcessed: number;
}

@Injectable()
export class BatchScoreLeadsHandler implements ICommandHandler<BatchScoreLeadsCommand> {
  constructor(
    private readonly scoreLeadHandler: ScoreLeadHandler,
  ) {}

  async execute(command: BatchScoreLeadsCommand): Promise<BatchScoreResponse> {
    const successful: BatchScoreResult[] = [];
    const failed: BatchScoreError[] = [];

    for (const leadId of command.leadIds) {
      try {
        const scoreLeadCommand = new ScoreLeadCommand(
          leadId,
          command.tenantId,
          command.scoreModelId,
          undefined,
          undefined,
          command.requestedBy
        );

        const leadScore = await this.scoreLeadHandler.execute(scoreLeadCommand);

        successful.push({
          leadId,
          scoreId: leadScore.id,
          score: leadScore.totalScore,
          grade: leadScore.grade,
        });
      } catch (error) {
        failed.push({
          leadId,
          error: error instanceof Error ? error.message : 'Unknown error',
        });
      }
    }

    return {
      successful,
      failed,
      totalProcessed: command.leadIds.length,
    };
  }
}
