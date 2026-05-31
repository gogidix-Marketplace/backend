export class ScoringDomainException extends Error { constructor(message: string) { super(message); this.name = 'ScoringDomainException'; } }
export class LeadScoreNotFoundException extends ScoringDomainException { constructor(id: string) { super(`Lead score ${id} not found`); this.name = 'LeadScoreNotFoundException'; } }
export class ScoringRuleNotFoundException extends ScoringDomainException { constructor(id: string) { super(`Scoring rule ${id} not found`); this.name = 'ScoringRuleNotFoundException'; } }
export class SegmentNotFoundException extends ScoringDomainException { constructor(id: string) { super(`Segment ${id} not found`); this.name = 'SegmentNotFoundException'; } }
export class InvalidScoringDataException extends ScoringDomainException { constructor(message: string) { super(message); this.name = 'InvalidScoringDataException'; } }
