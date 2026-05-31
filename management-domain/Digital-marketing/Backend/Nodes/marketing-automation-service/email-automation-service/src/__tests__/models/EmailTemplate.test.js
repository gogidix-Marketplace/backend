/**
 * Unit tests for EmailTemplate Model
 */

const EmailTemplate = require('../../models/EmailTemplate');

describe('EmailTemplate Model', () => {
  let templateData;

  beforeEach(() => {
    templateData = {
      templateId: 'template-welcome-001',
      tenantId: 'tenant-123',
      name: 'Welcome Email',
      subject: 'Welcome to Our Service',
      htmlBody: '<h1>Welcome!</h1><p>Dear {{firstName}},</p>',
      textBody: 'Welcome! Dear {{firstName}},',
      variables: [
        {
          name: 'firstName',
          description: 'Recipient first name',
          required: true
        },
        {
          name: 'companyName',
          description: 'Company name',
          required: false
        }
      ]
    };
  });

  describe('EmailTemplate Creation', () => {
    test('should create template with required fields', () => {
      const template = new EmailTemplate(templateData);

      expect(template.templateId).toBe('template-welcome-001');
      expect(template.tenantId).toBe('tenant-123');
      expect(template.name).toBe('Welcome Email');
      expect(template.subject).toBe('Welcome to Our Service');
      expect(template.htmlBody).toContain('<h1>Welcome!</h1>');
      expect(template.isActive).toBe(true);
      expect(template.version).toBe('1.0.0');
      expect(template.usageCount).toBe(0);
    });

    test('should set default values', () => {
      const template = new EmailTemplate(templateData);

      expect(template.isActive).toBe(true);
      expect(template.version).toBe('1.0.0');
      expect(template.usageCount).toBe(0);
      expect(template.lastUsedAt).toBeNull();
    });

    test('should handle optional fields', () => {
      templateData.description = 'Welcome email for new users';
      templateData.category = 'onboarding';
      templateData.tags = ['welcome', 'onboarding'];

      const template = new EmailTemplate(templateData);

      expect(template.description).toBe('Welcome email for new users');
      expect(template.category).toBe('onboarding');
      expect(template.tags).toContain('welcome');
    });
  });

  describe('Template Variables', () => {
    test('should handle template variables', () => {
      const template = new EmailTemplate(templateData);

      expect(template.variables.length).toBe(2);
      expect(template.variables[0].name).toBe('firstName');
      expect(template.variables[0].required).toBe(true);
      expect(template.variables[1].name).toBe('companyName');
    });

    test('should handle templates without variables', () => {
      delete templateData.variables;
      const template = new EmailTemplate(templateData);

      expect(template.variables).toBeUndefined();
    });
  });

  describe('Methods', () => {
    describe('incrementUsage', () => {
      test('should increment usage count', async () => {
        const template = new EmailTemplate(templateData);
        await template.incrementUsage();

        expect(template.usageCount).toBe(1);
        expect(template.lastUsedAt).toBeInstanceOf(Date);
      });

      test('should increment usage count multiple times', async () => {
        const template = new EmailTemplate(templateData);
        await template.incrementUsage();
        await template.incrementUsage();
        await template.incrementUsage();

        expect(template.usageCount).toBe(3);
      });
    });
  });

  describe('Static Methods', () => {
    describe('getActiveByTenant', () => {
      test('should return query for active templates', () => {
        const query = EmailTemplate.getActiveByTenant('tenant-123');
        expect(query).toBeDefined();
      });
    });

    describe('getByCategory', () => {
      test('should return query for templates by category', () => {
        const query = EmailTemplate.getByCategory('tenant-123', 'onboarding');
        expect(query).toBeDefined();
      });
    });

    describe('search', () => {
      test('should return query for template search', () => {
        const query = EmailTemplate.search('tenant-123', 'welcome');
        expect(query).toBeDefined();
      });
    });

    describe('upsertByTemplateId', () => {
      test('should upsert template by ID', () => {
        const upsert = EmailTemplate.upsertByTemplateId(
          'template-welcome-001',
          { name: 'Updated Welcome Email' },
          'tenant-123'
        );
        expect(upsert).toBeDefined();
      });
    });
  });

  describe('Category Support', () => {
    const categories = [
      'onboarding', 'promotional', 'newsletter', 'transactional',
      'announcement', 'reactivation', 'nurturing'
    ];

    test.each(categories)('should support %s category', (category) => {
      templateData.category = category;
      const template = new EmailTemplate(templateData);
      expect(template.category).toBe(category);
    });
  });

  describe('Version Control', () => {
    test('should handle version strings', () => {
      templateData.version = '2.1.0';
      const template = new EmailTemplate(templateData);
      expect(template.version).toBe('2.1.0');
    });
  });

  describe('Active Status', () => {
    test('should handle active status', () => {
      templateData.isActive = true;
      const template = new EmailTemplate(templateData);
      expect(template.isActive).toBe(true);
    });

    test('should handle inactive status', () => {
      templateData.isActive = false;
      const template = new EmailTemplate(templateData);
      expect(template.isActive).toBe(false);
    });
  });

  describe('Metadata', () => {
    test('should handle metadata', () => {
      templateData.metadata = {
        createdFrom: 'UI',
        designer: 'john.doe',
        lastModifiedBy: 'jane.doe'
      };

      const template = new EmailTemplate(templateData);
      expect(template.metadata.createdFrom).toBe('UI');
      expect(template.metadata.designer).toBe('john.doe');
    });
  });

  describe('Content Validation', () => {
    test('should require subject', () => {
      delete templateData.subject;
      const template = new EmailTemplate(templateData);

      const validationError = template.validateSync();
      expect(validationError.errors.subject).toBeDefined();
    });

    test('should require htmlBody', () => {
      delete templateData.htmlBody;
      const template = new EmailTemplate(templateData);

      const validationError = template.validateSync();
      expect(validationError.errors.htmlBody).toBeDefined();
    });
  });
});
