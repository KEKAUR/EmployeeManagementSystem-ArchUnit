package com.company.employee.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

public class ExceptionHandlingTest {

    private final JavaClasses importedClasses = new ClassFileImporter().importPackages("com.company.employee");

    @Test
    void allExceptionsShouldHaveSpecificSuffix() {
        classes()
            .that().areAssignableTo(Exception.class)
            .should().haveSimpleNameEndingWith("Exception")
            .because("All custom exceptions should have the suffix 'Exception'.")
            .check(importedClasses);
    }
}