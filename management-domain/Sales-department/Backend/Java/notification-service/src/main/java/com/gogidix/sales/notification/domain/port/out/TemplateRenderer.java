package com.gogidix.sales.notification.domain.port.out;

import java.util.Map;

/**
 * Template Renderer (Output Port)
 * Defines the contract for rendering notification templates
 */
public interface TemplateRenderer {

    /**
     * Renders a notification template with the provided variables
     * @param templateId The template ID
     * @param variables The template variables
     * @return The rendered content
     */
    RenderedTemplate render(String templateId, Map<String, Object> variables);

    /**
     * Renders a template by code with the provided variables
     * @param templateCode The template code
     * @param tenantId The tenant ID
     * @param variables The template variables
     * @return The rendered content
     */
    RenderedTemplate renderByCode(String templateCode, String tenantId, Map<String, Object> variables);

    /**
     * Checks if the renderer is ready
     * @return true if ready, false otherwise
     */
    boolean isReady();

    /**
     * Rendered template result
     */
    record RenderedTemplate(
        String subject,
        String content,
        String htmlContent,
        String templateId
    ) {}
}
