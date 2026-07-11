package hr.ht.rnd.wifiadmin.test.autoconfigure;

import hr.ht.rnd.wifiadmin.test.config.TestHashingConfiguration;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Disables password hashing.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(TestHashingConfiguration.class)
public @interface DisableHashing {}
