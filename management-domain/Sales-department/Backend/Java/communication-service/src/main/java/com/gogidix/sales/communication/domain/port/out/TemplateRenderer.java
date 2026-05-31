package com.gogidix.sales.communication.domain.port.out;

import java.util.Map;

/**
 * Template Renderer Interface (Port)
 * Defines the contract for rendering message templates
 */
public interface TemplateRenderer {

    /**
     * Renders a template with the given variables
     */
    String render(String templateContent, Map<String, Object> variables);

    /**
     * Renders a template by code with the given variables
     */
    String renderByCode(String templateCode, Map<String, Object> variables);

    /**
     * Validates template variables
     */
    boolean validateVariables(String templateCode, Map<String, Object> variables);

    /**
     * Gets template content by code
     */
    String getTemplateContent(String templateCode);
}
