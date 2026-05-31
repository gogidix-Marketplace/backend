package com.gogidix.globalbusinessmanagement.businessintelligence.application.service;

import com.gogidix.globalbusinessmanagement.businessintelligence.domain.model.BIReport;
import com.gogidix.globalbusinessmanagement.businessintelligence.domain.model.Insight;
import com.gogidix.globalbusinessmanagement.businessintelligence.domain.model.Forecast;
import com.gogidix.globalbusinessmanagement.businessintelligence.domain.model.TrendAnalysis;
import com.gogidix.globalbusinessmanagement.businessintelligence.domain.repository.BIReportRepository;
import com.gogidix.globalbusinessmanagement.businessintelligence.domain.repository.InsightRepository;
import com.gogidix.globalbusinessmanagement.businessintelligence.domain.repository.ForecastRepository;
import com.gogidix.globalbusinessmanagement.businessintelligence.domain.repository.TrendAnalysisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BusinessIntelligenceService {

    private final BIReportRepository reportRepository;
    private final InsightRepository insightRepository;
    private final ForecastRepository forecastRepository;
    private final TrendAnalysisRepository trendAnalysisRepository;

    public BIReport createReport(BIReport report) { return reportRepository.save(report); }
    public BIReport getReport(String id) { return reportRepository.findById(id).orElse(null); }
    public List<BIReport> getReportsByTenant(String tenantId) { return reportRepository.findByTenantId(tenantId); }
    public void deleteReport(String id) { reportRepository.deleteById(id); }

    public Insight createInsight(Insight insight) { return insightRepository.save(insight); }
    public Insight getInsight(String id) { return insightRepository.findById(id).orElse(null); }
    public List<Insight> getInsightsByTenant(String tenantId) { return insightRepository.findByTenantId(tenantId); }

    public Forecast createForecast(Forecast forecast) { return forecastRepository.save(forecast); }
    public Forecast getForecast(String id) { return forecastRepository.findById(id).orElse(null); }
    public List<Forecast> getForecastsByTenant(String tenantId) { return forecastRepository.findByTenantId(tenantId); }

    public TrendAnalysis createTrendAnalysis(TrendAnalysis analysis) { return trendAnalysisRepository.save(analysis); }
    public TrendAnalysis getTrendAnalysis(String id) { return trendAnalysisRepository.findById(id).orElse(null); }
    public List<TrendAnalysis> getTrendAnalysesByTenant(String tenantId) { return trendAnalysisRepository.findByTenantId(tenantId); }
    public List<TrendAnalysis> getTrendAnalysesByMetric(String metric) { return trendAnalysisRepository.findByMetric(metric); }
}
