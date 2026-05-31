"""
Chatbot Service
Conversational AI for customer support, sales, and general assistance
"""

import logging
from typing import List, Dict, Any, Optional
from datetime import datetime
import re
import random

logger = logging.getLogger(__name__)


class ChatbotResponse:
    """Response from chatbot"""
    def __init__(
        self,
        text: str,
        confidence: float,
        intent: Optional[str] = None,
        entities: Optional[Dict[str, Any]] = None,
        suggestions: Optional[List[str]] = None
    ):
        self.text = text
        self.confidence = confidence
        self.intent = intent
        self.entities = entities or {}
        self.suggestions = suggestions or []


class ChatbotService:
    """
    Chatbot service for various use cases
    - Customer service
    - Sales assistance
    - Technical support
    - General conversation
    """

    # Intent patterns and responses
    INTENT_PATTERNS = {
        "greeting": {
            "patterns": [r"\b(hi|hello|hey|good morning|good afternoon|good evening)\b"],
            "responses": [
                "Hello! How can I help you today?",
                "Hi there! What can I assist you with?",
                "Hey! I'm here to help. What do you need?"
            ]
        },
        "farewell": {
            "patterns": [r"\b(bye|goodbye|see you|take care|exit)\b"],
            "responses": [
                "Goodbye! Have a great day!",
                "See you later! Feel free to come back anytime.",
                "Take care! I'm here if you need anything else."
            ]
        },
        "order_status": {
            "patterns": [r"\b(order|shipment|delivery|tracking|where is my|status)\b"],
            "responses": [
                "I can help you check your order status. Please provide your order number.",
                "To check your order status, I'll need your order ID. Could you please share it?"
            ]
        },
        "product_info": {
            "patterns": [r"\b(product|item|information|details|tell me about|looking for)\b"],
            "responses": [
                "I'd be happy to help you find product information. What product are you interested in?",
                "Sure! Which product would you like to know more about?"
            ]
        },
        "refund": {
            "patterns": [r"\b(refund|return|money back|cancel order|exchange)\b"],
            "responses": [
                "I understand you need help with a refund. Let me connect you with our support team who can assist you better.",
                "For refund requests, I'll transfer you to our specialized support team. In the meantime, could you provide your order number?"
            ]
        },
        "complaint": {
            "patterns": [r"\b(complaint|issue|problem|disappointed|unhappy|not working|broken)\b"],
            "responses": [
                "I'm sorry to hear you're experiencing an issue. I'm here to help resolve this. Could you please describe the problem in detail?",
                "I apologize for the inconvenience. Let's work together to fix this. What seems to be the problem?"
            ]
        },
        "pricing": {
            "patterns": [r"\b(price|cost|how much|expensive|cheap|discount|offer)\b"],
            "responses": [
                "I can help you with pricing information. Which product or service are you interested in?",
                "Pricing varies by product. Could you specify which item you're looking at?"
            ]
        },
        "shipping": {
            "patterns": [r"\b(shipping|delivery time|how long|shipping cost|free shipping)\b"],
            "responses": [
                "Standard shipping takes 3-5 business days. Express shipping is available for 1-2 day delivery.",
                "We offer multiple shipping options. Standard is free on orders over $50!"
            ]
        },
        "payment": {
            "patterns": [r"\b(payment|pay|credit card|paypal|checkout|billing)\b"],
            "responses": [
                "We accept all major credit cards, PayPal, and Apple Pay. Would you like help with the checkout process?",
                "Payment is secure and easy. You can pay with credit card, PayPal, or other supported methods."
            ]
        },
        "thanks": {
            "patterns": [r"\b(thank|thanks|appreciate)\b"],
            "responses": [
                "You're welcome! Is there anything else I can help with?",
                "Happy to help! Let me know if you need anything else."
            ]
        }
    }

    def __init__(self):
        self.sessions = {}  # Session storage
        logger.info("Initialized ChatbotService")

    async def get_response(
        self,
        session_id: str,
        messages: List[Dict[str, str]],
        bot_type: str = "customer_service",
        context: Optional[Dict[str, Any]] = None
    ) -> ChatbotResponse:
        """
        Get chatbot response

        Args:
            session_id: Session identifier
            messages: Conversation history
            bot_type: Type of bot
            context: Additional context

        Returns:
            Chatbot response
        """
        # Get or create session
        if session_id not in self.sessions:
            self.sessions[session_id] = {
                "created_at": datetime.utcnow(),
                "messages": [],
                "context": context or {}
            }

        # Get latest message
        latest_message = messages[-1] if messages else {}
        user_input = latest_message.get("content", "")

        # Detect intent
        intent, confidence = self._detect_intent(user_input)

        # Extract entities
        entities = self._extract_entities(user_input, intent)

        # Generate response
        response_text = self._generate_response(intent, bot_type, context, entities)

        # Update session
        self.sessions[session_id]["messages"].append({
            "role": "user",
            "content": user_input,
            "timestamp": datetime.utcnow()
        })
        self.sessions[session_id]["messages"].append({
            "role": "assistant",
            "content": response_text,
            "timestamp": datetime.utcnow()
        })

        # Generate suggestions
        suggestions = self._generate_suggestions(intent)

        return ChatbotResponse(
            text=response_text,
            confidence=confidence,
            intent=intent,
            entities=entities,
            suggestions=suggestions
        )

    def _detect_intent(self, text: str) -> tuple:
        """Detect intent from user input"""
        text_lower = text.lower()

        for intent, config in self.INTENT_PATTERNS.items():
            for pattern in config["patterns"]:
                if re.search(pattern, text_lower, re.IGNORECASE):
                    return intent, 0.85

        # Default fallback
        return "unknown", 0.3

    def _extract_entities(self, text: str, intent: str) -> Dict[str, Any]:
        """Extract entities from text"""
        entities = {}

        # Extract order number
        order_match = re.search(r'\b(?:order|#)\s*(\w{3,}-?\d{3,}|\d{6,})\b', text, re.IGNORECASE)
        if order_match:
            entities["order_number"] = order_match.group(1)

        # Extract email
        email_match = re.search(r'\b[\w.-]+@[\w.-]+\.\w+\b', text)
        if email_match:
            entities["email"] = email_match.group(0)

        # Extract phone number
        phone_match = re.search(r'\b\d{3}[-.\s]?\d{3}[-.\s]?\d{4}\b', text)
        if phone_match:
            entities["phone"] = phone_match.group(0)

        # Extract product names (simplified)
        product_keywords = ["laptop", "phone", "tablet", "watch", "headphones", "camera"]
        for product in product_keywords:
            if product in text.lower():
                entities["product_type"] = product

        return entities

    def _generate_response(
        self,
        intent: str,
        bot_type: str,
        context: Optional[Dict[str, Any]],
        entities: Dict[str, Any]
    ) -> str:
        """Generate response based on intent and context"""
        # Get intent response
        if intent in self.INTENT_PATTERNS:
            responses = self.INTENT_PATTERNS[intent]["responses"]
            base_response = random.choice(responses)
        else:
            base_response = self._get_fallback_response(bot_type)

        # Customize response based on entities
        if "order_number" in entities:
            base_response += f" I see you're asking about order {entities['order_number']}. "

        return base_response

    def _get_fallback_response(self, bot_type: str) -> str:
        """Get fallback response for unknown intents"""
        fallbacks = {
            "customer_service": [
                "I'm not sure I understood that correctly. Could you please rephrase?",
                "I'd be happy to help! Could you provide more details about what you need?",
                "Let me make sure I understand. Are you asking about your order, a product, or something else?"
            ],
            "sales": [
                "I'd love to help you find the perfect product! What are you looking for?",
                "Can you tell me more about what you're interested in? I can suggest some great options!",
                "I'm here to help you make the best choice. What features are important to you?"
            ],
            "support": [
                "I understand you need technical assistance. Could you describe the issue you're experiencing?",
                "Let's troubleshoot this together. What specific problem are you having?",
                "I'm here to help resolve your issue. What seems to be the problem?"
            ],
            "general": [
                "I'm here to help! Could you tell me more about what you need?",
                "I'd be happy to assist you. What can I help you with today?"
            ]
        }

        bot_fallbacks = fallbacks.get(bot_type, fallbacks["general"])
        return random.choice(bot_fallbacks)

    def _generate_suggestions(self, intent: str) -> List[str]:
        """Generate follow-up suggestions"""
        suggestions_map = {
            "greeting": ["Check order status", "Browse products", "Talk to support"],
            "order_status": ["Track another order", "Return to main menu", "Contact support"],
            "product_info": ["See special offers", "Compare products", "Add to cart"],
            "refund": ["Start refund process", "View return policy", "Contact support"],
            "unknown": ["Check order status", "Browse products", "Talk to support"]
        }

        return suggestions_map.get(intent, ["Check order status", "Browse products", "Contact support"])

    def clear_session(self, session_id: str):
        """Clear chat session"""
        if session_id in self.sessions:
            del self.sessions[session_id]
            logger.info(f"Cleared session: {session_id}")

    def get_session(self, session_id: str) -> Optional[Dict[str, Any]]:
        """Get session data"""
        return self.sessions.get(session_id)
