"""
Configuration for Recommendation Engine Service
"""

from pydantic import BaseSettings, Field


class Settings(BaseSettings):
    """Application settings"""

    SERVICE_NAME: str = "recommendation-service"
    VERSION: str = "1.0.0"
    HOST: str = Field(default="0.0.0.0", env="RECOMMENDATION_HOST")
    PORT: int = Field(default=8006, env="RECOMMENDATION_PORT")
    DEBUG: bool = Field(default=False, env="RECOMMENDATION_DEBUG")

    # Database
    MONGODB_URL: str = Field(default="mongodb://localhost:27017", env="MONGODB_URL")
    MONGODB_DB_NAME: str = Field(default="recommendations", env="MONGODB_DB_NAME")

    # Cache
    REDIS_URL: str = Field(default="redis://localhost:6379", env="REDIS_URL")
    CACHE_TTL: int = Field(default=1800, env="CACHE_TTL")  # 30 minutes

    # Recommendation settings
    MIN_INTERACTIONS: int = Field(default=5, env="MIN_INTERACTIONS")
    MAX_RECOMMENDATIONS: int = Field(default=100, env="MAX_RECOMMENDATIONS")

    # Hybrid weights
    COLLABORATIVE_WEIGHT: float = Field(default=0.6, env="COLLABORATIVE_WEIGHT")

    class Config:
        env_file = ".env"
        case_sensitive = False


settings = Settings()
