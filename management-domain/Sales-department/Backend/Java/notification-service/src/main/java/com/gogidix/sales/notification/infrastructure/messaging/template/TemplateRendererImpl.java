package com.gogidix.sales.notification.infrastructure.messaging.template;

import com.gogidix.sales.notification.domain.model.NotificationTemplate;
import com.gogidix.sales.notification.domain.port.out.TemplateRenderer;
import com.gogidix.sales.notification.domain.repository.NotificationTemplateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Template Renderer Implementation
 * Renders notification templates with variable substitution
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class TemplateRendererImpl implements TemplateRenderer {

    private final NotificationTemplateRepository templateRepository;

    private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\{\\{([^}]+)\\}\\}");

    @Override
    public RenderedTemplate render(String templateId, Map<String, Object> variables) {
        log.debug("Rendering template: {}", templateId);

        String tenantId = com.gogidix.sales.notification.shared.requestcontext.RequestContextHolder.getTenantId();
        NotificationTemplate template = templateRepository.findByTemplateIdAndTenantId(templateId, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Template not found: " + templateId));

        return doRender(template, variables);
    }

    @Override
    public RenderedTemplate renderByCode(String templateCode, String tenantId, Map<String, Object> variables) {
        log.debug("Rendering template by code: {}", templateCode);

        NotificationTemplate template = templateRepository.findByCodeAndTenantId(templateCode, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Template not found: " + templateCode));

        return doRender(template, variables);
    }

    @Override
    public boolean isReady() {
        return true;
    }

    private RenderedTemplate doRender(NotificationTemplate template, Map<String, Object> variables) {
        if (!template.isValid()) {
            throw new IllegalStateException("Template is not valid or inactive");
        }

        String subject = renderText(template.getSubjectTemplate(), variables);
        String content = renderText(template.getContentTemplate(), variables);
        String htmlContent = renderText(template.getHtmlContentTemplate(), variables);

        return new RenderedTemplate(subject, content, htmlContent, template.getTemplateId());
    }

    private String renderText(String template, Map<String, Object> variables) {
        if (template == null || template.isEmpty()) {
            return "";
        }

        if (variables == null || variables.isEmpty()) {
            return template;
        }

        Matcher matcher = VARIABLE_PATTERN.matcher(template);
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            String variableName = matcher.group(1).trim();
            Object value = variables.get(variableName);

            String replacement = value != null ? value.toString() : matcher.group(0);
            matcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
        }

        matcher.appendTail(result);
        return result.toString();
    }
}
