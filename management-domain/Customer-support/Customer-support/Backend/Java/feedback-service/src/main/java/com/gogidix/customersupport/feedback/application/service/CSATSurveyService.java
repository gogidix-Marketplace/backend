package com.gogidix.customersupport.feedback.application.service;

import com.gogidix.customersupport.feedback.application.dto.request.CSATSurveyRequestDto;
import com.gogidix.customersupport.feedback.application.dto.response.CSATSurveyResponseDto;
import com.gogidix.customersupport.feedback.application.mapper.FeedbackMapper;
import com.gogidix.customersupport.feedback.domain.model.CSATSurvey;
import com.gogidix.customersupport.feedback.domain.repository.CSATSurveyRepository;
import com.gogidix.customersupport.feedback.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Application service for CSAT Survey management
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CSATSurveyService {

    private final CSATSurveyRepository surveyRepository;
    private final FeedbackMapper feedbackMapper;

    public Page<CSATSurveyResponseDto> getAllSurveys(Pageable pageable) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching all CSAT surveys for tenant: {}", tenantId);
        return surveyRepository.findByTenantId(tenantId, pageable)
                .map(feedbackMapper::toResponseDto);
    }

    public CSATSurveyResponseDto getSurveyById(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching CSAT survey with id: {} for tenant: {}", id, tenantId);
        CSATSurvey survey = surveyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("CSAT Survey not found with id: " + id));
        return feedbackMapper.toResponseDto(survey);
    }

    public CSATSurveyResponseDto getSurveyBySurveyId(String surveyId) {
        log.debug("Fetching CSAT survey with surveyId: {}", surveyId);
        CSATSurvey survey = surveyRepository.findBySurveyId(surveyId)
                .orElseThrow(() -> new IllegalArgumentException("CSAT Survey not found with surveyId: " + surveyId));
        return feedbackMapper.toResponseDto(survey);
    }

    public List<CSATSurveyResponseDto> getSurveysByStatus(CSATSurvey.SurveyStatus status) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching CSAT surveys with status: {} for tenant: {}", status, tenantId);
        return surveyRepository.findByTenantIdAndStatus(tenantId, status).stream()
                .map(feedbackMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<CSATSurveyResponseDto> getActiveSurveys() {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching active CSAT surveys for tenant: {}", tenantId);
        return surveyRepository.findActiveSurveysForEvent(tenantId, java.time.Instant.now()).stream()
                .map(feedbackMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<CSATSurveyResponseDto> getSurveysByLocale(String locale) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching CSAT surveys for locale: {} and tenant: {}", locale, tenantId);
        return surveyRepository.findByTenantIdAndLocale(tenantId, locale).stream()
                .map(feedbackMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public CSATSurveyResponseDto createSurvey(CSATSurveyRequestDto request) {
        String tenantId = RequestContextHolder.getTenantId();
        String userId = RequestContextHolder.getUserId().orElse("system");
        log.debug("Creating new CSAT survey for tenant: {}", tenantId);

        CSATSurvey survey = feedbackMapper.toEntity(request, tenantId, userId);

        CSATSurvey saved = surveyRepository.save(survey);

        log.info("Created CSAT survey with surveyId: {} for tenant: {}", saved.getSurveyId(), tenantId);
        return feedbackMapper.toResponseDto(saved);
    }

    @Transactional
    public CSATSurveyResponseDto updateSurvey(String id, CSATSurveyRequestDto request) {
        String tenantId = RequestContextHolder.getTenantId();
        String userId = RequestContextHolder.getUserId().orElse("system");
        log.debug("Updating CSAT survey with id: {} for tenant: {}", id, tenantId);

        CSATSurvey survey = surveyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("CSAT Survey not found with id: " + id));

        survey.setName(request.getName());
        survey.setDescription(request.getDescription());
        survey.setQuestions(request.getQuestions());
        survey.setRatingScale(request.getRatingScale());
        survey.setTriggerEvents(request.getTriggerEvents());
        survey.setTriggerDelayHours(request.getTriggerDelayHours());
        survey.setLocale(request.getLocale());
        survey.setActiveFrom(request.getActiveFrom());
        survey.setActiveUntil(request.getActiveUntil());
        survey.setMaxResponses(request.getMaxResponses());
        survey.setModifiedBy(userId);
        survey.updateTimestamp();

        CSATSurvey updated = surveyRepository.save(survey);
        log.info("Updated CSAT survey with surveyId: {} for tenant: {}", updated.getSurveyId(), tenantId);
        return feedbackMapper.toResponseDto(updated);
    }

    @Transactional
    public CSATSurveyResponseDto activateSurvey(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Activating CSAT survey: {} for tenant: {}", id, tenantId);

        CSATSurvey survey = surveyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("CSAT Survey not found with id: " + id));

        survey.activate();

        CSATSurvey updated = surveyRepository.save(survey);
        log.info("Activated CSAT survey: {}", updated.getSurveyId());
        return feedbackMapper.toResponseDto(updated);
    }

    @Transactional
    public CSATSurveyResponseDto pauseSurvey(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Pausing CSAT survey: {} for tenant: {}", id, tenantId);

        CSATSurvey survey = surveyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("CSAT Survey not found with id: " + id));

        survey.pause();

        CSATSurvey updated = surveyRepository.save(survey);
        log.info("Paused CSAT survey: {}", updated.getSurveyId());
        return feedbackMapper.toResponseDto(updated);
    }

    @Transactional
    public CSATSurveyResponseDto closeSurvey(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Closing CSAT survey: {} for tenant: {}", id, tenantId);

        CSATSurvey survey = surveyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("CSAT Survey not found with id: " + id));

        survey.close();

        CSATSurvey updated = surveyRepository.save(survey);
        log.info("Closed CSAT survey: {}", updated.getSurveyId());
        return feedbackMapper.toResponseDto(updated);
    }

    @Transactional
    public void incrementResponseCount(String surveyId) {
        log.debug("Incrementing response count for survey: {}", surveyId);

        CSATSurvey survey = surveyRepository.findBySurveyId(surveyId)
                .orElseThrow(() -> new IllegalArgumentException("CSAT Survey not found with surveyId: " + surveyId));

        survey.incrementResponseCount();

        surveyRepository.save(survey);
        log.info("Incremented response count for survey: {}", surveyId);
    }

    @Transactional
    public void deleteSurvey(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Deleting CSAT survey with id: {} for tenant: {}", id, tenantId);

        CSATSurvey survey = surveyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("CSAT Survey not found with id: " + id));

        surveyRepository.delete(survey);

        log.info("Deleted CSAT survey: {} for tenant: {}", survey.getSurveyId(), tenantId);
    }
}
