"""
Anomaly Detection Service - Main Application
FastAPI microservice for fraud detection and anomaly detection
"""

from fastapi import FastAPI, HTTPException, BackgroundTasks
from fastapi.middleware.cors import CORSMiddleware
from typing import List, Optional, Dict, Any
from datetime import datetime
import logging
import uvicorn

from app.config import settings
from app.models.schemas import (
    FraudDetectionRequest,
    FraudDetectionResponse,
    AnomalyDetectionRequest,
    AnomalyDetectionResponse,
    TransactionMonitoringRequest,
    SystemMonitoringRequest,
    MonitoringAlert
)
from app.services.fraud_detection_service import FraudDetectionService
from app.services.anomaly_detection_service import AnomalyDetectionService
from app.services.monitoring_service import MonitoringService

# Configure logging
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s'
)
logger = logging.getLogger(__name__)

# Initialize FastAPI app
app = FastAPI(
    title="Anomaly Detection Service",
    description="Anomaly Detection API for Gogidix Ecosystem - Fraud Detection, Monitoring, Outlier Detection",
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
fraud_service = FraudDetectionService()
anomaly_service = AnomalyDetectionService()
monitoring_service = MonitoringService()


@app.on_event("startup")
async def startup_event():
    """Initialize service on startup"""
    logger.info("Starting Anomaly Detection Service...")
    logger.info("Anomaly Detection Service started successfully")


@app.on_event("shutdown")
async def shutdown_event():
    """Cleanup on shutdown"""
    logger.info("Shutting down Anomaly Detection Service...")


@app.get("/", tags=["Health"])
async def root():
    """Root endpoint"""
    return {
        "service": "Anomaly Detection Service",
        "version": "1.0.0",
        "status": "healthy",
        "timestamp": datetime.utcnow().isoformat(),
        "capabilities": [
            "fraud_detection",
            "transaction_monitoring",
            "system_monitoring",
            "outlier_detection",
            "behavioral_analysis"
        ]
    }


@app.get("/health", tags=["Health"])
async def health_check():
    """Health check endpoint"""
    return {
        "status": "healthy",
        "timestamp": datetime.utcnow().isoformat()
    }


# ==================== Fraud Detection ====================

@app.post("/api/v1/fraud/detect", response_model=FraudDetectionResponse, tags=["Fraud Detection"])
async def detect_fraud(request: FraudDetectionRequest):
    """
    Detect fraudulent transactions

    - **transaction**: Transaction details
    - **user_history**: User's transaction history
    - **rules**: Custom fraud detection rules
    """
    try:
        result = await fraud_service.detect_fraud(
            transaction=request.transaction,
            user_history=request.user_history,
            rules=request.rules
        )

        return FraudDetectionResponse(
            is_fraudulent=result["is_fraudulent"],
            fraud_score=result["fraud_score"],
            risk_level=result["risk_level"],
            reasons=result["reasons"],
            alert_triggered=result["alert_triggered"],
            model_version=result["model_version"],
            processed_at=datetime.utcnow()
        )
    except Exception as e:
        logger.error(f"Error in fraud detection: {str(e)}")
        raise HTTPException(status_code=500, detail=str(e))


@app.post("/api/v1/fraud/batch-check", tags=["Fraud Detection"])
async def batch_check_fraud(transactions: List[Dict[str, Any]]):
    """Check multiple transactions for fraud"""
    try:
        results = []
        for txn in transactions:
            result = await fraud_service.detect_fraud(
                transaction=txn,
                user_history=None,
                rules=None
            )
            results.append({
                "transaction_id": txn.get("id"),
                "is_fraudulent": result["is_fraudulent"],
                "fraud_score": result["fraud_score"],
                "risk_level": result["risk_level"]
            })

        return {"results": results, "processed_at": datetime.utcnow()}
    except Exception as e:
        logger.error(f"Error in batch fraud check: {str(e)}")
        raise HTTPException(status_code=500, detail=str(e))


# ==================== Anomaly Detection ====================

@app.post("/api/v1/anomalies/detect", response_model=AnomalyDetectionResponse, tags=["Anomaly Detection"])
async def detect_anomalies(request: AnomalyDetectionRequest):
    """
    Detect anomalies in data

    - **data**: Data points to analyze
    - **algorithm**: Detection algorithm
    - **threshold**: Sensitivity threshold
    """
    try:
        result = await anomaly_service.detect_anomalies(
            data=request.data,
            algorithm=request.algorithm,
            threshold=request.threshold
        )

        return AnomalyDetectionResponse(
            anomalies=result["anomalies"],
            anomaly_count=result["anomaly_count"],
            anomaly_indices=result["anomaly_indices"],
            statistics=result["statistics"],
            algorithm_used=request.algorithm,
            processed_at=datetime.utcnow()
        )
    except Exception as e:
        logger.error(f"Error detecting anomalies: {str(e)}")
        raise HTTPException(status_code=500, detail=str(e))


# ==================== Monitoring ====================

@app.post("/api/v1/monitoring/transaction", tags=["Monitoring"])
async def monitor_transaction(request: TransactionMonitoringRequest):
    """Monitor transaction for suspicious activity"""
    try:
        result = await monitoring_service.monitor_transaction(
            transaction=request.transaction,
            user_id=request.user_id,
            session_id=request.session_id
        )

        return result
    except Exception as e:
        logger.error(f"Error monitoring transaction: {str(e)}")
        raise HTTPException(status_code=500, detail=str(e))


@app.post("/api/v1/monitoring/system", tags=["Monitoring"])
async def monitor_system(request: SystemMonitoringRequest):
    """Monitor system metrics for anomalies"""
    try:
        result = await monitoring_service.monitor_system(
            metrics=request.metrics,
            service_name=request.service_name,
            threshold=request.threshold
        )

        return result
    except Exception as e:
        logger.error(f"Error monitoring system: {str(e)}")
        raise HTTPException(status_code=500, detail=str(e))


@app.get("/api/v1/alerts", tags=["Monitoring"])
async def get_alerts(
    limit: int = 100,
    severity: Optional[str] = None,
    service: Optional[str] = None
):
    """Get monitoring alerts"""
    try:
        alerts = await monitoring_service.get_alerts(limit, severity, service)
        return {"alerts": alerts, "count": len(alerts)}
    except Exception as e:
        logger.error(f"Error getting alerts: {str(e)}")
        raise HTTPException(status_code=500, detail=str(e))


if __name__ == "__main__":
    uvicorn.run(
        "app.main:app",
        host=settings.HOST,
        port=settings.PORT,
        reload=settings.DEBUG
    )
