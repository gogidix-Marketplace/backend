"""
Sentiment Analysis Service
Analyzes text sentiment using various NLP models
"""

import logging
from typing import List, Dict, Any, Optional
import numpy as np

logger = logging.getLogger(__name__)


class SentimentResult:
    """Result of sentiment analysis"""
    def __init__(
        self,
        text: str,
        label: str,
        score: float,
        confidence: float,
        probabilities: Optional[Dict[str, float]] = None
    ):
        self.text = text
        self.label = label  # positive, negative, neutral
        self.score = score  # -1 to 1 scale
        self.confidence = confidence  # 0 to 1
        self.probabilities = probabilities or {}


class SentimentService:
    """
    Sentiment analysis service using multiple approaches
    1. Rule-based (VADER-like)
    2. ML-based (scikit-learn)
    3. Deep Learning (transformers)
    """

    # Sentiment lexicon (simplified VADER-like)
    POSITIVE_WORDS = {
        "good", "great", "excellent", "amazing", "wonderful", "fantastic",
        "love", "happy", "pleased", "satisfied", "recommend", "best",
        "awesome", "perfect", "outstanding", "brilliant", "superb",
        "delighted", "impressed", "helpful", "quality", "beautiful",
        "enjoy", "excited", "fortunate", "grateful", "pleasure"
    }

    NEGATIVE_WORDS = {
        "bad", "terrible", "horrible", "awful", "poor", "worst",
        "hate", "disappointed", "sad", "angry", "frustrated", "annoyed",
        "disgusting", "pathetic", "useless", "waste", "boring",
        "unhappy", "dissatisfied", "regret", "avoid", "problem",
        "issue", "complaint", "refund", "slow", "rude", "expensive"
    }

    BOOSTERS = {"very", "really", "extremely", "absolutely", "completely"}
    NEGATIONS = {"not", "no", "never", "neither", "nor", "none"}

    def __init__(self, model_type: str = "rule-based"):
        self.model_type = model_type
        logger.info(f"Initialized SentimentService with model: {model_type}")

    async def analyze_batch(
        self,
        texts: List[str],
        language: str = "en",
        model: str = "rule-based"
    ) -> List[Dict[str, Any]]:
        """
        Analyze sentiment for a batch of texts

        Args:
            texts: List of text strings
            language: Language code
            model: Model type to use

        Returns:
            List of sentiment results
        """
        results = []

        for text in texts:
            if model == "rule-based":
                result = self._analyze_rule_based(text)
            elif model == "ml":
                result = await self._analyze_ml(text)
            else:
                result = self._analyze_rule_based(text)  # Default to rule-based

            results.append({
                "text": text[:200] + "..." if len(text) > 200 else text,
                "label": result.label,
                "score": result.score,
                "confidence": result.confidence,
                "probabilities": result.probabilities
            })

        return results

    def _analyze_rule_based(self, text: str) -> SentimentResult:
        """Rule-based sentiment analysis (VADER-like approach)"""
        text_lower = text.lower()
        words = text_lower.split()

        # Count positive and negative words
        positive_count = sum(1 for word in words if word in self.POSITIVE_WORDS)
        negative_count = sum(1 for word in words if word in self.NEGATIVE_WORDS)

        # Apply negation
        negated = False
        for i, word in enumerate(words):
            if word in self.NEGATIONS:
                negated = True
                # Check next word
                if i + 1 < len(words):
                    next_word = words[i + 1]
                    if next_word in self.POSITIVE_WORDS:
                        positive_count -= 1
                        negative_count += 1
                    elif next_word in self.NEGATIVE_WORDS:
                        negative_count -= 1
                        positive_count += 1

        # Apply boosters
        for word in words:
            if word in self.BOOSTERS:
                positive_count = positive_count * 1.5
                negative_count = negative_count * 1.5

        # Calculate score
        total_words = len(words)
        if total_words == 0:
            return SentimentResult(text, "neutral", 0.0, 0.0)

        # Normalize score between -1 and 1
        raw_score = (positive_count - negative_count) / max(total_words * 0.1, 1)
        score = max(-1.0, min(1.0, raw_score))

        # Determine label
        if score > 0.1:
            label = "positive"
        elif score < -0.1:
            label = "negative"
        else:
            label = "neutral"

        # Calculate confidence based on extremity
        confidence = abs(score)

        # Calculate probabilities
        pos_prob = max(0, min(1, (score + 1) / 2))
        neg_prob = max(0, min(1, (1 - score) / 2))
        neu_prob = 1 - (pos_prob + neg_prob) / 2

        return SentimentResult(
            text=text,
            label=label,
            score=round(score, 4),
            confidence=round(confidence, 4),
            probabilities={
                "positive": round(pos_prob, 4),
                "negative": round(neg_prob, 4),
                "neutral": round(neu_prob, 4)
            }
        )

    async def _analyze_ml(self, text: str) -> SentimentResult:
        """ML-based sentiment analysis"""
        # This would use a trained ML model
        # For now, fall back to rule-based
        return self._analyze_rule_based(text)

    def _preprocess_text(self, text: str) -> str:
        """Preprocess text for analysis"""
        # Basic preprocessing
        text = text.strip()
        # Remove URLs
        import re
        text = re.sub(r'http\S+', '', text)
        # Remove mentions
        text = re.sub(r'@\w+', '', text)
        # Remove extra whitespace
        text = ' '.join(text.split())
        return text

    def analyze_with_emotion(self, text: str) -> Dict[str, Any]:
        """
        Analyze sentiment with emotion detection

        Returns sentiment plus detected emotions:
        - joy, sadness, anger, fear, surprise, disgust
        """
        text_lower = text.lower()

        # Emotion keywords
        emotions = {
            "joy": ["happy", "excited", "delighted", "thrilled", "joy", "cheerful"],
            "sadness": ["sad", "unhappy", "depressed", "down", "disappointed"],
            "anger": ["angry", "furious", "mad", "irritated", "annoyed"],
            "fear": ["afraid", "scared", "frightened", "anxious", "worried"],
            "surprise": ["surprised", "shocked", "amazed", "astonished"],
            "disgust": ["disgusted", "revolted", "sickened", "appalled"]
        }

        detected_emotions = {}
        for emotion, keywords in emotions.items():
            count = sum(1 for word in keywords if word in text_lower)
            if count > 0:
                detected_emotions[emotion] = min(1.0, count * 0.3)

        sentiment_result = self._analyze_rule_based(text)

        return {
            "sentiment": {
                "label": sentiment_result.label,
                "score": sentiment_result.score,
                "confidence": sentiment_result.confidence
            },
            "emotions": detected_emotions or {"neutral": 1.0}
        }
