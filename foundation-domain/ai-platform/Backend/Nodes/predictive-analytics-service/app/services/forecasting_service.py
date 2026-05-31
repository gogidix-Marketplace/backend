"""
Time Series Forecasting Service
Provides forecasting capabilities using various algorithms
"""

import logging
import numpy as np
from typing import List, Dict, Any, Optional
from datetime import datetime, timedelta

logger = logging.getLogger(__name__)


class ForecastingService:
    """Service for time series forecasting"""

    def __init__(self):
        logger.info("Initialized ForecastingService")

    async def generate_forecast(
        self,
        historical_data: List[float],
        forecast_horizon: int,
        frequency: str = "daily",
        model: str = "moving_average",
        confidence_interval: float = 0.95
    ) -> Dict[str, Any]:
        """
        Generate forecast using specified model

        Args:
            historical_data: Historical time series values
            forecast_horizon: Number of periods to forecast
            frequency: Data frequency
            model: Forecasting model
            confidence_interval: Confidence interval for predictions

        Returns:
            Dictionary with forecast and metrics
        """
        if len(historical_data) < 3:
            raise ValueError("Insufficient historical data for forecasting")

        if model == "moving_average":
            return self._moving_average_forecast(historical_data, forecast_horizon)
        elif model == "exponential_smoothing":
            return self._exponential_smoothing_forecast(historical_data, forecast_horizon)
        elif model == "linear_trend":
            return self._linear_trend_forecast(historical_data, forecast_horizon)
        elif model == "arima":
            return self._arima_forecast(historical_data, forecast_horizon)
        else:
            return self._moving_average_forecast(historical_data, forecast_horizon)

    def _moving_average_forecast(
        self,
        historical_data: List[float],
        forecast_horizon: int
    ) -> Dict[str, Any]:
        """Simple moving average forecast"""
        window = min(7, len(historical_data))
        forecast = []

        for i in range(forecast_horizon):
            # Use recent data for prediction
            recent_data = historical_data[-window:]
            prediction = sum(recent_data) / len(recent_data)
            forecast.append(prediction)

            # Add prediction to historical for next iteration
            historical_data.append(prediction)

        # Calculate confidence intervals
        std = np.std(historical_data[:-forecast_horizon])
        confidence_upper = [f + 1.96 * std for f in forecast]
        confidence_lower = [f - 1.96 * std for f in forecast]

        return {
            "forecast": forecast,
            "confidence_upper": confidence_upper,
            "confidence_lower": confidence_lower,
            "metrics": {
                "method": "moving_average",
                "window": window,
                "std": std
            },
            "forecast_start": datetime.utcnow().isoformat()
        }

    def _exponential_smoothing_forecast(
        self,
        historical_data: List[float],
        forecast_horizon: int
    ) -> Dict[str, Any]:
        """Exponential smoothing forecast"""
        alpha = 0.3  # Smoothing factor
        forecast = []

        # Initialize with last value
        last_smoothed = historical_data[-1]

        for _ in range(forecast_horizon):
            forecast.append(last_smoothed)
            # For multi-step, use the same value (simple approach)

        std = np.std(historical_data)
        return {
            "forecast": forecast,
            "confidence_upper": [f + 1.96 * std for f in forecast],
            "confidence_lower": [f - 1.96 * std for f in forecast],
            "metrics": {
                "method": "exponential_smoothing",
                "alpha": alpha
            },
            "forecast_start": datetime.utcnow().isoformat()
        }

    def _linear_trend_forecast(
        self,
        historical_data: List[float],
        forecast_horizon: int
    ) -> Dict[str, Any]:
        """Linear trend forecast"""
        x = np.arange(len(historical_data))
        y = np.array(historical_data)

        # Fit linear regression
        coeffs = np.polyfit(x, y, 1)
        trend = np.poly1d(coeffs)

        # Generate forecast
        forecast_x = np.arange(len(historical_data), len(historical_data) + forecast_horizon)
        forecast = trend(forecast_x).tolist()

        # Calculate residuals for confidence intervals
        predictions = trend(x)
        residuals = y - predictions
        std = np.std(residuals)

        return {
            "forecast": forecast,
            "confidence_upper": [f + 1.96 * std for f in forecast],
            "confidence_lower": [f - 1.96 * std for f in forecast],
            "metrics": {
                "method": "linear_trend",
                "slope": float(coeffs[0]),
                "intercept": float(coeffs[1]),
                "r_squared": self._calculate_r_squared(y, predictions)
            },
            "forecast_start": datetime.utcnow().isoformat()
        }

    def _arima_forecast(
        self,
        historical_data: List[float],
        forecast_horizon: int
    ) -> Dict[str, Any]:
        """ARIMA-like forecast (simplified implementation)"""
        # Simplified AR(1) model
        y = np.array(historical_data)

        # Calculate AR(1) coefficient
        if len(y) > 1:
            phi = np.corrcoef(y[:-1], y[1:])[0, 1]
        else:
            phi = 0

        phi = max(-0.99, min(0.99, phi))  # Bound phi

        forecast = []
        last_value = y[-1]

        for _ in range(forecast_horizon):
            next_value = phi * last_value + (1 - phi) * np.mean(y)
            forecast.append(next_value)
            last_value = next_value

        std = np.std(y)
        return {
            "forecast": forecast,
            "confidence_upper": [f + 1.96 * std for f in forecast],
            "confidence_lower": [f - 1.96 * std for f in forecast],
            "metrics": {
                "method": "ar1",
                "phi": phi
            },
            "forecast_start": datetime.utcnow().isoformat()
        }

    def _calculate_r_squared(self, y_actual, y_predicted):
        """Calculate R-squared"""
        ss_res = np.sum((y_actual - y_predicted) ** 2)
        ss_tot = np.sum((y_actual - np.mean(y_actual)) ** 2)
        return 1 - (ss_res / ss_tot) if ss_tot > 0 else 0
