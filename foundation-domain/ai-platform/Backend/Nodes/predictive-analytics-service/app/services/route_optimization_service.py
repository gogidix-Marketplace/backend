"""
Route Optimization Service
Optimizes delivery routes using various algorithms
"""

import logging
import math
from typing import List, Dict, Any, Optional, Tuple
from datetime import datetime

logger = logging.getLogger(__name__)


class RouteOptimizationService:
    """Service for route optimization"""

    def __init__(self):
        logger.info("Initialized RouteOptimizationService")

    async def optimize_routes(
        self,
        depot: Dict[str, float],
        deliveries: List[Dict[str, Any]],
        vehicles: List[Dict[str, Any]],
        constraints: Optional[Dict[str, Any]] = None,
        objective: str = "minimize_distance"
    ) -> Dict[str, Any]:
        """
        Optimize delivery routes

        Args:
            depot: Depot location {lat, lng}
            deliveries: List of delivery locations and requirements
            vehicles: Available vehicles
            constraints: Routing constraints
            objective: Optimization objective

        Returns:
            Optimized routes
        """
        if not deliveries:
            return {
                "routes": [],
                "total_distance": 0,
                "total_time": 0,
                "metrics": {}
            }

        # Use nearest neighbor heuristic for route construction
        routes = self._construct_routes(depot, deliveries, vehicles, constraints)

        # Calculate metrics
        total_distance = sum(r["distance"] for r in routes)
        total_time = sum(r["duration"] for r in routes)
        total_cost = self._calculate_cost(routes, vehicles)

        return {
            "routes": routes,
            "total_distance": round(total_distance, 2),
            "total_time": round(total_time, 2),
            "total_cost": round(total_cost, 2),
            "metrics": {
                "num_routes": len(routes),
                "stops_per_route": [len(r["stops"]) for r in routes],
                "vehicle_utilization": self._calculate_utilization(routes, vehicles)
            },
            "savings": self._calculate_savings(routes)
        }

    def _construct_routes(
        self,
        depot: Dict[str, float],
        deliveries: List[Dict[str, Any]],
        vehicles: List[Dict[str, Any]],
        constraints: Optional[Dict[str, Any]]
    ) -> List[Dict[str, Any]]:
        """Construct routes using nearest neighbor heuristic"""
        unassigned = deliveries.copy()
        routes = []

        for vehicle in vehicles:
            if not unassigned:
                break

            route = self._build_single_route(depot, unassigned, vehicle, constraints)
            if route["stops"]:
                routes.append(route)
                # Remove assigned deliveries
                assigned_ids = {stop["id"] for stop in route["stops"]}
                unassigned = [d for d in unassigned if d["id"] not in assigned_ids]

        return routes

    def _build_single_route(
        self,
        depot: Dict[str, float],
        deliveries: List[Dict[str, Any]],
        vehicle: Dict[str, Any],
        constraints: Optional[Dict[str, Any]]
    ) -> Dict[str, Any]:
        """Build a single route using nearest neighbor"""
        capacity = vehicle.get("capacity", float("inf"))
        max_stops = constraints.get("max_stops", float("inf")) if constraints else float("inf")

        current_location = depot
        remaining = deliveries.copy()
        route_stops = []
        total_distance = 0
        current_load = 0

        while remaining and len(route_stops) < max_stops:
            # Find nearest unvisited location
            nearest = None
            nearest_dist = float("inf")
            nearest_idx = -1

            for i, delivery in enumerate(remaining):
                # Check capacity constraint
                demand = delivery.get("demand", 1)
                if current_load + demand > capacity:
                    continue

                dist = self._haversine_distance(
                    current_location["lat"],
                    current_location["lng"],
                    delivery["location"]["lat"],
                    delivery["location"]["lng"]
                )

                if dist < nearest_dist:
                    nearest_dist = dist
                    nearest = delivery
                    nearest_idx = i

            if nearest is None:
                break  # No more reachable deliveries

            # Add to route
            route_stops.append({
                "id": nearest["id"],
                "location": nearest["location"],
                "demand": nearest.get("demand", 1),
                "distance_from_previous": nearest_dist
            })

            total_distance += nearest_dist
            current_load += nearest.get("demand", 1)
            current_location = nearest["location"]
            remaining.pop(nearest_idx)

        # Return to depot
        if route_stops:
            return_distance = self._haversine_distance(
                current_location["lat"],
                current_location["lng"],
                depot["lat"],
                depot["lng"]
            )
            total_distance += return_distance

        return {
            "vehicle_id": vehicle["id"],
            "stops": route_stops,
            "distance": round(total_distance, 2),
            "duration": round(total_distance / 50, 2),  # Assume 50 km/h
            "load": current_load
        }

    def _haversine_distance(self, lat1: float, lon1: float, lat2: float, lon2: float) -> float:
        """Calculate distance between two points using Haversine formula"""
        R = 6371  # Earth's radius in km

        lat1_rad = math.radians(lat1)
        lat2_rad = math.radians(lat2)
        delta_lat = math.radians(lat2 - lat1)
        delta_lon = math.radians(lon2 - lon1)

        a = (math.sin(delta_lat / 2) ** 2 +
             math.cos(lat1_rad) * math.cos(lat2_rad) *
             math.sin(delta_lon / 2) ** 2)

        c = 2 * math.asin(math.sqrt(a))
        return R * c

    def _calculate_cost(self, routes: List[Dict], vehicles: List[Dict]) -> float:
        """Calculate total cost of routes"""
        cost_per_km = 0.5  # Example cost
        driver_hourly_rate = 20

        total_cost = 0
        for route in routes:
            distance_cost = route["distance"] * cost_per_km
            time_cost = (route["duration"] / 60) * driver_hourly_rate
            total_cost += distance_cost + time_cost

        return total_cost

    def _calculate_utilization(
        self,
        routes: List[Dict],
        vehicles: List[Dict]
    ) -> List[float]:
        """Calculate vehicle utilization"""
        utilization = []
        for route in routes:
            capacity = next(
                (v["capacity"] for v in vehicles if v["id"] == route["vehicle_id"]),
                1
            )
            util = (route["load"] / capacity * 100) if capacity > 0 else 0
            utilization.append(round(util, 2))
        return utilization

    def _calculate_savings(self, routes: List[Dict]) -> Dict[str, Any]:
        """Calculate savings vs naive approach"""
        # Naive: each delivery separate trip from depot
        naive_distance = sum(
            2 * sum(stop["distance_from_previous"] for stop in route["stops"])
            for route in routes
        )

        optimized_distance = sum(route["distance"] for route in routes)

        return {
            "distance_km": round(naive_distance - optimized_distance, 2),
            "percentage": round((1 - optimized_distance / naive_distance) * 100, 2) if naive_distance > 0 else 0
        }
