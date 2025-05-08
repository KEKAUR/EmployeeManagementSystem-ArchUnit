package com.company.employee.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;

public class UtilityClassTest {

    private final JavaClasses importedClasses = new ClassFileImporter().importPackages("com.company.employee");

    @Test
    void utilityClassesShouldOnlyHaveStaticMethods() {
        methods()
            .that().areDeclaredInClassesThat().haveSimpleNameEndingWith("Util")
            .should().beStatic()
            .because("Utility classes should contain only static methods.")
            .check(importedClasses);
    }
}