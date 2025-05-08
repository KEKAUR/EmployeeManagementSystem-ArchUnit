package com.company.employee.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.library.GeneralCodingRules;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

public class ArchitectureTest {

    private final JavaClasses importedClasses = new ClassFileImporter().importPackages("com.company.employee");

    @Test
    void classesShouldUseLogger() {
        classes()
            .that().resideInAnyPackage("..controller..", "..service..")
            .should().dependOnClassesThat().areAssignableTo(org.slf4j.Logger.class)
            .because("All controllers and service classes should use a Logger for logging.")
            .check(importedClasses);
    }

    @Test
    void noAccessToStandardStreams() {
        GeneralCodingRules.NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS.check(importedClasses);
    }

    @Test
    void noCyclicDependenciesBetweenPackages() {
        slices()
            .matching("com.company.employee.(*)..")
            .should().beFreeOfCycles()
            .because("Cyclic dependencies between packages harm project maintainability.")
            .check(importedClasses);
    }

  
}