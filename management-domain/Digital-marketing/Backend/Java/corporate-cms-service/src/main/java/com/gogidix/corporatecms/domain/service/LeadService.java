package com.gogidix.corporatecms.domain.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.gogidix.corporatecms.application.dto.LeadDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.gogidix.corporatecms.application.dto.PageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.gogidix.corporatecms.application.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.gogidix.corporatecms.application.mapper.LeadMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.gogidix.corporatecms.domain.enums.LeadStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.gogidix.corporatecms.domain.model.Lead;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.gogidix.corporatecms.domain.repository.LeadRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

/**
 * Service for managing marketing leads.
 */
@Service
@RequiredArgsConstructor
public class LeadService {
    private static final Logger log = LoggerFactory.getLogger(LeadService.class);

    private final LeadRepository leadRepository;
    private final LeadMapper leadMapper;

    private static final int INITIAL_LEAD_SCORE = 10;

    @Transactional
    public LeadDTO createLead(LeadDTO dto) {
        log.info("Creating lead from email: {}", dto.getEmail());

        Lead lead = leadMapper.toEntity(dto);
        lead.setStatus(LeadStatus.NEW);
        lead.setScore(INITIAL_LEAD_SCORE);

        if (dto.getLeadMagnet() != null) {
            lead.updateScore(20);
        }

        if (dto.getCompany() != null) {
            lead.updateScore(10);
        }

        if (dto.getPhone() != null) {
            lead.updateScore(5);
        }

        Lead savedLead = leadRepository.save(lead);
        log.info("Lead created with ID: {}", savedLead.getId());

        return leadMapper.toDto(savedLead);
    }

    @Transactional
    public LeadDTO updateLead(String id, LeadDTO dto) {
        log.info("Updating lead: {}", id);

        Lead lead = leadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lead", "id", id));

        leadMapper.updateEntityFromDto(dto, lead);

        Lead savedLead = leadRepository.save(lead);
        log.info("Lead updated: {}", id);

        return leadMapper.toDto(savedLead);
    }

    public LeadDTO getLeadById(String id) {
        log.info("Fetching lead by ID: {}", id);
        Lead lead = leadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lead", "id", id));
        return leadMapper.toDto(lead);
    }

    public PageResponse<LeadDTO> getLeadsByStatus(LeadStatus status, int page, int size) {
        log.info("Fetching leads by status: {}", status);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Lead> leadPage = leadRepository.findByStatus(status, pageable);
        return PageResponse.of(leadPage.map(leadMapper::toDto));
    }

    public PageResponse<LeadDTO> getLeadsByAssignedUser(String assignedTo, int page, int size) {
        log.info("Fetching leads assigned to: {}", assignedTo);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Lead> leadPage = leadRepository.findByAssignedTo(assignedTo, pageable);
        return PageResponse.of(leadPage.map(leadMapper::toDto));
    }

    public PageResponse<LeadDTO> searchLeads(String keyword, int page, int size) {
        log.info("Searching leads with keyword: {}", keyword);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Lead> leadPage = leadRepository.searchByKeyword(keyword, pageable);
        return PageResponse.of(leadPage.map(leadMapper::toDto));
    }

    @Transactional
    public LeadDTO updateStatus(String id, LeadStatus newStatus, String userId) {
        log.info("Updating lead status: {} -> {}", id, newStatus);

        Lead lead = leadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lead", "id", id));

        lead.setStatus(newStatus);

        if (newStatus == LeadStatus.WON) {
            lead.setConvertedAt(LocalDateTime.now());
            lead.setConvertedBy(userId);
            lead.updateScore(50);
        }

        if (newStatus == LeadStatus.QUALIFIED) {
            lead.updateScore(20);
        }

        Lead savedLead = leadRepository.save(lead);
        log.info("Lead status updated: {} -> {}", id, newStatus);

        return leadMapper.toDto(savedLead);
    }

    @Transactional
    public LeadDTO assignLead(String id, String assignedTo, String assignedByName) {
        log.info("Assigning lead {} to: {}", id, assignedTo);

        Lead lead = leadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lead", "id", id));

        lead.setAssignedTo(assignedTo);
        lead.setAssignedToName(assignedByName);
        lead.updateScore(10);

        Lead savedLead = leadRepository.save(lead);
        log.info("Lead assigned: {} -> {}", id, assignedTo);

        return leadMapper.toDto(savedLead);
    }

    @Transactional
    public LeadDTO updateScore(String id, Integer delta) {
        log.info("Updating lead {} score by: {}", id, delta);

        Lead lead = leadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lead", "id", id));

        lead.updateScore(delta);

        Lead savedLead = leadRepository.save(lead);
        log.info("Lead score updated: {} -> {}", id, savedLead.getScore());

        return leadMapper.toDto(savedLead);
    }

    public List<LeadDTO> getNewLeads() {
        List<Lead> leads = leadRepository.findByStatus(LeadStatus.NEW);
        return leadMapper.toDtoList(leads);
    }

    public List<LeadDTO> getConvertedLeads(LocalDateTime startDate, LocalDateTime endDate) {
        List<Lead> leads = leadRepository.findConvertedBetween(startDate, endDate);
        return leadMapper.toDtoList(leads);
    }

    public Long countLeadsByStatus(LeadStatus status) {
        return leadRepository.countByStatus(status);
    }

    public Long countNewLeads() {
        return leadRepository.countNewLeads();
    }
}
