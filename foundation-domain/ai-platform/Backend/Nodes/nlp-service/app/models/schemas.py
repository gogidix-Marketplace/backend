"""
Pydantic schemas for NLP Service
"""

from pydantic import BaseModel, Field
from typing import List, Optional, Dict, Any
from datetime import datetime


# ==================== Sentiment Analysis ====================

class SentimentAnalysisRequest(BaseModel):
    """Request for sentiment analysis"""
    texts: List[str] = Field(..., description="List of texts to analyze", min_items=1, max_items=100)
    language: str = Field(default="en", description="Language code (en, es, fr, etc.)")
    model: str = Field(default="rule-based", description="Model type to use")


class SentimentResult(BaseModel):
    """Result of sentiment analysis for a single text"""
    text: str = Field(..., description="Input text (truncated)")
    label: str = Field(..., description="Sentiment label: positive, negative, neutral")
    score: float = Field(..., description="Sentiment score from -1 to 1")
    confidence: float = Field(..., description="Confidence score from 0 to 1")
    probabilities: Dict[str, float] = Field(default_factory=dict, description="Class probabilities")


class SentimentAnalysisResponse(BaseModel):
    """Response for sentiment analysis"""
    results: List[SentimentResult]
    language: str
    model_used: str
    processed_at: datetime


# ==================== Text Classification ====================

class TextClassificationRequest(BaseModel):
    """Request for text classification"""
    texts: List[str] = Field(..., description="List of texts to classify", min_items=1, max_items=100)
    categories: Optional[List[str]] = Field(default=None, description="Custom categories")
    model: str = Field(default="keyword", description="Model type to use")


class ClassificationResult(BaseModel):
    """Result of text classification"""
    text: str = Field(..., description="Input text (truncated)")
    category: str = Field(..., description="Predicted category")
    confidence: float = Field(..., description="Confidence score")
    all_scores: Dict[str, float] = Field(default_factory=dict, description="All category scores")


class TextClassificationResponse(BaseModel):
    """Response for text classification"""
    results: List[ClassificationResult]
    categories: Optional[List[str]] = None
    model_used: str
    processed_at: datetime


# ==================== Named Entity Recognition ====================

class Entity(BaseModel):
    """Named entity"""
    text: str = Field(..., description="Entity text")
    label: str = Field(..., description="Entity type (PERSON, ORG, LOC, etc.)")
    start: int = Field(..., description="Start position in text")
    end: int = Field(..., description="End position in text")
    confidence: float = Field(..., description="Confidence score")


class NERRequest(BaseModel):
    """Request for named entity recognition"""
    texts: List[str] = Field(..., description="List of texts to process", min_items=1, max_items=50)
    entity_types: Optional[List[str]] = Field(default=None, description="Entity types to extract")
    language: str = Field(default="en", description="Language code")


class NERTextResult(BaseModel):
    """NER result for a single text"""
    text: str = Field(..., description="Input text (truncated)")
    entities: List[Entity] = Field(default_factory=list, description="Extracted entities")


class NERResponse(BaseModel):
    """Response for NER"""
    results: List[NERTextResult]
    language: str
    processed_at: datetime


# ==================== Chatbot ====================

class ChatMessage(BaseModel):
    """Chat message"""
    role: str = Field(..., description="Message role: user, assistant, system")
    content: str = Field(..., description="Message content")
    timestamp: Optional[datetime] = Field(default=None)


class ChatRequest(BaseModel):
    """Request for chatbot interaction"""
    session_id: str = Field(..., description="Session identifier")
    messages: List[ChatMessage] = Field(..., description="Conversation history", min_items=1)
    bot_type: str = Field(default="customer_service", description="Type of bot")
    context: Optional[Dict[str, Any]] = Field(default=None, description="Additional context")


class ChatResponse(BaseModel):
    """Response from chatbot"""
    session_id: str
    response: str = Field(..., description="Bot response text")
    confidence: float = Field(..., description="Response confidence")
    intent: Optional[str] = Field(default=None, description="Detected intent")
    entities: Dict[str, Any] = Field(default_factory=dict, description="Extracted entities")
    suggestions: List[str] = Field(default_factory=list, description="Suggested replies")
    timestamp: datetime


# ==================== Text Embedding ====================

class TextEmbeddingRequest(BaseModel):
    """Request for text embeddings"""
    texts: List[str] = Field(..., description="List of texts", min_items=1, max_items=100)
    model: str = Field(default="default", description="Embedding model")


class TextEmbeddingResponse(BaseModel):
    """Response for text embeddings"""
    embeddings: List[List[float]] = Field(..., description="Embedding vectors")
    model: str
    dimension: int
    processed_at: datetime


# ==================== Text Summarization ====================

class SummarizationRequest(BaseModel):
    """Request for text summarization"""
    text: str = Field(..., description="Text to summarize", min_length=50)
    max_length: int = Field(default=150, description="Maximum summary length in words")
    min_length: int = Field(default=30, description="Minimum summary length in words")
    model: str = Field(default="extractive", description="Summarization model")


class SummarizationResponse(BaseModel):
    """Response for text summarization"""
    summary: str = Field(..., description="Generated summary")
    original_length: int = Field(..., description="Original text word count")
    summary_length: int = Field(..., description="Summary word count")
    compression_ratio: float = Field(..., description="Compression ratio")
    model_used: str
    processed_at: datetime
