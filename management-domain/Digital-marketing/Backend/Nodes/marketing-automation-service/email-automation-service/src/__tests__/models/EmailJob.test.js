/**
 * Unit tests for EmailJob Model
 */

const EmailJob = require('../../models/EmailJob');

describe('EmailJob Model', () => {
  let jobData;

  beforeEach(() => {
    jobData = {
      jobId: 'job-test-001',
      tenantId: 'tenant-123',
      from: {
        email: 'from@example.com',
        name: 'Sender Name'
      },
      to: [{
        email: 'to@example.com',
        name: 'Recipient Name'
      }],
      subject: 'Test Email Subject',
      html: '<h1>Test Email</h1>',
      text: 'Test Email'
    };
  });

  describe('EmailJob Creation', () => {
    test('should create email job with required fields', () => {
      const job = new EmailJob(jobData);

      expect(job.jobId).toBe('job-test-001');
      expect(job.tenantId).toBe('tenant-123');
      expect(job.from.email).toBe('from@example.com');
      expect(job.to[0].email).toBe('to@example.com');
      expect(job.subject).toBe('Test Email Subject');
      expect(job.status).toBe('pending');
      expect(job.priority).toBe('normal');
      expect(job.provider).toBe('sendgrid');
    });

    test('should set default values', () => {
      const job = new EmailJob(jobData);

      expect(job.deliveryAttempts).toBe(0);
      expect(job.opened).toBe(false);
      expect(job.clicked).toBe(false);
      expect(job.openCount).toBe(0);
      expect(job.clickCount).toBe(0);
      expect(job.bounce).toBe('none');
      expect(job.complaint.reported).toBe(false);
      expect(job.webhookProcessed).toBe(false);
      expect(job.webhookAttempts).toBe(0);
    });

    test('should handle multiple recipients', () => {
      jobData.to = [
        { email: 'to1@example.com', name: 'Recipient 1' },
        { email: 'to2@example.com', name: 'Recipient 2' },
        { email: 'to3@example.com', name: 'Recipient 3' }
      ];

      const job = new EmailJob(jobData);
      expect(job.to.length).toBe(3);
    });

    test('should handle CC recipients', () => {
      jobData.cc = [
        { email: 'cc@example.com', name: 'CC Recipient' }
      ];

      const job = new EmailJob(jobData);
      expect(job.cc.length).toBe(1);
      expect(job.cc[0].email).toBe('cc@example.com');
    });

    test('should handle BCC recipients', () => {
      jobData.bcc = [
        { email: 'bcc@example.com', name: 'BCC Recipient' }
      ];

      const job = new EmailJob(jobData);
      expect(job.bcc.length).toBe(1);
      expect(job.bcc[0].email).toBe('bcc@example.com');
    });

    test('should handle template data', () => {
      jobData.templateId = 'template-001';
      jobData.templateData = {
        firstName: 'John',
        lastName: 'Doe',
        company: 'Acme Corp'
      };

      const job = new EmailJob(jobData);
      expect(job.templateId).toBe('template-001');
      expect(job.templateData.firstName).toBe('John');
    });

    test('should handle attachments', () => {
      jobData.attachments = [
        {
          filename: 'document.pdf',
          contentType: 'application/pdf',
          size: 1024000,
          contentId: 'att-001'
        }
      ];

      const job = new EmailJob(jobData);
      expect(job.attachments.length).toBe(1);
      expect(job.attachments[0].filename).toBe('document.pdf');
    });
  });

  describe('Status Methods', () => {
    let job;

    beforeEach(() => {
      job = new EmailJob(jobData);
    });

    test('should mark email as sent', async () => {
      await job.markAsSent('provider-msg-123');

      expect(job.status).toBe('sent');
      expect(job.sentAt).toBeInstanceOf(Date);
      expect(job.providerMessageId).toBe('provider-msg-123');
    });

    test('should mark email as delivered', async () => {
      await job.markAsDelivered();

      expect(job.status).toBe('delivered');
    });

    test('should mark email as opened', async () => {
      await job.markAsOpened();

      expect(job.opened).toBe(true);
      expect(job.openedAt).toBeInstanceOf(Date);
      expect(job.openCount).toBe(1);
    });

    test('should increment open count on multiple opens', async () => {
      await job.markAsOpened();
      await job.markAsOpened();
      await job.markAsOpened();

      expect(job.openCount).toBe(3);
    });

    test('should mark email as clicked', async () => {
      job.status = 'opened';
      await job.markAsClicked();

      expect(job.clicked).toBe(true);
      expect(job.clickedAt).toBeInstanceOf(Date);
      expect(job.clickCount).toBe(1);
      expect(job.status).toBe('clicked');
    });

    test('should increment click count on multiple clicks', async () => {
      await job.markAsClicked();
      await job.markAsClicked();

      expect(job.clickCount).toBe(2);
    });

    test('should mark email as bounced', async () => {
      await job.markAsBounced('hard', 'Mailbox full');

      expect(job.status).toBe('bounced');
      expect(job.bounce).toBe('hard');
      expect(job.bounceReason).toBe('Mailbox full');
    });

    test('should record delivery attempt without error', async () => {
      await job.recordAttempt();

      expect(job.deliveryAttempts).toBe(1);
      expect(job.lastAttemptAt).toBeInstanceOf(Date);
      expect(job.error).toBeNull();
    });

    test('should record delivery attempt with error', async () => {
      const error = {
        message: 'Connection timeout',
        code: 'ETIMEDOUT'
      };

      await job.recordAttempt(error);

      expect(job.deliveryAttempts).toBe(1);
      expect(job.error.message).toBe('Connection timeout');
      expect(job.error.code).toBe('ETIMEDOUT');
      expect(job.nextRetryAt).toBeInstanceOf(Date);
    });
  });

  describe('Provider Support', () => {
    const providers = ['sendgrid', 'ses', 'mailgun', 'smtp'];

    test.each(providers)('should support %s provider', (provider) => {
      jobData.provider = provider;
      const job = new EmailJob(jobData);
      expect(job.provider).toBe(provider);
    });
  });

  describe('Priority Levels', () => {
    const priorities = ['high', 'normal', 'low'];

    test.each(priorities)('should support %s priority', (priority) => {
      jobData.priority = priority;
      const job = new EmailJob(jobData);
      expect(job.priority).toBe(priority);
    });
  });

  describe('Status Values', () => {
    const statuses = [
      'pending', 'queued', 'processing', 'sent', 'delivered',
      'opened', 'clicked', 'bounced', 'deferred', 'failed'
    ];

    test.each(statuses)('should support %s status', (status) => {
      jobData.status = status;
      const job = new EmailJob(jobData);
      expect(job.status).toBe(status);
    });
  });

  describe('Tags and Metadata', () => {
    test('should handle tags', () => {
      jobData.tags = ['newsletter', 'promotional', 'q1-2024'];
      const job = new EmailJob(jobData);

      expect(job.tags.length).toBe(3);
      expect(job.tags).toContain('newsletter');
    });

    test('should handle metadata', () => {
      jobData.metadata = {
        campaign: 'spring-sale',
        segment: 'vip-customers',
        source: 'landing-page'
      };
      const job = new EmailJob(jobData);

      expect(job.metadata.campaign).toBe('spring-sale');
    });
  });

  describe('Scheduling', () => {
    test('should handle scheduled sending', () => {
      const scheduledDate = new Date('2024-12-31T10:00:00Z');
      jobData.scheduledFor = scheduledDate;

      const job = new EmailJob(jobData);
      expect(job.scheduledFor).toEqual(scheduledDate);
    });
  });

  describe('Campaign Association', () => {
    test('should associate with campaign', () => {
      jobData.campaignId = 'campaign-456';
      const job = new EmailJob(jobData);

      expect(job.campaignId).toBe('campaign-456');
    });
  });

  describe('Static Methods', () => {
    describe('getByTenant', () => {
      test('should query by tenant ID', () => {
        const query = EmailJob.getByTenant('tenant-123');
        expect(query).toBeDefined();
      });

      test('should filter by status when provided', () => {
        const query = EmailJob.getByTenant('tenant-123', { status: 'sent' });
        expect(query).toBeDefined();
      });

      test('should filter by campaign ID when provided', () => {
        const query = EmailJob.getByTenant('tenant-123', { campaignId: 'campaign-456' });
        expect(query).toBeDefined();
      });
    });

    describe('getStatistics', () => {
      test('should get statistics for tenant', () => {
        const stats = EmailJob.getStatistics('tenant-123');
        expect(stats).toBeDefined();
      });

      test('should get statistics for campaign', () => {
        const stats = EmailJob.getStatistics('tenant-123', 'campaign-456');
        expect(stats).toBeDefined();
      });
    });
  });
});
