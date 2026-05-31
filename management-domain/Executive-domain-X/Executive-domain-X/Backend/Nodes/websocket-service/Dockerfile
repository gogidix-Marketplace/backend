# Multi-stage build for WebSocket Service
FROM node:18-alpine AS builder
WORKDIR /app
COPY package*.json ./
RUN npm ci --only=production
COPY src ./src

FROM node:18-alpine
WORKDIR /app
RUN addgroup -S gogidix && adduser -S gogidix -G gogidix
USER gogidix
COPY --from=builder --chown=gogidix:gogidix /app/node_modules ./node_modules
COPY --from=builder --chown=gogidix:gogidix /app/src ./src
COPY --from=builder --chown=gogidix:gogidix /app/package*.json .*
EXPOSE 3004
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
    CMD node -e "require('http').get('http://localhost:3004/health', (r) => {process.exit(r.statusCode === 200 ? 0 : 1)})"
CMD ["npm", "start"]
