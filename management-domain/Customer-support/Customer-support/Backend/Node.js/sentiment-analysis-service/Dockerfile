FROM node:18-alpine AS builder
WORKDIR /app
COPY package*.json tsconfig.json nest-cli.json ./
RUN npm ci && npm cache clean --force
COPY src ./src
RUN npm run build

FROM node:18-alpine AS production
WORKDIR /app
RUN apk add --no-cache dumb-init
RUN addgroup -g 1001 -S nodejs && adduser -S nodejs -u 1001
COPY --from=builder /app/dist ./dist
COPY --from=builder /app/node_modules ./node_modules
COPY --from=builder /app/package.json ./
RUN chown -R nodejs:nodejs /app
USER nodejs
EXPOSE 8114
HEALTHCHECK --interval=30s --timeout=10s --start-period=5s --retries=3 \
    CMD node -e "require('http').get('http://localhost:8114/api/v1/health', (r) => {process.exit(r.statusCode === 200 ? 0 : 1)})"
ENV NODE_ENV=production PORT=8114
ENTRYPOINT ["dumb-init", "--"]
CMD ["node", "dist/main.js"]
