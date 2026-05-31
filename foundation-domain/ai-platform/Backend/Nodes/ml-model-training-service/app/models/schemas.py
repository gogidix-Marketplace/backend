"""
Pydantic schemas for ML Model Training Service
"""

from pydantic import BaseModel, Field, validator
from typing import Optional, List, Dict, Any, Union
from datetime import datetime
from enum import Enum


class ModelType(str, Enum):
    """Supported model types"""
    REGRESSION = "regression"
    CLASSIFICATION = "classification"
    CLUSTERING = "clustering"
    TIME_SERIES = "time_series"
    ANOMALY_DETECTION = "anomaly_detection"
    RECOMMENDATION = "recommendation"
    NLP = "nlp"
    COMPUTER_VISION = "computer_vision"


class AlgorithmType(str, Enum):
    """Supported algorithms"""
    LINEAR_REGRESSION = "linear_regression"
    LOGISTIC_REGRESSION = "logistic_regression"
    RANDOM_FOREST = "random_forest"
    GRADIENT_BOOSTING = "gradient_boosting"
    XGBOOST = "xgboost"
    LIGHTGBM = "lightgbm"
    CATBOOST = "catboost"
    SVM = "svm"
    KNN = "knn"
    NEURAL_NETWORK = "neural_network"
    LSTM = "lstm"
    ARIMA = "arima"
    PROPHET = "prophet"
    ISOLATION_FOREST = "isolation_forest"
    ONE_CLASS_SVM = "one_class_svm"
    KMEANS = "kmeans"
    DBSCAN = "dbscan"
    COLLABORATIVE_FILTERING = "collaborative_filtering"
    MATRIX_FACTORIZATION = "matrix_factorization"
    TRANSFORMER = "transformer"
    BERT = "bert"
    RESNET = "resnet"
    YOLO = "yolo"


class TrainingStatus(str, Enum):
    """Training job status"""
    PENDING = "pending"
    RUNNING = "running"
    COMPLETED = "completed"
    FAILED = "failed"
    CANCELLED = "cancelled"


class TrainingConfig(BaseModel):
    """Training configuration"""
    # Hyperparameters
    n_estimators: Optional[int] = Field(default=100, description="Number of estimators")
    max_depth: Optional[int] = Field(default=None, description="Maximum depth")
    learning_rate: Optional[float] = Field(default=0.1, description="Learning rate")
    batch_size: Optional[int] = Field(default=32, description="Batch size")
    epochs: Optional[int] = Field(default=100, description="Number of epochs")
    test_size: Optional[float] = Field(default=0.2, description="Test split ratio")
    random_state: Optional[int] = Field(default=42, description="Random state")
    early_stopping: Optional[bool] = Field(default=True, description="Enable early stopping")
    early_stopping_patience: Optional[int] = Field(default=10, description="Early stopping patience")
    cross_validation_folds: Optional[int] = Field(default=5, description="CV folds")
    # Additional hyperparameters
    hyperparameters: Optional[Dict[str, Any]] = Field(default_factory=dict, description="Custom hyperparameters")

    class Config:
        extra = "allow"


class DataSource(BaseModel):
    """Data source configuration"""
    type: str = Field(..., description="Type: file, database, api, s3, etc.")
    connection_string: Optional[str] = Field(default=None, description="Database connection string")
    table_name: Optional[str] = Field(default=None, description="Table name")
    file_path: Optional[str] = Field(default=None, description="File path")
    query: Optional[str] = Field(default=None, description="SQL query")
    s3_bucket: Optional[str] = Field(default=None, description="S3 bucket")
    s3_key: Optional[str] = Field(default=None, description="S3 key")
    api_endpoint: Optional[str] = Field(default=None, description="API endpoint")
    format: str = Field(default="csv", description="Data format: csv, json, parquet, etc.")


class TrainingJobRequest(BaseModel):
    """Request to create a training job"""
    model_type: ModelType = Field(..., description="Type of model to train")
    algorithm: AlgorithmType = Field(..., description="Algorithm to use")
    training_config: Optional[TrainingConfig] = Field(default=None, description="Training configuration")
    data_source: Dict[str, Any] = Field(..., description="Data source configuration")
    feature_columns: Optional[List[str]] = Field(default=None, description="Feature column names")
    target_column: Optional[str] = Field(default=None, description="Target column name")
    job_name: Optional[str] = Field(default=None, description="Optional job name")
    description: Optional[str] = Field(default=None, description="Job description")
    tags: Optional[List[str]] = Field(default_factory=list, description="Job tags")


class TrainingJobResponse(BaseModel):
    """Response for training job creation"""
    job_id: str = Field(..., description="Unique job identifier")
    model_type: ModelType = Field(..., description="Model type")
    algorithm: AlgorithmType = Field(..., description="Algorithm used")
    status: TrainingStatus = Field(..., description="Job status")
    created_at: datetime = Field(..., description="Creation timestamp")
    message: str = Field(..., description="Response message")


class TrainingStatusResponse(BaseModel):
    """Response for training status query"""
    job_id: str = Field(..., description="Job identifier")
    status: TrainingStatus = Field(..., description="Current status")
    progress: float = Field(default=0, ge=0, le=100, description="Progress percentage")
    metrics: Optional[Dict[str, float]] = Field(default=None, description="Training metrics")
    started_at: Optional[datetime] = Field(default=None, description="Start time")
    completed_at: Optional[datetime] = Field(default=None, description="Completion time")
    error_message: Optional[str] = Field(default=None, description="Error message if failed")
    model_id: Optional[str] = Field(default=None, description="Resulting model ID")


class ModelInfo(BaseModel):
    """Model information"""
    model_id: str = Field(..., description="Unique model identifier")
    model_type: ModelType = Field(..., description="Model type")
    algorithm: AlgorithmType = Field(..., description="Algorithm used")
    version: int = Field(..., description="Model version")
    created_at: datetime = Field(..., description="Creation timestamp")
    metrics: Dict[str, float] = Field(default_factory=dict, description="Model metrics")
    config: Dict[str, Any] = Field(default_factory=dict, description="Training config")
    is_active: bool = Field(default=True, description="Whether model is active")
    file_size_bytes: Optional[int] = Field(default=None, description="Model file size")
    tags: List[str] = Field(default_factory=list, description="Model tags")


class PredictionRequest(BaseModel):
    """Request for prediction"""
    features: Union[List[List[float]], Dict[str, Any]] = Field(
        ...,
        description="Input features - list of feature vectors or feature dict"
    )
    return_probabilities: Optional[bool] = Field(default=False, description="Return class probabilities")


class PredictionResponse(BaseModel):
    """Response for prediction"""
    model_id: str = Field(..., description="Model used")
    predictions: List[Any] = Field(..., description="Prediction results")
    probabilities: Optional[List[List[float]]] = Field(default=None, description="Class probabilities")
    timestamp: datetime = Field(..., description="Prediction timestamp")


class ModelEvaluation(BaseModel):
    """Model evaluation metrics"""
    model_id: str = Field(..., description="Model being evaluated")
    metrics: Dict[str, float] = Field(..., description="Evaluation metrics")
    confusion_matrix: Optional[List[List[int]]] = Field(default=None, description="Confusion matrix")
    classification_report: Optional[Dict[str, Dict[str, float]]] = Field(default=None)
    feature_importance: Optional[Dict[str, float]] = Field(default=None, description="Feature importance")
    evaluated_at: datetime = Field(default_factory=datetime.utcnow, description="Evaluation timestamp")


class HyperparameterTuningRequest(BaseModel):
    """Request for hyperparameter tuning"""
    model_type: ModelType = Field(..., description="Model type")
    algorithm: AlgorithmType = Field(..., description="Algorithm")
    param_grid: Dict[str, List[Any]] = Field(..., description="Parameter grid for search")
    data_source: Dict[str, Any] = Field(..., description="Data source")
    feature_columns: Optional[List[str]] = Field(default=None, description="Feature columns")
    target_column: Optional[str] = Field(default=None, description="Target column")
    search_type: str = Field(default="grid", description="Search type: grid, random, bayesian")
    n_iter: Optional[int] = Field(default=10, description="Number of iterations for random search")
    cv_folds: Optional[int] = Field(default=5, description="Cross-validation folds")
    scoring_metric: Optional[str] = Field(default="accuracy", description="Scoring metric")


class HyperparameterTuningResponse(BaseModel):
    """Response for hyperparameter tuning"""
    tuning_job_id: str = Field(..., description="Tuning job ID")
    status: str = Field(..., description="Tuning status")
    best_params: Optional[Dict[str, Any]] = Field(default=None, description="Best parameters found")
    best_score: Optional[float] = Field(default=None, description="Best score achieved")
    results: Optional[List[Dict[str, Any]]] = Field(default=None, description="All trials")
