package com.gogidix.shared.ai.application.service;

import com.gogidix.shared.ai.domain.model.PromptTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PromptTemplateService {

    private final Map<String, PromptTemplate> templates = new ConcurrentHashMap<>();

    public void registerTemplate(PromptTemplate template) {
        templates.put(template.getName(), template);
    }

    public String resolve(String templateName, Map<String, String> variables) {
        PromptTemplate template = templates.get(templateName);
        if (template == null) {
            throw new IllegalArgumentException("Template not found: " + templateName);
        }
        return template.resolve(variables);
    }

    public PromptTemplate getTemplate(String name) {
        PromptTemplate template = templates.get(name);
        if (template == null) {
            throw new IllegalArgumentException("Template not found: " + name);
        }
        return template;
    }

    public Map<String, PromptTemplate> getAllTemplates() {
        return Map.copyOf(templates);
    }

    public void removeTemplate(String name) {
        templates.remove(name);
    }

    public boolean hasTemplate(String name) {
        return templates.containsKey(name);
    }
}
