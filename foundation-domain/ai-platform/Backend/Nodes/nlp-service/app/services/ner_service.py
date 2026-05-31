"""
Named Entity Recognition Service
Extracts entities like names, organizations, locations from text
"""

import logging
import re
from typing import List, Dict, Any, Optional, Set

logger = logging.getLogger(__name__)


class NERService:
    """Service for Named Entity Recognition"""

    def __init__(self):
        logger.info("Initialized NERService")

    async def extract_entities_batch(
        self,
        texts: List[str],
        entity_types: Optional[Set[str]] = None,
        language: str = "en"
    ) -> List[Dict[str, Any]]:
        """Extract entities from batch of texts"""
        results = []

        for text in texts:
            entities = self._extract_entities(text, entity_types, language)
            results.append({
                "text": text[:200] + "..." if len(text) > 200 else text,
                "entities": entities
            })

        return results

    def _extract_entities(
        self,
        text: str,
        entity_types: Optional[Set[str]] = None,
        language: str = "en"
    ) -> List[Dict[str, Any]]:
        """Extract entities from single text"""
        entities = []

        # Email addresses
        if not entity_types or "EMAIL" in entity_types:
            for match in re.finditer(r'\b[\w.-]+@[\w.-]+\.\w+\b', text):
                entities.append({
                    "text": match.group(),
                    "label": "EMAIL",
                    "start": match.start(),
                    "end": match.end(),
                    "confidence": 0.95
                })

        # Phone numbers
        if not entity_types or "PHONE" in entity_types:
            for match in re.finditer(r'\b\d{3}[-.\s]?\d{3}[-.\s]?\d{4}\b', text):
                entities.append({
                    "text": match.group(),
                    "label": "PHONE",
                    "start": match.start(),
                    "end": match.end(),
                    "confidence": 0.90
                })

        # URLs
        if not entity_types or "URL" in entity_types:
            for match in re.finditer(r'https?://\S+|www\.\S+', text):
                entities.append({
                    "text": match.group(),
                    "label": "URL",
                    "start": match.start(),
                    "end": match.end(),
                    "confidence": 0.98
                })

        # Dates
        if not entity_types or "DATE" in entity_types:
            date_patterns = [
                r'\b\d{1,2}[/-]\d{1,2}[/-]\d{2,4}\b',
                r'\b(?:Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)[a-z]*[\s,]+\d{1,2}[\s,]+\d{4}\b'
            ]
            for pattern in date_patterns:
                for match in re.finditer(pattern, text, re.IGNORECASE):
                    entities.append({
                        "text": match.group(),
                        "label": "DATE",
                        "start": match.start(),
                        "end": match.end(),
                        "confidence": 0.85
                    })

        # Numbers (currency, percentages)
        if not entity_types or "MONEY" in entity_types:
            for match in re.finditer(r'\$\d+(?:,\d{3})*(?:\.\d{2})?|\d+\s*(?:dollars?|USD|EUR|GBP)', text):
                entities.append({
                    "text": match.group(),
                    "label": "MONEY",
                    "start": match.start(),
                    "end": match.end(),
                    "confidence": 0.88
                })

        return entities
