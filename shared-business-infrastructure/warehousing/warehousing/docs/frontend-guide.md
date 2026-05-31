# Frontend Development Guide - Warehousing Domain

## Overview

This guide covers frontend development for the Shared Warehousing Core domain, including web dashboards and mobile applications.

## Table of Contents

- [Frontend Applications](#frontend-applications)
- [Technology Stack](#technology-stack)
- [Project Structure](#project-structure)
- [Development Setup](#development-setup)
- [Component Library](#component-library)
- [State Management](#state-management)
- [API Integration](#api-integration)
- [Styling](#styling)
- [Testing](#testing)
- [Build and Deployment](#build-and-deployment)

---

## Frontend Applications

### Web Applications

| Application | Framework | Port | Description |
|-------------|-----------|------|-------------|
| Partners Dashboard | Next.js 14 | 3001 | Warehouse operations management |
| Private Storage Dashboard | Next.js 14 | 3002 | Customer self-storage portal |

### Mobile Applications

| Application | Framework | Description |
|-------------|-----------|-------------|
| Warehouse Staff App | React Native + Expo | Picking, packing, warehouse operations |
| Vendor Self Storage App | React Native + Expo | Vendor inventory and location management |

---

## Technology Stack

### Web Stack

```json
{
  "dependencies": {
    "next": "14.2.0",
    "react": "^18.3.0",
    "react-dom": "^18.3.0",
    "@reduxjs/toolkit": "^2.2.0",
    "@tanstack/react-query": "^5.28.0",
    "antd": "^5.15.0",
    "tailwindcss": "^3.4.1",
    "axios": "^1.7.0",
    "socket.io-client": "^4.7.0",
    "recharts": "^2.12.0",
    "dayjs": "^1.11.10"
  }
}
```

### Mobile Stack

```json
{
  "dependencies": {
    "expo": "~51.0.0",
    "expo-router": "~3.5.0",
    "react": "18.2.0",
    "react-native": "0.74.1",
    "@reduxjs/toolkit": "^2.2.0",
    "axios": "^1.7.0",
    "expo-camera": "~15.0.0",
    "expo-barcode-scanner": "~13.0.0",
    "expo-local-authentication": "~14.0.0",
    "@react-navigation/native": "^6.1.17"
  }
}
```

---

## Project Structure

### Web Application Structure

```
partners-dashboard/
├── src/
│   ├── app/                    # Next.js App Router
│   │   ├── (auth)/             # Auth routes
│   │   │   ├── login/
│   │   │   └── layout.tsx
│   │   ├── dashboard/          # Dashboard routes
│   │   │   ├── inventory/
│   │   │   ├── fulfillment/
│   │   │   └── storage/
│   │   ├── layout.tsx
│   │   └── page.tsx
│   ├── components/
│   │   ├── ui/                 # Reusable UI components
│   │   ├── inventory/          # Inventory components
│   │   ├── fulfillment/        # Fulfillment components
│   │   └── shared/             # Shared components
│   ├── lib/
│   │   ├── api/                # API clients
│   │   ├── hooks/              # Custom hooks
│   │   ├── store/              # Redux store
│   │   └── utils/              # Utility functions
│   ├── styles/
│   │   └── globals.css
│   └── types/
│       └── index.d.ts
├── public/
└── package.json
```

### Mobile Application Structure

```
warehouse-staff-app/
├── app/                        # Expo Router
│   ├── (auth)/
│   │   ├── login.tsx
│   │   └── _layout.tsx
│   ├── (tabs)/
│   │   ├── tasks/
│   │   ├── profile/
│   │   └── _layout.tsx
│   ├── _layout.tsx
│   └── index.tsx
├── components/
│   ├── ui/
│   ├── tasks/
│   └── shared/
├── hooks/
│   ├── useTasks.ts
│   ├── useBarcodeScanner.ts
│   └── useBiometric.ts
├── services/
│   ├── api/
│   └── storage/
├── store/
│   └── slices/
├── assets/
├── app.json
└── package.json
```

---

## Development Setup

### Web Application

```bash
cd Frontends/Web/partners-dashboard

# Install dependencies
npm install

# Start development server
npm run dev

# Run type checking
npm run type-check

# Run linting
npm run lint

# Build for production
npm run build

# Start production server
npm run start
```

### Mobile Application

```bash
cd Frontends/Mobile/warehouse-staff-app

# Install dependencies
npm install

# Start development server (with Expo)
npm start

# Run on iOS simulator
npm run ios

# Run on Android emulator
npm run android

# Run in web browser
npm run web
```

### Environment Variables

Create `.env.local` file:

```bash
# API URLs
NEXT_PUBLIC_API_BASE_URL=http://localhost:8080
NEXT_PUBLIC_INVENTORY_API_URL=http://localhost:8081/api/v1/inventory
NEXT_PUBLIC_FULFILLMENT_API_URL=http://localhost:8085/api/v1/fulfillment

# Authentication
NEXT_PUBLIC_AUTH_DOMAIN=your-auth-domain
NEXT_PUBLIC_AUTH_CLIENT_ID=your-client-id

# Feature Flags
NEXT_PUBLIC_ENABLE_ANALYTICS=true
NEXT_PUBLIC_ENABLE_WEBSOCKETS=true
```

---

## Component Library

### Ant Design (Web)

```tsx
import { Button, Table, Form, Input, Select } from 'antd';
import { PlusOutlined } from '@ant-design/icons';

interface InventoryListProps {
  items: InventoryItem[];
  onAdd: () => void;
  onEdit: (item: InventoryItem) => void;
}

export function InventoryList({ items, onAdd, onEdit }: InventoryListProps) {
  const columns = [
    {
      title: 'SKU',
      dataIndex: 'sku',
      key: 'sku',
      sorter: true,
    },
    {
      title: 'Name',
      dataIndex: 'name',
      key: 'name',
    },
    {
      title: 'Quantity',
      dataIndex: 'quantity',
      key: 'quantity',
      render: (qty: number) => (
        <span className={qty < 10 ? 'text-red-500' : ''}>
          {qty}
        </span>
      ),
    },
    {
      title: 'Actions',
      key: 'actions',
      render: (_, record) => (
        <Button type="link" onClick={() => onEdit(record)}>
          Edit
        </Button>
      ),
    },
  ];

  return (
    <div className="inventory-list">
      <div className="mb-4 flex justify-between">
        <h2>Inventory Items</h2>
        <Button type="primary" icon={<PlusOutlined />} onClick={onAdd}>
          Add Item
        </Button>
      </div>
      <Table
        columns={columns}
        dataSource={items}
        rowKey="id"
        pagination={{ pageSize: 20 }}
      />
    </div>
  );
}
```

### React Native Paper (Mobile)

```tsx
import { Card, Title, Paragraph, Button } from 'react-native-paper';
import { View, StyleSheet } from 'react-native';

interface TaskCardProps {
  task: PickingTask;
  onStart: () => void;
}

export function TaskCard({ task, onStart }: TaskCardProps) {
  return (
    <Card style={styles.card}>
      <Card.Title title={`Task ${task.taskNumber}`} />
      <Card.Content>
        <View style={styles.row}>
          <Paragraph>Type: {task.type}</Paragraph>
        </View>
        <View style={styles.row}>
          <Paragraph>Items: {task.itemCount}</Paragraph>
        </View>
        <View style={styles.row}>
          <Paragraph>Zone: {task.zone}</Paragraph>
        </View>
      </Card.Content>
      <Card.Actions>
        <Button mode="contained" onPress={onStart}>
          Start Task
        </Button>
      </Card.Actions>
    </Card>
  );
}

const styles = StyleSheet.create({
  card: {
    marginHorizontal: 16,
    marginVertical: 8,
  },
  row: {
    flexDirection: 'row',
    marginBottom: 4,
  },
});
```

---

## State Management

### Redux Toolkit (Web)

```typescript
// lib/store/features/inventorySlice.ts
import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import { inventoryAPI } from '@/lib/api/inventory';

interface InventoryState {
  items: InventoryItem[];
  loading: boolean;
  error: string | null;
  selectedItemId: string | null;
}

const initialState: InventoryState = {
  items: [],
  loading: false,
  error: null,
  selectedItemId: null,
};

export const fetchItems = createAsyncThunk(
  'inventory/fetchItems',
  async (params: { page: number; size: number }) => {
    const response = await inventoryAPI.getItems(params);
    return response.data;
  }
);

export const inventorySlice = createSlice({
  name: 'inventory',
  initialState,
  reducers: {
    selectItem: (state, action) => {
      state.selectedItemId = action.payload;
    },
    clearSelection: (state) => {
      state.selectedItemId = null;
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(fetchItems.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchItems.fulfilled, (state, action) => {
        state.loading = false;
        state.items = action.payload.content;
      })
      .addCase(fetchItems.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || 'Failed to fetch items';
      });
  },
});

export const { selectItem, clearSelection } = inventorySlice.actions;
export default inventorySlice.reducer;
```

### React Query (Server State)

```typescript
// lib/hooks/useInventory.ts
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { inventoryAPI } from '@/lib/api/inventory';

export function useInventoryItems(page = 0, size = 20) {
  return useQuery({
    queryKey: ['inventory', page, size],
    queryFn: () => inventoryAPI.getItems({ page, size }),
    staleTime: 30000, // 30 seconds
  });
}

export function useCreateItem() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: inventoryAPI.createItem,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['inventory'] });
    },
  });
}

export function useUpdateItem() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({ id, data }: { id: string; data: Partial<InventoryItem> }) =>
      inventoryAPI.updateItem(id, data),
    onSuccess: (_, variables) => {
      queryClient.invalidateQueries({ queryKey: ['inventory'] });
      queryClient.invalidateQueries({ queryKey: ['item', variables.id] });
    },
  });
}
```

---

## API Integration

### Axios Configuration

```typescript
// lib/api/client.ts
import axios from 'axios';
import type { AxiosInstance, AxiosRequestConfig } from 'axios';

const BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL || 'http://localhost:8080';

export function createAPIClient(
  config?: AxiosRequestConfig
): AxiosInstance {
  const client = axios.create({
    baseURL: BASE_URL,
    timeout: 10000,
    headers: {
      'Content-Type': 'application/json',
    },
    ...config,
  });

  // Request interceptor
  client.interceptors.request.use(
    (config) => {
      const token = localStorage.getItem('access_token');
      if (token) {
        config.headers.Authorization = `Bearer ${token}`;
      }

      // Add tenant ID
      const tenantId = localStorage.getItem('tenant_id') || 'default';
      config.headers['X-Tenant-ID'] = tenantId;

      return config;
    },
    (error) => Promise.reject(error)
  );

  // Response interceptor
  client.interceptors.response.use(
    (response) => response,
    async (error) => {
      if (error.response?.status === 401) {
        // Handle token refresh
        const newToken = await refreshAccessToken();
        if (newToken) {
          error.config.headers.Authorization = `Bearer ${newToken}`;
          return client.request(error.config);
        }
      }
      return Promise.reject(error);
    }
  );

  return client;
}

export const apiClient = createAPIClient();
```

### Inventory API

```typescript
// lib/api/inventory.ts
import { apiClient } from './client';

export const inventoryAPI = {
  getItems: (params: { page: number; size: number }) =>
    apiClient.get('/api/v1/inventory/items', { params }),

  getItem: (id: string) =>
    apiClient.get(`/api/v1/inventory/items/${id}`),

  createItem: (data: CreateItemRequest) =>
    apiClient.post('/api/v1/inventory/items', data),

  updateItem: (id: string, data: Partial<InventoryItem>) =>
    apiClient.put(`/api/v1/inventory/items/${id}`, data),

  deleteItem: (id: string) =>
    apiClient.delete(`/api/v1/inventory/items/${id}`),

  adjustQuantity: (data: AdjustmentRequest) =>
    apiClient.post('/api/v1/inventory/adjust', data),

  getLowStockItems: (threshold: number) =>
    apiClient.get('/api/v1/inventory/items/low-stock', { params: { threshold } }),
};
```

---

## Styling

### Tailwind CSS Configuration

```javascript
// tailwind.config.js
module.exports = {
  content: [
    './src/pages/**/*.{js,ts,jsx,tsx,mdx}',
    './src/components/**/*.{js,ts,jsx,tsx,mdx}',
  ],
  theme: {
    extend: {
      colors: {
        primary: {
          50: '#f0f9ff',
          500: '#0ea5e9',
          600: '#0284c7',
          700: '#0369a1',
        },
      },
      fontFamily: {
        sans: ['var(--font-inter)'],
      },
    },
  },
  plugins: [
    require('@tailwindcss/forms'),
    require('@tailwindcss/typography'),
  ],
};
```

### Global Styles

```css
/* styles/globals.css */
@tailwind base;
@tailwind components;
@tailwind utilities;

:root {
  --foreground-rgb: 0, 0, 0;
  --background-start-rgb: 214, 219, 220;
  --background-end-rgb: 255, 255, 255;
}

@media (prefers-color-scheme: dark) {
  :root {
    --foreground-rgb: 255, 255, 255;
    --background-start-rgb: 0, 0, 0;
    --background-end-rgb: 0, 0, 0;
  }
}

body {
  color: rgb(var(--foreground-rgb));
  background: linear-gradient(
      to bottom,
      transparent,
      rgb(var(--background-end-rgb))
    )
    rgb(var(--background-start-rgb));
}

@layer components {
  .btn {
    @apply px-4 py-2 rounded-md font-medium transition-colors;
  }

  .btn-primary {
    @apply bg-primary-600 text-white hover:bg-primary-700;
  }

  .input {
    @apply w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary-500;
  }
}
```

---

## Testing

### Component Tests (Web)

```typescript
// components/inventory/__tests__/InventoryList.test.tsx
import { render, screen, fireEvent } from '@testing-library/react';
import { InventoryList } from '../InventoryList';

describe('InventoryList', () => {
  const mockItems = [
    { id: '1', sku: 'ITEM-001', name: 'Item 1', quantity: 100 },
    { id: '2', sku: 'ITEM-002', name: 'Item 2', quantity: 5 },
  ];

  it('renders inventory items', () => {
    render(<InventoryList items={mockItems} onAdd={jest.fn()} onEdit={jest.fn()} />);

    expect(screen.getByText('ITEM-001')).toBeInTheDocument();
    expect(screen.getByText('ITEM-002')).toBeInTheDocument();
  });

  it('calls onAdd when Add button is clicked', () => {
    const onAdd = jest.fn();
    render(<InventoryList items={mockItems} onAdd={onAdd} onEdit={jest.fn()} />);

    fireEvent.click(screen.getByText('Add Item'));
    expect(onAdd).toHaveBeenCalled();
  });

  it('highlights low stock items', () => {
    render(<InventoryList items={mockItems} onAdd={jest.fn()} onEdit={jest.fn()} />);

    const lowStockItem = screen.getByText('5');
    expect(lowStockItem).toHaveClass('text-red-500');
  });
});
```

### Mobile Tests

```typescript
// components/__tests__/TaskCard.test.tsx
import { render } from '@testing-library/react-native';
import { TaskCard } from '../TaskCard';

describe('TaskCard', () => {
  const mockTask = {
    id: '1',
    taskNumber: 'TASK-001',
    type: 'PICKING',
    itemCount: 10,
    zone: 'ZONE-A',
  };

  it('renders task information', () => {
    const { getByText } = render(
      <TaskCard task={mockTask} onStart={jest.fn()} />
    );

    expect(getByText('Task TASK-001')).toBeTruthy();
    expect(getByText('Type: PICKING')).toBeTruthy();
    expect(getByText('Items: 10')).toBeTruthy();
    expect(getByText('Zone: ZONE-A')).toBeTruthy();
  });

  it('calls onStart when Start Task button is pressed', () => {
    const onStart = jest.fn();
    const { getByText } = render(
      <TaskCard task={mockTask} onStart={onStart} />
    );

    fireEvent.press(getByText('Start Task'));
    expect(onStart).toHaveBeenCalled();
  });
});
```

---

## Build and Deployment

### Web Build

```bash
# Production build
npm run build

# The build output is in .next/ directory
# Contains optimized static assets and server bundles
```

### Docker Build

```dockerfile
# Dockerfile for Next.js app
FROM node:20-alpine AS deps
WORKDIR /app
COPY package*.json ./
RUN npm ci

FROM node:20-alpine AS builder
WORKDIR /app
COPY --from=deps /app/node_modules ./node_modules
COPY . .
RUN npm run build

FROM node:20-alpine AS runner
WORKDIR /app
ENV NODE_ENV production
COPY --from=builder /app/.next/standalone ./
COPY --from=builder /app/.next/static ./.next/static
EXPOSE 3000
CMD ["node", "server.js"]
```

### Mobile Build

```bash
# Build for iOS
eas build --platform ios

# Build for Android
eas build --platform android

# Build for both
eas build --platform all
```

---

## Real-time Updates

### WebSocket Integration

```typescript
// lib/hooks/useWebSocket.ts
import { useEffect, useRef } from 'react';
import { io, Socket } from 'socket.io-client';

export function useWebSocket(event: string, callback: (data: any) => void) {
  const socketRef = useRef<Socket | null>(null);

  useEffect(() => {
    const token = localStorage.getItem('access_token');
    socketRef.current = io(process.env.NEXT_PUBLIC_WS_URL || 'ws://localhost:8080', {
      auth: { token },
      transports: ['websocket'],
    });

    socketRef.current.on(event, callback);

    return () => {
      socketRef.current?.disconnect();
    };
  }, [event, callback]);

  return socketRef.current;
}

// Usage
function InventoryDashboard() {
  useWebSocket('inventory-updated', (data) => {
    console.log('Inventory updated:', data);
    // Refresh data or update state
  });

  return <div>...</div>;
}
```

---

## Performance Optimization

### Code Splitting

```typescript
// Dynamic imports for heavy components
import dynamic from 'next/dynamic';

const InventoryChart = dynamic(() => import('@/components/inventory/InventoryChart'), {
  loading: () => <div>Loading chart...</div>,
  ssr: false,
});

function Dashboard() {
  return (
    <div>
      <h2>Inventory Overview</h2>
      <InventoryChart />
    </div>
  );
}
```

### Image Optimization

```tsx
import Image from 'next/image';

export function ProductImage({ src, alt }: { src: string; alt: string }) {
  return (
    <Image
      src={src}
      alt={alt}
      width={200}
      height={200}
      placeholder="blur"
      blurDataURL="data:image/jpeg;base64,/9j/4AAQSkZJRg..."
    />
  );
}
```

---

## Accessibility

### ARIA Labels

```tsx
<button
  aria-label="Create new inventory item"
  onClick={onCreate}
>
  <PlusIcon aria-hidden="true" />
</button>
```

### Keyboard Navigation

```tsx
<div
  role="listbox"
  tabIndex={0}
  onKeyDown={(e) => {
    if (e.key === 'Enter') {
      // Handle selection
    }
  }}
>
  {items.map((item) => (
    <div
      key={item.id}
      role="option"
      tabIndex={-1}
      aria-selected={selectedItemId === item.id}
    >
      {item.name}
    </div>
  ))}
</div>
```
