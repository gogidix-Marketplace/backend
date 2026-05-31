import { Intent } from '@domain/models/intent.model';
import { IntentCategory } from '@domain/enums/intent-category.enum';

describe('Intent', () => {
  const validProps = {
    name: 'greeting',
    category: IntentCategory.GREETING,
    description: 'Greet the customer',
    trainingPhrases: ['hello', 'hi', 'hey'],
    responses: ['Hello! How can I help you?', 'Hi there!'],
  };

  describe('create', () => {
    it('should create an intent with valid props', () => {
      const intent = Intent.create(validProps);
      expect(intent.name).toBe('greeting');
      expect(intent.category).toBe(IntentCategory.GREETING);
      expect(intent.trainingPhrases).toEqual(['hello', 'hi', 'hey']);
      expect(intent.responses).toEqual(['Hello! How can I help you?', 'Hi there!']);
    });

    it('should default isActive to true', () => {
      const intent = Intent.create(validProps);
      expect(intent.isActive).toBe(true);
    });

    it('should default priority to 0', () => {
      const intent = Intent.create(validProps);
      expect(intent.priority).toBe(0);
    });

    it('should default language to en', () => {
      const intent = Intent.create(validProps);
      expect(intent.language).toBe('en');
    });
  });

  describe('addTrainingPhrase', () => {
    it('should add a training phrase', () => {
      const intent = Intent.create(validProps);
      intent.addTrainingPhrase('good morning');
      expect(intent.trainingPhrases).toContain('good morning');
    });

    it('should not add duplicate training phrases', () => {
      const intent = Intent.create(validProps);
      intent.addTrainingPhrase('hello');
      expect(intent.trainingPhrases.filter(p => p === 'hello').length).toBe(1);
    });
  });

  describe('addResponse', () => {
    it('should add a response', () => {
      const intent = Intent.create(validProps);
      intent.addResponse('Welcome!');
      expect(intent.responses).toContain('Welcome!');
    });

    it('should not add duplicate responses', () => {
      const intent = Intent.create(validProps);
      intent.addResponse('Hello! How can I help you?');
      expect(intent.responses.filter(r => r === 'Hello! How can I help you?').length).toBe(1);
    });
  });

  describe('toggleActive', () => {
    it('should toggle isActive from true to false', () => {
      const intent = Intent.create(validProps);
      intent.toggleActive();
      expect(intent.isActive).toBe(false);
    });

    it('should toggle isActive back to true', () => {
      const intent = Intent.create(validProps);
      intent.toggleActive();
      intent.toggleActive();
      expect(intent.isActive).toBe(true);
    });
  });
});
