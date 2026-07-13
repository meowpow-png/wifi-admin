package hr.ht.rnd.wifiadmin.test.autoconfigure;

import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a test class as a JPA integration
 * test and applies the standard persistence
 * testing configuration.
 * <p>
 * <strong>API Note:</strong>
 * Includes {@link IntegrationTest} annotation.
 * <p>
 * <strong>Implementation Note:</strong>
 * Configures a JPA test slice. Only persistence-related
 * beans are loaded into the application context.
 * Components outside the persistence layer are
 * not available unless explicitly imported.
 */
@IntegrationTest
@DataJpaTest(showSql = false)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public @interface JpaIntegrationTest {}
