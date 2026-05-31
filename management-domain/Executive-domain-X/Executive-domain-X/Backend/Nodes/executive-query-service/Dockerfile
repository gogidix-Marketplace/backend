# Multi-stage build for Executive Query Service
FROM node:18-alpine AS builder

WORKDIR /app

# Copy package files
COPY package*.json ./

# Install dependencies
RUN npm ci --only=production

# Copy source code
COPY src ./src

# Production stage
FROM node:18-alpine

WORKDIR /app

# Create non-root user
RUN addgroup -S gogidix && adduser -S gogidix -G gogidix
USER gogidix

# Copy node modules and source from builder
COPY --from=builder --chown=gogidix:gogidix /app/node_modules ./node_modules
COPY --from=builder --chown=gogidix:gogidix /app/src ./src
COPY --from=builder --chown=gogidix:gogidix /app/package*.json .*

# Expose port
EXPOSE 3001

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
    CMD node -e "require('http').get('http://localhost:3001/health', (r) => {process.exit(r.statusCode === 200 ? 0 : 1)})"

# Start application
CMD ["npm", "start"]
