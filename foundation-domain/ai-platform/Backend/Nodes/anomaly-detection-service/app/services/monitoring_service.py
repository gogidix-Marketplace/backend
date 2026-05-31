"""
Monitoring Service
Monitors transactions and system metrics for anomalies
"""

import logging
from typing import List, Dict, Any, Optional
from datetime import datetime, timedelta
from collections import defaultdict

logger = logging.getLogger(__name__)


class MonitoringService:
    """Service for monitoring and alerting"""

    def __init__(self):
        self.alerts = []  # Store recent alerts
        self.metrics_history = defaultdict(list)  # service -> list of metrics
        logger.info("Initialized MonitoringService")

    async def monitor_transaction(
        self,
        transaction: Dict[str, Any],
        user_id: str,
        session_id: Optional[str] = None
    ) -> Dict[str, Any]:
        """Monitor a transaction for suspicious activity"""
        alerts = []

        # Check for velocity alerts
        user_key = f"user:{user_id}"
        recent_count = self._count_recent_transactions(user_key, minutes=5)

        if recent_count > 10:
            alerts.append({
                "type": "velocity",
                "severity": "high",
                "message": f"High transaction velocity: {recent_count} transactions in 5 minutes"
            })

        # Check for amount alert
        amount = transaction.get("amount", 0)
        if amount > 5000:
            alerts.append({
                "type": "high_value",
                "severity": "medium",
                "message": f"High value transaction: ${amount}"
            })

        # Record transaction
        self._record_transaction(user_key, transaction)

        # Store alerts if any
        for alert in alerts:
            self.alerts.append({
                **alert,
                "timestamp": datetime.utcnow(),
                "user_id": user_id,
                "transaction_id": transaction.get("id")
            })

        return {
            "transaction_id": transaction.get("id"),
            "alerts": alerts,
            "alert_count": len(alerts),
            "monitored_at": datetime.utcnow().isoformat()
        }

    async def monitor_system(
        self,
        metrics: Dict[str, float],
        service_name: str,
        threshold: Optional[Dict[str, float]] = None
    ) -> Dict[str, Any]:
        """Monitor system metrics for anomalies"""
        threshold = threshold or {
            "cpu_percent": 80,
            "memory_percent": 85,
            "error_rate": 5,
            "response_time_ms": 1000
        }

        alerts = []

        # Check CPU
        if metrics.get("cpu_percent", 0) > threshold["cpu_percent"]:
            alerts.append({
                "type": "high_cpu",
                "severity": "warning" if metrics["cpu_percent"] < 90 else "critical",
                "message": f"High CPU usage: {metrics['cpu_percent']}%",
                "value": metrics["cpu_percent"],
                "threshold": threshold["cpu_percent"]
            })

        # Check memory
        if metrics.get("memory_percent", 0) > threshold["memory_percent"]:
            alerts.append({
                "type": "high_memory",
                "severity": "warning" if metrics["memory_percent"] < 95 else "critical",
                "message": f"High memory usage: {metrics['memory_percent']}%",
                "value": metrics["memory_percent"],
                "threshold": threshold["memory_percent"]
            })

        # Check error rate
        if metrics.get("error_rate", 0) > threshold["error_rate"]:
            alerts.append({
                "type": "high_error_rate",
                "severity": "critical",
                "message": f"High error rate: {metrics['error_rate']}%",
                "value": metrics["error_rate"],
                "threshold": threshold["error_rate"]
            })

        # Check response time
        if metrics.get("response_time_ms", 0) > threshold["response_time_ms"]:
            alerts.append({
                "type": "slow_response",
                "severity": "warning",
                "message": f"Slow response time: {metrics['response_time_ms']}ms",
                "value": metrics["response_time_ms"],
                "threshold": threshold["response_time_ms"]
            })

        # Store metrics history
        self.metrics_history[service_name].append({
            "timestamp": datetime.utcnow(),
            "metrics": metrics
        })

        # Keep only last 100 entries
        if len(self.metrics_history[service_name]) > 100:
            self.metrics_history[service_name] = self.metrics_history[service_name][-100:]

        # Store alerts
        for alert in alerts:
            self.alerts.append({
                **alert,
                "timestamp": datetime.utcnow(),
                "service": service_name
            })

        return {
            "service": service_name,
            "metrics": metrics,
            "alerts": alerts,
            "alert_count": len(alerts),
            "status": "healthy" if not alerts else "degraded" if all(a["severity"] == "warning" for a in alerts) else "critical",
            "monitored_at": datetime.utcnow().isoformat()
        }

    async def get_alerts(
        self,
        limit: int = 100,
        severity: Optional[str] = None,
        service: Optional[str] = None
    ) -> List[Dict[str, Any]]:
        """Get recent alerts"""
        filtered = self.alerts

        if severity:
            filtered = [a for a in filtered if a.get("severity") == severity]

        if service:
            filtered = [a for a in filtered if a.get("service") == service]

        # Sort by timestamp descending
        filtered = sorted(filtered, key=lambda a: a["timestamp"], reverse=True)

        return filtered[:limit]

    def _count_recent_transactions(self, key: str, minutes: int = 5) -> int:
        """Count recent transactions for a key"""
        cutoff = datetime.utcnow() - timedelta(minutes=minutes)

        if key not in self.metrics_history:
            return 0

        return sum(
            1 for entry in self.metrics_history[key]
            if entry["timestamp"] > cutoff
        )

    def _record_transaction(self, key: str, transaction: Dict[str, Any]):
        """Record a transaction for monitoring"""
        self.metrics_history[key].append({
            "timestamp": datetime.utcnow(),
            "transaction": transaction
        })

        # Keep only last 100 entries
        if len(self.metrics_history[key]) > 100:
            self.metrics_history[key] = self.metrics_history[key][-100:]
