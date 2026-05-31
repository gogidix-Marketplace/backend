# Multi-stage build for centralized-reporting service (Node.js)
FROM node:18-alpine AS build

# Set working directory
WORKDIR /app

# Copy package files for better layer caching
COPY centralized-reporting/package*.json ./

# Install dependencies
RUN npm ci --only=production

# Runtime stage
FROM node:18-alpine

# Install curl for health checks
RUN apk add --no-cache curl

# Create non-root user
RUN addgroup -g 1001 -S nodejs && \
    adduser -S nodejs -u 1001

# Set working directory
WORKDIR /app

# Copy node_modules from build stage
COPY --from=build /app/node_modules ./node_modules

# Copy source code
COPY --chown=nodejs:nodejs centralized-reporting/src ./src
COPY --chown=nodejs:nodejs centralized-reporting/package*.json ./

# Switch to non-root user
USER nodejs

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=30s --retries=3 \
    CMD curl -f http://localhost:8080/health || exit 1

# Entry point
CMD ["npm", "start"]