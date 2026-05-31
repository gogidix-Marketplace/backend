package com.gogidix.customersupport.customerportal.application.service;

import com.gogidix.customersupport.customerportal.application.dto.CustomerProfileRequestDto;
import com.gogidix.customersupport.customerportal.application.dto.CustomerProfileResponseDto;
import com.gogidix.customersupport.customerportal.application.dto.TicketHistoryResponseDto;
import com.gogidix.customersupport.customerportal.application.mapper.CustomerProfileMapper;
import com.gogidix.customersupport.customerportal.application.mapper.TicketHistoryMapper;
import com.gogidix.customersupport.customerportal.domain.model.CustomerProfile;
import com.gogidix.customersupport.customerportal.domain.model.TicketHistory;
import com.gogidix.customersupport.customerportal.domain.repository.CustomerProfileRepository;
import com.gogidix.customersupport.customerportal.domain.repository.TicketHistoryRepository;
import com.gogidix.customersupport.customerportal.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerPortalService {

    private final CustomerProfileRepository customerProfileRepository;
    private final TicketHistoryRepository ticketHistoryRepository;
    private final CustomerProfileMapper customerProfileMapper;
    private final TicketHistoryMapper ticketHistoryMapper;

    public List<CustomerProfileResponseDto> getAllProfiles() {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching all customer profiles for tenant: {}", tenantId);
        return customerProfileRepository.findByTenantId(tenantId).stream()
                .map(customerProfileMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public CustomerProfileResponseDto getProfileByCustomerId(String customerId) {
        log.debug("Fetching customer profile for customer: {}", customerId);
        CustomerProfile profile = customerProfileRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer profile not found for customer: " + customerId));
        return customerProfileMapper.toResponseDto(profile);
    }

    public CustomerProfileResponseDto getProfileById(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching customer profile with id: {} for tenant: {}", id, tenantId);
        CustomerProfile profile = customerProfileRepository.findByTenantIdAndId(tenantId, id)
                .orElseThrow(() -> new IllegalArgumentException("Customer profile not found with id: " + id));
        return customerProfileMapper.toResponseDto(profile);
    }

    @Transactional
    public CustomerProfileResponseDto createProfile(CustomerProfileRequestDto request) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Creating new customer profile for tenant: {}", tenantId);

        if (customerProfileRepository.existsByCustomerId(request.getCustomerId())) {
            throw new IllegalArgumentException("Customer profile already exists for customer: " + request.getCustomerId());
        }

        CustomerProfile profile = customerProfileMapper.toEntity(request, tenantId);
        CustomerProfile saved = customerProfileRepository.save(profile);
        log.info("Created customer profile for customer: {}", saved.getCustomerId());
        return customerProfileMapper.toResponseDto(saved);
    }

    @Transactional
    public CustomerProfileResponseDto updateProfile(String id, CustomerProfileRequestDto request) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Updating customer profile with id: {} for tenant: {}", id, tenantId);

        CustomerProfile existing = customerProfileRepository.findByTenantIdAndId(tenantId, id)
                .orElseThrow(() -> new IllegalArgumentException("Customer profile not found with id: " + id));

        CustomerProfile updated = customerProfileMapper.toEntity(request, tenantId);
        updated.setId(existing.getId());
        updated.setCreatedAt(existing.getCreatedAt());
        updated.setAccountCreatedAt(existing.getAccountCreatedAt());
        updated.setUpdatedAt(java.time.Instant.now());

        CustomerProfile saved = customerProfileRepository.save(updated);
        log.info("Updated customer profile for customer: {}", saved.getCustomerId());
        return customerProfileMapper.toResponseDto(saved);
    }

    @Transactional
    public void deleteProfile(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Deleting customer profile with id: {} for tenant: {}", id, tenantId);

        CustomerProfile profile = customerProfileRepository.findByTenantIdAndId(tenantId, id)
                .orElseThrow(() -> new IllegalArgumentException("Customer profile not found with id: " + id));

        customerProfileRepository.deleteByTenantIdAndId(tenantId, id);
        ticketHistoryRepository.deleteByCustomerId(profile.getCustomerId());

        log.info("Deleted customer profile for customer: {}", profile.getCustomerId());
    }

    public List<TicketHistoryResponseDto> getTicketHistoryByCustomerId(String customerId) {
        log.debug("Fetching ticket history for customer: {}", customerId);
        return ticketHistoryRepository.findByCustomerIdOrderByCreatedAtDesc(customerId).stream()
                .map(ticketHistoryMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public TicketHistoryResponseDto recordTicketHistory(String customerId, String ticketId, String ticketNumber,
                                                         String title, String description) {
        log.debug("Recording ticket history for customer: {}, ticket: {}", customerId, ticketId);

        TicketHistory history = ticketHistoryMapper.toEntity(customerId, ticketId, ticketNumber, title, description);
        TicketHistory saved = ticketHistoryRepository.save(history);

        log.info("Recorded ticket history for ticket: {}", ticketNumber);
        return ticketHistoryMapper.toResponseDto(saved);
    }

    public long getTicketCountByCustomerId(String customerId) {
        return ticketHistoryRepository.countByCustomerId(customerId);
    }

    public long getTicketCountByCustomerIdAndStatus(String customerId, String status) {
        return ticketHistoryRepository.countByCustomerIdAndStatus(customerId, status);
    }
}
