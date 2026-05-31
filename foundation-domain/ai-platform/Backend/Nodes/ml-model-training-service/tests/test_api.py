"""
Tests for ML Training Service API
"""

import pytest
import json
from unittest.mock import Mock, patch, MagicMock
from fastapi.testclient import TestClient

from app.main import app
from app.models.schemas import ModelType, AlgorithmType


@pytest.fixture
def client():
    """Create test client"""
    return TestClient(app)


@pytest.fixture
def mock_training_request():
    """Create mock training request"""
    return {
        "model_type": "classification",
        "algorithm": "random_forest",
        "training_config": {
            "n_estimators": 10,
            "max_depth": 3,
            "test_size": 0.2
        },
        "data_source": {
            "type": "file",
            "file_path": "/app/data/sample.csv",
            "format": "csv"
        },
        "feature_columns": ["feature1", "feature2", "feature3"],
        "target_column": "target"
    }


@pytest.fixture
def mock_prediction_request():
    """Create mock prediction request"""
    return {
        "features": [[0.5, -0.3, 1.0], [1.2, 0.8, 0.0]],
        "return_probabilities": False
    }


class TestHealthEndpoints:
    """Test health check endpoints"""

    def test_root_endpoint(self, client):
        """Test root endpoint returns service info"""
        response = client.get("/")
        assert response.status_code == 200

        data = response.json()
        assert "service" in data
        assert data["service"] == "ML Model Training Service"
        assert "version" in data
        assert "status" in data
        assert data["status"] == "healthy"

    def test_health_check_endpoint(self, client):
        """Test health check endpoint"""
        response = client.get("/health")
        assert response.status_code == 200

        data = response.json()
        assert "status" in data
        assert data["status"] == "healthy"
        assert "components" in data


class TestTrainingEndpoints:
    """Test training job endpoints"""

    @patch("app.main.run_training_job")
    @patch("app.main.ModelTrainer")
    def test_create_training_job(self, mock_trainer, mock_run_job, client, mock_training_request):
        """Test creating a training job"""
        response = client.post("/api/v1/training/jobs", json=mock_training_request)

        assert response.status_code == 200

        data = response.json()
        assert "job_id" in data
        assert data["model_type"] == "classification"
        assert data["algorithm"] == "random_forest"
        assert data["status"] == "started"

    def test_create_training_job_invalid_algorithm(self, client):
        """Test creating training job with invalid algorithm"""
        invalid_request = {
            "model_type": "invalid_type",
            "algorithm": "invalid_algorithm",
            "data_source": {"type": "file", "file_path": "/data.csv"}
        }

        response = client.post("/api/v1/training/jobs", json=invalid_request)
        assert response.status_code == 422  # Validation error

    def test_get_training_status_not_found(self, client):
        """Test getting status of non-existent job"""
        response = client.get("/api/v1/training/jobs/non-existent-job-id")
        assert response.status_code == 404


class TestModelEndpoints:
    """Test model management endpoints"""

    @patch("app.main.model_store")
    def test_list_models(self, mock_store, client):
        """Test listing models"""
        mock_store.list_models.return_value = []

        response = client.get("/api/v1/models")
        assert response.status_code == 200

        data = response.json()
        assert isinstance(data, list)

    @patch("app.main.model_store")
    def test_list_models_with_filter(self, mock_store, client):
        """Test listing models with type filter"""
        mock_store.list_models.return_value = []

        response = client.get("/api/v1/models?model_type=classification")
        assert response.status_code == 200

        mock_store.list_models.assert_called_once_with(
            skip=0,
            limit=100,
            model_type="classification"
        )

    @patch("app.main.model_store")
    def test_get_model_info_not_found(self, mock_store, client):
        """Test getting info for non-existent model"""
        mock_store.get_model_info.return_value = None

        response = client.get("/api/v1/models/non-existent-model")
        assert response.status_code == 404

    @patch("app.main.model_store")
    def test_delete_model(self, mock_store, client):
        """Test deleting a model"""
        mock_store.delete_model.return_value = True

        response = client.delete("/api/v1/models/test-model-id")
        assert response.status_code == 200

        data = response.json()
        assert "message" in data
        assert data["model_id"] == "test-model-id"

    @patch("app.main.model_store")
    def test_delete_model_not_found(self, mock_store, client):
        """Test deleting non-existent model"""
        mock_store.delete_model.return_value = False

        response = client.delete("/api/v1/models/non-existent-model")
        assert response.status_code == 404


class TestPredictionEndpoints:
    """Test prediction endpoints"""

    @patch("app.main.model_store")
    def test_predict_success(self, mock_store, client, mock_prediction_request):
        """Test making predictions"""
        # Mock model
        mock_model = Mock()
        mock_model.predict.return_value = [0, 1]
        mock_store.load_model.return_value = mock_model

        response = client.post(
            "/api/v1/models/test-model-id/predict",
            json=mock_prediction_request
        )

        assert response.status_code == 200

        data = response.json()
        assert "predictions" in data
        assert data["model_id"] == "test-model-id"

    @patch("app.main.model_store")
    def test_predict_model_not_found(self, mock_store, client, mock_prediction_request):
        """Test prediction with non-existent model"""
        mock_store.load_model.return_value = None

        response = client.post(
            "/api/v1/models/non-existent-model/predict",
            json=mock_prediction_request
        )

        assert response.status_code == 404

    @patch("app.main.model_store")
    def test_predict_batch(self, mock_store, client, mock_prediction_request):
        """Test batch predictions"""
        mock_model = Mock()
        mock_model.predict.return_value = [0, 1]
        mock_store.load_model.return_value = mock_model

        # Add more features for batch
        batch_request = mock_prediction_request.copy()
        batch_request["features"] = [[0.5, -0.3, 1.0]] * 10

        response = client.post(
            "/api/v1/models/test-model-id/predict-batch",
            json=batch_request
        )

        assert response.status_code == 200
        assert len(response.json()["predictions"]) == 10


class TestAPIValidation:
    """Test API input validation"""

    def test_training_request_missing_required_fields(self, client):
        """Test validation of missing required fields"""
        incomplete_request = {
            "model_type": "classification"
            # Missing algorithm and data_source
        }

        response = client.post("/api/v1/training/jobs", json=incomplete_request)
        assert response.status_code == 422

    def test_prediction_request_missing_features(self, client):
        """Test prediction without features"""
        response = client.post(
            "/api/v1/models/test-model/predict",
            json={}
        )
        assert response.status_code == 422


@pytest.mark.asyncio
class TestAsyncOperations:
    """Test async operations"""

    @patch("app.main.run_training_job")
    @patch("app.main.ModelTrainer")
    async def test_background_training(self, mock_trainer_class, mock_run_job, client, mock_training_request):
        """Test that training runs in background"""
        mock_trainer_instance = Mock()
        mock_trainer_class.return_value = mock_trainer_instance

        response = client.post("/api/v1/training/jobs", json=mock_training_request)
        assert response.status_code == 200

        # Background task should have been scheduled
        # (actual execution verified by integration tests)
