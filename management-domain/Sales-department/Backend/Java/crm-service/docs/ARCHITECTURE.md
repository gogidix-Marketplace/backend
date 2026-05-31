# CRM Service - Architecture Documentation

## Overview

The CRM Service manages customer relationships, interactions, and account information for the Sales Department.

## Domain Model

### Account Entity

- `accountId`: Unique identifier
- `tenantId`: Multi-tenant isolation
- `accountName`: Account name
- `accountType`: Type (PROSPECT, CUSTOMER, PARTNER)
- `industry`: Industry classification
- `size`: Company size
- `website`: Company website
- `phone`: Phone number
- `billingAddress`: Billing address
- `shippingAddress`: Shipping address
- `ownerId`: Account owner
- `tier`: Customer tier (BRONZE, SILVER, GOLD, PLATINUM)

### Contact Entity

- `contactId`: Unique identifier
- `accountId`: Associated account
- `firstName`: First name
- `lastName`: Last name
- `email`: Email address
- `phone`: Phone number
- `title`: Job title
- `isPrimary`: Primary contact indicator
- `contactSource`: How contact was acquired

### Interaction Entity

- `interactionId`: Unique identifier
- `accountId`: Related account
- `contactId`: Related contact
- `type`: Type (CALL, EMAIL, MEETING, NOTE)
- `subject`: Interaction subject
- `notes`: Interaction notes
- `timestamp`: When interaction occurred
- `userId': User who created interaction

## Application Services

### AccountCommandService

- `createAccount()`: Create new account
- `updateAccount()`: Update account details
- `mergeAccounts()`: Merge duplicate accounts
- `assignOwner()`: Assign account owner
- `updateTier()`: Update customer tier

### AccountQueryService

- `getAccountById()`: Get account details
- `getAccountsByOwner()`: Get owner's accounts
- `getAccountsByTier()`: Filter by tier
- `searchAccounts()`: Search accounts

### ContactCommandService

- `createContact()`: Create new contact
- `updateContact()`: Update contact details
- `linkToAccount()`: Link to account
- `setPrimary()`: Set as primary contact

### InteractionCommandService

- `logInteraction()`: Log customer interaction
- `updateInteraction()`: Update interaction details
- `deleteInteraction()`: Delete interaction

## Technology Stack

- **Language**: Java 17
- **Framework**: Spring Boot 3.1.5
- **Database**: MongoDB
- **Messaging**: Apache Kafka
