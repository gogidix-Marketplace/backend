"""
Collaborative Filtering Service
User-based and item-based collaborative filtering recommendations
"""

import logging
from typing import List, Dict, Any, Optional
import numpy as np
from collections import defaultdict

logger = logging.getLogger(__name__)


class CollaborativeFilteringService:
    """Service for collaborative filtering recommendations"""

    def __init__(self):
        # Simulated user-item interaction matrix
        self.user_items = defaultdict(set)  # user_id -> set of item_ids
        self.item_users = defaultdict(set)  # item_id -> set of user_ids
        self.user_ratings = defaultdict(dict)  # user_id -> {item_id: rating}
        logger.info("Initialized CollaborativeFilteringService")

    async def get_recommendations(
        self,
        user_id: str,
        count: int = 10,
        context: Optional[Dict[str, Any]] = None
    ) -> List[Dict[str, Any]]:
        """
        Get collaborative filtering recommendations

        Args:
            user_id: User identifier
            count: Number of recommendations
            context: Additional context

        Returns:
            List of recommended items with scores
        """
        # Get items user hasn't interacted with
        user_items = self.user_items.get(user_id, set())

        # Find similar users
        similar_users = self._find_similar_users(user_id, n=20)

        # Score items based on similar user interactions
        item_scores = defaultdict(float)
        item_counts = defaultdict(int)

        for similar_user, similarity in similar_users:
            for item_id in self.user_items.get(similar_user, set()):
                if item_id not in user_items:
                    item_scores[item_id] += similarity
                    item_counts[item_id] += 1

        # Aggregate scores
        recommendations = []
        for item_id, score in item_scores.items():
            avg_score = score / item_counts[item_id]
            recommendations.append({
                "item_id": item_id,
                "score": round(avg_score, 4),
                "reason": "similar_users_interacted"
            })

        # Sort by score and limit
        recommendations.sort(key=lambda x: x["score"], reverse=True)

        return recommendations[:count]

    async def get_similar_items(self, item_id: str, count: int = 10) -> List[Dict[str, Any]]:
        """Get similar items based on user interactions"""
        # Find items that were interacted with by similar users
        item_users = self.item_users.get(item_id, set())

        item_scores = defaultdict(float)

        for user_id in item_users:
            for other_item in self.user_items.get(user_id, set()):
                if other_item != item_id:
                    # Jaccard similarity approximation
                    item_scores[other_item] += 1

        # Normalize scores
        max_score = max(item_scores.values()) if item_scores else 1
        recommendations = [
            {
                "item_id": item,
                "score": round(score / max_score, 4)
            }
            for item, score in item_scores.items()
        ]

        recommendations.sort(key=lambda x: x["score"], reverse=True)

        return recommendations[:count]

    async def get_user_similarity(self, user_id1: str, user_id2: str) -> float:
        """Calculate similarity between two users"""
        items1 = self.user_items.get(user_id1, set())
        items2 = self.user_items.get(user_id2, set())

        if not items1 or not items2:
            return 0.0

        # Jaccard similarity
        intersection = len(items1 & items2)
        union = len(items1 | items2)

        return round(intersection / union if union > 0 else 0, 4)

    def _find_similar_users(self, user_id: str, n: int = 20) -> List[tuple]:
        """Find similar users using Jaccard similarity"""
        user_items = self.user_items.get(user_id, set())

        if not user_items:
            return []

        similarities = []

        for other_user_id, other_items in self.user_items.items():
            if other_user_id == user_id:
                continue

            # Jaccard similarity
            intersection = len(user_items & other_items)
            union = len(user_items | other_items)

            if union > 0:
                similarity = intersection / union
                if similarity > 0:
                    similarities.append((other_user_id, similarity))

        # Sort by similarity
        similarities.sort(key=lambda x: x[1], reverse=True)

        return similarities[:n]

    def add_interaction(self, user_id: str, item_id: str, rating: Optional[float] = None):
        """Add a user-item interaction"""
        self.user_items[user_id].add(item_id)
        self.item_users[item_id].add(user_id)

        if rating is not None:
            self.user_ratings[user_id][item_id] = rating

    def batch_add_interactions(self, interactions: List[Dict[str, Any]]):
        """Add multiple interactions at once"""
        for interaction in interactions:
            self.add_interaction(
                user_id=interaction["user_id"],
                item_id=interaction["item_id"],
                rating=interaction.get("rating")
            )
