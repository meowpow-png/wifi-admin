# Note: Configurable CPE Synchronization

## Context

While implementing the scheduled platform synchronization, I needed to determine how to interpret the assignment requirement stating that both the synchronization schedule and the number of synchronized CPE devices should be configurable. The requirement did not specify how the synchronized devices should be identified, so I investigated what production scenario this requirement was likely intended to model.

> Izrada schedulera koji će sinkronizirati bazu i podatke s platforme u noćnim satima (konfigurabilno vrijeme i broj CPE-ova).

## Observations

The provided SOAP platform exposes operations only for retrieving or updating a single CPE configuration. It does not provide any mechanism for enumerating available devices. The mock platform contains a fixed set of seeded CPE identifiers (`CPE_001` through `CPE_012`), while the assignment explicitly refers to configuring the number of synchronized CPE devices rather than providing a list of identifiers.

Synchronizing every device managed by an external telecom platform would not be realistic in a production environment, suggesting that the scheduler is intended to synchronize only a predefined subset of platform-managed devices.

## Analysis

Enumerating every synchronized CPE identifier in configuration would satisfy the assignment but would not scale beyond the small mock dataset. Discovering devices directly from the SOAP platform was ruled out because the external platform is outside my control and the provided WSDL does not expose such functionality.

Synchronizing only devices already present in the local database was also considered. This approach would prevent newly created or externally modified platform configurations from ever being discovered and did not align with the assignment wording referring to a configurable number of synchronized devices.

The remaining interpretation was that the assignment models a predefined subset of devices whose identifiers follow a predictable naming convention. Instead of enumerating every identifier, I modeled the synchronized device set using a configurable identifier format and configurable device count.

## Conclusions

I concluded that the assignment most likely intends to model synchronization of a predefined subset of platform-managed devices rather than the entire external platform.

To reflect this interpretation, the implementation generates synchronized CPE identifiers from a configurable identifier format and configurable device count instead of maintaining an explicit list of identifiers. This approach remains practical for the assignment while avoiding configuration that would not scale in a production environment.

```properties
platform.cpe-id-format=CPE_%03d
platform.cpe-id-count=12
```

This conclusion is based on interpretation of the assignment rather than an explicitly documented requirement.

## Next Steps

No further implementation changes are required. This interpretation should be preserved unless future assignment revisions clarify the intended synchronization model.

## References

- [ADR-002: Synchronize Platform Data](../adr/002-adr-synchronize-platform-data.md)
- [Implementation: Synchronization](../IMPLEMENTATION.md#Synchronization)
