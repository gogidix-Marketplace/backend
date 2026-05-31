package com.gogidix.sales.communication.domain.repository;

import com.gogidix.sales.communication.domain.model.Message;
import com.gogidix.sales.communication.domain.model.MessageTemplate;

import java.util.List;
import java.util.Optional;

/**
 * Message Template Repository Interface (Port)
 * Defines the contract for message template persistence operations
 */
public interface MessageTemplateRepository {

    MessageTemplate save(MessageTemplate template);

    List<MessageTemplate> saveAll(List<MessageTemplate> templates);

    Optional<MessageTemplate> findById(String id);

    Optional<MessageTemplate> findByTemplateIdAndTenantId(String templateId, String tenantId);

    Optional<MessageTemplate> findByCodeAndTenantId(String code, String tenantId);

    List<MessageTemplate> findByTenantId(String tenantId);

    List<MessageTemplate> findByTenantIdAndStatus(String tenantId, MessageTemplate.TemplateStatus status);

    List<MessageTemplate> findByTenantIdAndType(String tenantId, MessageTemplate.TemplateType type);

    List<MessageTemplate> findByTenantIdAndChannelType(String tenantId, Message.ChannelType channelType);

    List<MessageTemplate> findByTenantIdAndCategory(String tenantId, String category);

    List<MessageTemplate> findByTenantIdAndTagsContaining(String tenantId, String tag);

    List<MessageTemplate> findByTenantIdAndLanguageAndLocale(
            String tenantId, String language, String locale);

    List<MessageTemplate> findByParentTemplateIdAndTenantId(String parentTemplateId, String tenantId);

    boolean existsByTemplateIdAndTenantId(String templateId, String tenantId);

    boolean existsByCodeAndTenantId(String code, String tenantId);

    void deleteById(String id);

    void deleteByTemplateIdAndTenantId(String templateId, String tenantId);

    void deleteAllByTenantId(String tenantId);

    long countByTenantId(String tenantId);

    long countByTenantIdAndStatus(String tenantId, MessageTemplate.TemplateStatus status);

    List<MessageTemplate> searchByNameOrDescription(String tenantId, String searchTerm);
}
