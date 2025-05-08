package com.company.employee.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.library.GeneralCodingRules;
import org.junit.jupiter.api.Test;

public class GeneralCodingRulesTest {

    private final JavaClasses importedClasses = new ClassFileImporter().importPackages("com.company.employee");

    @Test
    void noAccessToStandardStreams() {
        GeneralCodingRules.NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS
            .because("Standard streams should not be accessed directly; use proper logging instead.")
            .check(importedClasses);
    }
}