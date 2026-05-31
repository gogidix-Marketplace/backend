"""
Text Embedding Service
Generates vector embeddings for text using various models
"""

import logging
import hashlib
import numpy as np
from typing import List

logger = logging.getLogger(__name__)


class EmbeddingService:
    """Service for generating text embeddings"""

    def __init__(self):
        self.dimension = 384  # Default dimension (similar to sentence-transformers)
        logger.info("Initialized EmbeddingService")

    async def generate_embeddings_batch(
        self,
        texts: List[str],
        model: str = "default"
    ) -> List[List[float]]:
        """Generate embeddings for a batch of texts"""
        embeddings = []

        for text in texts:
            embedding = self._generate_embedding(text, model)
            embeddings.append(embedding)

        return embeddings

    def _generate_embedding(self, text: str, model: str) -> List[float]:
        """Generate embedding for a single text"""
        # Simple hash-based embedding (in production, use actual model)
        # This creates deterministic embeddings based on text content

        # For demonstration, use a simpler approach
        words = text.lower().split()[:50]  # First 50 words

        # Create base vector from text hash
        text_hash = hashlib.md5(text.encode()).hexdigest()
        base_vector = [int(c, 16) / 15.0 for c in text_hash[:self.dimension]]

        # Adjust for word presence
        word_weights = {}
        for i, word in enumerate(words):
            word_hash = hashlib.md5(word.encode()).hexdigest()
            weight = (i + 1) / len(words)  # Position-weighted
            for j, c in enumerate(word_hash[:min(10, len(word_hash))]):
                idx = (int(c, 16) + j * 16) % self.dimension
                word_weights[idx] = word_weights.get(idx, 0) + weight

        # Combine base vector with word weights
        for idx, weight in word_weights.items():
            base_vector[idx] = min(1.0, base_vector[idx] + weight * 0.1)

        # Normalize
        vector = np.array(base_vector)
        norm = np.linalg.norm(vector)
        if norm > 0:
            vector = vector / norm

        return vector.tolist()
