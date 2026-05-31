"""
Pydantic schemas for Computer Vision Service
"""

from pydantic import BaseModel, Field
from typing import List, Optional, Dict, Any
from datetime import datetime


# ==================== OCR ====================

class OCRResponse(BaseModel):
    """Response for OCR extraction"""
    text: str = Field(..., description="Extracted text")
    confidence: float = Field(..., description="Overall confidence score")
    language: str = Field(..., description="Language code")
    words: List[Dict[str, Any]] = Field(default_factory=list, description="Individual words")
    lines: List[Dict[str, Any]] = Field(default_factory=list, description="Text lines")
    processed_at: datetime


class DocumentField(BaseModel):
    """Extracted document field"""
    name: str
    value: str
    confidence: float


class DocumentTable(BaseModel):
    """Extracted table"""
    rows: List[List[str]]
    headers: Optional[List[str]] = None
    bbox: Optional[List[float]] = None


class DocumentAnalysisResponse(BaseModel):
    """Response for document analysis"""
    doc_type: str
    extracted_fields: Dict[str, str]
    text: str
    confidence: float
    tables: List[DocumentTable]
    processed_at: datetime


# ==================== Image Classification ====================

class ClassPrediction(BaseModel):
    """Single class prediction"""
    class: str = Field(..., description="Predicted class")
    confidence: float = Field(..., description="Confidence score")
    index: int = Field(..., description="Class index")


class ImageClassificationResponse(BaseModel):
    """Response for image classification"""
    predictions: List[ClassPrediction]
    primary_class: str
    confidence: float
    model_used: str
    processed_at: datetime


# ==================== Object Detection ====================

class BoundingBox(BaseModel):
    """Bounding box coordinates"""
    x: float = Field(..., ge=0, le=1, description="X coordinate (normalized)")
    y: float = Field(..., ge=0, le=1, description="Y coordinate (normalized)")
    width: float = Field(..., ge=0, le=1, description="Width (normalized)")
    height: float = Field(..., ge=0, le=1, description="Height (normalized)")


class DetectedObject(BaseModel):
    """Detected object"""
    class: str = Field(..., description="Object class")
    confidence: float = Field(..., description="Confidence score")
    bbox: BoundingBox = Field(..., description="Bounding box")


class ObjectDetectionResponse(BaseModel):
    """Response for object detection"""
    objects: List[DetectedObject]
    count: int
    model_used: str
    processed_at: datetime


# ==================== Face Detection ====================

class FacialLandmark(BaseModel):
    """Facial landmark point"""
    x: float
    y: float


class FaceAttributes(BaseModel):
    """Face attributes"""
    age: Optional[int] = None
    gender: Optional[str] = None
    emotion: Optional[str] = None


class DetectedFace(BaseModel):
    """Detected face"""
    bbox: BoundingBox
    confidence: float
    landmarks: Optional[Dict[str, List[FacialLandmark]]] = None
    attributes: Optional[FaceAttributes] = None


class FaceDetectionResponse(BaseModel):
    """Response for face detection"""
    faces: List[DetectedFace]
    count: int
    attributes: Dict[str, Any]
    processed_at: datetime


# ==================== Image Similarity ====================

class ImageSimilarityResponse(BaseModel):
    """Response for image similarity"""
    similarity_score: float = Field(..., ge=0, le=1, description="Similarity score")
    are_similar: bool = Field(..., description="Whether images are similar")
    method: str = Field(..., description="Comparison method")
    processed_at: datetime
