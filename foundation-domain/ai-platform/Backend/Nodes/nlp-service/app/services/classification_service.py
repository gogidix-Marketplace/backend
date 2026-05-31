"""
Text Classification Service
Classifies text into categories using ML models
"""

import logging
from typing import List, Dict, Any, Optional
import re

logger = logging.getLogger(__name__)


class TextClassificationService:
    """Service for text classification"""

    def __init__(self):
        self.category_keywords = {
            "electronics": ["phone", "laptop", "computer", "tablet", "electronic", "device", "tech"],
            "clothing": ["shirt", "pants", "dress", "shoes", "clothing", "wear", "fashion"],
            "food": ["food", "restaurant", "meal", "eat", "drink", "cooking", "recipe"],
            "sports": ["sport", "game", "team", "player", "match", "fitness", "exercise"],
            "travel": ["travel", "trip", "vacation", "hotel", "flight", "destination", "journey"],
            "finance": ["money", "payment", "price", "cost", "bank", "finance", "investment"],
            "health": ["health", "medical", "doctor", "medicine", "wellness", "fitness", "symptom"],
            "entertainment": ["movie", "music", "game", "entertainment", "show", "concert", "theater"]
        }
        logger.info("Initialized TextClassificationService")

    async def classify_batch(
        self,
        texts: List[str],
        categories: Optional[List[str]] = None,
        model: str = "keyword"
    ) -> List[Dict[str, Any]]:
        """Classify a batch of texts"""
        results = []
        categories_to_use = categories or list(self.category_keywords.keys())

        for text in texts:
            result = self._classify(text, categories_to_use, model)
            results.append(result)

        return results

    def _classify(self, text: str, categories: List[str], model: str) -> Dict[str, Any]:
        """Classify single text"""
        scores = {}
        text_lower = text.lower()

        for category in categories:
            keywords = self.category_keywords.get(category, [])
            score = sum(1 for kw in keywords if kw in text_lower)
            scores[category] = score / max(len(keywords), 1)

        # Get top category
        if scores:
            top_category = max(scores, key=scores.get)
            confidence = min(1.0, scores[top_category] * 2)
        else:
            top_category = "general"
            confidence = 0.0

        return {
            "text": text[:100] + "..." if len(text) > 100 else text,
            "category": top_category,
            "confidence": round(confidence, 4),
            "all_scores": {k: round(v, 4) for k, v in scores.items()}
        }
