package com.gogidix.customersupport.phonesupport.application.service;

import com.gogidix.customersupport.phonesupport.domain.model.CallQueue;
import com.gogidix.customersupport.phonesupport.domain.model.PhoneCall;
import com.gogidix.customersupport.phonesupport.domain.repository.CallQueueRepository;
import com.gogidix.customersupport.phonesupport.domain.repository.PhoneCallRepository;
import com.gogidix.customersupport.phonesupport.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PhoneSupportService {

    private final PhoneCallRepository phoneCallRepository;
    private final CallQueueRepository callQueueRepository;

    public List<PhoneCall> getAllCalls() {
        String tenantId = RequestContextHolder.getTenantId();
        return phoneCallRepository.findByTenantIdOrderByStartedAtDesc(tenantId);
    }

    public PhoneCall getCallById(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        return phoneCallRepository.findByTenantIdAndId(tenantId, id)
                .orElseThrow(() -> new IllegalArgumentException("Phone call not found with id: " + id));
    }

    public PhoneCall getCallByCallId(String callId) {
        return phoneCallRepository.findByCallId(callId)
                .orElseThrow(() -> new IllegalArgumentException("Phone call not found with callId: " + callId));
    }

    public List<PhoneCall> getCallsByAgent(String agentId) {
        String tenantId = RequestContextHolder.getTenantId();
        return phoneCallRepository.findByTenantIdAndAgentId(tenantId, agentId);
    }

    public List<PhoneCall> getCallsByStatus(PhoneCall.CallStatus callStatus) {
        String tenantId = RequestContextHolder.getTenantId();
        return phoneCallRepository.findByTenantIdAndCallStatus(tenantId, callStatus);
    }

    public List<PhoneCall> getCallsByDateRange(Instant startDate, Instant endDate) {
        String tenantId = RequestContextHolder.getTenantId();
        return phoneCallRepository.findByTenantIdAndStartedAtBetween(tenantId, startDate, endDate);
    }

    public List<PhoneCall> getCallsByPhoneNumber(String phoneNumber) {
        String tenantId = RequestContextHolder.getTenantId();
        return phoneCallRepository.findByTenantIdAndCallerInfoPhoneNumber(tenantId, phoneNumber);
    }

    @Transactional
    public PhoneCall initiateCall(String callId, PhoneCall.CallerInfo callerInfo, PhoneCall.CallDirection direction) {
        String tenantId = RequestContextHolder.getTenantId();
        PhoneCall call = PhoneCall.create(tenantId, callId, callerInfo, direction);
        PhoneCall saved = phoneCallRepository.save(call);
        log.info("Initiated phone call with callId: {}", callId);
        return saved;
    }

    @Transactional
    public PhoneCall answerCall(String callId, String agentId, String agentName) {
        PhoneCall call = phoneCallRepository.findByCallId(callId)
                .orElseThrow(() -> new IllegalArgumentException("Phone call not found with callId: " + callId));

        call.answer(agentId, agentName);
        PhoneCall saved = phoneCallRepository.save(call);
        log.info("Answered phone call with callId: {} by agent: {}", callId, agentId);
        return saved;
    }

    @Transactional
    public PhoneCall endCall(String callId) {
        PhoneCall call = phoneCallRepository.findByCallId(callId)
                .orElseThrow(() -> new IllegalArgumentException("Phone call not found with callId: " + callId));

        call.end();
        PhoneCall saved = phoneCallRepository.save(call);
        log.info("Ended phone call with callId: {}", callId);
        return saved;
    }

    @Transactional
    public PhoneCall updateCallNotes(String callId, String notes) {
        PhoneCall call = phoneCallRepository.findByCallId(callId)
                .orElseThrow(() -> new IllegalArgumentException("Phone call not found with callId: " + callId));

        call.setCallNotes(notes);
        call.updateTimestamp();
        return phoneCallRepository.save(call);
    }

    @Transactional
    public PhoneCall linkToTicket(String callId, String ticketId) {
        PhoneCall call = phoneCallRepository.findByCallId(callId)
                .orElseThrow(() -> new IllegalArgumentException("Phone call not found with callId: " + callId));

        call.setRelatedTicketId(ticketId);
        call.updateTimestamp();
        return phoneCallRepository.save(call);
    }

    public List<CallQueue> getAllQueues() {
        String tenantId = RequestContextHolder.getTenantId();
        return callQueueRepository.findByTenantId(tenantId);
    }

    public List<CallQueue> getActiveQueues() {
        String tenantId = RequestContextHolder.getTenantId();
        return callQueueRepository.findByTenantIdAndIsActiveTrue(tenantId);
    }

    @Transactional
    public CallQueue createQueue(String queueName, String queueId, String description) {
        String tenantId = RequestContextHolder.getTenantId();
        CallQueue queue = CallQueue.create(tenantId, queueName, queueId);
        queue.setDescription(description);
        return callQueueRepository.save(queue);
    }

    @Transactional
    public void deleteCall(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        phoneCallRepository.deleteByTenantIdAndId(tenantId, id);
        log.info("Deleted phone call with id: {}", id);
    }

    public long getActiveCallsByAgent(String agentId) {
        String tenantId = RequestContextHolder.getTenantId();
        return phoneCallRepository.countByTenantIdAndAgentIdAndCallStatus(
                tenantId, agentId, PhoneCall.CallStatus.IN_PROGRESS);
    }
}
