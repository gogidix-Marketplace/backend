# ML Model Training Service - Dockerfile
# Multi-stage build for optimized image size

# Stage 1: Base image with Python
FROM python:3.11-slim as base

# Set environment variables
ENV PYTHONUNBUFFERED=1 \
    PYTHONDONTWRITEBYTECODE=1 \
    PIP_NO_CACHE_DIR=1 \
    PIP_DISABLE_PIP_VERSION_CHECK=1

# Install system dependencies
RUN apt-get update && apt-get install -y --no-install-recommends \
    gcc \
    g++ \
    cmake \
    libgomp1 \
    && rm -rf /var/lib/apt/lists/*

# Stage 2: Build stage
FROM base as builder

WORKDIR /build

# Copy requirements first for better caching
COPY requirements.txt .

# Install Python dependencies
RUN pip install --user -r requirements.txt

# Stage 3: Final runtime image
FROM base as runtime

# Create non-root user
RUN groupadd -r mluser && useradd -r -g mluser mluser

WORKDIR /app

# Copy Python packages from builder
COPY --from=builder /root/.local /root/.local

# Make sure scripts in .local are usable
ENV PATH=/root/.local/bin:$PATH

# Copy application code
COPY app ./app
COPY scripts ./scripts

# Create necessary directories
RUN mkdir -p /app/models /app/data /app/logs /app/checkpoints && \
    chown -R mluser:mluser /app

# Switch to non-root user
USER mluser

# Expose port
EXPOSE 8001

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
    CMD python -c "import urllib.request; urllib.request.urlopen('http://localhost:8001/health')" || exit 1

# Run the application
CMD ["uvicorn", "app.main:app", "--host", "0.0.0.0", "--port", "8001"]
