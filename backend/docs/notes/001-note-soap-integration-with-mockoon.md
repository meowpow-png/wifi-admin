# Note: SOAP Integration with Mockoon

## Context

I was validating the end-to-end REST → SOAP integration against the provided Mockoon-based SOAP service. The goal was to determine whether the generated SOAP client, the published WSDL, or the mock service itself required changes before continuing the integration.

## Observations

Initial REST requests failed with an HTTP 404 response when invoking the SOAP endpoint, and Mockoon did not record a matching request.

After resolving the transport issue, Mockoon received the SOAP request but consistently returned a `NotFound` SOAP fault, even when valid CPE identifiers such as `CPE_001` were supplied.

## Analysis

To validate the generated SOAP request independently of Mockoon, I temporarily redirected the SOAP client to an HTTP echo service. This allowed the complete HTTP request to be inspected without relying on the SOAP implementation.

The captured request confirmed that the generated client produced the expected HTTP request, including the SOAP envelope, `SOAPAction`, headers, and request body.

```http
POST /platform
Content-Type: text/xml; charset=UTF-8
SOAPAction: "http://wifi-admin.local/platform/v1#getCpeID"

<soap:Envelope>
    ...
    <tns:GetCpeIdRequest>
        <tns:cpeId>CPE_001</tns:cpeId>
    </tns:GetCpeIdRequest>
</soap:Envelope>
```

Comparing this request with a successful `curl` invocation revealed one notable difference: Apache CXF attempted an HTTP/2 (h2c) upgrade, whereas the reference request used HTTP/1.1.

Forcing Apache CXF to use HTTP/1.1 allowed Mockoon to receive the request successfully.

```java
requestContext.put(
    "org.apache.cxf.transport.http.forceVersion",
    "1.1"
);
```

To understand why Mockoon still returned a `NotFound` SOAP fault, I temporarily modified the response template to return the parsed XML object instead of the normal SOAP response.

This confirmed that Mockoon correctly parsed the incoming SOAP message, but exposed the parsed object using XML paths that did not preserve the namespace prefixes present in the original request. For example, the request contained:

```xml
<tns:GetCpeIdRequest>
    <tns:cpeId>CPE_001</tns:cpeId>
</tns:GetCpeIdRequest>
```

whereas Mockoon exposed the parsed object as:

```json
{
  "soap:Envelope": {
    "soap:Body": {
      "GetCpeIdRequest": {
        "cpeId": {
          "_text": "CPE_001"
        }
      }
    }
  }
}
```

The supplied template attempted to resolve namespace-qualified lookup paths:

```text
soap:Envelope.soap:Body.tns:GetCpeIdRequest.tns:cpeId._text
```

These paths did not exist in the parsed object. Updating the template to use the structure produced by Mockoon's XML parser allowed the request to be matched successfully.

## Conclusions

The generated Apache CXF client conforms to the published WSDL and interoperates correctly with the service contract when the transport and mock implementation are configured appropriately.

The REST layer, SOAP serialization, endpoint configuration, and `SOAPAction` were all verified to be correct.

Two independent interoperability issues were identified:

- Apache CXF 4.2 attempted an HTTP/2 (h2c) upgrade that was incompatible with the tested Mockoon environment. Forcing HTTP/1.1 resolved the transport issue
- Mockoon's XML parser exposed parsed XML paths without preserving the namespace prefixes present in the original SOAP message, while the supplied response template expected namespace-qualified lookup paths

Neither issue required changes to the generated SOAP client or the WSDL-derived integration model.

## Next Steps

- Configure Apache CXF to communicate with the SOAP platform using HTTP/1.1 for broader platform compatibility
- Configure a preferred JAXB namespace mapping to emit explicit namespace prefixes expected by the target platform
- Continue the integration using the generated SOAP client without modifying the WSDL-derived classes

## References

- [ADR: Adopt a Contract-First Integration Strategy](../adr/005-adr-contract-first-integration-strategy.md)
