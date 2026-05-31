"""
ML Model Training Service - Main Application
FastAPI microservice for training machine learning models
"""

from fastapi import FastAPI, HTTPException, BackgroundTasks, Depends
from fastapi.middleware.cors import CORSMiddleware
from fastapi.openapi.utils import get_openapi
import uvicorn
import logging
from typing import Optional, List, Dict, Any
from pydantic import BaseModel, Field
from datetime import datetime
import uuid
import json
import os

from app.training.trainer import ModelTrainer
from app.models.schemas import (
    TrainingJobRequest,
    TrainingJobResponse,
    TrainingStatusResponse,
    ModelInfo,
    TrainingConfig,
    PredictionRequest,
    PredictionResponse
)
from app.storage.model_store import ModelStore
from app.config import settings

# Configure logging
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s'
)
logger = logging.getLogger(__name__)

# Initialize FastAPI app
app = FastAPI(
    title="ML Model Training Service",
    description="Machine Learning Model Training and Inference API for Gogidix Ecosystem",
    version="1.0.0",
    docs_url="/docs",
    redoc_url="/redoc"
)

# CORS middleware
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Global dependencies
model_store = ModelStore(settings.MODEL_STORAGE_PATH)
trainer_registry = {}


def custom_openapi():
    """Custom OpenAPI schema"""
    if app.openapi_schema:
        return app.openapi_schema
    openapi_schema = get_openapi(
        title="ML Model Training Service",
        version="1.0.0",
        description="""
        ## ML Model Training Service

        This service provides machine learning model training, evaluation, and deployment capabilities.

        ### Features
        - **Model Training**: Train ML models with various algorithms
        - **Model Evaluation**: Evaluate model performance with metrics
        - **Model Registry**: Store and version trained models
        - **Prediction**: Make predictions with trained models
        - **Hyperparameter Tuning**: Optimize model hyperparameters

        ### Supported Algorithms
        - Linear Regression
        - Logistic Regression
        - Random Forest
        - Gradient Boosting
        - XGBoost
        - LightGBM
        - Neural Networks (via PyTorch/TensorFlow)
        """,
        routes=app.routes,
    )
    openapi_schema["info"]["x-logo"] = {
        "url": "https://gogidix.com/logo.png"
    }
    app.openapi_schema = openapi_schema
    return app.openapi_schema


app.openapi = custom_openapi


@app.on_event("startup")
async def startup_event():
    """Initialize service on startup"""
    logger.info("Starting ML Model Training Service...")
    os.makedirs(settings.MODEL_STORAGE_PATH, exist_ok=True)
    os.makedirs(settings.DATA_STORAGE_PATH, exist_ok=True)
    logger.info(f"Model storage: {settings.MODEL_STORAGE_PATH}")
    logger.info(f"Data storage: {settings.DATA_STORAGE_PATH}")
    logger.info("ML Model Training Service started successfully")


@app.on_event("shutdown")
async def shutdown_event():
    """Cleanup on shutdown"""
    logger.info("Shutting down ML Model Training Service...")


@app.get("/", tags=["Health"])
async def root():
    """Root endpoint - service health check"""
    return {
        "service": "ML Model Training Service",
        "version": "1.0.0",
        "status": "healthy",
        "timestamp": datetime.utcnow().isoformat()
    }


@app.get("/health", tags=["Health"])
async def health_check():
    """Detailed health check"""
    return {
        "status": "healthy",
        "timestamp": datetime.utcnow().isoformat(),
        "components": {
            "model_store": "ok",
            "trainer": "ok"
        }
    }


@app.post("/api/v1/training/jobs", response_model=TrainingJobResponse, tags=["Training"])
async def create_training_job(
    request: TrainingJobRequest,
    background_tasks: BackgroundTasks
):
    """
    Create and start a new model training job

    - **model_type**: Type of model to train (regression, classification, clustering, etc.)
    - **algorithm**: Algorithm to use (random_forest, xgboost, neural_network, etc.)
    - **training_config**: Training parameters and hyperparameters
    - **data_source**: Source of training data
    """
    try:
        job_id = str(uuid.uuid4())

        # Create trainer instance
        trainer = ModelTrainer(
            job_id=job_id,
            model_type=request.model_type,
            algorithm=request.algorithm,
            config=request.training_config.dict() if request.training_config else {}
        )

        trainer_registry[job_id] = trainer

        # Start training in background
        background_tasks.add_task(
            run_training_job,
            job_id,
            trainer,
            request.data_source,
            request.feature_columns
        )

        return TrainingJobResponse(
            job_id=job_id,
            model_type=request.model_type,
            algorithm=request.algorithm,
            status="started",
            created_at=datetime.utcnow(),
            message="Training job started successfully"
        )

    except Exception as e:
        logger.error(f"Error creating training job: {str(e)}")
        raise HTTPException(status_code=500, detail=str(e))


@app.get("/api/v1/training/jobs/{job_id}", response_model=TrainingStatusResponse, tags=["Training"])
async def get_training_status(job_id: str):
    """
    Get the status of a training job

    - **job_id**: Unique identifier for the training job
    """
    if job_id not in trainer_registry:
        raise HTTPException(status_code=404, detail="Training job not found")

    trainer = trainer_registry[job_id]
    return TrainingStatusResponse(
        job_id=job_id,
        status=trainer.status,
        progress=trainer.progress,
        metrics=trainer.metrics,
        started_at=trainer.started_at,
        completed_at=trainer.completed_at,
        error_message=trainer.error_message
    )


@app.get("/api/v1/models", response_model=List[ModelInfo], tags=["Models"])
async def list_models(
    skip: int = 0,
    limit: int = 100,
    model_type: Optional[str] = None
):
    """
    List all trained models

    - **skip**: Number of records to skip
    - **limit**: Maximum number of records to return
    - **model_type**: Filter by model type
    """
    try:
        models = model_store.list_models(
            skip=skip,
            limit=limit,
            model_type=model_type
        )
        return models
    except Exception as e:
        logger.error(f"Error listing models: {str(e)}")
        raise HTTPException(status_code=500, detail=str(e))


@app.get("/api/v1/models/{model_id}", response_model=ModelInfo, tags=["Models"])
async def get_model_info(model_id: str):
    """
    Get detailed information about a specific model

    - **model_id**: Unique identifier for the model
    """
    try:
        model_info = model_store.get_model_info(model_id)
        if not model_info:
            raise HTTPException(status_code=404, detail="Model not found")
        return model_info
    except HTTPException:
        raise
    except Exception as e:
        logger.error(f"Error getting model info: {str(e)}")
        raise HTTPException(status_code=500, detail=str(e))


@app.delete("/api/v1/models/{model_id}", tags=["Models"])
async def delete_model(model_id: str):
    """
    Delete a trained model

    - **model_id**: Unique identifier for the model
    """
    try:
        success = model_store.delete_model(model_id)
        if not success:
            raise HTTPException(status_code=404, detail="Model not found")
        return {"message": "Model deleted successfully", "model_id": model_id}
    except HTTPException:
        raise
    except Exception as e:
        logger.error(f"Error deleting model: {str(e)}")
        raise HTTPException(status_code=500, detail=str(e))


@app.post("/api/v1/models/{model_id}/predict", response_model=PredictionResponse, tags=["Prediction"])
async def predict(model_id: str, request: PredictionRequest):
    """
    Make predictions using a trained model

    - **model_id**: Unique identifier for the model
    - **features**: Input features for prediction
    """
    try:
        model = model_store.load_model(model_id)
        if model is None:
            raise HTTPException(status_code=404, detail="Model not found or not loaded")

        predictions = model.predict(request.features)

        return PredictionResponse(
            model_id=model_id,
            predictions=predictions.tolist() if hasattr(predictions, 'tolist') else list(predictions),
            timestamp=datetime.utcnow()
        )
    except HTTPException:
        raise
    except Exception as e:
        logger.error(f"Error making prediction: {str(e)}")
        raise HTTPException(status_code=500, detail=str(e))


@app.post("/api/v1/models/{model_id}/predict-batch", response_model=PredictionResponse, tags=["Prediction"])
async def predict_batch(model_id: str, request: PredictionRequest):
    """
    Make batch predictions using a trained model

    - **model_id**: Unique identifier for the model
    - **features**: Batch of input features for prediction
    """
    try:
        model = model_store.load_model(model_id)
        if model is None:
            raise HTTPException(status_code=404, detail="Model not found or not loaded")

        predictions = model.predict(request.features)

        return PredictionResponse(
            model_id=model_id,
            predictions=predictions.tolist() if hasattr(predictions, 'tolist') else list(predictions),
            timestamp=datetime.utcnow()
        )
    except HTTPException:
        raise
    except Exception as e:
        logger.error(f"Error making batch prediction: {str(e)}")
        raise HTTPException(status_code=500, detail=str(e))


async def run_training_job(
    job_id: str,
    trainer: ModelTrainer,
    data_source: Dict[str, Any],
    feature_columns: Optional[List[str]] = None
):
    """Run training job in background"""
    try:
        trainer.status = "running"
        trainer.started_at = datetime.utcnow()

        # Load data
        trainer.progress = 10
        data = trainer.load_data(data_source)

        # Train model
        trainer.progress = 30
        model, metrics = trainer.train(data, feature_columns)

        # Save model
        trainer.progress = 90
        model_id = model_store.save_model(
            model=model,
            model_type=trainer.model_type,
            algorithm=trainer.algorithm,
            metrics=metrics,
            config=trainer.config
        )

        trainer.progress = 100
        trainer.status = "completed"
        trainer.completed_at = datetime.utcnow()
        trainer.metrics = metrics
        trainer.model_id = model_id

        logger.info(f"Training job {job_id} completed successfully. Model ID: {model_id}")

    except Exception as e:
        logger.error(f"Training job {job_id} failed: {str(e)}")
        trainer.status = "failed"
        trainer.error_message = str(e)
        trainer.completed_at = datetime.utcnow()


if __name__ == "__main__":
    uvicorn.run(
        "app.main:app",
        host=settings.HOST,
        port=settings.PORT,
        reload=settings.DEBUG,
        log_level="info"
    )
