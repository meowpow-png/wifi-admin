package hr.ht.rnd.wifiadmin.test.autoconfigure;

import org.junit.jupiter.api.Tag;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks the class as a wiring integration test.
 */
@Tag("wiring")
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface WiringIntegrationTest {}
