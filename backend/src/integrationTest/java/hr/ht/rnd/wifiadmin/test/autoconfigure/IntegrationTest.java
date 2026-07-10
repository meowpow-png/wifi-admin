package hr.ht.rnd.wifiadmin.test.autoconfigure;

import hr.ht.rnd.wifiadmin.test.config.TestApplicationConfiguration;

import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks the class as an integration test and
 * applies the standard testing configuration.
 */
@ActiveProfiles("test")
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(TestApplicationConfiguration.class)
public @interface IntegrationTest {}
