# Note: SOAP Response Normalization

## Context

After resolving the initial SOAP interoperability issues, retrieval of WiFi configurations succeeded, while configuration updates consistently failed during SOAP response processing.

The objective was to determine whether the failure originated from the generated SOAP client, the published WSDL, or the SOAP platform itself.

## Observations

Update requests were successfully transmitted to the SOAP platform, which returned an HTTP `200 OK` response containing a valid-looking SOAP envelope.

Despite the successful response, Apache CXF failed while parsing the response body with the following exception:

```text
Illegal processing instruction target ("xml"); xml (case insensitive) is reserved by the specs.
```

The exception occurred before JAXB unmarshalling, indicating that the response could not be parsed as a well-formed XML document.

## Analysis

To verify that the request itself was not responsible for the failure, I enabled Apache CXF request and response logging.

The captured request confirmed that the generated SOAP client produced the expected request, including the SOAP envelope, `SOAPAction`, headers, and payload.

The captured response also appeared to be correct:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"
               xmlns:tns="http://wifi-admin.local/platform/v1">
    ...
</soap:Envelope>
```

Despite appearing valid in the logs, the XML parser consistently reported that the XML declaration was encountered on line 2 rather than at the beginning of the document.

This suggested that the raw HTTP response contained leading whitespace before the XML declaration that was not visible in the logged payload.

To verify this hypothesis, I implemented an inbound Apache CXF interceptor that inspected the raw response stream before XML parsing.

The interceptor removes only leading whitespace (`CR`, `LF`, `SPACE`, and `TAB`) preceding an XML declaration while leaving the SOAP document itself unchanged.

```text
HTTP Response
        │
        ▼
Raw InputStream
        │
        ▼
Normalize leading whitespace
        │
        ▼
XML Parser
        │
        ▼
JAXB
```

After introducing the interceptor, Apache CXF successfully parsed the response without requiring any modifications to the generated SOAP client, WSDL-derived classes, or application domain model.

## Conclusions

The generated Apache CXF client and the published WSDL were both verified to be correct.

The interoperability issue originated from the SOAP response received from the platform, where leading whitespace preceded the XML declaration. While visually insignificant, this violates the XML specification and causes strict XML parsers to reject the document.

Normalizing the response before XML parsing resolved the issue without modifying the SOAP payload itself.

## Next Steps

- Introduce an inbound interceptor that normalizes leading whitespace before XML parsing
- Keep the normalization confined to the platform integration layer
- Continue using the generated WSDL-derived SOAP client without modification

## References

- [StackOverflow: XML processing instructions and white space](https://stackoverflow.com/questions/8464124/xml-processing-instructions-and-white-space)
- [Oracle Forums: The processing instruction target matching '[xX][mM][lL]' is not allowed](https://forums.oracle.com/ords/apexds/post/the-processing-instruction-target-matching-xx-mm-ll-is-not-4604)
