"""
Demand Forecasting Service
Forecasts product demand using historical sales data
"""

import logging
from typing import List, Dict, Any, Optional
from datetime import datetime, timedelta
import numpy as np

logger = logging.getLogger(__name__)


class DemandForecastingService:
    """Service for demand forecasting"""

    def __init__(self):
        self.cache = {}
        logger.info("Initialized DemandForecastingService")

    async def forecast_demand(
        self,
        product_id: str,
        location: Optional[str],
        horizon_days: int,
        include_promotions: bool,
        include_seasonality: bool,
        historical_data: Optional[List[Dict[str, Any]]] = None
    ) -> Dict[str, Any]:
        """
        Forecast product demand

        Args:
            product_id: Product identifier
            location: Location for forecast
            horizon_days: Forecast horizon in days
            include_promotions: Include promotion effects
            include_seasonality: Include seasonal patterns
            historical_data: Historical sales data

        Returns:
            Demand forecast
        """
        # Use provided data or generate synthetic
        if historical_data:
            sales = [d.get("quantity", 0) for d in historical_data]
        else:
            # Generate synthetic data
            sales = self._generate_synthetic_sales(horizon_days + 30)

        # Generate forecast
        forecast = self._forecast_sales(sales, horizon_days, include_seasonality)

        # Apply promotion lift if enabled
        if include_promotions:
            forecast = self._apply_promotion_lift(forecast)

        total_demand = sum(forecast)

        # Generate insights
        insights = self._generate_insights(sales, forecast)

        # Generate recommendations
        recommendations = self._generate_recommendations(forecast, insights)

        return {
            "daily_forecast": [
                {
                    "date": (datetime.utcnow() + timedelta(days=i)).strftime("%Y-%m-%d"),
                    "demand": round(demand, 2),
                    "confidence_lower": round(demand * 0.8, 2),
                    "confidence_upper": round(demand * 1.2, 2)
                }
                for i, demand in enumerate(forecast)
            ],
            "total_demand": round(total_demand, 2),
            "confidence_intervals": {
                "lower": round(total_demand * 0.85, 2),
                "upper": round(total_demand * 1.15, 2)
            },
            "insights": insights,
            "recommendations": recommendations
        }

    def _generate_synthetic_sales(self, num_days: int) -> List[float]:
        """Generate synthetic sales data"""
        base_demand = 100
        trend = 0.5  # Daily trend
        seasonality_amp = 20

        sales = []
        for day in range(num_days):
            # Trend
            value = base_demand + trend * day

            # Weekly seasonality
            weekday_effect = 10 * np.sin(2 * np.pi * day / 7)

            # Random noise
            noise = np.random.normal(0, 5)

            value += weekday_effect + noise
            sales.append(max(0, value))

        return sales

    def _forecast_sales(
        self,
        historical_sales: List[float],
        horizon_days: int,
        include_seasonality: bool
    ) -> List[float]:
        """Generate sales forecast"""
        forecast = []

        # Calculate trend from historical data
        if len(historical_sales) >= 2:
            recent_avg = np.mean(historical_sales[-7:]) if len(historical_sales) >= 7 else np.mean(historical_sales)
            earlier_avg = np.mean(historical_sales[-14:-7]) if len(historical_sales) >= 14 else recent_avg
            daily_trend = (recent_avg - earlier_avg) / 7 if len(historical_sales) >= 14 else 0
        else:
            recent_avg = historical_sales[0] if historical_sales else 100
            daily_trend = 0

        # Generate forecast
        for day in range(horizon_days):
            base_value = recent_avg + daily_trend * day

            # Add seasonality if enabled
            if include_seasonality:
                seasonality = 10 * np.sin(2 * np.pi * day / 7)
                base_value += seasonality

            # Add some randomness
            noise = np.random.normal(0, 3)
            value = max(0, base_value + noise)
            forecast.append(value)

        return forecast

    def _apply_promotion_lift(self, forecast: List[float]) -> List[float]:
        """Apply promotion effect to forecast"""
        # Simulate promotion days (every 2 weeks)
        promotion_lift = 1.3  # 30% increase
        promotion_days = [7, 21, 35, 49]  # Every 2 weeks

        adjusted = []
        for i, value in enumerate(forecast):
            if i in promotion_days:
                adjusted.append(value * promotion_lift)
            else:
                adjusted.append(value)

        return adjusted

    def _generate_insights(
        self,
        historical_sales: List[float],
        forecast: List[float]
    ) -> List[str]:
        """Generate insights from forecast"""
        insights = []

        # Trend analysis
        if len(historical_sales) >= 14:
            recent_avg = np.mean(historical_sales[-7:])
            earlier_avg = np.mean(historical_sales[-14:-7])
            trend_pct = ((recent_avg - earlier_avg) / earlier_avg) * 100 if earlier_avg > 0 else 0

            if trend_pct > 5:
                insights.append(f"Sales trend: UP {trend_pct:.1f}% over the past week")
            elif trend_pct < -5:
                insights.append(f"Sales trend: DOWN {abs(trend_pct):.1f}% over the past week")
            else:
                insights.append("Sales trend: STABLE over the past week")

        # Volatility
        if len(historical_sales) >= 7:
            volatility = np.std(historical_sales[-7:]) / np.mean(historical_sales[-7:]) * 100
            insights.append(f"Demand volatility: {volatility:.1f}%")

        # Peak demand day
        avg_forecast = np.mean(forecast)
        peak_day_idx = np.argmax(forecast)
        insights.append(f"Highest demand expected in {peak_day_idx} days ({forecast[peak_day_idx]:.0f} units)")

        return insights

    def _generate_recommendations(
        self,
        forecast: List[float],
        insights: List[str]
    ) -> List[str]:
        """Generate recommendations based on forecast"""
        recommendations = []

        total_demand = sum(forecast)
        avg_daily = np.mean(forecast)
        max_daily = max(forecast)

        # Inventory recommendation
        safety_stock = max_daily * 3
        recommendations.append(f"Maintain safety stock of {safety_stock:.0f} units")

        # Reorder point
        if avg_daily > 0:
            lead_time_days = 7  # Assume 7-day lead time
            reorder_point = avg_daily * lead_time_days + safety_stock * 0.5
            recommendations.append(f"Set reorder point at {reorder_point:.0f} units")

        # Peak preparation
        peak_idx = np.argmax(forecast)
        if peak_idx < 7:
            recommendations.append(f"Prepare for peak demand in {peak_idx} days")

        # Trend-based recommendations
        for insight in insights:
            if "UP" in insight and avg_daily > 100:
                recommendations.append("Consider increasing stock levels due to upward trend")
                break

        return recommendations

    async def get_cached_forecast(
        self,
        product_id: str,
        location: Optional[str],
        days: int
    ) -> Dict[str, Any]:
        """Get cached or generate new forecast"""
        cache_key = f"{product_id}_{location}_{days}"

        if cache_key in self.cache:
            cached = self.cache[cache_key]
            if (datetime.utcnow() - cached["timestamp"]).total_seconds() < 3600:  # 1 hour cache
                return cached["data"]

        # Generate new forecast
        result = await self.forecast_demand(
            product_id=product_id,
            location=location,
            horizon_days=days,
            include_promotions=True,
            include_seasonality=True
        )

        # Cache result
        self.cache[cache_key] = {
            "timestamp": datetime.utcnow(),
            "data": result
        }

        return result
