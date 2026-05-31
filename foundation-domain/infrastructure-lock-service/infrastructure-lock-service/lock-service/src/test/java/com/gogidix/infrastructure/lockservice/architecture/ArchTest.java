package com.gogidix.infrastructure.lockservice.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

/**
 * Architecture tests to ensure clean code and proper layering.
 */
@DisplayName("Architecture Tests")
class ArchTest {

    private final JavaClasses importedClasses = new ClassFileImporter()
        .importPackages("com.gogidix.infrastructure.lockservice");

    @Test
    @DisplayName("Domain layer should not depend on application layer")
    void domainLayerShouldNotDependOnApplicationLayer() {
        ArchRule rule = noClasses()
            .that().resideInAPackage("..domain..")
            .should().dependOnClassesThat().resideInAPackage("..application..");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Domain layer should not depend on infrastructure layer")
    void domainLayerShouldNotDependOnInfrastructureLayer() {
        ArchRule rule = noClasses()
            .that().resideInAPackage("..domain..")
            .should().dependOnClassesThat().resideInAPackage("..lockservice.infrastructure..");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Domain layer should not depend on interfaces layer")
    void domainLayerShouldNotDependOnInterfacesLayer() {
        ArchRule rule = noClasses()
            .that().resideInAPackage("..domain..")
            .should().dependOnClassesThat().resideInAPackage("..interfaces..");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Application layer should not depend on interfaces layer")
    void applicationLayerShouldNotDependOnInterfacesLayer() {
        ArchRule rule = noClasses()
            .that().resideInAPackage("..application..")
            .should().dependOnClassesThat().resideInAPackage("..interfaces..");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Controllers should reside in interfaces layer")
    void controllersShouldResideInInterfacesLayer() {
        ArchRule rule = classes()
            .that().areAnnotatedWith("org.springframework.web.bind.annotation.RestController")
            .should().resideInAPackage("..interfaces..");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Repositories should reside in domain or infrastructure layer")
    void repositoriesShouldResideInDomainOrInfrastructureLayer() {
        ArchRule rule = classes()
            .that().haveNameMatching(".*Repository")
            .should().resideInAnyPackage("..domain..", "..infrastructure..");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Services should reside in application layer")
    void servicesShouldResideInApplicationLayer() {
        ArchRule rule = classes()
            .that().haveNameMatching(".*Service")
            .and().areNotAnnotatedWith("org.springframework.stereotype.Repository")
            .should().resideInAPackage("..application..");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Domain models should only be accessed by layers below them")
    void domainModelsShouldOnlyBeAccessedByLayersBelowThem() {
        ArchRule rule = noClasses()
            .that().resideInAPackage("..domain.model..")
            .should().dependOnClassesThat().resideInAPackage("..application..");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Configuration classes should be in infrastructure layer")
    void configurationClassesShouldBeInInfrastructureLayer() {
        ArchRule rule = classes()
            .that().areAnnotatedWith("org.springframework.context.annotation.Configuration")
            .should().resideInAPackage("..infrastructure..");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Should enforce clean architecture - hexagonal boundaries")
    void shouldEnforceCleanArchitectureHexagonalBoundaries() {
        ArchRule rule = noClasses()
            .that().resideInAPackage("..domain..")
            .should().dependOnClassesThat()
            .resideInAnyPackage("org.springframework", "org.springframework.data", "org.springframework.web");

        rule.check(importedClasses);
    }
}
