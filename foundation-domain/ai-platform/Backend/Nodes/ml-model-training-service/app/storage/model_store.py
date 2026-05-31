"""
Model Store - Persistent storage for trained models
"""

import os
import json
import logging
import hashlib
import pickle
import joblib
from typing import Optional, List, Dict, Any
from datetime import datetime
from pathlib import Path
from dataclasses import dataclass, asdict

from app.models.schemas import ModelInfo, ModelType, AlgorithmType

logger = logging.getLogger(__name__)


@dataclass
class ModelMetadata:
    """Model metadata"""
    model_id: str
    model_type: str
    algorithm: str
    version: int
    created_at: str
    metrics: Dict[str, Any]
    config: Dict[str, Any]
    feature_columns: List[str]
    tags: List[str]
    file_name: str
    file_size_bytes: int
    is_active: bool = True


class ModelStore:
    """
    Model store for saving and loading trained models
    Supports versioning and metadata tracking
    """

    def __init__(self, storage_path: str):
        self.storage_path = Path(storage_path)
        self.models_dir = self.storage_path / "models"
        self.metadata_file = self.storage_path / "metadata.json"

        # Create directories
        self.models_dir.mkdir(parents=True, exist_ok=True)

        # Load existing metadata
        self.metadata: Dict[str, ModelMetadata] = {}
        self._load_metadata()

    def _load_metadata(self):
        """Load metadata from file"""
        if self.metadata_file.exists():
            try:
                with open(self.metadata_file, "r") as f:
                    data = json.load(f)
                    for model_id, meta in data.items():
                        self.metadata[model_id] = ModelMetadata(**meta)
                logger.info(f"Loaded metadata for {len(self.metadata)} models")
            except Exception as e:
                logger.error(f"Error loading metadata: {e}")

    def _save_metadata(self):
        """Save metadata to file"""
        try:
            data = {
                model_id: asdict(meta)
                for model_id, meta in self.metadata.items()
            }
            with open(self.metadata_file, "w") as f:
                json.dump(data, f, indent=2, default=str)
        except Exception as e:
            logger.error(f"Error saving metadata: {e}")

    def _generate_model_id(self, model_type: ModelType, algorithm: AlgorithmType) -> str:
        """Generate unique model ID"""
        timestamp = datetime.utcnow().strftime("%Y%m%d_%H%M%S")
        hash_input = f"{model_type.value}_{algorithm.value}_{timestamp}"
        hash_suffix = hashlib.md5(hash_input.encode()).hexdigest()[:8]
        return f"{model_type.value}_{algorithm.value}_{timestamp}_{hash_suffix}"

    def save_model(
        self,
        model: Any,
        model_type: ModelType,
        algorithm: AlgorithmType,
        metrics: Dict[str, float],
        config: Dict[str, Any],
        feature_columns: Optional[List[str]] = None,
        tags: Optional[List[str]] = None
    ) -> str:
        """
        Save a trained model to the store

        Returns:
            str: The model ID
        """
        model_id = self._generate_model_id(model_type, algorithm)

        # Get version for this algorithm
        existing_versions = [
            meta.version for meta in self.metadata.values()
            if meta.algorithm == algorithm.value and meta.model_type == model_type.value
        ]
        version = max(existing_versions, default=0) + 1

        # Save model file
        file_name = f"{model_id}.pkl"
        file_path = self.models_dir / file_name

        try:
            joblib.dump(model, file_path)
            file_size = file_path.stat().st_size
        except Exception as e:
            logger.error(f"Error saving model file: {e}")
            raise

        # Create metadata
        metadata = ModelMetadata(
            model_id=model_id,
            model_type=model_type.value,
            algorithm=algorithm.value,
            version=version,
            created_at=datetime.utcnow().isoformat(),
            metrics=metrics,
            config=config,
            feature_columns=feature_columns or [],
            tags=tags or [],
            file_name=file_name,
            file_size_bytes=file_size,
            is_active=True
        )

        # Store metadata
        self.metadata[model_id] = metadata
        self._save_metadata()

        logger.info(f"Saved model {model_id} (version {version})")
        return model_id

    def load_model(self, model_id: str) -> Optional[Any]:
        """
        Load a model from the store

        Returns:
            The loaded model or None if not found
        """
        if model_id not in self.metadata:
            logger.warning(f"Model {model_id} not found in metadata")
            return None

        metadata = self.metadata[model_id]
        file_path = self.models_dir / metadata.file_name

        if not file_path.exists():
            logger.error(f"Model file {file_path} not found")
            return None

        try:
            model = joblib.load(file_path)
            logger.info(f"Loaded model {model_id}")
            return model
        except Exception as e:
            logger.error(f"Error loading model {model_id}: {e}")
            return None

    def get_model_info(self, model_id: str) -> Optional[ModelInfo]:
        """Get model information"""
        if model_id not in self.metadata:
            return None

        meta = self.metadata[model_id]
        return ModelInfo(
            model_id=meta.model_id,
            model_type=ModelType(meta.model_type),
            algorithm=AlgorithmType(meta.algorithm),
            version=meta.version,
            created_at=datetime.fromisoformat(meta.created_at),
            metrics=meta.metrics,
            config=meta.config,
            is_active=meta.is_active,
            file_size_bytes=meta.file_size_bytes,
            tags=meta.tags
        )

    def list_models(
        self,
        skip: int = 0,
        limit: int = 100,
        model_type: Optional[str] = None
    ) -> List[ModelInfo]:
        """List all models with optional filtering"""
        models = []

        for meta in self.metadata.values():
            if model_type and meta.model_type != model_type:
                continue
            models.append(ModelInfo(
                model_id=meta.model_id,
                model_type=ModelType(meta.model_type),
                algorithm=AlgorithmType(meta.algorithm),
                version=meta.version,
                created_at=datetime.fromisoformat(meta.created_at),
                metrics=meta.metrics,
                config=meta.config,
                is_active=meta.is_active,
                file_size_bytes=meta.file_size_bytes,
                tags=meta.tags
            ))

        # Sort by creation date (newest first)
        models.sort(key=lambda m: m.created_at, reverse=True)

        return models[skip:skip + limit]

    def delete_model(self, model_id: str) -> bool:
        """Delete a model from the store"""
        if model_id not in self.metadata:
            return False

        metadata = self.metadata[model_id]
        file_path = self.models_dir / metadata.file_name

        # Delete file
        try:
            if file_path.exists():
                file_path.unlink()
        except Exception as e:
            logger.error(f"Error deleting model file: {e}")

        # Remove metadata
        del self.metadata[model_id]
        self._save_metadata()

        logger.info(f"Deleted model {model_id}")
        return True

    def get_latest_model(
        self,
        model_type: ModelType,
        algorithm: Optional[AlgorithmType] = None
    ) -> Optional[str]:
        """Get the latest model ID for a given type/algorithm"""
        candidates = []

        for meta in self.metadata.values():
            if meta.model_type == model_type.value:
                if algorithm is None or meta.algorithm == algorithm.value:
                    candidates.append((meta.created_at, meta.model_id))

        if not candidates:
            return None

        # Return the most recent
        candidates.sort(reverse=True)
        return candidates[0][1]

    def activate_model(self, model_id: str) -> bool:
        """Activate a model (mark as active)"""
        if model_id not in self.metadata:
            return False

        self.metadata[model_id].is_active = True
        self._save_metadata()
        return True

    def deactivate_model(self, model_id: str) -> bool:
        """Deactivate a model"""
        if model_id not in self.metadata:
            return False

        self.metadata[model_id].is_active = False
        self._save_metadata()
        return True

    def get_storage_stats(self) -> Dict[str, Any]:
        """Get storage statistics"""
        total_size = sum(
            meta.file_size_bytes
            for meta in self.metadata.values()
        )

        models_by_type = {}
        for meta in self.metadata.values():
            models_by_type[meta.model_type] = models_by_type.get(meta.model_type, 0) + 1

        return {
            "total_models": len(self.metadata),
            "total_storage_bytes": total_size,
            "total_storage_mb": round(total_size / (1024 * 1024), 2),
            "models_by_type": models_by_type,
            "active_models": sum(1 for meta in self.metadata.values() if meta.is_active)
        }
