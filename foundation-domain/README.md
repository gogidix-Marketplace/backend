# CargoNexus Shared Infrastructure (Tier 0)

This repository contains ALL shared infrastructure required by every CargoNexus domain and service.

## Contents

| Directory | Description |
|-----------|-------------|
| `shared-libraries/` | 12 Maven modules (Java, Node.js, Frontend shared libs) |
| `Shared-infrastructure-Hexgonal/` | Infrastructure services (API mgmt, communication, config, gateway, observability, security, storage) |
| `centralized-configuration/` | Config server, feature flags, audit, notification, environment service |
| `ai-platform/` | AI/ML services (Java + Node) |
| `platform/` | Platform service, subscription, usage metering |
| `shared-business/` | Shared business cores (admin, haulage, procurement, warehousing) |
| `infrastructure-config/` | Infrastructure configuration |
| `infrastructure-database/` | Database infrastructure |
| `infrastructure-devtools/` | Developer tooling |
| `infrastructure-lock-service/` | Distributed lock service |

## Usage by CI/CD

All domain CI/CD pipelines install shared libraries from this repo:

```yaml
SHARED_LIBS_REPO: cargoNexus-africa/CargoNexus-Shared-Infrastructure
SHARED_LIBS_PATH: shared-libraries
```

## Build

```bash
# Install shared libraries to local Maven repo
cd shared-libraries/Backend
mvn clean install -DskipTests

# Install shared Node.js libraries
cd shared-libraries/dev-tools
npm install
```
