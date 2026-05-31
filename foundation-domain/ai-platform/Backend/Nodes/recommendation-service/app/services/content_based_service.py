"""
Content-Based Recommendation Service
Item recommendations based on content similarity
"""

import logging
from typing import List, Dict, Any, Optional
from collections import defaultdict
import re

logger = logging.getLogger(__name__)


class ContentBasedService:
    """Service for content-based recommendations"""

    def __init__(self):
        # Item catalog with features
        self.item_features = {}  # item_id -> {feature: value}
        self.user_profiles = defaultdict(lambda: defaultdict(float))  # user_id -> {feature: weight}
        logger.info("Initialized ContentBasedService")

    async def get_recommendations(
        self,
        user_id: str,
        count: int = 10,
        context: Optional[Dict[str, Any]] = None
    ) -> List[Dict[str, Any]]:
        """Get content-based recommendations"""
        user_profile = self.user_profiles.get(user_id, {})

        if not user_profile:
            # No profile, return popular items
            return await self._get_popular_items(count)

        # Score items based on user profile
        item_scores = []

        for item_id, features in self.item_features.items():
            score = self._calculate_content_score(user_profile, features)

            if score > 0:
                item_scores.append({
                    "item_id": item_id,
                    "score": round(score, 4),
                    "reason": "content_match"
                })

        # Sort by score
        item_scores.sort(key=lambda x: x["score"], reverse=True)

        return item_scores[:count]

    async def get_similar_items(self, item_id: str, count: int = 10) -> List[Dict[str, Any]]:
        """Get similar items based on content"""
        if item_id not in self.item_features:
            return []

        target_features = self.item_features[item_id]
        similarities = []

        for other_id, other_features in self.item_features.items():
            if other_id == item_id:
                continue

            similarity = self._cosine_similarity(target_features, other_features)

            if similarity > 0:
                similarities.append({
                    "item_id": other_id,
                    "score": round(similarity, 4)
                })

        similarities.sort(key=lambda x: x["score"], reverse=True)

        return similarities[:count]

    async def get_item_similarity(self, item_id1: str, item_id2: str) -> float:
        """Calculate content similarity between items"""
        if item_id1 not in self.item_features or item_id2 not in self.item_features:
            return 0.0

        features1 = self.item_features[item_id1]
        features2 = self.item_features[item_id2]

        return round(self._cosine_similarity(features1, features2), 4)

    def _calculate_content_score(
        self,
        user_profile: Dict[str, float],
        item_features: Dict[str, Any]
    ) -> float:
        """Calculate content-based score for item"""
        score = 0.0

        for feature, value in item_features.items():
            if isinstance(value, (int, float)):
                feature_weight = user_profile.get(feature, 0)
                score += feature_weight * value
            elif isinstance(value, str):
                # For categorical features, check exact match
                feature_weight = user_profile.get(value, 0)
                score += feature_weight

        return score

    def _cosine_similarity(
        self,
        features1: Dict[str, Any],
        features2: Dict[str, Any]
    ) -> float:
        """Calculate cosine similarity between feature vectors"""
        # Convert to simple numerical vectors
        all_keys = set(features1.keys()) | set(features2.keys())

        vec1 = []
        vec2 = []

        for key in all_keys:
            val1 = float(features1.get(key, 0)) if isinstance(features1.get(key), (int, float)) else 0
            val2 = float(features2.get(key, 0)) if isinstance(features2.get(key), (int, float)) else 0

            # For string values, use match indicator
            if isinstance(features1.get(key), str) and isinstance(features2.get(key), str):
                val1 = 1.0 if features1[key] == features2[key] else 0
                val2 = val1

            vec1.append(val1)
            vec2.append(val2)

        # Cosine similarity
        dot_product = sum(v1 * v2 for v1, v2 in zip(vec1, vec2))
        norm1 = sum(v ** 2 for v in vec1) ** 0.5
        norm2 = sum(v ** 2 for v in vec2) ** 0.5

        if norm1 == 0 or norm2 == 0:
            return 0.0

        return dot_product / (norm1 * norm2)

    async def _get_popular_items(self, count: int) -> List[Dict[str, Any]]:
        """Get popular items as fallback"""
        items = []
        for i, item_id in enumerate(list(self.item_features.keys())[:count]):
            items.append({
                "item_id": item_id,
                "score": round(1.0 - i * 0.1, 4),  # Decreasing scores
                "reason": "popular"
            })
        return items

    def add_item(self, item_id: str, features: Dict[str, Any]):
        """Add item to catalog"""
        self.item_features[item_id] = features

    def update_user_profile(self, user_id: str, item_id: str, rating: float):
        """Update user profile based on interaction"""
        if item_id not in self.item_features:
            return

        features = self.item_features[item_id]

        # Update feature weights based on rating
        for feature, value in features.items():
            if isinstance(value, str):
                feature_key = value
                weight = rating
            else:
                feature_key = feature
                weight = rating * value if isinstance(value, (int, float)) else rating

            # Exponential moving average
            current_weight = self.user_profiles[user_id][feature_key]
            self.user_profiles[user_id][feature_key] = 0.8 * current_weight + 0.2 * weight
