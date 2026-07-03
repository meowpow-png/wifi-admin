package hr.ht.rnd.wifiadmin.infra.transport.soap.cxf;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("cxf")
public record CxfProperties(boolean logFaults) {}
