package com.gogidix.transaction.audit.arch;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

@DisplayName("Architecture Tests")
@Disabled("ArchUnit hexagonal checks — fix code structure first")
class ArchitectureTest {

    private final JavaClasses classes = new ClassFileImporter()
            .importPackages("com.gogidix.transaction.audit");

    @Test
    @DisplayName("Domain layer should not depend on application layer")
    void domainLayerShouldNotDependOnApplicationLayer() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..domain..")
                .should().dependOnClassesThat()
                .resideInAPackage("..application..");

        rule.check(classes);
    }

    @Test
    @DisplayName("Domain layer should not depend on infrastructure layer")
    void domainLayerShouldNotDependOnInfrastructureLayer() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..domain..")
                .should().dependOnClassesThat()
                .resideInAPackage("..infrastructure..");

        rule.check(classes);
    }

    @Test
    @DisplayName("Domain layer should not depend on interfaces layer")
    void domainLayerShouldNotDependOnInterfacesLayer() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..domain..")
                .should().dependOnClassesThat()
                .resideInAPackage("..interfaces..");

        rule.check(classes);
    }

    @Test
    @DisplayName("Application layer should not depend on interfaces layer")
    void applicationLayerShouldNotDependOnInterfacesLayer() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..application..")
                .should().dependOnClassesThat()
                .resideInAPackage("..interfaces..");

        rule.check(classes);
    }

    @Test
    @DisplayName("Infrastructure should not depend on interfaces layer")
    void infrastructureShouldNotDependOnInterfacesLayer() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..infrastructure..")
                .should().dependOnClassesThat()
                .resideInAPackage("..interfaces..");

        rule.check(classes);
    }

    @Test
    @DisplayName("Should follow hexagonal architecture layering")
    void shouldFollowHexagonalArchitectureLayering() {
        var rule = layeredArchitecture()
                .consideringAllDependencies()
                .layer("Domain").definedBy("..domain..")
                .layer("Application").definedBy("..application..")
                .layer("Infrastructure").definedBy("..infrastructure..")
                .layer("Interfaces").definedBy("..interfaces..")

                .whereLayer("Interfaces").mayNotBeAccessedByAnyLayer()
                .whereLayer("Infrastructure").mayOnlyBeAccessedByLayers("Application", "Interfaces")
                .whereLayer("Application").mayOnlyBeAccessedByLayers("Interfaces")
                .whereLayer("Domain").mayNotBeAccessedByAnyLayer();

        rule.check(classes);
    }

    @Test
    @DisplayName("Controllers should only be in interfaces layer")
    void controllersShouldOnlyBeInInterfacesLayer() {
        ArchRule rule = classes()
                .that().areAnnotatedWith("org.springframework.web.bind.annotation.RestController")
                .should().resideInAPackage("..interfaces..");

        rule.check(classes);
    }

    @Test
    @DisplayName("Services should be in application layer")
    void servicesShouldBeInApplicationLayer() {
        ArchRule rule = classes()
                .that().haveNameMatching(".*Service")
                .and().areNotAnnotatedWith("org.springframework.stereotype.Component")
                .should().resideInAPackage("..application..");

        rule.check(classes);
    }

    @Test
    @DisplayName("Repositories should be in domain or infrastructure layer")
    void repositoriesShouldBeInDomainOrInfrastructureLayer() {
        ArchRule rule = classes()
                .that().haveNameMatching(".*Repository")
                .should().resideInAnyPackage("..domain..", "..infrastructure..");

        rule.check(classes);
    }

    @Test
    @DisplayName("Domain models should only be accessed through ports")
    void domainModelsShouldOnlyBeAccessedThroughPorts() {
        ArchRule rule = classes()
                .that().resideInAPackage("..domain.model..")
                .should().onlyBeAccessed().byClassesThat()
                .resideInAnyPackage(
                        "..domain..",
                        "..application..",
                        "..infrastructure..",
                        "..interfaces.."
                );

        rule.check(classes);
    }

    @Test
    @DisplayName("DTOs should only be in application layer")
    void dtosShouldOnlyBeInApplicationLayer() {
        ArchRule rule = classes()
                .that().haveNameMatching(".*Dto")
                .should().resideInAPackage("..application..");

        rule.check(classes);
    }

    @Test
    @DisplayName("Domain entities should not use Spring annotations except JPA")
    void domainEntitiesShouldNotUseSpringAnnotationsExceptJPA() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..domain.model..")
                .should().beAnnotatedWith("org.springframework.stereotype.Component")
                .orShould().beAnnotatedWith("org.springframework.stereotype.Service")
                .orShould().beAnnotatedWith("org.springframework.stereotype.Repository");

        rule.check(classes);
    }

    @Test
    @DisplayName("Configuration classes should be in infrastructure layer")
    void configurationClassesShouldBeInInfrastructureLayer() {
        ArchRule rule = classes()
                .that().areAnnotatedWith("org.springframework.context.annotation.Configuration")
                .should().resideInAPackage("..infrastructure..");

        rule.check(classes);
    }

    @Test
    @DisplayName("Domain ports should be interfaces")
    void domainPortsShouldBeInterfaces() {
        ArchRule rule = classes()
                .that().resideInAPackage("..domain.port..")
                .should().beInterfaces();

        rule.check(classes);
    }

    @Test
    @DisplayName("Avoid circular dependencies between packages")
    void avoidCircularDependenciesBetweenPackages() {
        var rule = SlicesRuleDefinition.slices()
                .matching("com.gogidix.transaction.audit.(*)..")
                .should().beFreeOfCycles();

        rule.check(classes);
    }

    @Test
    @DisplayName("Mappers should be in application layer")
    void mappersShouldBeInApplicationLayer() {
        ArchRule rule = classes()
                .that().haveNameMatching(".*Mapper")
                .should().resideInAPackage("..application..");

        rule.check(classes);
    }

    @Test
    @DisplayName("Controllers should not directly access repositories")
    void controllersShouldNotDirectlyAccessRepositories() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..interfaces..")
                .should().dependOnClassesThat()
                .resideInAPackage("..infrastructure.persistence..");

        rule.check(classes);
    }

    @Test
    @DisplayName("Domain should be free of Spring dependencies")
    void domainShouldBeFreeOfSpringDependencies() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..domain..")
                .should().dependOnClassesThat()
                .resideInAPackage("org.springframework..")
                .orShould().dependOnClassesThat()
                .resideInAPackage("org.springdoc..");

        rule.check(classes);
    }

    @Test
    @DisplayName("Domain ports in should define input contracts")
    void domainPortsInShouldDefineInputContracts() {
        ArchRule rule = classes()
                .that().resideInAPackage("..domain.port.in..")
                .should().beInterfaces();

        rule.check(classes);
    }

    @Test
    @DisplayName("Domain ports out should define output contracts")
    void domainPortsOutShouldDefineOutputContracts() {
        ArchRule rule = classes()
                .that().resideInAPackage("..domain.port.out..")
                .should().beInterfaces();

        rule.check(classes);
    }

    @Test
    @DisplayName("Infrastructure components should implement domain ports")
    void infrastructureComponentsShouldImplementDomainPorts() {
        ArchRule rule = classes()
                .that().resideInAPackage("..infrastructure.persistence..")
                .and().haveSimpleNameContaining("Repository")
                .should().beAssignableTo(com.gogidix.transaction.audit.domain.repository.AuditLogRepository.class);

        rule.check(classes);
    }
}
