"""
Computer Vision Service - Main Application
FastAPI microservice for OCR, image recognition, and visual analysis
"""

from fastapi import FastAPI, HTTPException, File, UploadFile, Form
from fastapi.middleware.cors import CORSMiddleware
from fastapi.responses import JSONResponse
from typing import List, Optional, Dict, Any
from datetime import datetime
import logging
import uvicorn
import io

from app.config import settings
from app.models.schemas import (
    OCRResponse,
    ImageClassificationResponse,
    ObjectDetectionResponse,
    FaceDetectionResponse,
    DocumentAnalysisResponse,
    ImageSimilarityResponse
)
from app.services.ocr_service import OCRService
from app.services.classification_service import ImageClassificationService
from app.services.object_detection_service import ObjectDetectionService
from app.services.face_detection_service import FaceDetectionService
from app.services.similarity_service import ImageSimilarityService

# Configure logging
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s'
)
logger = logging.getLogger(__name__)

# Initialize FastAPI app
app = FastAPI(
    title="Computer Vision Service",
    description="Computer Vision API for Gogidix Ecosystem - OCR, Image Recognition, Visual Analysis",
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
ocr_service = OCRService()
classification_service = ImageClassificationService()
object_detection_service = ObjectDetectionService()
face_detection_service = FaceDetectionService()
similarity_service = ImageSimilarityService()


@app.on_event("startup")
async def startup_event():
    """Initialize service on startup"""
    logger.info("Starting Computer Vision Service...")
    logger.info("Computer Vision Service started successfully")


@app.on_event("shutdown")
async def shutdown_event():
    """Cleanup on shutdown"""
    logger.info("Shutting down Computer Vision Service...")


@app.get("/", tags=["Health"])
async def root():
    """Root endpoint"""
    return {
        "service": "Computer Vision Service",
        "version": "1.0.0",
        "status": "healthy",
        "timestamp": datetime.utcnow().isoformat(),
        "capabilities": [
            "ocr",
            "image_classification",
            "object_detection",
            "face_detection",
            "document_analysis",
            "image_similarity",
            "barcode_qr_reading"
        ]
    }


@app.get("/health", tags=["Health"])
async def health_check():
    """Health check endpoint"""
    return {
        "status": "healthy",
        "timestamp": datetime.utcnow().isoformat(),
        "services": {
            "ocr": "ok",
            "classification": "ok",
            "object_detection": "ok",
            "face_detection": "ok"
        }
    }


# ==================== OCR ====================

@app.post("/api/v1/ocr/extract", response_model=OCRResponse, tags=["OCR"])
async def extract_text(
    file: UploadFile = File(...),
    language: str = Form(default="eng"),
    preprocess: bool = Form(default=True)
):
    """
    Extract text from image using OCR

    - **file**: Image file (JPEG, PNG, TIFF, PDF)
    - **language**: Language code(s) for OCR
    - **preprocess**: Apply image preprocessing
    """
    try:
        # Read image
        image_data = await file.read()

        # Process OCR
        result = await ocr_service.extract_text(
            image_data=image_data,
            language=language,
            preprocess=preprocess
        )

        return OCRResponse(
            text=result["text"],
            confidence=result["confidence"],
            language=language,
            words=result.get("words", []),
            lines=result.get("lines", []),
        #    bounding_boxes=result.get("bounding_boxes", []),
            processed_at=datetime.utcnow()
        )
    except Exception as e:
        logger.error(f"Error in OCR: {str(e)}")
        raise HTTPException(status_code=500, detail=str(e))


@app.post("/api/v1/ocr/document", response_model=DocumentAnalysisResponse, tags=["OCR"])
async def analyze_document(
    file: UploadFile = File(...),
    doc_type: str = Form(default="auto")
):
    """
    Analyze document structure and extract key information

    - **file**: Document image
    - **doc_type**: Document type (invoice, receipt, id_card, passport, auto)
    """
    try:
        image_data = await file.read()
        result = await ocr_service.analyze_document(image_data, doc_type)

        return DocumentAnalysisResponse(
            doc_type=result["doc_type"],
            extracted_fields=result.get("fields", {}),
            text=result["text"],
            confidence=result.get("confidence", 0.0),
            tables=result.get("tables", []),
            processed_at=datetime.utcnow()
        )
    except Exception as e:
        logger.error(f"Error analyzing document: {str(e)}")
        raise HTTPException(status_code=500, detail=str(e))


# ==================== Image Classification ====================

@app.post("/api/v1/classification/classify", response_model=ImageClassificationResponse, tags=["Classification"])
async def classify_image(
    file: UploadFile = File(...),
    top_k: int = Form(default=5)
):
    """
    Classify image into categories

    - **file**: Image file
    - **top_k**: Number of top predictions to return
    """
    try:
        image_data = await file.read()
        result = await classification_service.classify(image_data, top_k)

        return ImageClassificationResponse(
            predictions=result["predictions"],
            primary_class=result["primary_class"],
            confidence=result["confidence"],
            model_used=result["model"],
            processed_at=datetime.utcnow()
        )
    except Exception as e:
        logger.error(f"Error in image classification: {str(e)}")
        raise HTTPException(status_code=500, detail=str(e))


# ==================== Object Detection ====================

@app.post("/api/v1/detection/detect", response_model=ObjectDetectionResponse, tags=["Object Detection"])
async def detect_objects(
    file: UploadFile = File(...),
    confidence_threshold: float = Form(default=0.5)
):
    """
    Detect objects in image

    - **file**: Image file
    - **confidence_threshold**: Minimum confidence for detections
    """
    try:
        image_data = await file.read()
        result = await object_detection_service.detect(image_data, confidence_threshold)

        return ObjectDetectionResponse(
            objects=result["objects"],
            count=result["count"],
            model_used=result["model"],
            processed_at=datetime.utcnow()
        )
    except Exception as e:
        logger.error(f"Error in object detection: {str(e)}")
        raise HTTPException(status_code=500, detail=str(e))


# ==================== Face Detection ====================

@app.post("/api/v1/faces/detect", response_model=FaceDetectionResponse, tags=["Face Detection"])
async def detect_faces(
    file: UploadFile = File(...),
    return_landmarks: bool = Form(default=False)
):
    """
    Detect faces in image

    - **file**: Image file
    - **return_landmarks**: Return facial landmarks
    """
    try:
        image_data = await file.read()
        result = await face_detection_service.detect(image_data, return_landmarks)

        return FaceDetectionResponse(
            faces=result["faces"],
            count=result["count"],
            attributes=result.get("attributes", {}),
            processed_at=datetime.utcnow()
        )
    except Exception as e:
        logger.error(f"Error in face detection: {str(e)}")
        raise HTTPException(status_code=500, detail=str(e))


# ==================== Image Similarity ====================

@app.post("/api/v1/similarity/compare", response_model=ImageSimilarityResponse, tags=["Similarity"])
async def compare_images(
    file1: UploadFile = File(...),
    file2: UploadFile = File(...)
):
    """
    Compare two images for similarity

    - **file1**: First image
    - **file2**: Second image
    """
    try:
        image_data1 = await file1.read()
        image_data2 = await file2.read()

        result = await similarity_service.compare(image_data1, image_data2)

        return ImageSimilarityResponse(
            similarity_score=result["similarity"],
            are_similar=result["are_similar"],
            method=result["method"],
            processed_at=datetime.utcnow()
        )
    except Exception as e:
        logger.error(f"Error comparing images: {str(e)}")
        raise HTTPException(status_code=500, detail=str(e))


if __name__ == "__main__":
    uvicorn.run(
        "app.main:app",
        host=settings.HOST,
        port=settings.PORT,
        reload=settings.DEBUG
    )
