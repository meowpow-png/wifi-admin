package hr.ht.rnd.wifiadmin.test.autoconfigure;

import hr.ht.rnd.wifiadmin.test.config.TestEncryptionConfiguration;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Disables password encryption.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(TestEncryptionConfiguration.class)
public @interface DisableEncryption {}
