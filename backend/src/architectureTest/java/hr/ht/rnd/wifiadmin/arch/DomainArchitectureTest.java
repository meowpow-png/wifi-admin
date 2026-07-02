package hr.ht.rnd.wifiadmin.arch;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@SuppressWarnings("unused")
@AnalyzeClasses(packages = "hr.ht.rnd.wifiadmin")
class DomainArchitectureTest {

    @ArchTest
    static final ArchRule is_independent = noClasses()
            .that()
            .resideInAPackage("..domain..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage(
                    "..application..",
                    "..infra..",
                    "..common.."
            );

    @ArchTest
    static final ArchRule is_framework_independent = noClasses()
            .that()
            .resideInAPackage("..domain..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage(
                    "org.springframework..",
                    "jakarta.."
            );
}
