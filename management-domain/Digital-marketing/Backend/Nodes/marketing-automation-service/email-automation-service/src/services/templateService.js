/**
 * Template Service
 * Handlebars template rendering service
 */

const Handlebars = require('handlebars');
const fs = require('fs').promises;
const path = require('path');
const EmailTemplate = require('../models/EmailTemplate');
const logger = require('../utils/logger');

// Register Handlebars helpers
Handlebars.registerHelper('eq', (a, b) => a === b);
Handlebars.registerHelper('ne', (a, b) => a !== b);
Handlebars.registerHelper('gt', (a, b) => a > b);
Handlebars.registerHelper('lt', (a, b) => a < b);
Handlebars.registerHelper('and', (a, b) => a && b);
Handlebars.registerHelper('or', (a, b) => a || b);
Handlebars.registerHelper('not', (a) => !a);
Handlebars.registerHelper('default', (value, defaultValue) => value || defaultValue);
Handlebars.registerHelper('json', (obj) => JSON.stringify(obj));
Handlebars.registerHelper('formatDate', (date, format) => {
  if (!date) return '';
  const d = new Date(date);
  if (format === 'short') {
    return d.toLocaleDateString();
  }
  return d.toLocaleString();
});
Handlebars.registerHelper('uppercase', (str) => str && str.toUpperCase());
Handlebars.registerHelper('lowercase', (str) => str && str.toLowerCase());
Handlebars.registerHelper('capitalize', (str) => {
  if (!str) return '';
  return str.charAt(0).toUpperCase() + str.slice(1).toLowerCase();
});

/**
 * Template cache
 */
const templateCache = new Map();
const CACHE_TTL = 5 * 60 * 1000; // 5 minutes

/**
 * Get template from database
 */
const getTemplate = async (templateId, tenantId) => {
  try {
    const template = await EmailTemplate.findOne({
      templateId,
      tenantId,
      isActive: true
    });

    if (!template) {
      throw new Error(`Template not found: ${templateId}`);
    }

    return template;
  } catch (error) {
    logger.error('Error fetching template:', error);
    throw error;
  }
};

/**
 * Compile template
 */
const compileTemplate = (templateString) => {
  try {
    return Handlebars.compile(templateString, { strict: false });
  } catch (error) {
    logger.error('Error compiling template:', error);
    throw new Error(`Template compilation error: ${error.message}`);
  }
};

/**
 * Render template with data
 */
const renderTemplate = async (templateId, templateData, tenantId) => {
  try {
    // Check cache first
    const cacheKey = `${tenantId}:${templateId}`;
    const cached = templateCache.get(cacheKey);

    let compiledTemplate;

    if (cached && Date.now() - cached.timestamp < CACHE_TTL) {
      compiledTemplate = cached.template;
    } else {
      // Fetch template from database
      const template = await getTemplate(templateId, tenantId);

      // Compile templates
      compiledTemplate = {
        subject: compileTemplate(template.subject),
        html: compileTemplate(template.htmlBody),
        text: template.textBody ? compileTemplate(template.textBody) : null
      };

      // Cache compiled template
      templateCache.set(cacheKey, {
        template: compiledTemplate,
        timestamp: Date.now()
      });
    }

    // Validate required variables
    const template = await getTemplate(templateId, tenantId);
    const missingVars = template.variables
      .filter(v => v.required && !templateData.hasOwnProperty(v.name))
      .map(v => v.name);

    if (missingVars.length > 0) {
      logger.warn('Missing required template variables', {
        templateId,
        missingVars
      });
    }

    // Add default values for missing variables
    const renderData = { ...templateData };
    template.variables.forEach(v => {
      if (!renderData.hasOwnProperty(v.name) && v.defaultValue !== undefined) {
        renderData[v.name] = v.defaultValue;
      }
    });

    // Render templates
    const result = {
      subject: compiledTemplate.subject(renderData),
      html: compiledTemplate.html(renderData),
      text: compiledTemplate.text ? compiledTemplate.text(renderData) : null
    };

    // Increment usage count
    await template.incrementUsage();

    return result;
  } catch (error) {
    logger.error('Error rendering template:', error);
    throw error;
  }
};

/**
 * Render inline template (from string)
 */
const renderInlineTemplate = (templateString, data) => {
  try {
    const template = compileTemplate(templateString);
    return template(data);
  } catch (error) {
    logger.error('Error rendering inline template:', error);
    throw error;
  }
};

/**
 * Validate template syntax
 */
const validateTemplate = (templateString) => {
  try {
    Handlebars.compile(templateString);
    return { valid: true };
  } catch (error) {
    return {
      valid: false,
      error: error.message
    };
  }
};

/**
 * Extract variables from template
 */
const extractVariables = (templateString) => {
  const variableRegex = /\{\{([^}]+)\}\}/g;
  const variables = new Set();
  let match;

  while ((match = variableRegex.exec(templateString)) !== null) {
    let varName = match[1].trim();
    // Remove helpers and special syntax
    if (!varName.startsWith('#') && !varName.startsWith('/') && !varName.startsWith('!')) {
      // Get the first part (property path)
      varName = varName.split(' ')[0].split('|')[0];
      variables.add(varName);
    }
  }

  return Array.from(variables);
};

/**
 * Create template
 */
const createTemplate = async (templateData) => {
  try {
    const { templateId, tenantId, name, description, subject, htmlBody, textBody, variables, category, tags } = templateData;

    // Check if template already exists
    const existing = await EmailTemplate.findOne({ templateId, tenantId });
    if (existing) {
      throw new Error('Template with this ID already exists');
    }

    // Validate template syntax
    const subjectValidation = validateTemplate(subject);
    if (!subjectValidation.valid) {
      throw new Error(`Invalid subject template: ${subjectValidation.error}`);
    }

    const htmlValidation = validateTemplate(htmlBody);
    if (!htmlValidation.valid) {
      throw new Error(`Invalid HTML template: ${htmlValidation.error}`);
    }

    // Extract variables if not provided
    let templateVars = variables;
    if (!templateVars || templateVars.length === 0) {
      const extractedVars = extractVariables(htmlBody);
      const subjectVars = extractVariables(subject);
      const allVars = [...new Set([...extractedVars, ...subjectVars])];
      templateVars = allVars.map(v => ({ name: v, required: false }));
    }

    const template = new EmailTemplate({
      templateId,
      tenantId,
      name,
      description,
      subject,
      htmlBody,
      textBody,
      variables: templateVars,
      category,
      tags
    });

    await template.save();

    // Clear cache for this template
    const cacheKey = `${tenantId}:${templateId}`;
    templateCache.delete(cacheKey);

    logger.info('Template created', { templateId, tenantId });

    return template;
  } catch (error) {
    logger.error('Error creating template:', error);
    throw error;
  }
};

/**
 * Update template
 */
const updateTemplate = async (templateId, tenantId, updates) => {
  try {
    const template = await EmailTemplate.findOne({ templateId, tenantId });
    if (!template) {
      throw new Error('Template not found');
    }

    // Validate new templates if provided
    if (updates.subject) {
      const validation = validateTemplate(updates.subject);
      if (!validation.valid) {
        throw new Error(`Invalid subject template: ${validation.error}`);
      }
    }

    if (updates.htmlBody) {
      const validation = validateTemplate(updates.htmlBody);
      if (!validation.valid) {
        throw new Error(`Invalid HTML template: ${validation.error}`);
      }
    }

    Object.assign(template, updates);
    await template.save();

    // Clear cache
    const cacheKey = `${tenantId}:${templateId}`;
    templateCache.delete(cacheKey);

    logger.info('Template updated', { templateId, tenantId });

    return template;
  } catch (error) {
    logger.error('Error updating template:', error);
    throw error;
  }
};

/**
 * Delete template
 */
const deleteTemplate = async (templateId, tenantId) => {
  try {
    const template = await EmailTemplate.findOne({ templateId, tenantId });
    if (!template) {
      throw new Error('Template not found');
    }

    // Soft delete by setting isActive to false
    template.isActive = false;
    await template.save();

    // Clear cache
    const cacheKey = `${tenantId}:${templateId}`;
    templateCache.delete(cacheKey);

    logger.info('Template deleted', { templateId, tenantId });

    return { success: true };
  } catch (error) {
    logger.error('Error deleting template:', error);
    throw error;
  }
};

/**
 * List templates
 */
const listTemplates = async (tenantId, options = {}) => {
  try {
    const query = { tenantId, isActive: true };

    if (options.category) {
      query.category = options.category;
    }

    if (options.search) {
      query.$or = [
        { name: { $regex: options.search, $options: 'i' } },
        { description: { $regex: options.search, $options: 'i' } }
      ];
    }

    const page = parseInt(options.page) || 1;
    const limit = parseInt(options.limit) || 20;
    const skip = (page - 1) * limit;

    const [templates, total] = await Promise.all([
      EmailTemplate.find(query)
        .sort({ name: 1 })
        .limit(limit)
        .skip(skip),
      EmailTemplate.countDocuments(query)
    ]);

    return {
      templates,
      pagination: {
        page,
        limit,
        total,
        totalPages: Math.ceil(total / limit)
      }
    };
  } catch (error) {
    logger.error('Error listing templates:', error);
    throw error;
  }
};

/**
 * Clear template cache
 */
const clearCache = (templateId = null, tenantId = null) => {
  if (templateId && tenantId) {
    const cacheKey = `${tenantId}:${templateId}`;
    templateCache.delete(cacheKey);
  } else {
    templateCache.clear();
  }
  logger.info('Template cache cleared');
};

/**
 * Preload commonly used templates
 */
const preloadTemplates = async (tenantId) => {
  try {
    const templates = await EmailTemplate.find({
      tenantId,
      isActive: true
    }).sort({ usageCount: -1 }).limit(10);

    for (const template of templates) {
      const cacheKey = `${tenantId}:${template.templateId}`;
      if (!templateCache.has(cacheKey)) {
        const compiled = {
          subject: compileTemplate(template.subject),
          html: compileTemplate(template.htmlBody),
          text: template.textBody ? compileTemplate(template.textBody) : null
        };
        templateCache.set(cacheKey, {
          template: compiled,
          timestamp: Date.now()
        });
      }
    }

    logger.info('Preloaded templates', { count: templates.length });
  } catch (error) {
    logger.error('Error preloading templates:', error);
  }
};

module.exports = {
  getTemplate,
  renderTemplate,
  renderInlineTemplate,
  validateTemplate,
  extractVariables,
  createTemplate,
  updateTemplate,
  deleteTemplate,
  listTemplates,
  clearCache,
  preloadTemplates
};
