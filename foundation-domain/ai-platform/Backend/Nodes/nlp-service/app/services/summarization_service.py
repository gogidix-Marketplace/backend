"""
Text Summarization Service
Generates summaries of long text content
"""

import logging
from typing import Optional

logger = logging.getLogger(__name__)


class SummarizationService:
    """Service for text summarization"""

    def __init__(self):
        logger.info("Initialized SummarizationService")

    async def summarize(
        self,
        text: str,
        max_length: int = 150,
        min_length: int = 30,
        model: str = "extractive"
    ) -> str:
        """Summarize text"""
        if model == "extractive":
            return self._extractive_summarization(text, max_length, min_length)
        else:
            return self._extractive_summarization(text, max_length, min_length)

    def _extractive_summarization(self, text: str, max_length: int, min_length: int) -> str:
        """Extractive summarization using sentence scoring"""
        sentences = self._split_sentences(text)

        if len(sentences) <= 2:
            return text[:max_length * 5]  # Rough character limit

        # Score sentences
        scored_sentences = []
        for i, sent in enumerate(sentences):
            score = self._score_sentence(sent, sentences, i)
            scored_sentences.append((sent, score, len(sent.split())))

        # Sort by score
        scored_sentences.sort(key=lambda x: x[1], reverse=True)

        # Select sentences until we hit max length
        selected = []
        current_length = 0
        for sent, score, length in scored_sentences:
            if current_length + length <= max_length:
                selected.append((sent, score))
                current_length += length
            if current_length >= min_length:
                break

        if not selected:
            selected = [(scored_sentences[0][0], scored_sentences[0][1])]

        # Reorder by original position
        selected_with_pos = []
        for sent, score in selected:
            original_pos = sentences.index(sent)
            selected_with_pos.append((original_pos, sent))

        selected_with_pos.sort(key=lambda x: x[0])

        summary = " ".join([sent for _, sent in selected_with_pos])
        return summary

    def _split_sentences(self, text: str) -> list:
        """Split text into sentences"""
        import re
        sentences = re.split(r'[.!?]+', text)
        return [s.strip() for s in sentences if s.strip()]

    def _score_sentence(self, sentence: str, all_sentences: list, position: int) -> float:
        """Score a sentence for summary selection"""
        score = 0.0

        # Length preference (not too short, not too long)
        words = sentence.split()
        word_count = len(words)
        if 10 <= word_count <= 30:
            score += 0.5
        elif word_count < 5:
            score -= 0.3

        # Position preference (earlier sentences often more important)
        if position < len(all_sentences) * 0.3:
            score += 0.3
        elif position > len(all_sentences) * 0.7:
            score -= 0.1

        # Keyword preference
        important_words = ["important", "significant", "key", "main", "primary", "conclusion", "result"]
        word_lower = sentence.lower()
        for word in important_words:
            if word in word_lower:
                score += 0.1

        # Title case words (often proper nouns/important terms)
        title_case_words = sum(1 for w in words if w[0].isupper())
        score += min(0.3, title_case_words * 0.05)

        return score
