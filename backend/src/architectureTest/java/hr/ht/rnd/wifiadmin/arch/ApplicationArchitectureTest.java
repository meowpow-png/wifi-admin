package hr.ht.rnd.wifiadmin.arch;

import hr.ht.rnd.wifiadmin.application.event.ApplicationEvent;
import hr.ht.rnd.wifiadmin.application.outbound.EventPublisher;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;

@SuppressWarnings("unused")
@AnalyzeClasses(packages = "hr.ht.rnd.wifiadmin")
class ApplicationArchitectureTest {

    private static final DescribedPredicate<JavaClass> INBOUND_PORT =
            JavaClass.Predicates.resideInAPackage("..application.inbound..");

    private static final DescribedPredicate<JavaClass> APPLICATION_EVENT =
            JavaClass.Predicates.assignableTo(ApplicationEvent.class);

    private static final DescribedPredicate<JavaClass> EVENT_PUBLISHER =
            JavaClass.Predicates.assignableTo(EventPublisher.class);

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

    @ArchTest
    static final ArchRule events_implement_application_event = classes()
            .that()
            .resideInAPackage("..application.event..")
            .and()
            .areNotInterfaces()
            .should()
            .implement(ApplicationEvent.class);

    @ArchTest
    static final ArchRule event_publishers_reside_in_infrastructure = classes()
            .that(EVENT_PUBLISHER)
            .and()
            .areNotInterfaces()
            .should()
            .resideInAPackage("..infra..");

    @ArchTest
    static final ArchRule event_listeners_reside_in_infrastructure = methods()
            .that()
            .areAnnotatedWith(EventListener.class)
            .should()
            .beDeclaredInClassesThat()
            .resideInAPackage("..infra..");
}
