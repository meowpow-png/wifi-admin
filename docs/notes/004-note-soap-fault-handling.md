# Note: SOAP Fault Handling

## Context

While implementing the SOAP client, I needed to distinguish between transport failures and business failures returned by the platform. In particular, the REST API was required to return `404 Not Found` when the requested CPE did not exist on the platform, which required detecting the corresponding SOAP fault returned by the external service.

## Observations

The mock SOAP platform returns business failures as standard SOAP faults containing both a fault code and a human-readable fault message. For example, an unknown CPE results in a SOAP fault with the code `tns:NotFound` and the message `CPE not found`.

However, the generated Apache CXF client does not expose this fault to the application. Instead, the client throws a generic `WebServiceException` whose root cause is:

```text
java.lang.RuntimeException: Invalid QName in mapping: tns:NotFound
```

The original SOAP fault message is not available through the exception hierarchy.

## Analysis

I investigated several approaches before deciding on the final implementation.

I first attempted to recover the SOAP fault from the exception hierarchy and considered introducing a CXF interceptor to translate SOAP faults before they reached the client implementation. I also reviewed the generated WSDL and compared the behaviour with Spring Web Services, whose `SoapFaultClientException` exposes both the fault code and fault message directly.

The investigation showed that the provided WSDL does not declare any `wsdl:fault` definitions. Because of this, Apache CXF attempts to resolve the returned fault code against the WSDL, fails while processing the undeclared QName (`tns:NotFound`), and aborts fault deserialization before constructing a SOAP fault object. As a result, neither the original fault message nor a typed SOAP fault exception is available to the application.

## Conclusions

The root cause is an inconsistency between the published WSDL and the behaviour of the SOAP platform. The platform returns SOAP faults that are not declared by the service contract, preventing Apache CXF from generating or exposing typed fault exceptions.

To isolate this limitation, I introduced a dedicated `SoapFaultDecoder`. The decoder recognizes supported fault codes embedded in the `WebServiceException` message and translates them into structured transport exceptions. The SOAP adapter then maps recognized platform faults to application exceptions while treating all remaining transport failures generically.

This confines the workaround to the SOAP adapter and prevents the application layer from depending on SOAP-specific implementation details.

## Next Steps

- Document why SOAP fault decoding is necessary and why the original SOAP fault message cannot be recovered when using Apache CXF with the provided WSDL
- No additional implementation changes are currently required

## References

- [Apache CXF Users: Error when parsing a SOAP Fault – Invalid QName in mapping](https://mail-archives.apache.org/mod_mbox/cxf-users/201506.mbox/%3C557573BB.8080401%40gmail.com%3E)
- [Apache CXF JIRA: CXF-7295 – CXF error when parsing a SOAP 1.2 fault: Invalid QName in mapping](https://issues.apache.org/jira/browse/CXF-7295)
- [Stack Overflow: Apache CXF (Mule) – Invalid QName in mapping: SOAP-ENV:Client](https://stackoverflow.com/questions/27476133/apache-cxf-mule-invalid-qname-in-mapping-soap-envclient)
