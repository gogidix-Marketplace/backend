package com.gogidix.finance.reporting.infrastructure.persistence.mongo;

import com.gogidix.finance.reporting.domain.model.Report;
import com.gogidix.finance.reporting.domain.event.ReportGeneratedEvent;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * MongoDB document entity for storing Report domain model.
 * This is the persistence layer representation optimized for MongoDB storage.
 */
@Document(collection = "reports")
public class ReportEntity {

    @Id
    private String id;

    @Indexed
    @Field("report_id")
    private String reportId;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

    @Field("name")
    private String name;

    @Indexed
    @Field("report_type")
    private String reportType;

    @Field("format")
    private String format;

    @Indexed
    @Field("status")
    private String status;

    @Field("report_date")
    private LocalDate reportDate;

    @Field("period_start")
    private LocalDate periodStart;

    @Field("period_end")
    private LocalDate periodEnd;

    @Field("generated_by")
    private String generatedBy;

    @Field("file_url")
    private String fileUrl;

    @Field("file_size")
    private Long fileSize;

    @Field("record_count")
    private Integer recordCount;

    @Field("parameters")
    private Map<String, Object> parameters;

    @Field("columns")
    private List<ReportColumnEmbed> columns;

    @Field("filters")
    private List<ReportFilterEmbed> filters;

    @Field("error_message")
    private String errorMessage;

    @Field("progress_percentage")
    private Integer progressPercentage;

    @Field("started_at")
    private Instant startedAt;

    @Field("completed_at")
    private Instant completedAt;

    @Field("processing_time_ms")
    private Long processingTimeMs;

    @Field("schedule_id")
    private String scheduleId;

    @Field("is_scheduled")
    private Boolean isScheduled;

    @Field("recipient_emails")
    private List<String> recipientEmails;

    @Field("domain_events")
    private List<ReportGeneratedEvent> domainEvents;

    // Default constructor for MongoDB
    public ReportEntity() {
    }

    // Constructor from domain model
    public ReportEntity(Report report) {
        this.reportId = report.getReportId();
        this.tenantId = report.getTenantId();
        this.name = report.getName();
        this.reportType = report.getReportType() != null ? report.getReportType().name() : null;
        this.format = report.getFormat() != null ? report.getFormat().name() : null;
        this.status = report.getStatus() != null ? report.getStatus().name() : null;
        this.reportDate = report.getReportDate();
        this.periodStart = report.getPeriodStart();
        this.periodEnd = report.getPeriodEnd();
        this.generatedBy = report.getGeneratedBy();
        this.fileUrl = report.getFileUrl();
        this.fileSize = report.getFileSize();
        this.recordCount = report.getRecordCount();
        this.parameters = report.getParameters();
        this.errorMessage = report.getErrorMessage();
        this.progressPercentage = report.getProgressPercentage();
        this.startedAt = report.getStartedAt();
        this.completedAt = report.getCompletedAt();
        this.processingTimeMs = report.getProcessingTimeMs();
        this.scheduleId = report.getScheduleId();
        this.isScheduled = report.getIsScheduled();

        // Convert columns
        if (report.getColumns() != null) {
            this.columns = new ArrayList<>();
            for (Report.ReportColumn column : report.getColumns()) {
                this.columns.add(new ReportColumnEmbed(column));
            }
        }

        // Convert filters
        if (report.getFilters() != null) {
            this.filters = new ArrayList<>();
            for (Report.ReportFilter filter : report.getFilters()) {
                this.filters.add(new ReportFilterEmbed(filter));
            }
        }

        this.recipientEmails = report.getRecipientEmails() != null ? new ArrayList<>(report.getRecipientEmails()) : new ArrayList<>();
        this.domainEvents = report.getDomainEvents() != null ? new ArrayList<>(report.getDomainEvents()) : new ArrayList<>();
    }

    // Convert to domain model
    public Report toDomainModel() {
        List<Report.ReportColumn> columnList = new ArrayList<>();
        if (this.columns != null) {
            for (ReportColumnEmbed embed : this.columns) {
                columnList.add(embed.toDomainModel());
            }
        }

        List<Report.ReportFilter> filterList = new ArrayList<>();
        if (this.filters != null) {
            for (ReportFilterEmbed embed : this.filters) {
                filterList.add(embed.toDomainModel());
            }
        }

        return Report.builder()
                .reportId(this.reportId)
                .tenantId(this.tenantId)
                .name(this.name)
                .reportType(this.reportType != null ? Report.ReportType.valueOf(this.reportType) : null)
                .format(this.format != null ? Report.ReportFormat.valueOf(this.format) : null)
                .status(this.status != null ? Report.ReportStatus.valueOf(this.status) : null)
                .reportDate(this.reportDate)
                .periodStart(this.periodStart)
                .periodEnd(this.periodEnd)
                .generatedBy(this.generatedBy)
                .fileUrl(this.fileUrl)
                .fileSize(this.fileSize)
                .recordCount(this.recordCount)
                .parameters(this.parameters)
                .columns(columnList)
                .filters(filterList)
                .errorMessage(this.errorMessage)
                .progressPercentage(this.progressPercentage)
                .startedAt(this.startedAt)
                .completedAt(this.completedAt)
                .processingTimeMs(this.processingTimeMs)
                .scheduleId(this.scheduleId)
                .isScheduled(this.isScheduled)
                .recipientEmails(this.recipientEmails != null ? new ArrayList<>(this.recipientEmails) : new ArrayList<>())
                .domainEvents(this.domainEvents != null ? new ArrayList<>(this.domainEvents) : new ArrayList<>())
                .build();
    }

    // Update from domain model (for partial updates)
    public void updateFrom(Report report) {
        this.name = report.getName();
        this.reportType = report.getReportType() != null ? report.getReportType().name() : null;
        this.format = report.getFormat() != null ? report.getFormat().name() : null;
        this.status = report.getStatus() != null ? report.getStatus().name() : null;
        this.reportDate = report.getReportDate();
        this.periodStart = report.getPeriodStart();
        this.periodEnd = report.getPeriodEnd();
        this.generatedBy = report.getGeneratedBy();
        this.fileUrl = report.getFileUrl();
        this.fileSize = report.getFileSize();
        this.recordCount = report.getRecordCount();
        this.parameters = report.getParameters();
        this.errorMessage = report.getErrorMessage();
        this.progressPercentage = report.getProgressPercentage();
        this.startedAt = report.getStartedAt();
        this.completedAt = report.getCompletedAt();
        this.processingTimeMs = report.getProcessingTimeMs();
        this.scheduleId = report.getScheduleId();
        this.isScheduled = report.getIsScheduled();

        // Update columns
        if (report.getColumns() != null) {
            this.columns = new ArrayList<>();
            for (Report.ReportColumn column : report.getColumns()) {
                this.columns.add(new ReportColumnEmbed(column));
            }
        }

        // Update filters
        if (report.getFilters() != null) {
            this.filters = new ArrayList<>();
            for (Report.ReportFilter filter : report.getFilters()) {
                this.filters.add(new ReportFilterEmbed(filter));
            }
        }

        this.recipientEmails = report.getRecipientEmails() != null ? new ArrayList<>(report.getRecipientEmails()) : new ArrayList<>();
        this.domainEvents = report.getDomainEvents() != null ? new ArrayList<>(report.getDomainEvents()) : new ArrayList<>();
    }

    // Embedded class for columns
    public static class ReportColumnEmbed {
        private String field;
        private String label;
        private String dataType;
        private Integer width;

        public ReportColumnEmbed() {
        }

        public ReportColumnEmbed(Report.ReportColumn column) {
            this.field = column.field();
            this.label = column.label();
            this.dataType = column.dataType();
            this.width = column.width();
        }

        public Report.ReportColumn toDomainModel() {
            return new Report.ReportColumn(this.field, this.label, this.dataType, this.width);
        }

        // Getters and setters
        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getDataType() {
            return dataType;
        }

        public void setDataType(String dataType) {
            this.dataType = dataType;
        }

        public Integer getWidth() {
            return width;
        }

        public void setWidth(Integer width) {
            this.width = width;
        }
    }

    // Embedded class for filters
    public static class ReportFilterEmbed {
        private String field;
        private String operator;
        private Object value;

        public ReportFilterEmbed() {
        }

        public ReportFilterEmbed(Report.ReportFilter filter) {
            this.field = filter.field();
            this.operator = filter.operator();
            this.value = filter.value();
        }

        public Report.ReportFilter toDomainModel() {
            return new Report.ReportFilter(this.field, this.operator, this.value);
        }

        // Getters and setters
        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public String getOperator() {
            return operator;
        }

        public void setOperator(String operator) {
            this.operator = operator;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }

    public LocalDate getPeriodStart() {
        return periodStart;
    }

    public void setPeriodStart(LocalDate periodStart) {
        this.periodStart = periodStart;
    }

    public LocalDate getPeriodEnd() {
        return periodEnd;
    }

    public void setPeriodEnd(LocalDate periodEnd) {
        this.periodEnd = periodEnd;
    }

    public String getGeneratedBy() {
        return generatedBy;
    }

    public void setGeneratedBy(String generatedBy) {
        this.generatedBy = generatedBy;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Integer getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(Integer recordCount) {
        this.recordCount = recordCount;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    public List<ReportColumnEmbed> getColumns() {
        return columns;
    }

    public void setColumns(List<ReportColumnEmbed> columns) {
        this.columns = columns;
    }

    public List<ReportFilterEmbed> getFilters() {
        return filters;
    }

    public void setFilters(List<ReportFilterEmbed> filters) {
        this.filters = filters;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Integer getProgressPercentage() {
        return progressPercentage;
    }

    public void setProgressPercentage(Integer progressPercentage) {
        this.progressPercentage = progressPercentage;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Instant startedAt) {
        this.startedAt = startedAt;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Instant completedAt) {
        this.completedAt = completedAt;
    }

    public Long getProcessingTimeMs() {
        return processingTimeMs;
    }

    public void setProcessingTimeMs(Long processingTimeMs) {
        this.processingTimeMs = processingTimeMs;
    }

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Boolean getIsScheduled() {
        return isScheduled;
    }

    public void setIsScheduled(Boolean isScheduled) {
        this.isScheduled = isScheduled;
    }

    public List<String> getRecipientEmails() {
        return recipientEmails;
    }

    public void setRecipientEmails(List<String> recipientEmails) {
        this.recipientEmails = recipientEmails;
    }

    public List<ReportGeneratedEvent> getDomainEvents() {
        return domainEvents;
    }

    public void setDomainEvents(List<ReportGeneratedEvent> domainEvents) {
        this.domainEvents = domainEvents;
    }
}
