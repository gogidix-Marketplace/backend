"""
Configuration settings for ML Model Training Service
"""

import os
from typing import Optional
from pydantic import BaseSettings, Field


class Settings(BaseSettings):
    """Application settings"""

    # Service configuration
    SERVICE_NAME: str = "ml-model-training-service"
    VERSION: str = "1.0.0"
    HOST: str = Field(default="0.0.0.0", env="ML_TRAINING_HOST")
    PORT: int = Field(default=8001, env="ML_TRAINING_PORT")
    DEBUG: bool = Field(default=False, env="ML_TRAINING_DEBUG")

    # Storage paths
    MODEL_STORAGE_PATH: str = Field(default="/app/models", env="MODEL_STORAGE_PATH")
    DATA_STORAGE_PATH: str = Field(default="/app/data", env="DATA_STORAGE_PATH")
    LOG_STORAGE_PATH: str = Field(default="/app/logs", env="LOG_STORAGE_PATH")

    # Database
    MONGODB_URL: str = Field(default="mongodb://localhost:27017", env="MONGODB_URL")
    MONGODB_DB_NAME: str = Field(default="ml_training", env="MONGODB_DB_NAME")

    # Redis
    REDIS_URL: str = Field(default="redis://localhost:6379", env="REDIS_URL")
    REDIS_TTL: int = Field(default=3600, env="REDIS_TTL")

    # Message queue
    KAFKA_BOOTSTRAP_SERVERS: str = Field(
        default="localhost:9092",
        env="KAFKA_BOOTSTRAP_SERVERS"
    )
    KAFKA_TOPIC_TRAINING: str = Field(
        default="ml.training.jobs",
        env="KAFKA_TOPIC_TRAINING"
    )

    # Training configuration
    MAX_CONCURRENT_JOBS: int = Field(default=5, env="MAX_CONCURRENT_JOBS")
    JOB_TIMEOUT_SECONDS: int = Field(default=3600, env="JOB_TIMEOUT_SECONDS")
    MAX_MODEL_SIZE_MB: int = Field(default=500, env="MAX_MODEL_SIZE_MB")

    # Model registry
    MODEL_REGISTRY_ENABLED: bool = Field(default=True, env="MODEL_REGISTRY_ENABLED")
    MODEL_VERSIONING: bool = Field(default=True, env="MODEL_VERSIONING")

    # Security
    API_KEY_REQUIRED: bool = Field(default=False, env="API_KEY_REQUIRED")
    API_KEY: Optional[str] = Field(default=None, env="API_KEY")

    # Feature store integration
    FEATURE_STORE_URL: str = Field(
        default="http://localhost:8002",
        env="FEATURE_STORE_URL"
    )

    # Observability
    METRICS_ENABLED: bool = Field(default=True, env="METRICS_ENABLED")
    TRACING_ENABLED: bool = Field(default=False, env="TRACING_ENABLED")
    LOG_LEVEL: str = Field(default="INFO", env="LOG_LEVEL")

    class Config:
        env_file = ".env"
        env_file_encoding = "utf-8"
        case_sensitive = False


# Global settings instance
settings = Settings()
