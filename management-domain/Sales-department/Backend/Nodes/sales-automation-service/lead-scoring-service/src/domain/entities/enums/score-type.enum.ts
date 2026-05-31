export enum ScoreType {
  DEMOGRAPHIC = 'demographic',
  BEHAVIORAL = 'behavioral',
  FIRMOGRAPHIC = 'firmographic',
  ENGAGEMENT = 'engagement',
  CUSTOM = 'custom',
  COMBINED = 'combined',
}

export enum ScoreRuleOperator {
  EQUALS = 'equals',
  NOT_EQUALS = 'not_equals',
  GREATER_THAN = 'greater_than',
  LESS_THAN = 'less_than',
  GREATER_THAN_OR_EQUAL = 'greater_than_or_equal',
  LESS_THAN_OR_EQUAL = 'less_than_or_equal',
  CONTAINS = 'contains',
  NOT_CONTAINS = 'not_contains',
  STARTS_WITH = 'starts_with',
  ENDS_WITH = 'ends_with',
  IN = 'in',
  NOT_IN = 'not_in',
  BETWEEN = 'between',
  REGEX = 'regex',
}

export enum ScoreModelStatus {
  DRAFT = 'draft',
  ACTIVE = 'active',
  PAUSED = 'paused',
  ARCHIVED = 'archived',
}

export enum ScoreRuleType {
  CONDITION = 'condition',
  FORMULA = 'formula',
  MACHINE_LEARNING = 'machine_learning',
  EXTERNAL = 'external',
}

export enum ScoreModelType {
  DEMOGRAPHIC = 'demographic',
  BEHAVIORAL = 'behavioral',
  COMBINED = 'combined',
  CUSTOM = 'custom',
  MACHINE_LEARNING = 'machine_learning',
}
