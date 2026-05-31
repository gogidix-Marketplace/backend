"""
Hybrid Recommendation Service
Combines collaborative filtering and content-based approaches
"""

import logging
from typing import List, Dict, Any, Optional
import heapq

logger = logging.getLogger(__name__)


class HybridRecommendationService:
    """Service for hybrid recommendations"""

    def __init__(
        self,
        collaborative_service,
        content_based_service,
        collaborative_weight: float = 0.6
    ):
        self.collaborative_service = collaborative_service
        self.content_service = content_based_service
        self.collaborative_weight = collaborative_weight
        self.content_weight = 1.0 - collaborative_weight
        logger.info("Initialized HybridRecommendationService")

    async def get_recommendations(
        self,
        user_id: str,
        count: int = 10,
        context: Optional[Dict[str, Any]] = None
    ) -> List[Dict[str, Any]]:
        """Get hybrid recommendations combining both approaches"""
        # Get recommendations from both services
        collab_recs = await self.collaborative_service.get_recommendations(
            user_id=user_id,
            count=count * 2,  # Get more to merge
            context=context
        )

        content_recs = await self.content_service.get_recommendations(
            user_id=user_id,
            count=count * 2,
            context=context
        )

        # Merge and re-score
        merged_scores = {}

        # Add collaborative scores
        for rec in collab_recs:
            item_id = rec["item_id"]
            merged_scores[item_id] = rec["score"] * self.collaborative_weight

        # Add content scores
        for rec in content_recs:
            item_id = rec["item_id"]
            if item_id in merged_scores:
                merged_scores[item_id] += rec["score"] * self.content_weight
            else:
                merged_scores[item_id] = rec["score"] * self.content_weight

        # Convert to list and sort
        recommendations = [
            {
                "item_id": item_id,
                "score": round(score, 4),
                "reason": "hybrid_recommendation"
            }
            for item_id, score in merged_scores.items()
        ]

        recommendations.sort(key=lambda x: x["score"], reverse=True)

        return recommendations[:count]

    async def personalize_ranking(
        self,
        user_id: str,
        items: List[str],
        context: Optional[Dict[str, Any]] = None
    ) -> List[Dict[str, Any]]:
        """Re-rank items based on user preferences"""
        scored_items = []

        for item_id in items:
            # Get content score
            if item_id in self.content_service.item_features:
                user_profile = self.content_service.user_profiles.get(user_id, {})
                features = self.content_service.item_features[item_id]
                content_score = self.content_service._calculate_content_score(user_profile, features)
            else:
                content_score = 0.0

            # Get collaborative score
            user_items = self.collaborative_service.user_items.get(user_id, set())
            similar_users = self.collaborative_service._find_similar_users(user_id, n=10)

            collab_score = 0.0
            for similar_user, similarity in similar_users:
                if item_id in self.collaborative_service.user_items.get(similar_user, set()):
                    collab_score += similarity

            # Combine scores
            hybrid_score = (
                content_score * self.content_weight +
                min(collab_score, 1.0) * self.collaborative_weight
            )

            scored_items.append({
                "item_id": item_id,
                "score": round(hybrid_score, 4),
                "content_score": round(content_score, 4),
                "collaborative_score": round(min(collab_score, 1.0), 4)
            })

        # Sort by score
        scored_items.sort(key=lambda x: x["score"], reverse=True)

        return scored_items
