package hr.ht.rnd.wifiadmin.infra.platform;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("cxf")
public record CxfProperties(boolean logFaults) {}
