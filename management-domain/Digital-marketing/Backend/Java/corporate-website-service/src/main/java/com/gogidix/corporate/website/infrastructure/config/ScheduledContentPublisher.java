package com.gogidix.corporate.website.infrastructure.config;

import com.gogidix.corporate.website.domain.model.BlogPost;
import com.gogidix.corporate.website.domain.model.CaseStudy;
import com.gogidix.corporate.website.domain.model.ContentStatus;
import com.gogidix.corporate.website.domain.model.Job;
import com.gogidix.corporate.website.domain.model.Page;
import com.gogidix.corporate.website.domain.model.PressRelease;
import com.gogidix.corporate.website.domain.service.BlogPostDomainService;
import com.gogidix.corporate.website.domain.service.CaseStudyDomainService;
import com.gogidix.corporate.website.domain.service.JobDomainService;
import com.gogidix.corporate.website.domain.service.PageDomainService;
import com.gogidix.corporate.website.domain.service.PressReleaseDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ScheduledContentPublisher {

    private final PageDomainService pageDomainService;
    private final BlogPostDomainService blogPostDomainService;
    private final PressReleaseDomainService pressReleaseDomainService;
    private final JobDomainService jobDomainService;
    private final CaseStudyDomainService caseStudyDomainService;

    @Scheduled(fixedRate = 300000) // Run every 5 minutes
    @CacheEvict(value = {"pages", "blogPosts", "pressReleases", "jobs", "caseStudies"}, allEntries = true)
    public void publishScheduledContent() {
        log.debug("Checking for scheduled content to publish...");

        LocalDateTime now = LocalDateTime.now();

        // Publish scheduled pages
        List<Page> pagesToPublish = pageDomainService.getScheduledPages();
        for (Page page : pagesToPublish) {
            if (page.getPublishDate() != null && !page.getPublishDate().isAfter(now)) {
                try {
                    pageDomainService.publishPage(page.getId());
                    log.info("Published scheduled page: {}", page.getPageKey());
                } catch (Exception e) {
                    log.error("Failed to publish page: {}", page.getId(), e);
                }
            }
        }

        // Unpublish expired pages
        List<Page> pagesToUnpublish = pageDomainService.getPagesToUnpublish();
        for (Page page : pagesToUnpublish) {
            try {
                pageDomainService.unpublishPage(page.getId());
                log.info("Unpublished expired page: {}", page.getPageKey());
            } catch (Exception e) {
                log.error("Failed to unpublish page: {}", page.getId(), e);
            }
        }

        // Publish scheduled blog posts
        List<BlogPost> blogPostsToPublish = blogPostDomainService.getScheduledPosts();
        for (BlogPost post : blogPostsToPublish) {
            if (post.getPublishDate() != null && !post.getPublishDate().isAfter(now)) {
                try {
                    blogPostDomainService.publishBlogPost(post.getId());
                    log.info("Published scheduled blog post: {}", post.getSlug());
                } catch (Exception e) {
                    log.error("Failed to publish blog post: {}", post.getId(), e);
                }
            }
        }

        // Publish scheduled press releases
        List<PressRelease> releasesToPublish = pressReleaseDomainService.getScheduledReleases();
        for (PressRelease release : releasesToPublish) {
            if (release.getPublishDate() != null && !release.getPublishDate().isAfter(now)) {
                try {
                    pressReleaseDomainService.publishPressRelease(release.getId());
                    log.info("Published scheduled press release: {}", release.getSlug());
                } catch (Exception e) {
                    log.error("Failed to publish press release: {}", release.getId(), e);
                }
            }
        }

        // Publish scheduled jobs
        List<Job> jobsToPublish = jobDomainService.getScheduledJobs();
        for (Job job : jobsToPublish) {
            if (job.getPublishDate() != null && !job.getPublishDate().isAfter(now)) {
                try {
                    jobDomainService.publishJob(job.getId());
                    log.info("Published scheduled job: {}", job.getJobKey());
                } catch (Exception e) {
                    log.error("Failed to publish job: {}", job.getId(), e);
                }
            }
        }

        // Publish scheduled case studies
        List<CaseStudy> studiesToPublish = caseStudyDomainService.getScheduledStudies();
        for (CaseStudy study : studiesToPublish) {
            if (study.getPublishDate() != null && !study.getPublishDate().isAfter(now)) {
                try {
                    caseStudyDomainService.publishCaseStudy(study.getId());
                    log.info("Published scheduled case study: {}", study.getSlug());
                } catch (Exception e) {
                    log.error("Failed to publish case study: {}", study.getId(), e);
                }
            }
        }
    }

    @Scheduled(cron = "0 0 * * * *") // Run every hour
    @CacheEvict(value = {"sitemaps"}, allEntries = true)
    public void clearSitemapCache() {
        log.debug("Clearing sitemap cache");
    }
}
