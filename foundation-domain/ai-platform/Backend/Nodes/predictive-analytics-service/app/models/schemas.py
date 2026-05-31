"""
Pydantic schemas for Predictive Analytics Service
"""

from pydantic import BaseModel, Field
from typing import List, Optional, Dict, Any
from datetime import datetime
from enum import Enum


# ==================== Forecasting ====================

class ForecastFrequency(str, Enum):
    """Time series frequency"""
    HOURLY = "hourly"
    DAILY = "daily"
    WEEKLY = "weekly"
    MONTHLY = "monthly"


class ForecastRequest(BaseModel):
    """Request for time series forecast"""
    historical_data: List[float] = Field(..., description="Historical time series values", min_items=3)
    forecast_horizon: int = Field(..., description="Number of periods to forecast", ge=1, le=365)
    frequency: ForecastFrequency = Field(default=ForecastFrequency.DAILY, description="Data frequency")
    model: str = Field(default="moving_average", description="Forecasting model")
    confidence_interval: float = Field(default=0.95, description="Confidence interval level")


class ForecastResponse(BaseModel):
    """Response for forecast"""
    forecast: List[float] = Field(..., description="Forecasted values")
    confidence_upper: Optional[List[float]] = Field(default=None, description="Upper confidence bound")
    confidence_lower: Optional[List[float]] = Field(default=None, description="Lower confidence bound")
    metrics: Dict[str, Any] = Field(default_factory=dict, description="Forecast metrics")
    model_used: str = Field(..., description="Model used for forecasting")
    forecast_start: str = Field(..., description="Forecast start timestamp")
    processed_at: datetime


# ==================== Demand Forecasting ====================

class DemandForecastRequest(BaseModel):
    """Request for demand forecast"""
    product_id: str = Field(..., description="Product identifier")
    location: Optional[str] = Field(default=None, description="Location for forecast")
    forecast_horizon_days: int = Field(default=30, description="Forecast horizon in days", ge=1, le=365)
    include_promotions: bool = Field(default=True, description="Include promotion effects")
    include_seasonality: bool = Field(default=True, description="Include seasonal patterns")
    historical_data: Optional[List[Dict[str, Any]]] = Field(default=None, description="Historical sales data")


class DailyForecast(BaseModel):
    """Daily forecast entry"""
    date: str = Field(..., description="Forecast date")
    demand: float = Field(..., description="Forecasted demand")
    confidence_lower: float = Field(..., description="Lower confidence bound")
    confidence_upper: float = Field(..., description="Upper confidence bound")


class DemandForecastResponse(BaseModel):
    """Response for demand forecast"""
    product_id: str
    location: Optional[str]
    daily_forecast: List[DailyForecast]
    total_demand: float
    confidence_intervals: Dict[str, float]
    insights: List[str] = Field(default_factory=list)
    recommendations: List[str] = Field(default_factory=list)
    forecast_date: datetime


# ==================== Route Optimization ====================

class Location(BaseModel):
    """Geographic location"""
    lat: float = Field(..., ge=-90, le=90, description="Latitude")
    lng: float = Field(..., ge=-180, le=180, description="Longitude")


class Delivery(BaseModel):
    """Delivery request"""
    id: str = Field(..., description="Delivery ID")
    location: Location = Field(..., description="Delivery location")
    demand: int = Field(default=1, description="Demand/weight", ge=1)
    time_window: Optional[Dict[str, str]] = Field(default=None, description="Time window")
    priority: int = Field(default=1, description="Priority level", ge=1, le=5)


class Vehicle(BaseModel):
    """Vehicle definition"""
    id: str = Field(..., description="Vehicle ID")
    capacity: int = Field(default=100, description="Vehicle capacity", ge=1)
    start_location: Optional[Location] = Field(default=None, description="Start location")
    cost_per_km: float = Field(default=0.5, description="Cost per kilometer")


class RouteConstraints(BaseModel):
    """Routing constraints"""
    max_stops: Optional[int] = Field(default=None, description="Maximum stops per route")
    max_distance: Optional[float] = Field(default=None, description="Maximum distance per route")
    max_duration: Optional[float] = Field(default=None, description="Maximum duration per route")
    time_windows: bool = Field(default=False, description="Enforce time windows")


class RouteOptimizationRequest(BaseModel):
    """Request for route optimization"""
    depot: Location = Field(..., description="Depot location")
    deliveries: List[Delivery] = Field(..., description="List of deliveries", min_items=1)
    vehicles: List[Vehicle] = Field(..., description="Available vehicles", min_items=1)
    constraints: Optional[RouteConstraints] = Field(default=None, description="Routing constraints")
    objective: str = Field(default="minimize_distance", description="Optimization objective")


class RouteStop(BaseModel):
    """Route stop"""
    id: str
    location: Location
    demand: int
    distance_from_previous: float


class Route(BaseModel):
    """Optimized route"""
    vehicle_id: str
    stops: List[RouteStop]
    distance: float
    duration: float
    load: int


class RouteOptimizationResponse(BaseModel):
    """Response for route optimization"""
    routes: List[Route]
    total_distance: float
    total_time: float
    total_cost: float
    savings: Dict[str, Any]
    metrics: Dict[str, Any]
    optimized_at: datetime


# ==================== Model Management ====================

class PredictionModelResponse(BaseModel):
    """Prediction model info"""
    model_id: str
    model_type: str
    algorithm: str
    description: str
    is_active: bool
    created_at: Optional[datetime] = None
    metrics: Optional[Dict[str, float]] = None


class ModelTrainingRequest(BaseModel):
    """Request to train a new model"""
    model_type: str = Field(..., description="Type of model to train")
    algorithm: str = Field(..., description="Algorithm to use")
    training_data: Dict[str, Any] = Field(..., description="Training configuration")
    parameters: Optional[Dict[str, Any]] = Field(default=None, description="Model parameters")
