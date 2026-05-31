"""
Recommendation Engine Service - Main Application
FastAPI microservice for personalized recommendations
"""

from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from typing import List, Optional, Dict, Any
from datetime import datetime
import logging
import uvicorn

from app.config import settings
from app.models.schemas import (
    RecommendationRequest,
    RecommendationResponse,
    Recommendation,
    UserSimilarityRequest,
    ItemSimilarityRequest,
    PersonalizedRankingRequest
)
from app.services.collaborative_service import CollaborativeFilteringService
from app.services.content_based_service import ContentBasedService
from app.services.hybrid_service import HybridRecommendationService

# Configure logging
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s'
)
logger = logging.getLogger(__name__)

# Initialize FastAPI app
app = FastAPI(
    title="Recommendation Engine Service",
    description="Personalized Recommendation API for Gogidix Ecosystem",
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
collaborative_service = CollaborativeFilteringService()
content_service = ContentBasedService()
hybrid_service = HybridRecommendationService(collaborative_service, content_service)


@app.on_event("startup")
async def startup_event():
    """Initialize service on startup"""
    logger.info("Starting Recommendation Engine Service...")
    logger.info("Recommendation Engine Service started successfully")


@app.on_event("shutdown")
async def shutdown_event():
    """Cleanup on shutdown"""
    logger.info("Shutting down Recommendation Engine Service...")


@app.get("/", tags=["Health"])
async def root():
    """Root endpoint"""
    return {
        "service": "Recommendation Engine Service",
        "version": "1.0.0",
        "status": "healthy",
        "timestamp": datetime.utcnow().isoformat(),
        "capabilities": [
            "collaborative_filtering",
            "content_based",
            "hybrid_recommendations",
            "user_similarity",
            "item_similarity",
            "personalized_ranking"
        ]
    }


@app.get("/health", tags=["Health"])
async def health_check():
    """Health check endpoint"""
    return {
        "status": "healthy",
        "timestamp": datetime.utcnow().isoformat()
    }


# ==================== Recommendations ====================

@app.post("/api/v1/recommendations", response_model=RecommendationResponse, tags=["Recommendations"])
async def get_recommendations(request: RecommendationRequest):
    """
    Get personalized recommendations for a user

    - **user_id**: User identifier
    - **algorithm**: Recommendation algorithm
    - **count**: Number of recommendations
    - **context**: Additional context (category, session_id, etc.)
    """
    try:
        if request.algorithm == "collaborative":
            recommendations = await collaborative_service.get_recommendations(
                user_id=request.user_id,
                count=request.count,
                context=request.context
            )
        elif request.algorithm == "content_based":
            recommendations = await content_service.get_recommendations(
                user_id=request.user_id,
                count=request.count,
                context=request.context
            )
        elif request.algorithm == "hybrid":
            recommendations = await hybrid_service.get_recommendations(
                user_id=request.user_id,
                count=request.count,
                context=request.context
            )
        else:
            recommendations = await collaborative_service.get_recommendations(
                user_id=request.user_id,
                count=request.count,
                context=request.context
            )

        return RecommendationResponse(
            user_id=request.user_id,
            recommendations=recommendations,
            algorithm=request.algorithm,
            generated_at=datetime.utcnow()
        )
    except Exception as e:
        logger.error(f"Error generating recommendations: {str(e)}")
        raise HTTPException(status_code=500, detail=str(e))


@app.post("/api/v1/recommendations/items/{item_id}/similar", tags=["Recommendations"])
async def get_similar_items(
    item_id: str,
    count: int = 10,
    algorithm: str = "content"
):
    """Get items similar to a given item"""
    try:
        if algorithm == "content":
            similar = await content_service.get_similar_items(item_id, count)
        else:
            similar = await collaborative_service.get_similar_items(item_id, count)

        return {
            "item_id": item_id,
            "similar_items": similar,
            "algorithm": algorithm
        }
    except Exception as e:
        logger.error(f"Error finding similar items: {str(e)}")
        raise HTTPException(status_code=500, detail=str(e))


# ==================== User/Item Similarity ====================

@app.post("/api/v1/similarity/users", tags=["Similarity"])
async def get_user_similarity(request: UserSimilarityRequest):
    """Calculate similarity between users"""
    try:
        similarity = await collaborative_service.get_user_similarity(
            user_id1=request.user_id1,
            user_id2=request.user_id2
        )

        return {
            "user_id1": request.user_id1,
            "user_id2": request.user_id2,
            "similarity_score": similarity,
            "method": "cosine"
        }
    except Exception as e:
        logger.error(f"Error calculating user similarity: {str(e)}")
        raise HTTPException(status_code=500, detail=str(e))


@app.post("/api/v1/similarity/items", tags=["Similarity"])
async def get_item_similarity(request: ItemSimilarityRequest):
    """Calculate similarity between items"""
    try:
        similarity = await content_service.get_item_similarity(
            item_id1=request.item_id1,
            item_id2=request.item_id2
        )

        return {
            "item_id1": request.item_id1,
            "item_id2": request.item_id2,
            "similarity_score": similarity,
            "method": "cosine"
        }
    except Exception as e:
        logger.error(f"Error calculating item similarity: {str(e)}")
        raise HTTPException(status_code=500, detail=str(e))


# ==================== Personalized Ranking ====================

@app.post("/api/v1/ranking/personalize", tags=["Ranking"])
async def personalize_ranking(request: PersonalizedRankingRequest):
    """Re-rank items based on user preferences"""
    try:
        ranked = await hybrid_service.personalize_ranking(
            user_id=request.user_id,
            items=request.items,
            context=request.context
        )

        return {
            "user_id": request.user_id,
            "ranked_items": ranked,
            "original_count": len(request.items),
            "ranked_at": datetime.utcnow()
        }
    except Exception as e:
        logger.error(f"Error personalizing ranking: {str(e)}")
        raise HTTPException(status_code=500, detail=str(e))


if __name__ == "__main__":
    uvicorn.run(
        "app.main:app",
        host=settings.HOST,
        port=settings.PORT,
        reload=settings.DEBUG
    )
