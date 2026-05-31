# AI Security Service - Service Contract

## Service Responsibility
Core security functions including encryption and secrets management.

## Core Functionality
- Data encryption/decryption
- Secrets management
- Key rotation
- Security policies

## API Contracts

### 1. Encrypt Data
**Endpoint:** `POST /api/v1/security/encrypt`

**Input:**
```json
{
  "data": "string",
  "algorithm": "AES256|RSA2048"
}
```

**Output:**
```json
{
  "encryptedData": "string (base64)",
  "keyId": "string",
  "algorithm": "string"
}
```

### 2. Decrypt Data
**Endpoint:** `POST /api/v1/security/decrypt`

## Business Rules
- Key rotation: every 90 days
- Max data size: 10MB
- Encryption required at rest

## Error Conditions
- Invalid key (400)
- Decryption failure (422)
