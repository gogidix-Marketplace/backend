# Build stage
FROM node:18-alpine AS builder

WORKDIR /app

# Copy package files
COPY package*.json ./
COPY tsconfig.json ./
COPY tsconfig.node.json ./
COPY vite.config.ts ./

# Install dependencies
RUN npm ci

# Copy source code for all dashboards
COPY deployment-dashboard ./deployment-dashboard
COPY monitoring-dashboard ./monitoring-dashboard
COPY security-dashboard ./security-dashboard

# Build all dashboards
RUN cd deployment-dashboard && npm run build && \
    cd ../monitoring-dashboard && npm run build && \
    cd ../security-dashboard && npm run build

# Production stage
FROM nginx:alpine

# Copy custom nginx config
COPY <<EOF /etc/nginx/conf.d/default.conf
server {
    listen 80;
    server_name localhost;
    root /usr/share/nginx/html;
    index index.html;

    # Enable gzip compression
    gzip on;
    gzip_vary on;
    gzip_min_length 1024;
    gzip_types text/plain text/css text/xml text/javascript application/javascript application/xml+rss application/json;

    # Deployment dashboard
    location /deployment {
        alias /usr/share/nginx/html/deployment-dashboard;
        try_files \$uri \$uri/ /deployment/index.html;
    }

    # Monitoring dashboard
    location /monitoring {
        alias /usr/share/nginx/html/monitoring-dashboard;
        try_files \$uri \$uri/ /monitoring/index.html;
    }

    # Security dashboard
    location /security {
        alias /usr/share/nginx/html/security-dashboard;
        try_files \$uri \$uri/ /security/index.html;
    }

    # Default redirect to monitoring dashboard
    location = / {
        return 301 /monitoring;
    }

    # Cache static assets
    location ~* \.(js|css|png|jpg|jpeg|gif|ico|svg|woff|woff2|ttf|eot)$ {
        expires 1y;
        add_header Cache-Control "public, immutable";
    }

    # Security headers
    add_header X-Frame-Options "SAMEORIGIN" always;
    add_header X-Content-Type-Options "nosniff" always;
    add_header X-XSS-Protection "1; mode=block" always;

    # Health check
    location /health {
        access_log off;
        return 200 "healthy\n";
        add_header Content-Type text/plain;
    }
}
EOF

# Copy built files from builder
COPY --from=builder /app/deployment-dashboard/dist /usr/share/nginx/html/deployment-dashboard
COPY --from=builder /app/monitoring-dashboard/dist /usr/share/nginx/html/monitoring-dashboard
COPY --from=builder /app/security-dashboard/dist /usr/share/nginx/html/security-dashboard

# Expose port
EXPOSE 80

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=10s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost/health || exit 1

# Start nginx
CMD ["nginx", "-g", "daemon off;"]
