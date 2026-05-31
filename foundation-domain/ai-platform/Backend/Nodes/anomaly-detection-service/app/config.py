"""
Configuration for Anomaly Detection Service
"""

from pydantic import BaseSettings, Field


class Settings(BaseSettings):
    """Application settings"""

    SERVICE_NAME: str = "anomaly-detection-service"
    VERSION: str = "1.0.0"
    HOST: str = Field(default="0.0.0.0", env="ANOMALY_HOST")
    PORT: int = Field(default=8005, env="ANOMALY_PORT")
    DEBUG: bool = Field(default=False, env="ANOMALY_DEBUG")

    # Database
    MONGODB_URL: str = Field(default="mongodb://localhost:27017", env="MONGODB_URL")
    MONGODB_DB_NAME: str = Field(default="anomaly_detection", env="MONGODB_DB_NAME")

    # Cache
    REDIS_URL: str = Field(default="redis://localhost:6379", env="REDIS_URL")

    # Message queue
    KAFKA_BOOTSTRAP_SERVERS: str = Field(default="localhost:9092", env="KAFKA_BOOTSTRAP_SERVERS")
    ALERTS_TOPIC: str = Field(default="anomaly.alerts", env="ALERTS_TOPIC")

    # Alert settings
    ALERT_RETENTION_HOURS: int = Field(default=168, env="ALERT_RETENTION_HOURS")  # 7 days

    class Config:
        env_file = ".env"
        case_sensitive = False


settings = Settings()
