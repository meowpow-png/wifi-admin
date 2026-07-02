package hr.ht.rnd.wifiadmin.arch;

import org.springframework.web.bind.annotation.RestController;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@SuppressWarnings("unused")
@AnalyzeClasses(packages = "hr.ht.rnd.wifiadmin")
class InfrastructureArchitectureTest {

    @ArchTest
    static final ArchRule platform_models_are_isolated = noClasses()
            .that()
            .resideOutsideOfPackage("..infra.platform..")
            .should()
            .dependOnClassesThat()
            .resideInAPackage("..infra.platform.wsdl..");

    @ArchTest
    static final ArchRule controllers_depend_only_on_inbound_ports = noClasses()
            .that()
            .areAnnotatedWith(RestController.class)
            .should()
            .dependOnClassesThat()
            .resideInAPackage("..application.service..");
}
