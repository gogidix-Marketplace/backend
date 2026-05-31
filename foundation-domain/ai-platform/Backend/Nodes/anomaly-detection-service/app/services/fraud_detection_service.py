"""
Fraud Detection Service
Detects fraudulent transactions using ML models and rule-based systems
"""

import logging
from typing import List, Dict, Any, Optional
from datetime import datetime, timedelta
import numpy as np

logger = logging.getLogger(__name__)


class FraudDetectionService:
    """Service for fraud detection"""

    # Fraud risk factors
    RISK_FACTORS = {
        "high_amount": 1000,  # Amount above which is high risk
        "unusual_location": "international",
        "new_user_hours": 24,  # Hours since account creation for new user
        "velocity_limit": 5,  # Transactions per hour limit
        "unusual_time_range": (22, 6),  # High risk hours (10 PM - 6 AM)
    }

    def __init__(self):
        # Track transaction velocity
        self.transaction_history = {}  # user_id -> list of timestamps
        logger.info("Initialized FraudDetectionService")

    async def detect_fraud(
        self,
        transaction: Dict[str, Any],
        user_history: Optional[List[Dict[str, Any]]] = None,
        rules: Optional[Dict[str, Any]] = None
    ) -> Dict[str, Any]:
        """
        Detect if transaction is fraudulent

        Args:
            transaction: Transaction details
            user_history: User's historical transactions
            rules: Custom fraud detection rules

        Returns:
            Fraud detection result
        """
        risk_score = 0.0
        reasons = []

        # 1. Check amount
        amount = transaction.get("amount", 0)
        if amount > self.RISK_FACTORS["high_amount"]:
            risk_score += 0.3
            reasons.append(f"High transaction amount: {amount}")

        # 2. Check location (if available)
        location = transaction.get("location", {})
        if location.get("country") != transaction.get("user_country"):
            risk_score += 0.25
            reasons.append("Transaction from unusual location")

        # 3. Check transaction velocity
        user_id = transaction.get("user_id")
        if user_id:
            velocity_risk = self._check_velocity(user_id)
            if velocity_risk > 0:
                risk_score += velocity_risk
                reasons.append("High transaction velocity")

        # 4. Check time of day
        current_hour = datetime.utcnow().hour
        if self.RISK_FACTORS["unusual_time_range"][0] <= current_hour or current_hour < self.RISK_FACTORS["unusual_time_range"][1]:
            if amount > 500:  # Only flag for significant amounts
                risk_score += 0.15
                reasons.append("Transaction at unusual hours")

        # 5. Check if new user
        if user_history is not None and len(user_history) < 3:
            risk_score += 0.2
            reasons.append("New user with limited history")

        # 6. Check against user history patterns
        if user_history:
            pattern_risk = self._check_patterns(transaction, user_history)
            risk_score += pattern_risk
            if pattern_risk > 0:
                reasons.append("Deviation from normal spending pattern")

        # Determine risk level
        fraud_score = round(min(1.0, risk_score), 4)
        if fraud_score >= 0.7:
            risk_level = "high"
            is_fraudulent = True
            alert_triggered = True
        elif fraud_score >= 0.4:
            risk_level = "medium"
            is_fraudulent = False
            alert_triggered = True
        else:
            risk_level = "low"
            is_fraudulent = False
            alert_triggered = False

        return {
            "is_fraudulent": is_fraudulent,
            "fraud_score": fraud_score,
            "risk_level": risk_level,
            "reasons": reasons,
            "alert_triggered": alert_triggered,
            "model_version": "fraud_detection_v1.0"
        }

    def _check_velocity(self, user_id: str) -> float:
        """Check transaction velocity for user"""
        now = datetime.utcnow()
        hour_ago = now - timedelta(hours=1)

        if user_id not in self.transaction_history:
            self.transaction_history[user_id] = []

        # Clean old entries
        self.transaction_history[user_id] = [
            ts for ts in self.transaction_history[user_id]
            if ts > hour_ago
        ]

        # Add current transaction
        self.transaction_history[user_id].append(now)

        # Check velocity
        count = len(self.transaction_history[user_id])
        if count > self.RISK_FACTORS["velocity_limit"]:
            return min(0.4, (count - self.RISK_FACTORS["velocity_limit"]) * 0.1)

        return 0.0

    def _check_patterns(self, transaction: Dict, history: List[Dict]) -> float:
        """Check if transaction deviates from user's normal pattern"""
        risk = 0.0

        if not history:
            return risk

        # Calculate average transaction amount
        amounts = [h.get("amount", 0) for h in history]
        avg_amount = np.mean(amounts) if amounts else 0
        std_amount = np.std(amounts) if amounts else 0

        current_amount = transaction.get("amount", 0)

        # Check if amount is significantly different
        if std_amount > 0:
            z_score = abs(current_amount - avg_amount) / std_amount
            if z_score > 3:
                risk += 0.25
            elif z_score > 2:
                risk += 0.1

        # Check merchant category (if available)
        current_merchant = transaction.get("merchant_category")
        if current_merchant:
            merchant_freq = sum(1 for h in history if h.get("merchant_category") == current_merchant)
            if len(history) > 5 and merchant_freq == 0:
                risk += 0.15

        return min(0.4, risk)
