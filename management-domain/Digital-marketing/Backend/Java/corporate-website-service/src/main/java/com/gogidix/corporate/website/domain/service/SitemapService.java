package com.gogidix.corporate.website.domain.service;

import com.gogidix.corporate.website.application.dto.SitemapEntry;
import com.gogidix.corporate.website.domain.model.BlogPost;
import com.gogidix.corporate.website.domain.model.CaseStudy;
import com.gogidix.corporate.website.domain.model.Job;
import com.gogidix.corporate.website.domain.model.Page;
import com.gogidix.corporate.website.domain.model.PressRelease;
import com.gogidix.corporate.website.domain.model.Product;
import com.gogidix.corporate.website.domain.model.Region;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SitemapService {

    @Value("${application.sitemap.base-url:https://gogidix.com}")
    private String baseUrl;

    @Value("${application.sitemap.changefreq:weekly}")
    private String defaultChangeFreq;

    @Value("${application.sitemap.priority:0.8}")
    private double defaultPriority;

    private final PageDomainService pageDomainService;
    private final BlogPostDomainService blogPostDomainService;
    private final PressReleaseDomainService pressReleaseDomainService;
    private final ProductDomainService productDomainService;
    private final JobDomainService jobDomainService;
    private final CaseStudyDomainService caseStudyDomainService;

    @Cacheable(value = "sitemaps", key = "#region.name()")
    public List<SitemapEntry> generateSitemap(Region region) {
        List<SitemapEntry> entries = new ArrayList<>();

        // Add static pages
        entries.add(SitemapEntry.builder()
                .url(baseUrl + "/")
                .lastModified(LocalDateTime.now())
                .changeFrequency("daily")
                .priority(1.0)
                .build());

        // Add content pages
        List<Page> pages = pageDomainService.getPublishedPagesByRegion(region);
        for (Page page : pages) {
            if (page.getPath() != null && !page.getPath().isBlank()) {
                entries.add(SitemapEntry.builder()
                        .url(baseUrl + page.getPath())
                        .lastModified(page.getUpdatedAt() != null ? page.getUpdatedAt() : page.getCreatedAt())
                        .changeFrequency(defaultChangeFreq)
                        .priority(0.9)
                        .build());
            }
        }

        // Add blog posts
        List<BlogPost> blogPosts = blogPostDomainService.getPublishedPostsByRegion(region);
        for (BlogPost post : blogPosts) {
            if (post.getSlug() != null) {
                entries.add(SitemapEntry.builder()
                        .url(baseUrl + "/blog/" + post.getSlug())
                        .lastModified(post.getUpdatedAt() != null ? post.getUpdatedAt() : post.getCreatedAt())
                        .changeFrequency("monthly")
                        .priority(0.7)
                        .build());
            }
        }

        // Add press releases
        List<PressRelease> pressReleases = pressReleaseDomainService.getPublishedReleasesByRegion(region);
        for (PressRelease release : pressReleases) {
            if (release.getSlug() != null) {
                entries.add(SitemapEntry.builder()
                        .url(baseUrl + "/press/" + release.getSlug())
                        .lastModified(release.getUpdatedAt() != null ? release.getUpdatedAt() : release.getCreatedAt())
                        .changeFrequency("monthly")
                        .priority(0.6)
                        .build());
            }
        }

        // Add products
        List<Product> products = productDomainService.getPublishedProductsByRegion(region);
        for (Product product : products) {
            if (product.getSlug() != null) {
                entries.add(SitemapEntry.builder()
                        .url(baseUrl + "/products/" + product.getSlug())
                        .lastModified(product.getUpdatedAt() != null ? product.getUpdatedAt() : product.getCreatedAt())
                        .changeFrequency(defaultChangeFreq)
                        .priority(0.8)
                        .build());
            }
        }

        // Add jobs
        List<Job> jobs = jobDomainService.getOpenJobsByRegion(region);
        for (Job job : jobs) {
            if (job.getSlug() != null) {
                entries.add(SitemapEntry.builder()
                        .url(baseUrl + "/careers/" + job.getSlug())
                        .lastModified(job.getCreatedAt())
                        .changeFrequency("weekly")
                        .priority(0.6)
                        .build());
            }
        }

        // Add case studies
        List<CaseStudy> caseStudies = caseStudyDomainService.getPublishedStudiesByRegion(region);
        for (CaseStudy study : caseStudies) {
            if (study.getSlug() != null) {
                entries.add(SitemapEntry.builder()
                        .url(baseUrl + "/case-studies/" + study.getSlug())
                        .lastModified(study.getUpdatedAt() != null ? study.getUpdatedAt() : study.getCreatedAt())
                        .changeFrequency("monthly")
                        .priority(0.7)
                        .build());
            }
        }

        log.info("Generated sitemap for region {} with {} entries", region, entries.size());
        return entries;
    }

    public String generateSitemapXml(Region region) {
        List<SitemapEntry> entries = generateSitemap(region);
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n");

        for (SitemapEntry entry : entries) {
            xml.append("  <url>\n");
            xml.append("    <loc>").append(escapeXml(entry.getUrl())).append("</loc>\n");
            if (entry.getLastModified() != null) {
                xml.append("    <lastmod>").append(entry.getLastModified().toLocalDate()).append("</lastmod>\n");
            }
            if (entry.getChangeFrequency() != null) {
                xml.append("    <changefreq>").append(entry.getChangeFrequency()).append("</changefreq>\n");
            }
            if (entry.getPriority() != null) {
                xml.append("    <priority>").append(entry.getPriority()).append("</priority>\n");
            }
            xml.append("  </url>\n");
        }

        xml.append("</urlset>");
        return xml.toString();
    }

    private String escapeXml(String input) {
        return input.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }
}
