package com.gogidix.digitalmarketing.emailmarketing.application.service;

import com.gogidix.digitalmarketing.emailmarketing.domain.model.EmailCampaign;
import com.gogidix.digitalmarketing.emailmarketing.domain.repository.EmailCampaignRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailCampaignService {
    private final EmailCampaignRepository repository;

    public EmailCampaign create(EmailCampaign campaign) { return repository.save(campaign); }
    public EmailCampaign getById(String id) { return repository.findById(id).orElse(null); }
    public List<EmailCampaign> getAll() { return repository.findAll(); }
    public EmailCampaign update(EmailCampaign campaign) { return repository.save(campaign); }
    public void delete(String id) { repository.deleteById(id); }
}
