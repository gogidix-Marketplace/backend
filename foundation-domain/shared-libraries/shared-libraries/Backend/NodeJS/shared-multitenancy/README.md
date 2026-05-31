# MongoDB Multi-Tenant Library for Node.js

Node.js/Express library for multi-tenant MongoDB applications.

## Installation

```bash
npm install @gogidix/shared-multitenancy
```

## Quick Start

```javascript
const express = require('express');
const { tenantMiddleware, findDocuments, insertDocument } = require('@gogidix/shared-multitenancy');

const app = express();

// 1. Add tenant middleware
app.use(tenantMiddleware);

// 2. Use in routes
app.get('/api/users', async (req, res) => {
    const users = await findDocuments('users', { status: 'active' });
    // Automatically filtered by req.tenantId
    res.json(users);
});

app.post('/api/users', async (req, res) => {
    const user = await insertDocument('users', req.body);
    // Automatically adds req.tenantId
    res.status(201).json(user);
});

// 3. Headers required
// X-Tenant-ID: acme-corp
// X-Tenant-Type: ORGANIZATION
```

## Features

- ✅ Automatic tenant filtering for all queries
- ✅ Soft delete support
- ✅ Tenant context management
- ✅ Express middleware
- ✅ MongoDB connection pooling
- ✅ Background job support (setCurrentTenant)

## API Documentation

See `index.js` for detailed API documentation.

## License

MIT
