"""
Tests for Model Trainer
"""

import pytest
import numpy as np
import pandas as pd
from unittest.mock import Mock, patch, MagicMock
from datetime import datetime

from app.training.trainer import ModelTrainer
from app.models.schemas import ModelType, AlgorithmType, TrainingStatus


@pytest.fixture
def sample_data():
    """Create sample training data"""
    np.random.seed(42)
    return pd.DataFrame({
        "feature1": np.random.randn(100),
        "feature2": np.random.randn(100),
        "feature3": np.random.randint(0, 2, 100),
        "target": np.random.randint(0, 2, 100)
    })


@pytest.fixture
def sample_regression_data():
    """Create sample regression data"""
    np.random.seed(42)
    X = np.random.randn(100, 3)
    y = X[:, 0] + 2 * X[:, 1] + 0.5 * X[:, 2] + np.random.randn(100) * 0.1
    return pd.DataFrame({
        "feature1": X[:, 0],
        "feature2": X[:, 1],
        "feature3": X[:, 2],
        "target": y
    })


class TestModelTrainer:
    """Test cases for ModelTrainer class"""

    def test_initialization(self):
        """Test trainer initialization"""
        trainer = ModelTrainer(
            job_id="test-job-1",
            model_type=ModelType.CLASSIFICATION,
            algorithm=AlgorithmType.RANDOM_FOREST,
            config={"n_estimators": 50}
        )

        assert trainer.job_id == "test-job-1"
        assert trainer.model_type == ModelType.CLASSIFICATION
        assert trainer.algorithm == AlgorithmType.RANDOM_FOREST
        assert trainer.config["n_estimators"] == 50
        assert trainer.status == TrainingStatus.PENDING
        assert trainer.progress == 0.0

    def test_get_model_random_forest_classifier(self):
        """Test getting Random Forest classifier model"""
        trainer = ModelTrainer(
            job_id="test-job-1",
            model_type=ModelType.CLASSIFICATION,
            algorithm=AlgorithmType.RANDOM_FOREST,
            config={"n_estimators": 50}
        )

        model = trainer._get_model()
        assert model is not None
        assert hasattr(model, "fit")
        assert hasattr(model, "predict")

    def test_get_model_linear_regression(self):
        """Test getting Linear Regression model"""
        trainer = ModelTrainer(
            job_id="test-job-2",
            model_type=ModelType.REGRESSION,
            algorithm=AlgorithmType.LINEAR_REGRESSION
        )

        model = trainer._get_model()
        assert model is not None
        from sklearn.linear_model import LinearRegression
        assert isinstance(model, LinearRegression)

    def test_get_model_kmeans(self):
        """Test getting K-Means clustering model"""
        trainer = ModelTrainer(
            job_id="test-job-3",
            model_type=ModelType.CLUSTERING,
            algorithm=AlgorithmType.KMEANS,
            config={"n_clusters": 3}
        )

        model = trainer._get_model()
        assert model is not None
        from sklearn.cluster import KMeans
        assert isinstance(model, KMeans)

    def test_preprocess_data(self, sample_data):
        """Test data preprocessing"""
        trainer = ModelTrainer(
            job_id="test-job-1",
            model_type=ModelType.CLASSIFICATION,
            algorithm=AlgorithmType.RANDOM_FOREST
        )

        X, y = trainer.preprocess_data(
            sample_data,
            feature_columns=["feature1", "feature2", "feature3"],
            target_column="target"
        )

        assert X is not None
        assert y is not None
        assert len(X) == len(sample_data)
        assert len(y) == len(sample_data)
        assert trainer.feature_columns == ["feature1", "feature2", "feature3"]

    def test_train_classification_model(self, sample_data):
        """Test training a classification model"""
        trainer = ModelTrainer(
            job_id="test-job-1",
            model_type=ModelType.CLASSIFICATION,
            algorithm=AlgorithmType.LOGISTIC_REGRESSION,
            config={"epochs": 100}
        )

        model, metrics = trainer.train(
            sample_data,
            feature_columns=["feature1", "feature2", "feature3"],
            target_column="target"
        )

        assert model is not None
        assert metrics is not None
        assert "accuracy" in metrics
        assert "f1" in metrics
        assert trainer.model is not None

    def test_train_regression_model(self, sample_regression_data):
        """Test training a regression model"""
        trainer = ModelTrainer(
            job_id="test-job-2",
            model_type=ModelType.REGRESSION,
            algorithm=AlgorithmType.LINEAR_REGRESSION
        )

        model, metrics = trainer.train(
            sample_regression_data,
            feature_columns=["feature1", "feature2", "feature3"],
            target_column="target"
        )

        assert model is not None
        assert metrics is not None
        assert "mse" in metrics
        assert "mae" in metrics
        assert "r2" in metrics

    def test_train_clustering_model(self, sample_data):
        """Test training a clustering model"""
        trainer = ModelTrainer(
            job_id="test-job-3",
            model_type=ModelType.CLUSTERING,
            algorithm=AlgorithmType.KMEANS,
            config={"n_clusters": 2}
        )

        model, metrics = trainer.train(
            sample_data,
            feature_columns=["feature1", "feature2", "feature3"]
        )

        assert model is not None
        assert metrics is not None
        assert "n_clusters" in metrics

    def test_predict_after_training(self, sample_data):
        """Test making predictions after training"""
        trainer = ModelTrainer(
            job_id="test-job-1",
            model_type=ModelType.CLASSIFICATION,
            algorithm=AlgorithmType.LOGISTIC_REGRESSION
        )

        trainer.train(
            sample_data,
            feature_columns=["feature1", "feature2", "feature3"],
            target_column="target"
        )

        # Make predictions
        test_features = [[0.5, -0.3, 1.0], [1.2, 0.8, 0.0]]
        predictions = trainer.predict(test_features)

        assert predictions is not None
        assert len(predictions) == 2

    def test_predict_without_training_raises_error(self):
        """Test that predicting without training raises an error"""
        trainer = ModelTrainer(
            job_id="test-job-1",
            model_type=ModelType.CLASSIFICATION,
            algorithm=AlgorithmType.LOGISTIC_REGRESSION
        )

        with pytest.raises(ValueError, match="Model not trained yet"):
            trainer.predict([[0.5, -0.3, 1.0]])

    def test_load_data_from_csv(self, sample_data, tmp_path):
        """Test loading data from CSV file"""
        # Save sample data to temp file
        csv_path = tmp_path / "sample_data.csv"
        sample_data.to_csv(csv_path, index=False)

        trainer = ModelTrainer(
            job_id="test-job-1",
            model_type=ModelType.CLASSIFICATION,
            algorithm=AlgorithmType.LOGISTIC_REGRESSION
        )

        data_source = {
            "type": "file",
            "file_path": str(csv_path),
            "format": "csv"
        }

        loaded_data = trainer.load_data(data_source)

        assert loaded_data is not None
        assert len(loaded_data) == len(sample_data)
        assert list(loaded_data.columns) == list(sample_data.columns)

    def test_calculate_classification_metrics(self):
        """Test calculation of classification metrics"""
        trainer = ModelTrainer(
            job_id="test-job-1",
            model_type=ModelType.CLASSIFICATION,
            algorithm=AlgorithmType.LOGISTIC_REGRESSION
        )

        y_true = np.array([0, 1, 0, 1, 0, 1])
        y_pred = np.array([0, 1, 0, 0, 0, 1])

        metrics = trainer._calculate_metrics(y_true, y_pred)

        assert "accuracy" in metrics
        assert metrics["accuracy"] > 0
        assert "precision" in metrics
        assert "recall" in metrics
        assert "f1" in metrics

    def test_calculate_regression_metrics(self):
        """Test calculation of regression metrics"""
        trainer = ModelTrainer(
            job_id="test-job-2",
            model_type=ModelType.REGRESSION,
            algorithm=AlgorithmType.LINEAR_REGRESSION
        )

        y_true = np.array([1.0, 2.0, 3.0, 4.0, 5.0])
        y_pred = np.array([1.1, 2.1, 2.9, 4.0, 5.1])

        metrics = trainer._calculate_metrics(y_true, y_pred)

        assert "mse" in metrics
        assert "mae" in metrics
        assert "rmse" in metrics
        assert "r2" in metrics
        assert metrics["r2"] > 0  # Should have decent R2 for similar values


class TestModelTrainerIntegration:
    """Integration tests for ModelTrainer"""

    def test_full_training_workflow(self, sample_data):
        """Test complete training workflow from data to predictions"""
        trainer = ModelTrainer(
            job_id="integration-test-1",
            model_type=ModelType.CLASSIFICATION,
            algorithm=AlgorithmType.RANDOM_FOREST,
            config={"n_estimators": 10, "max_depth": 3}
        )

        # Train
        model, metrics = trainer.train(
            sample_data,
            feature_columns=["feature1", "feature2", "feature3"],
            target_column="target"
        )

        assert trainer.status == TrainingStatus.PENDING
        assert trainer.metrics == metrics

        # Predict
        test_features = sample_data[["feature1", "feature2", "feature3"]].head(10).values.tolist()
        predictions = trainer.predict(test_features)

        assert len(predictions) == 10
        assert all(p in [0, 1] for p in predictions)

    def test_multiple_training_runs(self, sample_data):
        """Test that trainer can handle multiple training runs"""
        results = []

        for i in range(3):
            trainer = ModelTrainer(
                job_id=f"test-job-{i}",
                model_type=ModelType.CLASSIFICATION,
                algorithm=AlgorithmType.LOGISTIC_REGRESSION
            )

            model, metrics = trainer.train(
                sample_data,
                feature_columns=["feature1", "feature2", "feature3"],
                target_column="target"
            )

            results.append(metrics)

        # All should have completed successfully
        assert all("accuracy" in m for m in results)
