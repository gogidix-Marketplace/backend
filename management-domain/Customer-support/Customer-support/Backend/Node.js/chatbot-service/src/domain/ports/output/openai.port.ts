export interface IOpenAIPort {
  chatCompletion(systemPrompt: string, messages: Array<{ role: string; content: string }>, maxTokens?: number, temperature?: number): Promise<{ text: string; usage?: any }>;
}
