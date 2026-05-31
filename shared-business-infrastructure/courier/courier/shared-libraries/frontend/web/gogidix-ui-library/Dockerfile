# Enterprise-Grade Node.js Service Dockerfile Template
FROM node:18-alpine AS build

# Install security updates
RUN apk update && apk upgrade && apk add --no-cache dumb-init

# Create non-root user
RUN addgroup -g 1001 -S appuser && adduser -u 1001 -S appuser -G appuser

# Set working directory
WORKDIR /app

# Copy package files first (for layer caching)
COPY package*.json ./

# Install all dependencies first (needed for build)
RUN npm install && npm cache clean --force

# Copy source code
COPY . .

# Build the application
RUN npm run build

# Create optimized production image
FROM node:18-alpine AS production

# Install security updates
RUN apk update && apk upgrade && apk add --no-cache dumb-init

# Create non-root user
RUN addgroup -g 1001 -S appuser && adduser -u 1001 -S appuser -G appuser

# Set working directory
WORKDIR /app

# Copy package files for production install
COPY package*.json ./

# Install only production dependencies
RUN npm ci --omit=dev && npm cache clean --force

# Copy built application from build stage
COPY --from=build /app/dist ./dist

# Change ownership to non-root user
RUN chown -R appuser:appuser /app

# Switch to non-root user
USER appuser

# Default port (override in individual services)
EXPOSE 3700

# Health check (override URL in individual services)
HEALTHCHECK --interval=30s --timeout=10s --retries=3 --start-period=40s \
    CMD wget --no-verbose --tries=1 --spider http://localhost:3700/health || exit 1

# Application entry point
ENTRYPOINT ["/usr/bin/dumb-init", "--"]
CMD ["npm", "start"]