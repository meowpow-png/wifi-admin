package hr.ht.rnd.wifiadmin.test.autoconfigure;

import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a test class as a MockMvc integration test
 * and applies the standard web testing configuration.
 * <p>
 * <strong>API Note:</strong>
 * Includes {@link IntegrationTest} annotation.
 */
@IntegrationTest
@AutoConfigureMockMvc
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MockMvcIntegrationTest {}
