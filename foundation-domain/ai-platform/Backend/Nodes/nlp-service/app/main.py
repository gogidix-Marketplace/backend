"""
NLP Service - Main Application
FastAPI microservice for Natural Language Processing capabilities
"""

from fastapi import FastAPI, HTTPException, BackgroundTasks
from fastapi.middleware.cors import CORSMiddleware
from typing import List, Optional, Dict, Any
from datetime import datetime
import logging
import uvicorn

from app.config import settings
from app.models.schemas import (
    SentimentAnalysisRequest,
    SentimentAnalysisResponse,
    TextClassificationRequest,
    TextClassificationResponse,
    NERRequest,
    NERResponse,
    ChatMessage,
    ChatRequest,
    ChatResponse,
    TextEmbeddingRequest,
    TextEmbeddingResponse,
    SummarizationRequest,
    SummarizationResponse
)
from app.services.sentiment_service import SentimentService
from app.services.classification_service import TextClassificationService
from app.services.ner_service import NERService
from app.services.chatbot_service import ChatbotService
from app.services.embedding_service import EmbeddingService
from app.services.summarization_service import SummarizationService

# Configure logging
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s'
)
logger = logging.getLogger(__name__)

# Initialize FastAPI app
app = FastAPI(
    title="NLP Service",
    description="Natural Language Processing API for Gogidix Ecosystem",
    version="1.0.0",
    docs_url="/docs",
    redoc_url="/redoc"
)

# CORS middleware
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Initialize services
sentiment_service = SentimentService()
classification_service = TextClassificationService()
ner_service = NERService()
chatbot_service = ChatbotService()
embedding_service = EmbeddingService()
summarization_service = SummarizationService()


@app.on_event("startup")
async def startup_event():
    """Initialize service on startup"""
    logger.info("Starting NLP Service...")
    logger.info("NLP Service started successfully")


@app.on_event("shutdown")
async def shutdown_event():
    """Cleanup on shutdown"""
    logger.info("Shutting down NLP Service...")


@app.get("/", tags=["Health"])
async def root():
    """Root endpoint"""
    return {
        "service": "NLP Service",
        "version": "1.0.0",
        "status": "healthy",
        "timestamp": datetime.utcnow().isoformat(),
        "capabilities": [
            "sentiment_analysis",
            "text_classification",
            "named_entity_recognition",
            "chatbot",
            "text_embedding",
            "text_summarization"
        ]
    }


@app.get("/health", tags=["Health"])
async def health_check():
    """Health check endpoint"""
    return {
        "status": "healthy",
        "timestamp": datetime.utcnow().isoformat()
    }


# ==================== Sentiment Analysis ====================

@app.post("/api/v1/sentiment/analyze", response_model=SentimentAnalysisResponse, tags=["Sentiment"])
async def analyze_sentiment(request: SentimentAnalysisRequest):
    """
    Analyze sentiment of text

    - **texts**: List of text strings to analyze
    - **language**: Language code (default: en)
    - **model**: Model to use (default: distilbert)
    """
    try:
        results = await sentiment_service.analyze_batch(
            texts=request.texts,
            language=request.language,
            model=request.model
        )

        return SentimentAnalysisResponse(
            results=results,
            language=request.language,
            model_used=request.model,
            processed_at=datetime.utcnow()
        )
    except Exception as e:
        logger.error(f"Error in sentiment analysis: {str(e)}")
        raise HTTPException(status_code=500, detail=str(e))


# ==================== Text Classification ====================

@app.post("/api/v1/classification/predict", response_model=TextClassificationResponse, tags=["Classification"])
async def classify_text(request: TextClassificationRequest):
    """
    Classify text into categories

    - **texts**: List of text strings to classify
    - **categories**: Custom categories (optional)
    - **model**: Model to use (default: zero-shot)
    """
    try:
        results = await classification_service.classify_batch(
            texts=request.texts,
            categories=request.categories,
            model=request.model
        )

        return TextClassificationResponse(
            results=results,
            categories=request.categories,
            model_used=request.model,
            processed_at=datetime.utcnow()
        )
    except Exception as e:
        logger.error(f"Error in text classification: {str(e)}")
        raise HTTPException(status_code=500, detail=str(e))


# ==================== Named Entity Recognition ====================

@app.post("/api/v1/ner/extract", response_model=NERResponse, tags=["NER"])
async def extract_entities(request: NERRequest):
    """
    Extract named entities from text

    - **texts**: List of text strings to process
    - **entity_types**: Types of entities to extract (PERSON, ORG, LOC, etc.)
    - **language**: Language code (default: en)
    """
    try:
        results = await ner_service.extract_entities_batch(
            texts=request.texts,
            entity_types=request.entity_types,
            language=request.language
        )

        return NERResponse(
            results=results,
            language=request.language,
            processed_at=datetime.utcnow()
        )
    except Exception as e:
        logger.error(f"Error in NER: {str(e)}")
        raise HTTPException(status_code=500, detail=str(e))


# ==================== Chatbot ====================

@app.post("/api/v1/chatbot/message", response_model=ChatResponse, tags=["Chatbot"])
async def chat_message(request: ChatRequest):
    """
    Send message to chatbot and get response

    - **session_id**: Session identifier for conversation context
    - **messages**: List of messages in the conversation
    - **bot_type**: Type of bot (customer_service, sales, support, general)
    - **context**: Additional context for the conversation
    """
    try:
        response = await chatbot_service.get_response(
            session_id=request.session_id,
            messages=request.messages,
            bot_type=request.bot_type,
            context=request.context
        )

        return ChatResponse(
            session_id=request.session_id,
            response=response.text,
            confidence=response.confidence,
            intent=response.intent,
            entities=response.entities,
            suggestions=response.suggestions,
            timestamp=datetime.utcnow()
        )
    except Exception as e:
        logger.error(f"Error in chatbot: {str(e)}")
        raise HTTPException(status_code=500, detail=str(e))


@app.delete("/api/v1/chatbot/sessions/{session_id}", tags=["Chatbot"])
async def clear_chat_session(session_id: str):
    """Clear chat session context"""
    try:
        chatbot_service.clear_session(session_id)
        return {"message": "Session cleared", "session_id": session_id}
    except Exception as e:
        logger.error(f"Error clearing session: {str(e)}")
        raise HTTPException(status_code=500, detail=str(e))


# ==================== Text Embedding ====================

@app.post("/api/v1/embeddings/generate", response_model=TextEmbeddingResponse, tags=["Embeddings"])
async def generate_embeddings(request: TextEmbeddingRequest):
    """
    Generate text embeddings

    - **texts**: List of text strings
    - **model**: Embedding model to use
    - **dimension**: Embedding dimension
    """
    try:
        embeddings = await embedding_service.generate_embeddings_batch(
            texts=request.texts,
            model=request.model
        )

        return TextEmbeddingResponse(
            embeddings=embeddings,
            model=request.model,
            dimension=len(embeddings[0]) if embeddings else 0,
            processed_at=datetime.utcnow()
        )
    except Exception as e:
        logger.error(f"Error generating embeddings: {str(e)}")
        raise HTTPException(status_code=500, detail=str(e))


# ==================== Text Summarization ====================

@app.post("/api/v1/summarization/summarize", response_model=SummarizationResponse, tags=["Summarization"])
async def summarize_text(request: SummarizationRequest):
    """
    Summarize text

    - **text**: Text to summarize
    - **max_length**: Maximum length of summary
    - **min_length**: Minimum length of summary
    - **model**: Model to use
    """
    try:
        summary = await summarization_service.summarize(
            text=request.text,
            max_length=request.max_length,
            min_length=request.min_length,
            model=request.model
        )

        return SummarizationResponse(
            summary=summary,
            original_length=len(request.text.split()),
            summary_length=len(summary.split()),
            compression_ratio=round(len(summary.split()) / len(request.text.split()), 2),
            model_used=request.model,
            processed_at=datetime.utcnow()
        )
    except Exception as e:
        logger.error(f"Error in summarization: {str(e)}")
        raise HTTPException(status_code=500, detail=str(e))


if __name__ == "__main__":
    uvicorn.run(
        "app.main:app",
        host=settings.HOST,
        port=settings.PORT,
        reload=settings.DEBUG
    )
