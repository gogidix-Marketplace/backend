"""
Model Trainer - Core training logic for ML models
"""

import logging
import pickle
import joblib
import numpy as np
import pandas as pd
from typing import Optional, Dict, Any, List, Tuple, Union
from datetime import datetime
from pathlib import Path
import json

# Scikit-learn imports
from sklearn.model_selection import train_test_split, cross_val_score, GridSearchCV
from sklearn.preprocessing import StandardScaler, LabelEncoder
from sklearn.pipeline import Pipeline
from sklearn.compose import ColumnTransformer

# Regression models
from sklearn.linear_model import LinearRegression, Ridge, Lasso, ElasticNet
from sklearn.ensemble import RandomForestRegressor, GradientBoostingRegressor
from sklearn.svm import SVR

# Classification models
from sklearn.linear_model import LogisticRegression
from sklearn.ensemble import RandomForestClassifier, GradientBoostingClassifier
from sklearn.svm import SVC
from sklearn.neighbors import KNeighborsClassifier
from sklearn.naive_bayes import GaussianNB

# Clustering models
from sklearn.cluster import KMeans, DBSCAN, AgglomerativeClustering
from sklearn.mixture import GaussianMixture

# Anomaly detection
from sklearn.ensemble import IsolationForest
from sklearn.svm import OneClassSVM

# Metrics
from sklearn.metrics import (
    mean_squared_error, mean_absolute_error, r2_score,
    accuracy_score, precision_score, recall_score, f1_score,
    roc_auc_score, confusion_matrix, classification_report
)

from app.models.schemas import ModelType, AlgorithmType, TrainingStatus

logger = logging.getLogger(__name__)


class ModelTrainer:
    """
    Model trainer for various ML algorithms
    Supports regression, classification, clustering, and anomaly detection
    """

    def __init__(
        self,
        job_id: str,
        model_type: ModelType,
        algorithm: AlgorithmType,
        config: Optional[Dict[str, Any]] = None
    ):
        self.job_id = job_id
        self.model_type = model_type
        self.algorithm = algorithm
        self.config = config or {}
        self.status = TrainingStatus.PENDING
        self.progress = 0.0
        self.metrics: Optional[Dict[str, float]] = None
        self.started_at: Optional[datetime] = None
        self.completed_at: Optional[datetime] = None
        self.error_message: Optional[str] = None
        self.model_id: Optional[str] = None

        # Training artifacts
        self.model: Optional[Any] = None
        self.scaler: Optional[StandardScaler] = None
        self.label_encoders: Dict[str, LabelEncoder] = {}
        self.feature_columns: Optional[List[str]] = None

    def load_data(self, data_source: Dict[str, Any]) -> pd.DataFrame:
        """Load data from various sources"""
        source_type = data_source.get("type", "file")

        if source_type == "file":
            file_path = data_source.get("file_path")
            file_format = data_source.get("format", "csv")

            if file_format == "csv":
                df = pd.read_csv(file_path)
            elif file_format == "json":
                df = pd.read_json(file_path)
            elif file_format == "parquet":
                df = pd.read_parquet(file_path)
            elif file_format == "excel":
                df = pd.read_excel(file_path)
            else:
                raise ValueError(f"Unsupported file format: {file_format}")

        elif source_type == "database":
            # Database connection logic would go here
            raise NotImplementedError("Database connection not implemented")

        elif source_type == "s3":
            # S3 loading logic would go here
            raise NotImplementedError("S3 loading not implemented")

        else:
            raise ValueError(f"Unsupported data source type: {source_type}")

        logger.info(f"Loaded data with shape: {df.shape}")
        return df

    def preprocess_data(
        self,
        df: pd.DataFrame,
        feature_columns: Optional[List[str]] = None,
        target_column: Optional[str] = None
    ) -> Tuple[pd.DataFrame, Optional[pd.Series]]:
        """Preprocess data for training"""
        # Select features
        if feature_columns:
            X = df[feature_columns].copy()
        else:
            # Assume all columns except target are features
            X = df.drop(columns=[target_column]) if target_column else df.copy()

        # Select target
        y = df[target_column] if target_column and target_column in df.columns else None

        # Handle missing values
        X = X.fillna(X.mean(numeric_only=True))
        X = X.fillna("unknown")

        # Encode categorical variables
        categorical_cols = X.select_dtypes(include=["object"]).columns.tolist()
        for col in categorical_cols:
            if col not in self.label_encoders:
                self.label_encoders[col] = LabelEncoder()
            X[col] = self.label_encoders[col].fit_transform(X[col].astype(str))

        self.feature_columns = X.columns.tolist()

        return X, y

    def _get_model(self) -> Any:
        """Get the model instance based on algorithm"""
        params = {
            k: v for k, v in self.config.items()
            if k not in ["test_size", "random_state", "epochs", "batch_size"]
        }

        if self.algorithm == AlgorithmType.LINEAR_REGRESSION:
            return LinearRegression()

        elif self.algorithm == AlgorithmType.LOGISTIC_REGRESSION:
            return LogisticRegression(
                max_iter=self.config.get("epochs", 1000),
                random_state=self.config.get("random_state", 42)
            )

        elif self.algorithm == AlgorithmType.RANDOM_FOREST:
            n_estimators = self.config.get("n_estimators", 100)
            max_depth = self.config.get("max_depth")
            return RandomForestClassifier(
                n_estimators=n_estimators,
                max_depth=max_depth,
                random_state=self.config.get("random_state", 42)
            ) if self.model_type == ModelType.CLASSIFICATION else RandomForestRegressor(
                n_estimators=n_estimators,
                max_depth=max_depth,
                random_state=self.config.get("random_state", 42)
            )

        elif self.algorithm == AlgorithmType.GRADIENT_BOOSTING:
            n_estimators = self.config.get("n_estimators", 100)
            learning_rate = self.config.get("learning_rate", 0.1)
            if self.model_type == ModelType.CLASSIFICATION:
                return GradientBoostingClassifier(
                    n_estimators=n_estimators,
                    learning_rate=learning_rate,
                    random_state=self.config.get("random_state", 42)
                )
            else:
                return GradientBoostingRegressor(
                    n_estimators=n_estimators,
                    learning_rate=learning_rate,
                    random_state=self.config.get("random_state", 42)
                )

        elif self.algorithm == AlgorithmType.KNN:
            return KNeighborsClassifier(
                n_neighbors=self.config.get("n_neighbors", 5)
            )

        elif self.algorithm == AlgorithmType.SVM:
            if self.model_type == ModelType.CLASSIFICATION:
                return SVC(probability=True)
            else:
                return SVR()

        elif self.algorithm == AlgorithmType.KMEANS:
            return KMeans(
                n_clusters=self.config.get("n_clusters", 3),
                random_state=self.config.get("random_state", 42)
            )

        elif self.algorithm == AlgorithmType.DBSCAN:
            return DBSCAN(
                eps=self.config.get("eps", 0.5),
                min_samples=self.config.get("min_samples", 5)
            )

        elif self.algorithm == AlgorithmType.ISOLATION_FOREST:
            return IsolationForest(
                contamination=self.config.get("contamination", 0.1),
                random_state=self.config.get("random_state", 42)
            )

        elif self.algorithm == AlgorithmType.ONE_CLASS_SVM:
            return OneClassSVM(
                nu=self.config.get("nu", 0.05)
            )

        else:
            raise ValueError(f"Unsupported algorithm: {self.algorithm}")

    def train(
        self,
        data: pd.DataFrame,
        feature_columns: Optional[List[str]] = None,
        target_column: Optional[str] = None
    ) -> Tuple[Any, Dict[str, float]]:
        """Train the model"""
        try:
            # Preprocess data
            X, y = self.preprocess_data(data, feature_columns, target_column)

            # Get model
            self.model = self._get_model()

            # For supervised learning, split data
            if y is not None and self.model_type in [
                ModelType.REGRESSION, ModelType.CLASSIFICATION
            ]:
                test_size = self.config.get("test_size", 0.2)
                X_train, X_test, y_train, y_test = train_test_split(
                    X, y, test_size=test_size, random_state=self.config.get("random_state", 42)
                )

                # Scale features
                self.scaler = StandardScaler()
                X_train_scaled = self.scaler.fit_transform(X_train)
                X_test_scaled = self.scaler.transform(X_test)

                # Train model
                self.progress = 50
                self.model.fit(X_train_scaled, y_train)

                # Evaluate
                self.progress = 75
                predictions = self.model.predict(X_test_scaled)
                metrics = self._calculate_metrics(y_test, predictions, y_train)

            # For unsupervised learning
            else:
                self.scaler = StandardScaler()
                X_scaled = self.scaler.fit_transform(X)
                self.model.fit(X_scaled)
                predictions = self.model.predict(X_scaled)
                metrics = self._calculate_unsupervised_metrics(X_scaled, predictions)

            self.metrics = metrics
            return self.model, metrics

        except Exception as e:
            logger.error(f"Training error: {str(e)}")
            raise

    def _calculate_metrics(
        self,
        y_true: Union[pd.Series, np.ndarray],
        y_pred: Union[pd.Series, np.ndarray],
        y_train: Optional[Union[pd.Series, np.ndarray]] = None
    ) -> Dict[str, float]:
        """Calculate evaluation metrics"""
        metrics = {}

        if self.model_type == ModelType.REGRESSION:
            metrics["mse"] = float(mean_squared_error(y_true, y_pred))
            metrics["mae"] = float(mean_absolute_error(y_true, y_pred))
            metrics["rmse"] = float(np.sqrt(metrics["mse"]))
            metrics["r2"] = float(r2_score(y_true, y_pred))

            # Calculate adjusted R2
            if y_train is not None:
                n = len(y_true)
                p = len(self.feature_columns) if self.feature_columns else 1
                metrics["adjusted_r2"] = float(1 - (1 - metrics["r2"]) * (n - 1) / (n - p - 1))

        elif self.model_type == ModelType.CLASSIFICATION:
            metrics["accuracy"] = float(accuracy_score(y_true, y_pred))
            metrics["precision"] = float(precision_score(y_true, y_pred, average="weighted", zero_division=0))
            metrics["recall"] = float(recall_score(y_true, y_pred, average="weighted", zero_division=0))
            metrics["f1"] = float(f1_score(y_true, y_pred, average="weighted", zero_division=0))

            # Try to get ROC AUC
            try:
                if hasattr(self.model, "predict_proba"):
                    y_proba = self.model.predict_proba(self.scaler.transform(y_true.reshape(-1, 1) if len(y_true.shape) == 1 else []))
                    metrics["roc_auc"] = float(roc_auc_score(y_true, y_proba, multi_class="ovr", average="weighted"))
            except Exception:
                pass

        return metrics

    def _calculate_unsupervised_metrics(
        self,
        X: np.ndarray,
        predictions: np.ndarray
    ) -> Dict[str, float]:
        """Calculate metrics for unsupervised learning"""
        metrics = {}

        if self.model_type == ModelType.CLUSTERING:
            from sklearn.metrics import silhouette_score, davies_bouldin_score, calinski_harabasz_score

            try:
                metrics["silhouette_score"] = float(silhouette_score(X, predictions))
            except Exception:
                pass

            try:
                metrics["davies_bouldin_score"] = float(davies_bouldin_score(X, predictions))
            except Exception:
                pass

            try:
                metrics["calinski_harabasz_score"] = float(calinski_harabasz_score(X, predictions))
            except Exception:
                pass

            # Number of clusters
            metrics["n_clusters"] = int(len(set(predictions)))

        return metrics

    def predict(self, features: Union[List[List[float]], np.ndarray, pd.DataFrame]) -> np.ndarray:
        """Make predictions with the trained model"""
        if self.model is None:
            raise ValueError("Model not trained yet")

        if isinstance(features, list):
            features = np.array(features)

        if isinstance(features, pd.DataFrame):
            features = features.values

        if self.scaler:
            features = self.scaler.transform(features)

        return self.model.predict(features)

    def predict_proba(self, features: Union[List[List[float]], np.ndarray]) -> np.ndarray:
        """Get prediction probabilities"""
        if self.model is None:
            raise ValueError("Model not trained yet")

        if not hasattr(self.model, "predict_proba"):
            raise ValueError("Model does not support probability predictions")

        if isinstance(features, list):
            features = np.array(features)

        if self.scaler:
            features = self.scaler.transform(features)

        return self.model.predict_proba(features)
