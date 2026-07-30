package hr.ht.rnd.wifiadmin.arch;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@SuppressWarnings("unused")
@AnalyzeClasses(packages = "hr.ht.rnd.wifiadmin")
class CommonArchitectureTest {

    @ArchTest
    static final ArchRule is_independent = noClasses()
            .that()
            .resideInAPackage("..common..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage(
                    "..application..",
                    "..domain..",
                    "..infra.."
            )
            .allowEmptyShould(true);
}
