"""
Configuration for NLP Service
"""

from pydantic import BaseSettings, Field


class Settings(BaseSettings):
    """Application settings"""

    SERVICE_NAME: str = "nlp-service"
    VERSION: str = "1.0.0"
    HOST: str = Field(default="0.0.0.0", env="NLP_SERVICE_HOST")
    PORT: int = Field(default=8002, env="NLP_SERVICE_PORT")
    DEBUG: bool = Field(default=False, env="NLP_SERVICE_DEBUG")

    # Model storage
    MODEL_STORAGE_PATH: str = Field(default="/app/models", env="MODEL_STORAGE_PATH")

    # Cache
    REDIS_URL: str = Field(default="redis://localhost:6379", env="REDIS_URL")
    CACHE_TTL: int = Field(default=3600, env="CACHE_TTL")

    # Database
    MONGODB_URL: str = Field(default="mongodb://localhost:27017", env="MONGODB_URL")
    MONGODB_DB_NAME: str = Field(default="nlp_service", env="MONGODB_DB_NAME")

    # Message queue
    KAFKA_BOOTSTRAP_SERVERS: str = Field(default="localhost:9092", env="KAFKA_BOOTSTRAP_SERVERS")

    # API limits
    MAX_TEXT_LENGTH: int = Field(default=10000, env="MAX_TEXT_LENGTH")
    MAX_BATCH_SIZE: int = Field(default=100, env="MAX_BATCH_SIZE")

    class Config:
        env_file = ".env"
        case_sensitive = False


settings = Settings()
