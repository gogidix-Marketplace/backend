"""
Configuration for Predictive Analytics Service
"""

from pydantic import BaseSettings, Field


class Settings(BaseSettings):
    """Application settings"""

    SERVICE_NAME: str = "predictive-analytics-service"
    VERSION: str = "1.0.0"
    HOST: str = Field(default="0.0.0.0", env="PREDICTIVE_HOST")
    PORT: int = Field(default=8003, env="PREDICTIVE_PORT")
    DEBUG: bool = Field(default=False, env="PREDICTIVE_DEBUG")

    # Cache
    REDIS_URL: str = Field(default="redis://localhost:6379", env="REDIS_URL")
    CACHE_TTL: int = Field(default=3600, env="CACHE_TTL")

    # Database
    MONGODB_URL: str = Field(default="mongodb://localhost:27017", env="MONGODB_URL")
    MONGODB_DB_NAME: str = Field(default="predictive_analytics", env="MONGODB_DB_NAME")

    # Message queue
    KAFKA_BOOTSTRAP_SERVERS: str = Field(default="localhost:9092", env="KAFKA_BOOTSTRAP_SERVERS")

    class Config:
        env_file = ".env"
        case_sensitive = False


settings = Settings()
