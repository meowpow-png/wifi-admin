# Shut down backend server
server-shutdown:
    @curl -X POST {{actuator-url}}/shutdown

# Start backend server
server-start:
    @./gradlew bootRun

# Check application health
server-health:
    @curl -s {{actuator-url}}/health | jq

# Synchronize platform and database
server-sync:
    @curl -X POST {{actuator-url}}/sync

# Set application logging level
[arg("level", help="Logging level")]
server-logging level:
    @curl -X POST {{actuator-url}}/logging/{{level}}

# Enable or disable SOAP payload logging
[arg("enabled", help="Whether SOAP payload logging is enabled", pattern="true|false")]
server-payload-logging enabled:
    @curl -X POST {{actuator-url}}/payload-logging/{{enabled}}
