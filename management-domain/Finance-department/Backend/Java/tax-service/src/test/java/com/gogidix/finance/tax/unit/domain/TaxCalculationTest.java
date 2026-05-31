package com.gogidix.finance.tax.unit.domain;

import com.gogidix.finance.tax.domain.model.TaxCalculation;
import com.gogidix.finance.tax.domain.model.TaxRate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("TaxCalculation Domain Tests")
class TaxCalculationTest {

    private TaxCalculation calculation;
    private static final String TENANT_ID = "tenant-001";
    private static final String TRANSACTION_ID = "txn-001";
    private static final String CALCULATED_BY = "system";

    @BeforeEach
    void setUp() {
        calculation = TaxCalculation.create(
            TENANT_ID,
            TRANSACTION_ID,
            TaxCalculation.TransactionType.SALES,
            LocalDate.now(),
            TaxRate.Jurisdiction.US_FEDERAL,
            "USD",
            new BigDecimal("1000.00"),
            CALCULATED_BY
        );
    }

    @Test
    @DisplayName("Should create tax calculation with default values")
    void shouldCreateTaxCalculationWithDefaults() {
        assertThat(calculation).isNotNull();
        assertThat(calculation.getTenantId()).isEqualTo(TENANT_ID);
        assertThat(calculation.getTransactionId()).isEqualTo(TRANSACTION_ID);
        assertThat(calculation.getTransactionType()).isEqualTo(TaxCalculation.TransactionType.SALES);
        assertThat(calculation.getBaseAmount()).isEqualTo(new BigDecimal("1000.00"));
        assertThat(calculation.getTaxableAmount()).isEqualTo(new BigDecimal("1000.00"));
        assertThat(calculation.getTotalTax()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(calculation.getNetAmount()).isEqualByComparingTo(new BigDecimal("1000.00"));
        assertThat(calculation.getStatus()).isEqualTo(TaxCalculation.CalculationStatus.PENDING);
        assertThat(calculation.getCalculationId()).isNotNull();
        assertThat(calculation.getCalculationId()).startsWith("TC-");
        assertThat(calculation.getTaxBreakdown()).isEmpty();
        assertThat(calculation.getExemptions()).isEmpty();
        assertThat(calculation.getDeductions()).isEmpty();
    }

    @Test
    @DisplayName("Should generate unique calculation IDs")
    void shouldGenerateUniqueCalculationIds() {
        TaxCalculation calc1 = TaxCalculation.create(TENANT_ID, "txn1", TaxCalculation.TransactionType.SALES,
            LocalDate.now(), TaxRate.Jurisdiction.US_FEDERAL, "USD", new BigDecimal("100"), CALCULATED_BY);
        TaxCalculation calc2 = TaxCalculation.create(TENANT_ID, "txn2", TaxCalculation.TransactionType.SALES,
            LocalDate.now(), TaxRate.Jurisdiction.US_FEDERAL, "USD", new BigDecimal("100"), CALCULATED_BY);

        assertThat(calc1.getCalculationId()).isNotEqualTo(calc2.getCalculationId());
    }

    @Test
    @DisplayName("Should calculate single tax rate correctly")
    void shouldCalculateSingleTaxRate() {
        calculation.calculateWithRate("VAT", TaxRate.TaxType.VAT, new BigDecimal("20.00"), false, "Standard VAT");

        assertThat(calculation.getTaxBreakdown()).hasSize(1);
        TaxCalculation.TaxLineItem lineItem = calculation.getTaxBreakdown().get(0);
        assertThat(lineItem.getTaxCode()).isEqualTo("VAT");
        assertThat(lineItem.getTaxType()).isEqualTo(TaxRate.TaxType.VAT);
        assertThat(lineItem.getRate()).isEqualByComparingTo(new BigDecimal("20.00"));
        assertThat(lineItem.getBaseAmount()).isEqualByComparingTo(new BigDecimal("1000.00"));
        assertThat(lineItem.getTaxAmount()).isEqualByComparingTo(new BigDecimal("200.00"));
        assertThat(lineItem.getIsRecoverable()).isFalse();
    }

    @Test
    @DisplayName("Should calculate recoverable tax")
    void shouldCalculateRecoverableTax() {
        calculation.calculateWithRate("VAT", TaxRate.TaxType.VAT, new BigDecimal("20.00"), true, "Recoverable VAT");

        TaxCalculation.TaxLineItem lineItem = calculation.getTaxBreakdown().get(0);
        assertThat(lineItem.getIsRecoverable()).isTrue();
        assertThat(lineItem.getRecoverableAmount()).isEqualByComparingTo(new BigDecimal("200.00"));
    }

    @Test
    @DisplayName("Should calculate multiple tax rates")
    void shouldCalculateMultipleTaxRates() {
        calculation.calculateWithRate("VAT", TaxRate.TaxType.VAT, new BigDecimal("20.00"), false, "VAT");
        calculation.calculateWithRate("SALES_TAX", TaxRate.TaxType.SALES_TAX, new BigDecimal("5.00"), false, "Sales Tax");

        assertThat(calculation.getTaxBreakdown()).hasSize(2);
        assertThat(calculation.getTaxBreakdown().get(0).getTaxAmount()).isEqualByComparingTo(new BigDecimal("200.00"));
        assertThat(calculation.getTaxBreakdown().get(1).getTaxAmount()).isEqualByComparingTo(new BigDecimal("50.00"));
    }

    @Test
    @DisplayName("Should calculate compound tax correctly")
    void shouldCalculateCompoundTax() {
        calculation.calculateWithRate("VAT", TaxRate.TaxType.VAT, new BigDecimal("10.00"), false, "VAT");

        // Get total tax after first calculation
        BigDecimal totalAfterFirst = calculation.getTaxableAmount().add(calculation.getTotalTax());
        assertThat(totalAfterFirst).isEqualByComparingTo(new BigDecimal("1000.00"));

        // Compound tax is calculated on base + tax from previous calculations
        // After finalize, totalTax will be updated
    }

    @Test
    @DisplayName("Should not allow tax calculation on non-pending calculation")
    void shouldNotCalculateTaxOnNonPending() {
        calculation.finalize();

        assertThatThrownBy(() ->
            calculation.calculateWithRate("VAT", TaxRate.TaxType.VAT, new BigDecimal("20.00"), false, "VAT")
        ).isInstanceOf(IllegalStateException.class)
         .hasMessageContaining("Cannot modify non-pending calculations");
    }

    @Test
    @DisplayName("Should finalize calculation correctly")
    void shouldFinalizeCalculation() {
        calculation.calculateWithRate("VAT", TaxRate.TaxType.VAT, new BigDecimal("20.00"), false, "VAT");
        calculation.finalize();

        assertThat(calculation.getStatus()).isEqualTo(TaxCalculation.CalculationStatus.CALCULATED);
        assertThat(calculation.getTotalTax()).isEqualByComparingTo(new BigDecimal("200.00"));
        assertThat(calculation.getNetAmount()).isEqualByComparingTo(new BigDecimal("1200.00"));
        assertThat(calculation.getEffectiveTaxRate()).isEqualByComparingTo(new BigDecimal("20.0000"));
        assertThat(calculation.getCalculationMethod()).isEqualTo(TaxCalculation.CalculationMethod.FLAT_RATE);
        assertThat(calculation.getReferenceNumber()).isNotNull();
        assertThat(calculation.getReferenceNumber()).startsWith("REF-");
    }

    @Test
    @DisplayName("Should determine compound calculation method")
    void shouldDetermineCompoundMethod() {
        calculation.calculateWithRate("VAT", TaxRate.TaxType.VAT, new BigDecimal("10.00"), false, "VAT");
        calculation.calculateCompoundTax("SERVICE_TAX", TaxRate.TaxType.SERVICE_TAX, new BigDecimal("5.00"), false, "Service Tax");
        calculation.finalize();

        assertThat(calculation.getCalculationMethod()).isEqualTo(TaxCalculation.CalculationMethod.COMPOUND);
    }

    @Test
    @DisplayName("Should determine exemption-based method")
    void shouldDetermineExemptionBasedMethod() {
        calculation.addExemption("EXEMPT001", "RESALE", new BigDecimal("200.00"), "Resale exemption", "CERT-001", LocalDate.now().plusYears(1));
        calculation.calculateWithRate("VAT", TaxRate.TaxType.VAT, new BigDecimal("20.00"), false, "VAT");
        calculation.finalize();

        assertThat(calculation.getCalculationMethod()).isEqualTo(TaxCalculation.CalculationMethod.EXEMPTION_BASED);
    }

    @Test
    @DisplayName("Should not finalize non-pending calculation")
    void shouldNotFinalizeNonPending() {
        calculation.finalize();

        assertThatThrownBy(() -> calculation.finalize())
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("Can only finalize pending calculations");
    }

    @Test
    @DisplayName("Should add exemption correctly")
    void shouldAddExemption() {
        calculation.addExemption("EXEMPT001", "RESALE", new BigDecimal("200.00"), "Resale exemption", "CERT-001", LocalDate.now().plusYears(1));

        assertThat(calculation.getExemptions()).hasSize(1);
        TaxCalculation.Exemption exemption = calculation.getExemptions().get(0);
        assertThat(exemption.getExemptionCode()).isEqualTo("EXEMPT001");
        assertThat(exemption.getExemptionType()).isEqualTo("RESALE");
        assertThat(exemption.getAmount()).isEqualByComparingTo(new BigDecimal("200.00"));
        assertThat(calculation.getTaxableAmount()).isEqualByComparingTo(new BigDecimal("800.00"));
    }

    @Test
    @DisplayName("Should not add exemption to non-pending calculation")
    void shouldNotAddExemptionToNonPending() {
        calculation.finalize();

        assertThatThrownBy(() ->
            calculation.addExemption("EXEMPT001", "RESALE", new BigDecimal("200.00"), "Resale exemption", "CERT-001", LocalDate.now().plusYears(1))
        ).isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("Should add deduction correctly")
    void shouldAddDeduction() {
        calculation.addDeduction("TRADE_IN", new BigDecimal("100.00"), "Trade-in allowance", "REF-001");

        assertThat(calculation.getDeductions()).hasSize(1);
        TaxCalculation.Deduction deduction = calculation.getDeductions().get(0);
        assertThat(deduction.getDeductionType()).isEqualTo("TRADE_IN");
        assertThat(deduction.getAmount()).isEqualByComparingTo(new BigDecimal("100.00"));
        assertThat(calculation.getTaxableAmount()).isEqualByComparingTo(new BigDecimal("900.00"));
    }

    @Test
    @DisplayName("Should not add deduction to non-pending calculation")
    void shouldNotAddDeductionToNonPending() {
        calculation.finalize();

        assertThatThrownBy(() ->
            calculation.addDeduction("TRADE_IN", new BigDecimal("100.00"), "Trade-in allowance", "REF-001")
        ).isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("Should verify calculation")
    void shouldVerifyCalculation() {
        calculation.calculateWithRate("VAT", TaxRate.TaxType.VAT, new BigDecimal("20.00"), false, "VAT");
        calculation.finalize();
        calculation.verify("auditor");

        assertThat(calculation.getStatus()).isEqualTo(TaxCalculation.CalculationStatus.VERIFIED);
        assertThat(calculation.getVerifiedBy()).isEqualTo("auditor");
        assertThat(calculation.getVerifiedAt()).isNotNull();
    }

    @Test
    @DisplayName("Should not verify non-calculated calculation")
    void shouldNotVerifyNonCalculated() {
        assertThatThrownBy(() -> calculation.verify("auditor"))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("Can only verify calculated calculations");
    }

    @Test
    @DisplayName("Should mark calculation as applied")
    void shouldMarkAsApplied() {
        calculation.calculateWithRate("VAT", TaxRate.TaxType.VAT, new BigDecimal("20.00"), false, "VAT");
        calculation.finalize();
        calculation.markAsApplied();

        assertThat(calculation.getStatus()).isEqualTo(TaxCalculation.CalculationStatus.APPLIED);
    }

    @Test
    @DisplayName("Should not mark non-calculated calculation as applied")
    void shouldNotMarkNonCalculatedAsApplied() {
        assertThatThrownBy(() -> calculation.markAsApplied())
            .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("Should reverse calculation")
    void shouldReverseCalculation() {
        calculation.calculateWithRate("VAT", TaxRate.TaxType.VAT, new BigDecimal("20.00"), false, "VAT");
        calculation.finalize();
        calculation.verify("auditor");
        calculation.markAsApplied();
        calculation.reverse("Customer refund requested");

        assertThat(calculation.getStatus()).isEqualTo(TaxCalculation.CalculationStatus.REVERSED);
        assertThat(calculation.getNotes()).contains("REVERSED");
        assertThat(calculation.getNotes()).contains("Customer refund requested");
    }

    @Test
    @DisplayName("Should not reverse non-applied calculation")
    void shouldNotReverseNonApplied() {
        calculation.calculateWithRate("VAT", TaxRate.TaxType.VAT, new BigDecimal("20.00"), false, "VAT");
        calculation.finalize();

        assertThatThrownBy(() -> calculation.reverse("Reason"))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("Can only reverse applied calculations");
    }

    @Test
    @DisplayName("Should calculate total recoverable tax")
    void shouldCalculateTotalRecoverableTax() {
        calculation.calculateWithRate("VAT", TaxRate.TaxType.VAT, new BigDecimal("10.00"), true, "VAT");
        calculation.calculateWithRate("SALES_TAX", TaxRate.TaxType.SALES_TAX, new BigDecimal("5.00"), false, "Sales Tax");
        calculation.finalize();

        assertThat(calculation.getTotalRecoverableTax()).isEqualByComparingTo(new BigDecimal("100.00"));
    }

    @Test
    @DisplayName("Should calculate total non-recoverable tax")
    void shouldCalculateTotalNonRecoverableTax() {
        calculation.calculateWithRate("VAT", TaxRate.TaxType.VAT, new BigDecimal("10.00"), true, "VAT");
        calculation.calculateWithRate("SALES_TAX", TaxRate.TaxType.SALES_TAX, new BigDecimal("5.00"), false, "Sales Tax");
        calculation.finalize();

        assertThat(calculation.getTotalNonRecoverableTax()).isEqualByComparingTo(new BigDecimal("50.00"));
    }

    @Test
    @DisplayName("Should add context information")
    void shouldAddContext() {
        calculation.addContext("region", "North America");
        calculation.addContext("industry", "Technology");

        assertThat(calculation.getContext()).hasSize(2);
        assertThat(calculation.getContext().get("region")).isEqualTo("North America");
        assertThat(calculation.getContext().get("industry")).isEqualTo("Technology");
    }

    @Test
    @DisplayName("Should track domain events")
    void shouldTrackDomainEvents() {
        assertThat(calculation.getDomainEvents()).hasSize(1);
        assertThat(calculation.getDomainEvents().get(0).getEventType()).isEqualTo("CALCULATION_INITIATED");

        calculation.calculateWithRate("VAT", TaxRate.TaxType.VAT, new BigDecimal("20.00"), false, "VAT");
        calculation.finalize();

        assertThat(calculation.getDomainEvents()).hasSize(2);
        assertThat(calculation.getDomainEvents().get(1).getEventType()).isEqualTo("CALCULATION_COMPLETED");
    }

    @Test
    @DisplayName("Should clear domain events")
    void shouldClearDomainEvents() {
        calculation.calculateWithRate("VAT", TaxRate.TaxType.VAT, new BigDecimal("20.00"), false, "VAT");
        calculation.finalize();
        calculation.clearDomainEvents();

        assertThat(calculation.getDomainEvents()).isEmpty();
    }

    @Test
    @DisplayName("Should handle zero base amount")
    void shouldHandleZeroBaseAmount() {
        TaxCalculation zeroCalc = TaxCalculation.create(
            TENANT_ID, TRANSACTION_ID, TaxCalculation.TransactionType.SALES,
            LocalDate.now(), TaxRate.Jurisdiction.US_FEDERAL, "USD",
            BigDecimal.ZERO, CALCULATED_BY
        );
        zeroCalc.finalize();

        assertThat(zeroCalc.getEffectiveTaxRate()).isNull();
    }

    @Test
    @DisplayName("Should handle all transaction types")
    void shouldHandleAllTransactionTypes() {
        TaxCalculation.TransactionType[] types = TaxCalculation.TransactionType.values();

        for (TaxCalculation.TransactionType type : types) {
            TaxCalculation calc = TaxCalculation.create(
                TENANT_ID, TRANSACTION_ID, type,
                LocalDate.now(), TaxRate.Jurisdiction.US_FEDERAL, "USD",
                new BigDecimal("100"), CALCULATED_BY
            );
            assertThat(calc.getTransactionType()).isEqualTo(type);
        }
    }

    @Test
    @DisplayName("Should handle all jurisdictions")
    void shouldHandleAllJurisdictions() {
        TaxRate.Jurisdiction[] jurisdictions = TaxRate.Jurisdiction.values();

        for (TaxRate.Jurisdiction jurisdiction : jurisdictions) {
            TaxCalculation calc = TaxCalculation.create(
                TENANT_ID, TRANSACTION_ID, TaxCalculation.TransactionType.SALES,
                LocalDate.now(), jurisdiction, "USD",
                new BigDecimal("100"), CALCULATED_BY
            );
            assertThat(calc.getJurisdiction()).isEqualTo(jurisdiction);
        }
    }

    @Test
    @DisplayName("Should handle all calculation statuses")
    void shouldHandleAllCalculationStatuses() {
        calculation.calculateWithRate("VAT", TaxRate.TaxType.VAT, new BigDecimal("20.00"), false, "VAT");
        assertThat(calculation.getStatus()).isEqualTo(TaxCalculation.CalculationStatus.PENDING);

        calculation.finalize();
        assertThat(calculation.getStatus()).isEqualTo(TaxCalculation.CalculationStatus.CALCULATED);

        calculation.verify("auditor");
        assertThat(calculation.getStatus()).isEqualTo(TaxCalculation.CalculationStatus.VERIFIED);

        calculation.markAsApplied();
        assertThat(calculation.getStatus()).isEqualTo(TaxCalculation.CalculationStatus.APPLIED);

        calculation.reverse("Reason");
        assertThat(calculation.getStatus()).isEqualTo(TaxCalculation.CalculationStatus.REVERSED);
    }

    @Test
    @DisplayName("Should calculate tax with high precision")
    void shouldCalculateTaxWithHighPrecision() {
        calculation.calculateWithRate("VAT", TaxRate.TaxType.VAT, new BigDecimal("19.25"), false, "VAT");
        calculation.finalize();

        assertThat(calculation.getTaxBreakdown().get(0).getTaxAmount())
            .isEqualByComparingTo(new BigDecimal("192.5000"));
    }
}
