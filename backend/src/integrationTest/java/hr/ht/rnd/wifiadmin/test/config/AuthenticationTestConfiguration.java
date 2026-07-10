package hr.ht.rnd.wifiadmin.test.config;

import hr.ht.rnd.wifiadmin.test.support.AuthenticationHandler;
import hr.ht.rnd.wifiadmin.test.support.AuthenticationRequests;
import hr.ht.rnd.wifiadmin.test.support.AuthenticationResponses;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;

@TestConfiguration
@Import({
        AuthenticationHandler.class,
        AuthenticationRequests.class,
        AuthenticationResponses.class
})
public class AuthenticationTestConfiguration {}
