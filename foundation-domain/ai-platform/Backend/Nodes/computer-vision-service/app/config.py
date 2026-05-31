"""
Configuration for Computer Vision Service
"""

from pydantic import BaseSettings, Field


class Settings(BaseSettings):
    """Application settings"""

    SERVICE_NAME: str = "computer-vision-service"
    VERSION: str = "1.0.0"
    HOST: str = Field(default="0.0.0.0", env="CV_SERVICE_HOST")
    PORT: int = Field(default=8004, env="CV_SERVICE_PORT")
    DEBUG: bool = Field(default=False, env="CV_SERVICE_DEBUG")

    # OCR settings
    TESSERACT_PATH: str = Field(default="/usr/bin/tesseract", env="TESSERACT_PATH")
    OCR_LANGUAGE: str = Field(default="eng", env="OCR_LANGUAGE")

    # Model storage
    MODEL_STORAGE_PATH: str = Field(default="/app/models", env="MODEL_STORAGE_PATH")

    # Cache
    REDIS_URL: str = Field(default="redis://localhost:6379", env="REDIS_URL")

    # Database
    MONGODB_URL: str = Field(default="mongodb://localhost:27017", env="MONGODB_URL")
    MONGODB_DB_NAME: str = Field(default="computer_vision", env="MONGODB_DB_NAME")

    # API limits
    MAX_IMAGE_SIZE_MB: int = Field(default=10, env="MAX_IMAGE_SIZE_MB")

    class Config:
        env_file = ".env"
        case_sensitive = False


settings = Settings()
