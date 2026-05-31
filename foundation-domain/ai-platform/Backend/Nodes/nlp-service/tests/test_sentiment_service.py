"""
Tests for NLP Service - Sentiment Analysis
"""

import pytest
from app.services.sentiment_service import SentimentService


class TestSentimentService:
    """Test cases for SentimentService"""

    @pytest.fixture
    def service(self):
        """Create sentiment service instance"""
        return SentimentService()

    def test_positive_sentiment_detection(self, service):
        """Test detection of positive sentiment"""
        texts = ["I love this product!", "Amazing service, very happy"]
        results = service._analyze_rule_based(texts[0])

        assert results.label == "positive"
        assert results.score > 0

    def test_negative_sentiment_detection(self, service):
        """Test detection of negative sentiment"""
        texts = ["This is terrible and awful", "Very disappointed with the service"]
        results = service._analyze_rule_based(texts[0])

        assert results.label == "negative"
        assert results.score < 0

    def test_neutral_sentiment_detection(self, service):
        """Test detection of neutral sentiment"""
        text = "The product arrived yesterday"
        result = service._analyze_rule_based(text)

        assert result.label == "neutral"

    def test_batch_analysis(self, service):
        """Test batch sentiment analysis"""
        texts = ["Great product", "Terrible experience", "It's okay"]

        import asyncio
        results = asyncio.run(service.analyze_batch(texts))

        assert len(results) == 3
        assert results[0]["label"] == "positive"
        assert results[1]["label"] == "negative"

    def test_with_negation(self, service):
        """Test sentiment with negation words"""
        text = "Not good at all"
        result = service._analyze_rule_based(text)

        # Negation should flip sentiment
        assert result.label == "negative" or result.score < 0

    def test_with_booster_words(self, service):
        """Test sentiment with booster words"""
        text = "Very very good!"
        result = service._analyze_rule_based(text)

        assert "very" in SentimentService.BOOSTERS
        assert result.label == "positive"

    def test_confidence_calculation(self, service):
        """Test confidence score calculation"""
        text = "I absolutely love this amazing product!"
        result = service._analyze_rule_based(text)

        assert 0 <= result.confidence <= 1
        assert result.confidence > 0.5  # Strong sentiment should have high confidence

    def test_probabilities_sum_to_one(self, service):
        """Test that sentiment probabilities sum to approximately 1"""
        text = "This is a test"
        result = service._analyze_rule_based(text)

        if result.probabilities:
            total = sum(result.probabilities.values())
            assert 0.9 <= total <= 1.1  # Allow small floating point error

    def test_emotion_detection(self, service):
        """Test emotion detection"""
        text = "I am so happy and excited!"
        result = service.analyze_with_emotion(text)

        assert "sentiment" in result
        assert "emotions" in result
        assert result["sentiment"]["label"] == "positive"

    def test_empty_text_handling(self, service):
        """Test handling of empty text"""
        result = service._analyze_rule_based("")

        assert result.label == "neutral"
        assert result.score == 0.0


@pytest.mark.asyncio
class TestSentimentServiceAsync:
    """Async tests for SentimentService"""

    async def test_concurrent_analysis(self):
        """Test concurrent sentiment analysis"""
        service = SentimentService()
        texts = ["test"] * 100

        results = await service.analyze_batch(texts)

        assert len(results) == 100


class TestSentimentServiceIntegration:
    """Integration tests for sentiment service"""

    def test_full_pipeline(self):
        """Test complete sentiment analysis pipeline"""
        service = SentimentService()

        # Test input
        review = """
        I purchased this product last week and I'm extremely satisfied!
        The quality is excellent and delivery was fast. Highly recommend!
        """

        result = service._analyze_rule_based(review)

        assert result.label == "positive"
        assert result.confidence > 0.5
        assert "excellent" in SentimentService.POSITIVE_WORDS

    def test_real_world_reviews(self):
        """Test with real-world review examples"""
        service = SentimentService()

        reviews = [
            "Worst purchase ever. Complete waste of money.",
            "Absolutely fantastic! Exceeded my expectations.",
            "It's okay, nothing special but does the job.",
            "Would not recommend to anyone. Poor quality.",
            "Great value for money. Very pleased!"
        ]

        results = [service._analyze_rule_based(review) for review in reviews]

        assert results[0].label == "negative"
        assert results[1].label == "positive"
        assert results[2].label == "neutral"
        assert results[3].label == "negative"
        assert results[4].label == "positive"


class TestNLPServiceWithMocks:
    """Tests with mocked dependencies"""

    def test_with_custom_lexicon(self):
        """Test sentiment analysis with custom lexicon"""
        service = SentimentService()

        # Add custom words
        service.POSITIVE_WORDS.add("fantabulous")
        service.NEGATIVE_WORDS.add("meh")

        pos_result = service._analyze_rule_based("This is fantabulous!")
        neg_result = service._analyze_rule_based("This is meh")

        assert pos_result.label == "positive"
        assert neg_result.label == "negative"
