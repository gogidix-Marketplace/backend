"""
Pydantic schemas for Anomaly Detection Service
"""

from pydantic import BaseModel, Field
from typing import List, Optional, Dict, Any
from datetime import datetime
from enum import Enum


# ==================== Fraud Detection ====================

class Transaction(BaseModel):
    """Transaction details"""
    id: str = Field(..., description="Transaction ID")
    user_id: str = Field(..., description="User ID")
    amount: float = Field(..., description="Transaction amount", ge=0)
    currency: str = Field(default="USD", description="Currency code")
    merchant: Optional[str] = Field(default=None, description="Merchant name")
    merchant_category: Optional[str] = Field(default=None, description="Merchant category")
    location: Optional[Dict[str, Any]] = Field(default=None, description="Transaction location")
    timestamp: Optional[datetime] = Field(default=None, description="Transaction timestamp")


class FraudDetectionRequest(BaseModel):
    """Request for fraud detection"""
    transaction: Dict[str, Any] = Field(..., description="Transaction to analyze")
    user_history: Optional[List[Dict[str, Any]]] = Field(default=None, description="User's transaction history")
    rules: Optional[Dict[str, Any]] = Field(default=None, description="Custom fraud rules")


class FraudDetectionResponse(BaseModel):
    """Response for fraud detection"""
    is_fraudulent: bool = Field(..., description="Whether transaction is fraudulent")
    fraud_score: float = Field(..., description="Fraud probability score (0-1)")
    risk_level: str = Field(..., description="Risk level: low, medium, high")
    reasons: List[str] = Field(default_factory=list, description="Reasons for fraud score")
    alert_triggered: bool = Field(..., description="Whether alert was triggered")
    model_version: str = Field(..., description="Model version used")
    processed_at: datetime


# ==================== Anomaly Detection ====================

class AnomalyAlgorithm(str, Enum):
    """Anomaly detection algorithms"""
    ZSCORE = "zscore"
    IQR = "iqr"
    ISOLATION_FOREST = "isolation_forest"
    DBSCAN = "dbscan"


class AnomalyDetectionRequest(BaseModel):
    """Request for anomaly detection"""
    data: List[float] = Field(..., description="Data points to analyze", min_items=3)
    algorithm: AnomalyAlgorithm = Field(default=AnomalyAlgorithm.ZSCORE, description="Detection algorithm")
    threshold: float = Field(default=3.0, description="Sensitivity threshold")


class AnomalyDetectionResponse(BaseModel):
    """Response for anomaly detection"""
    anomalies: List[float] = Field(..., description="Anomalous values")
    anomaly_count: int = Field(..., description="Number of anomalies")
    anomaly_indices: List[int] = Field(..., description="Indices of anomalies")
    statistics: Dict[str, Any] = Field(default_factory=dict, description="Statistical summary")
    algorithm_used: str = Field(..., description="Algorithm used")
    processed_at: datetime


# ==================== Monitoring ====================

class TransactionMonitoringRequest(BaseModel):
    """Request for transaction monitoring"""
    transaction: Dict[str, Any] = Field(..., description="Transaction to monitor")
    user_id: str = Field(..., description="User ID")
    session_id: Optional[str] = Field(default=None, description="Session ID")


class SystemMonitoringRequest(BaseModel):
    """Request for system monitoring"""
    metrics: Dict[str, float] = Field(..., description="System metrics")
    service_name: str = Field(..., description="Service name")
    threshold: Optional[Dict[str, float]] = Field(default=None, description="Custom thresholds")


class MonitoringAlert(BaseModel):
    """Monitoring alert"""
    type: str
    severity: str
    message: str
    timestamp: datetime
    service: Optional[str] = None
    user_id: Optional[str] = None
