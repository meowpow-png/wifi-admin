package hr.ht.rnd.wifiadmin.arch;

import org.springframework.stereotype.Service;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@SuppressWarnings("unused")
@AnalyzeClasses(packages = "hr.ht.rnd.wifiadmin")
class ApplicationArchitectureTest {

    private static final DescribedPredicate<JavaClass> INBOUND_PORT =
            JavaClass.Predicates.resideInAPackage("..application.inbound..");

    @ArchTest
    static final ArchRule depends_only_on_domain_and_common = noClasses()
            .that()
            .resideInAPackage("..application..")
            .should()
            .dependOnClassesThat()
            .resideInAPackage("..infra..");

    @ArchTest
    static final ArchRule is_transport_independent = noClasses()
            .that()
            .resideInAPackage("..application..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage(
                    "org.springframework.web..",
                    "org.springframework.http..",
                    "jakarta.servlet..",
                    "jakarta.ws.rs.."
            );

    @ArchTest
    static final ArchRule services_implement_inbound_ports = classes()
            .that()
            .areAnnotatedWith(Service.class)
            .and()
            .resideInAPackage("..application.service..")
            .should()
            .implement(INBOUND_PORT);
}
