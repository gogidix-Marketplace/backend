package com.gogidix.aiservices.aiinferenceservice.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

/**
 * Architecture tests ensuring hexagonal architecture compliance.
 */
@DisplayName("Hexagonal Architecture Tests")
class HexagonalArchitectureTests {

    private final JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.gogidix.aiservices.aiinferenceservice");

    @Test
    @DisplayName("Domain layer should not depend on application layer")
    void domainShouldNotDependOnApplication() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..domain..")
                .should().dependOnClassesThat()
                .resideInAPackage("..application..");
        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Domain layer should not depend on infrastructure layer")
    void domainShouldNotDependOnInfrastructure() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..domain..")
                .should().dependOnClassesThat()
                .resideInAPackage("..infrastructure..");
        rule.check(importedClasses);
    }

    @Test
    @DisplayName("DTOs should be immutable records")
    void dtosShouldBeImmutableRecords() {
        ArchRule rule = classes()
                .that().haveSimpleNameEndingWith("Dto")
                .and().resideInAPackage("..application..")
                .should().beRecords();
        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Exceptions should reside in shared.exception package")
    void exceptionsShouldResideInSharedException() {
        ArchRule rule = classes()
                .that().areAssignableTo(Throwable.class)
                .and().areNotAssignableTo(Error.class)
                .should().resideInAPackage("..shared.exception..");
        rule.check(importedClasses);
    }
}
