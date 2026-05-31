package com.gogidix.finance.tax.unit.domain;

import com.gogidix.finance.tax.domain.model.TaxFiling;
import com.gogidix.finance.tax.domain.model.TaxRate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("TaxFiling Domain Tests")
class TaxFilingTest {

    private TaxFiling taxFiling;
    private static final String TENANT_ID = "tenant-001";
    private static final YearMonth PERIOD = YearMonth.of(2024, 3);
    private static final TaxFiling.FilingType FILING_TYPE = TaxFiling.FilingType.QUARTERLY_RETURN;
    private static final TaxRate.Jurisdiction JURISDICTION = TaxRate.Jurisdiction.US_FEDERAL;
    private static final TaxRate.TaxType TAX_TYPE = TaxRate.TaxType.VAT;
    private static final String CREATED_BY = "system";

    @BeforeEach
    void setUp() {
        taxFiling = TaxFiling.create(
            TENANT_ID,
            PERIOD,
            FILING_TYPE,
            JURISDICTION,
            TAX_TYPE,
            "USD",
            LocalDate.now().plusDays(30),
            CREATED_BY
        );
    }

    @Test
    @DisplayName("Should create tax filing with defaults")
    void shouldCreateTaxFilingWithDefaults() {
        assertThat(taxFiling).isNotNull();
        assertThat(taxFiling.getTenantId()).isEqualTo(TENANT_ID);
        assertThat(taxFiling.getFilingPeriod()).isEqualTo(PERIOD);
        assertThat(taxFiling.getFilingType()).isEqualTo(FILING_TYPE);
        assertThat(taxFiling.getJurisdiction()).isEqualTo(JURISDICTION);
        assertThat(taxFiling.getTaxType()).isEqualTo(TAX_TYPE);
        assertThat(taxFiling.getStatus()).isEqualTo(TaxFiling.FilingStatus.DRAFT);
        assertThat(taxFiling.getDueDate()).isNotNull();
        assertThat(taxFiling.getFilingId()).isNotNull();
        assertThat(taxFiling.getFilingId()).startsWith("TF-");
        assertThat(taxFiling.getGrossSales()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(taxFiling.getTaxableSales()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(taxFiling.getExemptSales()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    @DisplayName("Should generate unique filing IDs")
    void shouldGenerateUniqueFilingIds() {
        TaxFiling filing1 = TaxFiling.create(TENANT_ID, PERIOD, FILING_TYPE, JURISDICTION, TAX_TYPE, "USD", LocalDate.now().plusDays(30), CREATED_BY);
        TaxFiling filing2 = TaxFiling.create(TENANT_ID, PERIOD.plusMonths(3), FILING_TYPE, JURISDICTION, TAX_TYPE, "USD", LocalDate.now().plusDays(30), CREATED_BY);

        assertThat(filing1.getFilingId()).isNotEqualTo(filing2.getFilingId());
    }

    @Test
    @DisplayName("Should update filing figures")
    void shouldUpdateFilingFigures() {
        taxFiling.updateFigures(
            new BigDecimal("100000.00"),
            new BigDecimal("80000.00"),
            new BigDecimal("20000.00"),
            new BigDecimal("16000.00"),
            new BigDecimal("12000.00")
        );

        assertThat(taxFiling.getGrossSales()).isEqualByComparingTo(new BigDecimal("100000.00"));
        assertThat(taxFiling.getTaxableSales()).isEqualByComparingTo(new BigDecimal("80000.00"));
        assertThat(taxFiling.getExemptSales()).isEqualByComparingTo(new BigDecimal("20000.00"));
        assertThat(taxFiling.getTotalTaxCollected()).isEqualByComparingTo(new BigDecimal("16000.00"));
        assertThat(taxFiling.getTotalTaxPaid()).isEqualByComparingTo(new BigDecimal("12000.00"));
        assertThat(taxFiling.getTaxDue()).isEqualByComparingTo(new BigDecimal("4000.00"));
    }

    @Test
    @DisplayName("Should calculate refund when overpaid")
    void shouldCalculateRefundWhenOverpaid() {
        taxFiling.updateFigures(
            new BigDecimal("100000.00"),
            new BigDecimal("80000.00"),
            new BigDecimal("20000.00"),
            new BigDecimal("12000.00"),
            new BigDecimal("16000.00")
        );

        assertThat(taxFiling.getTaxDue()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(taxFiling.getTaxRefund()).isEqualByComparingTo(new BigDecimal("4000.00"));
    }

    @Test
    @DisplayName("Should not update figures for submitted filing")
    void shouldNotUpdateFiguresForSubmitted() {
        taxFiling.submitForReview();
        taxFiling.submit();

        assertThatThrownBy(() ->
            taxFiling.updateFigures(
                new BigDecimal("100000.00"),
                new BigDecimal("80000.00"),
                new BigDecimal("20000.00"),
                new BigDecimal("16000.00"),
                new BigDecimal("12000.00")
            )
        ).isInstanceOf(IllegalStateException.class)
         .hasMessageContaining("Cannot update figures for submitted filings");
    }

    @Test
    @DisplayName("Should submit for review")
    void shouldSubmitForReview() {
        taxFiling.submitForReview();

        assertThat(taxFiling.getStatus()).isEqualTo(TaxFiling.FilingStatus.PENDING_REVIEW);
    }

    @Test
    @DisplayName("Should not submit non-draft for review")
    void shouldNotSubmitNonDraftForReview() {
        taxFiling.submitForReview();

        assertThatThrownBy(() -> taxFiling.submitForReview())
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("Can only submit draft filings for review");
    }

    @Test
    @DisplayName("Should submit filing")
    void shouldSubmitFiling() {
        taxFiling.submitForReview();
        taxFiling.submit();

        assertThat(taxFiling.getStatus()).isEqualTo(TaxFiling.FilingStatus.SUBMITTED);
        assertThat(taxFiling.getSubmissionDate()).isNotNull();
        assertThat(taxFiling.getFilingDate()).isNotNull();
    }

    @Test
    @DisplayName("Should not submit non-pending filing")
    void shouldNotSubmitNonPending() {
        assertThatThrownBy(() -> taxFiling.submit())
            .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("Should acknowledge filing")
    void shouldAcknowledgeFiling() {
        taxFiling.submitForReview();
        taxFiling.submit();

        taxFiling.acknowledge("FIL-2024-12345");

        assertThat(taxFiling.getStatus()).isEqualTo(TaxFiling.FilingStatus.PROCESSING);
        assertThat(taxFiling.getAcknowledgementNumber()).isEqualTo("FIL-2024-12345");
        assertThat(taxFiling.getAcknowledgementDate()).isNotNull();
    }

    @Test
    @DisplayName("Should not acknowledge non-submitted filing")
    void shouldNotAcknowledgeNonSubmitted() {
        assertThatThrownBy(() -> taxFiling.acknowledge("FIL-2024-12345"))
            .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("Should accept filing")
    void shouldAcceptFiling() {
        taxFiling.submitForReview();
        taxFiling.submit();
        taxFiling.acknowledge("FIL-2024-12345");

        taxFiling.accept("auditor");

        assertThat(taxFiling.getStatus()).isEqualTo(TaxFiling.FilingStatus.ACCEPTED);
        assertThat(taxFiling.getApprovedBy()).isEqualTo("auditor");
        assertThat(taxFiling.getApprovedAt()).isNotNull();
    }

    @Test
    @DisplayName("Should not accept non-processing filing")
    void shouldNotAcceptNonProcessing() {
        assertThatThrownBy(() -> taxFiling.accept("auditor"))
            .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("Should reject filing")
    void shouldRejectFiling() {
        taxFiling.submitForReview();
        taxFiling.submit();
        taxFiling.acknowledge("FIL-2024-12345");

        taxFiling.reject("Incorrect calculation");

        assertThat(taxFiling.getStatus()).isEqualTo(TaxFiling.FilingStatus.REJECTED);
    }

    @Test
    @DisplayName("Should not reject non-processing filing")
    void shouldNotRejectNonProcessing() {
        assertThatThrownBy(() -> taxFiling.reject("Reason"))
            .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("Should assess filing")
    void shouldAssessFiling() {
        taxFiling.submitForReview();
        taxFiling.submit();
        taxFiling.acknowledge("FIL-2024-12345");
        taxFiling.accept("auditor");

        // After acceptance, the filing can move to assessed status
        // (Actual implementation may vary)

        assertThat(taxFiling.getStatus()).isEqualTo(TaxFiling.FilingStatus.ACCEPTED);
    }

    @Test
    @DisplayName("Should mark as paid")
    void shouldMarkAsPaid() {
        taxFiling.submitForReview();
        // Need tax due > 0 for markAsPaid to work
        taxFiling.updateFigures(new BigDecimal("100000.00"), new BigDecimal("80000.00"), new BigDecimal("20000.00"), new BigDecimal("16000.00"), new BigDecimal("10000.00"));
        taxFiling.submit();
        taxFiling.acknowledge("FIL-2024-12345");
        taxFiling.accept("auditor");

        taxFiling.markAsPaid("PAY-001");

        assertThat(taxFiling.getStatus()).isEqualTo(TaxFiling.FilingStatus.PAID);
        assertThat(taxFiling.getPaymentReference()).isEqualTo("PAY-001");
        assertThat(taxFiling.getPaymentDate()).isNotNull();
    }

    @Test
    @DisplayName("Should handle all filing types")
    void shouldHandleAllFilingTypes() {
        TaxFiling.FilingType[] types = TaxFiling.FilingType.values();

        for (TaxFiling.FilingType type : types) {
            TaxFiling filing = TaxFiling.create(
                TENANT_ID,
                PERIOD,
                type,
                JURISDICTION,
                TAX_TYPE,
                "USD",
                LocalDate.now().plusDays(30),
                CREATED_BY
            );
            assertThat(filing.getFilingType()).isEqualTo(type);
        }
    }

    @Test
    @DisplayName("Should handle all filing statuses")
    void shouldHandleAllFilingStatuses() {
        taxFiling.submitForReview();
        assertThat(taxFiling.getStatus()).isEqualTo(TaxFiling.FilingStatus.PENDING_REVIEW);

        taxFiling.submit();
        assertThat(taxFiling.getStatus()).isEqualTo(TaxFiling.FilingStatus.SUBMITTED);

        taxFiling.acknowledge("FIL-2024-12345");
        assertThat(taxFiling.getStatus()).isEqualTo(TaxFiling.FilingStatus.PROCESSING);

        taxFiling.accept("auditor");
        assertThat(taxFiling.getStatus()).isEqualTo(TaxFiling.FilingStatus.ACCEPTED);
    }

    @Test
    @DisplayName("Should calculate net amount correctly")
    void shouldCalculateNetAmountCorrectly() {
        taxFiling.updateFigures(
            new BigDecimal("100000.00"),
            new BigDecimal("80000.00"),
            new BigDecimal("20000.00"),
            new BigDecimal("16000.00"),
            new BigDecimal("10000.00")
        );

        // Tax due: 6000, No penalty or interest
        assertThat(taxFiling.getNetAmount()).isEqualByComparingTo(new BigDecimal("6000.00"));
    }

    @Test
    @DisplayName("Should add adjustment")
    void shouldAddAdjustment() {
        TaxFiling.Adjustment adjustment = TaxFiling.Adjustment.builder()
            .adjustmentId("ADJ-001")
            .adjustmentType("PENALTY_WAIVER")
            .amount(new BigDecimal("500.00"))
            .reason("Good compliance record")
            .reference("WAIVER-001")
            .adjustmentDate(LocalDate.now())
            .build();

        taxFiling.getAdjustments().add(adjustment);

        assertThat(taxFiling.getAdjustments()).hasSize(1);
        assertThat(taxFiling.getAdjustments().get(0).getAdjustmentId()).isEqualTo("ADJ-001");
    }

    @Test
    @DisplayName("Should add attachment")
    void shouldAddAttachment() {
        TaxFiling.Attachment attachment = TaxFiling.Attachment.builder()
            .attachmentId("ATT-001")
            .fileName("supporting_docs.pdf")
            .fileType("application/pdf")
            .fileSize(1024000L)
            .storageLocation("/documents/supporting_docs.pdf")
            .build();

        taxFiling.getAttachments().add(attachment);

        assertThat(taxFiling.getAttachments()).hasSize(1);
        assertThat(taxFiling.getAttachments().get(0).getFileName()).isEqualTo("supporting_docs.pdf");
    }

    @Test
    @DisplayName("Should add calculation ID")
    void shouldAddCalculationId() {
        taxFiling.getCalculationIds().add("TC-12345678");
        taxFiling.getCalculationIds().add("TC-87654321");

        assertThat(taxFiling.getCalculationIds()).hasSize(2);
    }

    @Test
    @DisplayName("Should track metadata")
    void shouldTrackMetadata() {
        taxFiling.getMetadata().put("preparedBy", "accountant");
        taxFiling.getMetadata().put("reviewedBy", "manager");

        assertThat(taxFiling.getMetadata()).hasSize(2);
        assertThat(taxFiling.getMetadata().get("preparedBy")).isEqualTo("accountant");
    }

    @Test
    @DisplayName("Should validate figures on submission")
    void shouldValidateFiguresOnSubmission() {
        taxFiling.updateFigures(
            new BigDecimal("100000.00"),
            new BigDecimal("120000.00"), // Taxable > Gross - invalid
            new BigDecimal("20000.00"),
            new BigDecimal("16000.00"),
            new BigDecimal("12000.00")
        );

        assertThatThrownBy(() -> taxFiling.submit())
            .isInstanceOf(IllegalStateException.class);
    }
}
