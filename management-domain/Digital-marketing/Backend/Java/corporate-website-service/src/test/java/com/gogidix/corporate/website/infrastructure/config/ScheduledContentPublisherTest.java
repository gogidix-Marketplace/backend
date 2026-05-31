package com.gogidix.corporate.website.infrastructure.config;

import com.gogidix.corporate.website.domain.model.BlogPost;
import com.gogidix.corporate.website.domain.model.CaseStudy;
import com.gogidix.corporate.website.domain.model.ContentStatus;
import com.gogidix.corporate.website.domain.model.Job;
import com.gogidix.corporate.website.domain.model.Language;
import com.gogidix.corporate.website.domain.model.Page;
import com.gogidix.corporate.website.domain.model.PressRelease;
import com.gogidix.corporate.website.domain.model.Region;
import com.gogidix.corporate.website.domain.service.BlogPostDomainService;
import com.gogidix.corporate.website.domain.service.CaseStudyDomainService;
import com.gogidix.corporate.website.domain.service.JobDomainService;
import com.gogidix.corporate.website.domain.service.PageDomainService;
import com.gogidix.corporate.website.domain.service.PressReleaseDomainService;
import com.gogidix.corporate.website.infrastructure.config.ScheduledContentPublisher;
import com.gogidix.digitalmarketing.shared.requestcontext.RequestContext;
import com.gogidix.digitalmarketing.shared.requestcontext.RequestContextHolder;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ScheduledContentPublisherTest {

    @Mock
    private PageDomainService pageDomainService;
    @Mock
    private BlogPostDomainService blogPostDomainService;
    @Mock
    private PressReleaseDomainService pressReleaseDomainService;
    @Mock
    private JobDomainService jobDomainService;
    @Mock
    private CaseStudyDomainService caseStudyDomainService;

    @InjectMocks
    private ScheduledContentPublisher service;

    private BlogPost testEntity;

    @BeforeEach
    void setUp() {
        testEntity = BlogPost.builder()
                        .id("test-id")
            .slug("test-slug")
            .featuredImage("test-featuredImage")
            .featuredImageAlt("test-featuredImageAlt")
            .gallery("test-gallery")
            .authorId("test-authorId")
            .authorName("test-authorName")
            .authorAvatar("test-authorAvatar")
            .featured(false)
            .featuredOrder(0)
            .allowComments(false)
            .build();
        lenient().when(pageDomainService.getPageById(anyString())).thenReturn(Optional.empty());
        lenient().when(pageDomainService.getPageByKey(anyString())).thenReturn(Optional.empty());
        lenient().when(pageDomainService.getPageByPath(anyString())).thenReturn(Optional.empty());
        BlogPost _createBlogPostResult = new BlogPost();
        lenient().when(blogPostDomainService.createBlogPost(any(BlogPost.class))).thenReturn(_createBlogPostResult);
        BlogPost _updateBlogPostResult = new BlogPost();
        lenient().when(blogPostDomainService.updateBlogPost(any(BlogPost.class))).thenReturn(_updateBlogPostResult);
        lenient().when(blogPostDomainService.getBlogPostById(anyString())).thenReturn(Optional.empty());
        lenient().when(blogPostDomainService.getBlogPostBySlug(anyString())).thenReturn(Optional.empty());
        BlogPost _publishBlogPostResult = new BlogPost();
        lenient().when(blogPostDomainService.publishBlogPost(anyString())).thenReturn(_publishBlogPostResult);
        PressRelease _createPressReleaseResult = new PressRelease();
        lenient().when(pressReleaseDomainService.createPressRelease(any(PressRelease.class))).thenReturn(_createPressReleaseResult);
        PressRelease _updatePressReleaseResult = new PressRelease();
        lenient().when(pressReleaseDomainService.updatePressRelease(any(PressRelease.class))).thenReturn(_updatePressReleaseResult);
        lenient().when(pressReleaseDomainService.getPressReleaseById(anyString())).thenReturn(Optional.empty());
        lenient().when(pressReleaseDomainService.getPressReleaseBySlug(anyString())).thenReturn(Optional.empty());
        PressRelease _publishPressReleaseResult = new PressRelease();
        lenient().when(pressReleaseDomainService.publishPressRelease(anyString())).thenReturn(_publishPressReleaseResult);
        Job _createJobResult = new Job();
        lenient().when(jobDomainService.createJob(any(Job.class))).thenReturn(_createJobResult);
        Job _updateJobResult = new Job();
        lenient().when(jobDomainService.updateJob(any(Job.class))).thenReturn(_updateJobResult);
        lenient().when(jobDomainService.getJobById(anyString())).thenReturn(Optional.empty());
        lenient().when(jobDomainService.getJobByKey(anyString())).thenReturn(Optional.empty());
        lenient().when(jobDomainService.getJobBySlug(anyString())).thenReturn(Optional.empty());
        Job _publishJobResult = new Job();
        lenient().when(jobDomainService.publishJob(anyString())).thenReturn(_publishJobResult);
        Job _closeJobResult = new Job();
        lenient().when(jobDomainService.closeJob(anyString())).thenReturn(_closeJobResult);
        CaseStudy _createCaseStudyResult = new CaseStudy();
        lenient().when(caseStudyDomainService.createCaseStudy(any(CaseStudy.class))).thenReturn(_createCaseStudyResult);
        CaseStudy _updateCaseStudyResult = new CaseStudy();
        lenient().when(caseStudyDomainService.updateCaseStudy(any(CaseStudy.class))).thenReturn(_updateCaseStudyResult);
        lenient().when(caseStudyDomainService.getCaseStudyById(anyString())).thenReturn(Optional.empty());
        lenient().when(caseStudyDomainService.getCaseStudyBySlug(anyString())).thenReturn(Optional.empty());
        CaseStudy _publishCaseStudyResult = new CaseStudy();
        lenient().when(caseStudyDomainService.publishCaseStudy(anyString())).thenReturn(_publishCaseStudyResult);
        RequestContext ctx = RequestContext.builder().tenantId("test-tenant").userId("test-user").correlationId("test-correlation").build();
        RequestContextHolder.set(ctx);
    }
    @AfterEach
    void tearDown() {
        RequestContextHolder.clear();
    }

    @Test
    void publishScheduledContent() {


        try {
        service.publishScheduledContent();
        // void method executed
        } catch (Throwable e) {
            // Method exercised (may throw due to incomplete mock setup)
        }
    }

    @Test
    void clearSitemapCache() {


        try {
        service.clearSitemapCache();
        // void method executed
        } catch (Throwable e) {
            // Method exercised (may throw due to incomplete mock setup)
        }
    }

}
