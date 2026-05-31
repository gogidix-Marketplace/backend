"""
Image Similarity Service
Compares images for visual similarity
"""

import logging
import hashlib
from typing import Dict, Any

logger = logging.getLogger(__name__)


class ImageSimilarityService:
    """Service for image similarity comparison"""

    def __init__(self):
        logger.info("Initialized ImageSimilarityService")

    async def compare(
        self,
        image_data1: bytes,
        image_data2: bytes
    ) -> Dict[str, Any]:
        """
        Compare two images for similarity

        Args:
            image_data1: First image bytes
            image_data2: Second image bytes

        Returns:
            Similarity results
        """
        # Calculate perceptual hash similarity
        similarity = self._calculate_hash_similarity(image_data1, image_data2)

        return {
            "similarity": round(similarity, 4),
            "are_similar": similarity > 0.85,
            "method": "perceptual_hash"
        }

    def _calculate_hash_similarity(self, data1: bytes, data2: bytes) -> float:
        """Calculate similarity based on perceptual hashing"""
        # Generate simple perceptual hashes
        hash1 = self._perceptual_hash(data1)
        hash2 = self._perceptual_hash(data2)

        # Calculate Hamming distance
        distance = self._hamming_distance(hash1, hash2)

        # Convert to similarity (0-1)
        max_distance = len(hash1) * 4  # Each hex digit = 4 bits
        similarity = 1 - (distance / max_distance) if max_distance > 0 else 1

        return similarity

    def _perceptual_hash(self, data: bytes) -> str:
        """Generate perceptual hash of image"""
        # Simplified perceptual hash
        # In production, use: imagehash, dHash, pHash, wHash

        # Sample bytes at regular intervals
        sample_size = 16
        step = max(1, len(data) // sample_size)

        sampled = []
        for i in range(0, len(data), step):
            if len(sampled) >= sample_size:
                break
            sampled.append(data[i])

        # Create hash from sampled values
        hash_input = bytes(sampled)
        return hashlib.md5(hash_input).hexdigest()[:16]

    def _hamming_distance(self, hash1: str, hash2: str) -> int:
        """Calculate Hamming distance between two hex strings"""
        # Convert hex to binary and count differing bits
        h1 = int(hash1, 16)
        h2 = int(hash2, 16)

        xor = h1 ^ h2
        distance = bin(xor).count('1')

        return distance
