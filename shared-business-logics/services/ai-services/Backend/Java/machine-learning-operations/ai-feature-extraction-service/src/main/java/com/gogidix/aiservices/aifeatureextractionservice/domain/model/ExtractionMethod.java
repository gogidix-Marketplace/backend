package com.gogidix.aiservices.aifeatureextractionservice.domain.model;

/**
 * Enumeration of feature extraction methods.
 */
public enum ExtractionMethod {
    TFIDF,
    PCA,
    AUTOENCODER,
    WORD2VEC,
    BERT,
    COUNT_VECTORIZER,
    HASHING_VECTORIZER
}
