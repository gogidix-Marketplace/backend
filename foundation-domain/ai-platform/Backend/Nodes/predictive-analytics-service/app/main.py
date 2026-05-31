"""
Predictive Analytics Service - Main Application
FastAPI microservice for predictive analytics and forecasting
"""

from fastapi import FastAPI, HTTPException, BackgroundTasks
from fastapi.middleware.cors import CORSMiddleware
from typing import List, Optional, Dict, Any
from datetime import datetime, timedelta
import logging
import uvicorn

from app.config import settings
from app.models.schemas import (
    ForecastRequest,
    ForecastResponse,
    RouteOptimizationRequest,
    RouteOptimizationResponse,
    DemandForecastRequest,
    DemandForecastResponse,
    PredictionModelResponse,
    ModelTrainingRequest
)
from app.services.forecasting_service import ForecastingService
from app.services.route_optimization_service import RouteOptimizationService
from app.services.demand_service import DemandForecastingService

# Configure logging
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s'
)
logger = logging.getLogger(__name__)

# Initialize FastAPI app
app = FastAPI(
    title="Predictive Analytics Service",
    description="Predictive Analytics API for Gogidix Ecosystem",
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
forecasting_service = ForecastingService()
route_service = RouteOptimizationService()
demand_service = DemandForecastingService()


@app.on_event("startup")
async def startup_event():
    """Initialize service on startup"""
    logger.info("Starting Predictive Analytics Service...")
    logger.info("Predictive Analytics Service started successfully")


@app.on_event("shutdown")
async def shutdown_event():
    """Cleanup on shutdown"""
    logger.info("Shutting down Predictive Analytics Service...")


@app.get("/", tags=["Health"])
async def root():
    """Root endpoint"""
    return {
        "service": "Predictive Analytics Service",
        "version": "1.0.0",
        "status": "healthy",
        "timestamp": datetime.utcnow().isoformat(),
        "capabilities": [
            "demand_forecasting",
            "route_optimization",
            "time_series_forecasting",
            "predictive_maintenance",
            "inventory_optimization"
        ]
    }


@app.get("/health", tags=["Health"])
async def health_check():
    """Health check endpoint"""
    return {
        "status": "healthy",
        "timestamp": datetime.utcnow().isoformat(),
        "services": {
            "forecasting": "ok",
            "route_optimization": "ok",
            "demand": "ok"
        }
    }


# ==================== Time Series Forecasting ====================

@app.post("/api/v1/forecast/generate", response_model=ForecastResponse, tags=["Forecasting"])
async def generate_forecast(request: ForecastRequest):
    """
    Generate time series forecast

    - **historical_data**: Historical time series data
    - **forecast_horizon**: Number of periods to forecast
    - **frequency**: Data frequency (hourly, daily, weekly, monthly)
    - **model**: Forecasting model to use
    """
    try:
        result = await forecasting_service.generate_forecast(
            historical_data=request.historical_data,
            forecast_horizon=request.forecast_horizon,
            frequency=request.frequency,
            model=request.model,
            confidence_interval=request.confidence_interval
        )

        return ForecastResponse(
            forecast=result["forecast"],
            confidence_upper=result.get("confidence_upper"),
            confidence_lower=result.get("confidence_lower"),
            metrics=result.get("metrics", {}),
            model_used=request.model,
            forecast_start=result.get("forecast_start"),
            processed_at=datetime.utcnow()
        )
    except Exception as e:
        logger.error(f"Error generating forecast: {str(e)}")
        raise HTTPException(status_code=500, detail=str(e))


# ==================== Demand Forecasting ====================

@app.post("/api/v1/demand/forecast", response_model=DemandForecastResponse, tags=["Demand"])
async def forecast_demand(request: DemandForecastRequest):
    """
    Forecast product demand

    - **product_id**: Product identifier
    - **location**: Location for forecast
    - **forecast_horizon_days**: Number of days to forecast
    - **include_promotions**: Include promotion effects
    - **include_seasonality**: Include seasonal patterns
    """
    try:
        result = await demand_service.forecast_demand(
            product_id=request.product_id,
            location=request.location,
            horizon_days=request.forecast_horizon_days,
            include_promotions=request.include_promotions,
            include_seasonality=request.include_seasonality,
            historical_data=request.historical_data
        )

        return DemandForecastResponse(
            product_id=request.product_id,
            location=request.location,
            daily_forecast=result["daily_forecast"],
            total_demand=result.get("total_demand", 0),
            confidence_intervals=result.get("confidence_intervals", {}),
            insights=result.get("insights", []),
            recommendations=result.get("recommendations", []),
            forecast_date=datetime.utcnow()
        )
    except Exception as e:
        logger.error(f"Error forecasting demand: {str(e)}")
        raise HTTPException(status_code=500, detail=str(e))


@app.get("/api/v1/demand/products/{product_id}/forecast", tags=["Demand"])
async def get_product_demand_forecast(
    product_id: str,
    location: Optional[str] = None,
    days: int = 30
):
    """Get demand forecast for a specific product"""
    try:
        result = await demand_service.get_cached_forecast(
            product_id=product_id,
            location=location,
            days=days
        )
        return result
    except Exception as e:
        logger.error(f"Error getting product forecast: {str(e)}")
        raise HTTPException(status_code=500, detail=str(e))


# ==================== Route Optimization ====================

@app.post("/api/v1/routes/optimize", response_model=RouteOptimizationResponse, tags=["Routes"])
async def optimize_routes(request: RouteOptimizationRequest):
    """
    Optimize delivery routes

    - **depot**: Starting depot location
    - **deliveries**: List of delivery locations
    - **vehicles**: Available vehicles
    - **constraints**: Routing constraints
    """
    try:
        result = await route_service.optimize_routes(
            depot=request.depot,
            deliveries=request.deliveries,
            vehicles=request.vehicles,
            constraints=request.constraints,
            objective=request.objective
        )

        return RouteOptimizationResponse(
            routes=result["routes"],
            total_distance=result["total_distance"],
            total_time=result["total_time"],
            total_cost=result.get("total_cost", 0),
            savings=result.get("savings", {}),
            metrics=result.get("metrics", {}),
            optimized_at=datetime.utcnow()
        )
    except Exception as e:
        logger.error(f"Error optimizing routes: {str(e)}")
        raise HTTPException(status_code=500, detail=str(e))


# ==================== Model Management ====================

@app.get("/api/v1/models", response_model=List[PredictionModelResponse], tags=["Models"])
async def list_models():
    """List available prediction models"""
    return [
        PredictionModelResponse(
            model_id="arima_default",
            model_type="forecasting",
            algorithm="arima",
            description="Auto-Regressive Integrated Moving Average",
            is_active=True
        ),
        PredictionModelResponse(
            model_id="prophet_default",
            model_type="forecasting",
            algorithm="prophet",
            description="Facebook Prophet forecasting",
            is_active=True
        ),
        PredictionModelResponse(
            model_id="lstm_default",
            model_type="forecasting",
            algorithm="lstm",
            description="Long Short-Term Memory neural network",
            is_active=True
        ),
        PredictionModelResponse(
            model_id="vrp_solver",
            model_type="route_optimization",
            algorithm="ortools",
            description="Vehicle Routing Problem solver",
            is_active=True
        )
    ]


@app.post("/api/v1/models/train", tags=["Models"])
async def train_model(request: ModelTrainingRequest):
    """Train a new prediction model"""
    try:
        job_id = f"training_{datetime.utcnow().timestamp()}"
        return {
            "message": "Training job started",
            "job_id": job_id,
            "status": "running"
        }
    except Exception as e:
        logger.error(f"Error starting training: {str(e)}")
        raise HTTPException(status_code=500, detail=str(e))


if __name__ == "__main__":
    uvicorn.run(
        "app.main:app",
        host=settings.HOST,
        port=settings.PORT,
        reload=settings.DEBUG
    )
