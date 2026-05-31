# support-web-portal

## Overview
Gogidix Management Portal - Built with **Hexagonal Architecture**

## Architecture Layers

### Domain Layer (`src/domain/`)
- Core business logic (framework-independent)
- Entities, Value Objects, Use Cases
- Domain Services, Events, Exceptions
- Repository Interfaces (Ports)

### Application Layer (`src/application/`)
- Application Services & Orchestration
- Data Transfer Objects (DTOs)
- Input/Output Ports

### Infrastructure Layer (`src/infrastructure/`)
- API Adapters (REST, GraphQL, WebSocket)
- Storage Adapters (Local, Session, IndexedDB)
- State Management (Zustand stores)
- Routing, Auth, Logging, Validation

### Presentation Layer (`src/presentation/`)
- React Components & Pages
- Custom Hooks, Contexts
- Styles & Themes

### Shared Layer (`src/shared/`)
- Configuration, Utilities, Types
- Constants, Mappers, Interceptors

## Getting Started

```bash
npm install
npm run dev
```

## Directory Structure

```
src/
├── domain/              # Core business logic
├── application/         # Application services
├── infrastructure/      # External dependencies (adapters)
├── presentation/        # UI components
├── shared/             # Cross-cutting concerns
└── features/           # Feature-based modules
```
