# Note: Password Validation Requirement

## Context

While implementing request validation, I needed to determine whether a password should be mandatory for different Wi-Fi configuration types.

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

I researched the supported Wi-Fi authentication modes and found that `WEP`, `WPA_PSK`, `WPA2_PSK`, and `WPA3_SAE` all rely on a shared secret for client authentication and therefore require a password. By contrast, `OPEN` does not require authentication, while `WPA2_ENTERPRISE` uses 802.1X/RADIUS credentials rather than a shared network password.

Although the OpenAPI specification does not explicitly define this as a request validation rule, the documented `400 Bad Request` response explicitly identifies a missing password for `WPA2_PSK` as an example of invalid input. I interpreted this as expressing the intended API behavior and implemented the corresponding validation.

It is worth noting that Wi-Fi encryption does not universally imply password-based authentication. Modern standards also support passwordless encrypted networks (for example, Enhanced Open / OWE), although those authentication modes are not represented by this API.

## Conclusions

I implemented request validation requiring a password for all password-protected authentication modes supported by the API (`WEP`, `WPA_PSK`, `WPA2_PSK`, and `WPA3_SAE`).

The OpenAPI specification remains somewhat ambiguous because this requirement is documented through an example error response rather than expressed as a formal schema constraint.

In a real project, I would still clarify the expected behavior with the API owner or domain experts to ensure the documented behavior reflects the intended contract.

## Next Steps

- Confirm that the implemented validation matches the intended API behavior
- Express the password requirement as an explicit validation constraint in the API specification where possible

## References

- [Android Open Source Project – Enhanced Open (OWE)](https://source.android.com/docs/core/connect/wifi-wpa3-owe)
- [Smallstep – Everything You Need to Know About Wi-Fi Security](https://smallstep.com/blog/everything-wifi-security/)
- [HPE Aruba Networking – WPA3-Personal (SAE)](https://arubanetworking.hpe.com/techdocs/aos/wifi-design-deploy/security/modes/wpa3-personal/)
- [Huawei Enterprise Documentation – Security – WPA2/WPA3-PSK-SAE](https://support.huawei.com/enterprise/en/doc/EDOC1100279155/54c5c238/security-wpa2-wpa3-psk-sae)
- [Arista Networks – WPA3-Personal: Simultaneous Authentication of Equals (SAE)](https://arista.my.site.com/AristaCommunity/s/article/WPA3-Personal-Simultaneous-Authentication-of-Equals-SAE)
