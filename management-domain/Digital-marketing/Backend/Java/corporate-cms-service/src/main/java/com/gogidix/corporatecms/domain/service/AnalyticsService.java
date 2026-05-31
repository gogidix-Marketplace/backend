package com.gogidix.corporatecms.domain.service;

import com.gogidix.corporatecms.domain.repository.ContentRepository;
import com.gogidix.corporatecms.domain.repository.JobRepository;
import com.gogidix.corporatecms.domain.repository.LeadRepository;
import com.gogidix.corporatecms.domain.repository.MediaRepository;
import com.gogidix.corporatecms.domain.repository.ProductRepository;
import com.gogidix.corporatecms.domain.repository.UserRepository;
import com.gogidix.corporatecms.domain.repository.WorkflowRepository;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Service for providing analytics data.
 */
@Service
@RequiredArgsConstructor
public class AnalyticsService {
    private static final Logger log = LoggerFactory.getLogger(AnalyticsService.class);

    private final ContentRepository contentRepository;
    private final MediaRepository mediaRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final JobRepository jobRepository;
    private final LeadRepository leadRepository;
    private final WorkflowRepository workflowRepository;

    public AnalyticsDashboard getDashboardData(LocalDateTime startDate, LocalDateTime endDate) {
        log.info("Fetching dashboard data from {} to {}", startDate, endDate);
        return AnalyticsDashboard.builder()
                .contentStats(getContentStats())
                .mediaStats(getMediaStats())
                .userStats(getUserStats())
                .productStats(getProductStats())
                .jobStats(getJobStats())
                .leadStats(getLeadStats(startDate, endDate))
                .workflowStats(getWorkflowStats())
                .recentActivity(getRecentActivity())
                .build();
    }

    public ContentStats getContentStats() {
        long totalContent = contentRepository.count();
        long publishedContent = contentRepository.countPublishedContent();
        long draftContent = contentRepository.countByStatusAndDeletedFalse(
                com.gogidix.corporatecms.domain.enums.ContentStatus.DRAFT);
        return ContentStats.builder()
                .total(totalContent)
                .published(publishedContent)
                .draft(draftContent)
                .pendingReview(contentRepository.countByStatusAndDeletedFalse(
                        com.gogidix.corporatecms.domain.enums.ContentStatus.PENDING_REVIEW))
                .pendingApproval(contentRepository.countByStatusAndDeletedFalse(
                        com.gogidix.corporatecms.domain.enums.ContentStatus.PENDING_APPROVAL))
                .build();
    }

    public MediaStats getMediaStats() {
        long totalMedia = mediaRepository.countTotalMedia();
        long images = mediaRepository.countByMediaTypeAndDeletedFalse(
                com.gogidix.corporatecms.domain.enums.MediaType.IMAGE);
        long videos = mediaRepository.countByMediaTypeAndDeletedFalse(
                com.gogidix.corporatecms.domain.enums.MediaType.VIDEO);
        long documents = mediaRepository.countByMediaTypeAndDeletedFalse(
                com.gogidix.corporatecms.domain.enums.MediaType.DOCUMENT);
        return MediaStats.builder()
                .total(totalMedia)
                .images(images)
                .videos(videos)
                .documents(documents)
                .build();
    }

    public UserStats getUserStats() {
        long totalUsers = userRepository.count();
        long activeUsers = userRepository.countActiveUsers();
        Map<com.gogidix.corporatecms.domain.enums.UserRole, Long> usersByRole = Map.of(
                com.gogidix.corporatecms.domain.enums.UserRole.ADMIN,
                        userRepository.countByRoleAndDeletedFalse(
                                com.gogidix.corporatecms.domain.enums.UserRole.ADMIN),
                com.gogidix.corporatecms.domain.enums.UserRole.CONTENT_EDITOR,
                        userRepository.countByRoleAndDeletedFalse(
                                com.gogidix.corporatecms.domain.enums.UserRole.CONTENT_EDITOR),
                com.gogidix.corporatecms.domain.enums.UserRole.PRODUCT_MANAGER,
                        userRepository.countByRoleAndDeletedFalse(
                                com.gogidix.corporatecms.domain.enums.UserRole.PRODUCT_MANAGER)
        );
        return UserStats.builder()
                .total(totalUsers)
                .active(activeUsers)
                .byRole(usersByRole)
                .build();
    }

    public ProductStats getProductStats() {
        long total = productRepository.count();
        long published = productRepository.countPublishedProducts();
        return ProductStats.builder()
                .total(total)
                .published(published)
                .draft(total - published)
                .build();
    }

    public JobStats getJobStats() {
        long total = jobRepository.count();
        long open = jobRepository.countOpenJobs();
        return JobStats.builder()
                .total(total)
                .open(open)
                .closed(jobRepository.countByStatusAndDeletedFalse(
                        com.gogidix.corporatecms.domain.enums.JobStatus.CLOSED))
                .draft(jobRepository.countByStatusAndDeletedFalse(
                        com.gogidix.corporatecms.domain.enums.JobStatus.DRAFT))
                .build();
    }

    public LeadStats getLeadStats(LocalDateTime startDate, LocalDateTime endDate) {
        long total = leadRepository.count();
        long newLeads = leadRepository.countNewLeads();
        long converted = leadRepository.countByStatus(
                com.gogidix.corporatecms.domain.enums.LeadStatus.WON);
        return LeadStats.builder()
                .total(total)
                .newLeads(newLeads)
                .qualified(leadRepository.countByStatus(
                        com.gogidix.corporatecms.domain.enums.LeadStatus.QUALIFIED))
                .converted(converted)
                .conversionRate(total > 0 ? (converted * 100.0 / total) : 0.0)
                .build();
    }

    public WorkflowStats getWorkflowStats() {
        long pending = workflowRepository.countByStatus(
                com.gogidix.corporatecms.domain.enums.WorkflowStatus.PENDING);
        long approved = workflowRepository.countByStatus(
                com.gogidix.corporatecms.domain.enums.WorkflowStatus.APPROVED);
        long rejected = workflowRepository.countByStatus(
                com.gogidix.corporatecms.domain.enums.WorkflowStatus.REJECTED);
        return WorkflowStats.builder()
                .pending(pending)
                .approved(approved)
                .rejected(rejected)
                .overdue(workflowRepository.findOverdueWorkflows(LocalDateTime.now()).size())
                .build();
    }

    public List<Map<String, Object>> getRecentActivity() {
        return List.of(
                Map.of("type", "content", "action", "published", "timestamp", LocalDateTime.now().minusHours(2)),
                Map.of("type", "user", "action", "created", "timestamp", LocalDateTime.now().minusHours(4)),
                Map.of("type", "workflow", "action", "approved", "timestamp", LocalDateTime.now().minusHours(6))
        );
    }

    @Data
    @Builder
    public static class AnalyticsDashboard {
        private ContentStats contentStats;
        private MediaStats mediaStats;
        private UserStats userStats;
        private ProductStats productStats;
        private JobStats jobStats;
        private LeadStats leadStats;
        private WorkflowStats workflowStats;
        private List<Map<String, Object>> recentActivity;
    }

    @Data
    @Builder
    public static class ContentStats {
        private long total;
        private long published;
        private long draft;
        private long pendingReview;
        private long pendingApproval;
    }

    @Data
    @Builder
    public static class MediaStats {
        private long total;
        private long images;
        private long videos;
        private long documents;
    }

    @Data
    @Builder
    public static class UserStats {
        private long total;
        private long active;
        private Map<com.gogidix.corporatecms.domain.enums.UserRole, Long> byRole;
    }

    @Data
    @Builder
    public static class ProductStats {
        private long total;
        private long published;
        private long draft;
    }

    @Data
    @Builder
    public static class JobStats {
        private long total;
        private long open;
        private long closed;
        private long draft;
    }

    @Data
    @Builder
    public static class LeadStats {
        private long total;
        private long newLeads;
        private long qualified;
        private long converted;
        private double conversionRate;
    }

    @Data
    @Builder
    public static class WorkflowStats {
        private long pending;
        private long approved;
        private long rejected;
        private long overdue;
    }
}
