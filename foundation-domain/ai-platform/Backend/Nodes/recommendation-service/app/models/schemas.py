"""
Pydantic schemas for Recommendation Engine Service
"""

from pydantic import BaseModel, Field
from typing import List, Optional, Dict, Any
from datetime import datetime
from enum import Enum


class RecommendationAlgorithm(str, Enum):
    """Recommendation algorithms"""
    COLLABORATIVE = "collaborative"
    CONTENT_BASED = "content_based"
    HYBRID = "hybrid"


class Recommendation(BaseModel):
    """Single recommendation"""
    item_id: str
    score: float = Field(..., ge=0, le=1, description="Recommendation score")
    reason: str = Field(default="recommendation", description="Reason for recommendation")
    metadata: Optional[Dict[str, Any]] = Field(default=None, description="Additional metadata")


class RecommendationRequest(BaseModel):
    """Request for recommendations"""
    user_id: str = Field(..., description="User identifier")
    algorithm: RecommendationAlgorithm = Field(default=RecommendationAlgorithm.HYBRID)
    count: int = Field(default=10, ge=1, le=100, description="Number of recommendations")
    context: Optional[Dict[str, Any]] = Field(default=None, description="Additional context")
    filters: Optional[Dict[str, Any]] = Field(default=None, description="Filters to apply")


class RecommendationResponse(BaseModel):
    """Response for recommendations"""
    user_id: str
    recommendations: List[Recommendation]
    algorithm: str
    generated_at: datetime


class UserSimilarityRequest(BaseModel):
    """Request for user similarity"""
    user_id1: str
    user_id2: str


class ItemSimilarityRequest(BaseModel):
    """Request for item similarity"""
    item_id1: str
    item_id2: str


class PersonalizedRankingRequest(BaseModel):
    """Request for personalized ranking"""
    user_id: str
    items: List[str] = Field(..., min_items=1, description="Items to rank")
    context: Optional[Dict[str, Any]] = Field(default=None)
