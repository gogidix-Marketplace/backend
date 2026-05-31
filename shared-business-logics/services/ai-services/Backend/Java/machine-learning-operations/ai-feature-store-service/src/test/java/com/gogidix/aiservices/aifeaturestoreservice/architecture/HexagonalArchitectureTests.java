package com.gogidix.aiservices.aifeaturestoreservice.architecture;

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
            .importPackages("com.gogidix.aiservices.aifeaturestoreservice");

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
    @DisplayName("Domain layer should not depend on interfaces layer")
    void domainShouldNotDependOnInterfaces() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..domain..")
                .should().dependOnClassesThat()
                .resideInAPackage("..interfaces..");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Application layer should not depend on infrastructure layer")
    void applicationShouldNotDependOnInfrastructure() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..application..")
                .should().dependOnClassesThat()
                .resideInAPackage("..infrastructure..");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Application layer should not depend on interfaces layer")
    void applicationShouldNotDependOnInterfaces() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..application..")
                .should().dependOnClassesThat()
                .resideInAPackage("..interfaces..");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Domain ports should be in domain.port package")
    void domainPortsShouldBeInCorrectPackage() {
        ArchRule rule = classes()
                .that().haveSimpleNameEndingWith("Port")
                .should().resideInAPackage("..domain.port..");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Controllers should reside in interfaces.rest package")
    void controllersShouldResideInInterfacesRest() {
        ArchRule rule = classes()
                .that().haveSimpleNameEndingWith("Controller")
                .should().resideInAPackage("..interfaces.rest..");

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
