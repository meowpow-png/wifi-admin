# Note: Password Validation Requirement

## Context

While implementing request validation, I needed to determine whether a password should be mandatory for encrypted Wi-Fi configurations.

## Observations

The OpenAPI specification describes the `password` field as optional while noting that it is expected for some encryption modes. However, it does not define this as a validation requirement or specify the expected behavior when the password is omitted.

**Schema documentation**

```yaml
password:
  type: string
  nullable: true
  description: >
    Opcionalno; očekivano kada šifriranje zahtijeva lozinku (npr. WPA2_PSK).
    Za OPEN obično nije potrebno.
```

**Response documentation**

```yaml
400:
  description: Nevažeći ulaz (npr. nedostaje lozinka uz WPA2_PSK).
```

These two parts of the specification can be interpreted differently:

- The schema defines `password` as optional
- The response documentation implies that omitting the password for `WPA2_PSK` results in a `400 Bad Request`
- The schema does not list `password` as a required property
- The schema does not define conditional validation (such as `oneOf` or `if`/`then`) that would make `password` mandatory for specific encryption types

## Analysis

I researched the supported Wi-Fi authentication modes and found that WPA-Personal (PSK), WPA2-Personal (PSK), and WPA3-Personal (SAE) are password-based authentication mechanisms. Therefore, these authentication modes require a password for clients to authenticate to the wireless network.

It is important to distinguish this from a broader statement that Wi-Fi encryption always requires a password. Modern Wi-Fi standards also support passwordless encrypted networks (for example, Enhanced Open / OWE), although those authentication modes are not represented by this API.

Because the OpenAPI specification does not explicitly define the password requirement as a request validation rule, nor specify the expected API behavior when the password is omitted, I chose not to introduce additional business rules beyond those defined by the API contract.

## Conclusions

I implemented the API according to the specification as written.

Although the documented authentication modes indicate that a password should be required for `WPA_PSK`, `WPA2_PSK`, and `WPA3_SAE`, the API contract itself does not express this as a validation constraint. Given the ambiguity between the schema and the example error response, I avoided introducing additional business rules that are not explicitly defined by the specification.

In a real project, I would clarify the expected behavior with the API owner or domain experts before implementing such validation.

## Next Steps

- Add explicit validation if the password requirement is confirmed
- Document the corresponding validation behavior and error responses

## References

- [IEEE Spectrum: Everything You Need to Know About WPA3](https://spectrum.ieee.org/everything-you-need-to-know-about-wpa3)
- [HPE Aruba Networking: WPA3-Personal (SAE)](https://arubanetworking.hpe.com/techdocs/aos/wifi-design-deploy/security/modes/wpa3-personal/)
- [Huawei Enterprise Documentation: Security – WPA2/WPA3-PSK-SAE](https://support.huawei.com/enterprise/en/doc/EDOC1100279155/54c5c238/security-wpa2-wpa3-psk-sae)
