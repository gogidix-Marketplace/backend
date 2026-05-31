"""
Anomaly Detection Service
Detects outliers in data using various statistical and ML methods
"""

import logging
import numpy as np
from typing import List, Dict, Any, Union
from scipy import stats

logger = logging.getLogger(__name__)


class AnomalyDetectionService:
    """Service for general anomaly detection"""

    def __init__(self):
        logger.info("Initialized AnomalyDetectionService")

    async def detect_anomalies(
        self,
        data: List[float],
        algorithm: str = "zscore",
        threshold: float = 3.0
    ) -> Dict[str, Any]:
        """
        Detect anomalies in data

        Args:
            data: List of numeric values
            algorithm: Detection algorithm (zscore, iqr, isolation_forest, dbscan)
            threshold: Sensitivity threshold

        Returns:
            Anomaly detection results
        """
        if len(data) < 3:
            return {
                "anomalies": [],
                "anomaly_count": 0,
                "anomaly_indices": [],
                "statistics": {},
                "algorithm_used": algorithm
            }

        if algorithm == "zscore":
            return self._zscore_detection(data, threshold)
        elif algorithm == "iqr":
            return self._iqr_detection(data, threshold)
        elif algorithm == "isolation_forest":
            return self._isolation_forest_detection(data, threshold)
        elif algorithm == "dbscan":
            return self._dbscan_detection(data)
        else:
            return self._zscore_detection(data, threshold)

    def _zscore_detection(self, data: List[float], threshold: float) -> Dict[str, Any]:
        """Z-score based anomaly detection"""
        arr = np.array(data)
        mean = np.mean(arr)
        std = np.std(arr)

        z_scores = np.abs((arr - mean) / std) if std > 0 else np.zeros_like(arr)

        anomaly_indices = np.where(z_scores > threshold)[0].tolist()
        anomalies = [data[i] for i in anomaly_indices]

        return {
            "anomalies": anomalies,
            "anomaly_count": len(anomalies),
            "anomaly_indices": anomaly_indices,
            "statistics": {
                "mean": float(mean),
                "std": float(std),
                "min": float(np.min(arr)),
                "max": float(np.max(arr)),
                "median": float(np.median(arr))
            },
            "algorithm_used": "zscore"
        }

    def _iqr_detection(self, data: List[float], multiplier: float) -> Dict[str, Any]:
        """Interquartile Range (IQR) based anomaly detection"""
        arr = np.array(data)
        q1 = np.percentile(arr, 25)
        q3 = np.percentile(arr, 75)
        iqr = q3 - q1

        lower_bound = q1 - multiplier * iqr
        upper_bound = q3 + multiplier * iqr

        anomaly_indices = [
            i for i, val in enumerate(data)
            if val < lower_bound or val > upper_bound
        ]
        anomalies = [data[i] for i in anomaly_indices]

        return {
            "anomalies": anomalies,
            "anomaly_count": len(anomalies),
            "anomaly_indices": anomaly_indices,
            "statistics": {
                "q1": float(q1),
                "q3": float(q3),
                "iqr": float(iqr),
                "lower_bound": float(lower_bound),
                "upper_bound": float(upper_bound)
            },
            "algorithm_used": "iqr"
        }

    def _isolation_forest_detection(self, data: List[float], threshold: float) -> Dict[str, Any]:
        """Isolation Forest based anomaly detection"""
        arr = np.array(data).reshape(-1, 1)

        # Simplified isolation forest (using sklearn would be better)
        # This is a basic approximation
        mean = np.mean(arr)
        std = np.std(arr)

        # Use modified z-score for isolation
        median = np.median(arr)
        mad = np.median(np.abs(arr - median))

        if mad > 0:
            modified_z_scores = 0.6745 * (arr - median) / mad
            anomaly_indices = np.where(np.abs(modified_z_scores) > threshold)[0].tolist()
        else:
            anomaly_indices = []

        anomalies = [data[i] for i in anomaly_indices]

        return {
            "anomalies": anomalies,
            "anomaly_count": len(anomalies),
            "anomaly_indices": anomaly_indices,
            "statistics": {
                "median": float(median),
                "mad": float(mad),
                "mean": float(mean),
                "std": float(std)
            },
            "algorithm_used": "isolation_forest"
        }

    def _dbscan_detection(self, data: List[float]) -> Dict[str, Any]:
        """DBSCAN clustering for anomaly detection"""
        arr = np.array(data).reshape(-1, 1)

        # Simple density-based detection
        # Points far from dense regions are anomalies

        # Use percentile-based approach as approximation
        q1 = np.percentile(arr, 25)
        q3 = np.percentile(arr, 75)

        # Points in sparse regions (outside IQR extended)
        iqr = q3 - q1
        lower_bound = q1 - 2 * iqr
        upper_bound = q3 + 2 * iqr

        anomaly_indices = [
            i for i, val in enumerate(data)
            if val < lower_bound or val > upper_bound
        ]
        anomalies = [data[i] for i in anomaly_indices]

        return {
            "anomalies": anomalies,
            "anomaly_count": len(anomalies),
            "anomaly_indices": anomaly_indices,
            "statistics": {
                "clusters": 1,  # Simplified
                "noise_points": len(anomalies)
            },
            "algorithm_used": "dbscan"
        }

    async def detect_timeseries_anomalies(
        self,
        timestamps: List[str],
        values: List[float],
        window_size: int = 10
    ) -> Dict[str, Any]:
        """
        Detect anomalies in time series data

        Args:
            timestamps: List of timestamp strings
            values: List of values
            window_size: Size of sliding window

        Returns:
            Time series anomaly results
        """
        if len(values) < window_size * 2:
            return {"anomalies": [], "anomaly_count": 0}

        anomalies = []
        anomaly_indices = []

        for i in range(window_size, len(values) - window_size):
            window = values[i - window_size:i + window_size]
            current = values[i]

            window_mean = np.mean(window)
            window_std = np.std(window)

            if window_std > 0:
                z_score = abs(current - window_mean) / window_std
                if z_score > 3:
                    anomalies.append({
                        "timestamp": timestamps[i],
                        "value": current,
                        "expected": window_mean,
                        "z_score": float(z_score)
                    })
                    anomaly_indices.append(i)

        return {
            "anomalies": anomalies,
            "anomaly_count": len(anomalies),
            "anomaly_indices": anomaly_indices
        }
