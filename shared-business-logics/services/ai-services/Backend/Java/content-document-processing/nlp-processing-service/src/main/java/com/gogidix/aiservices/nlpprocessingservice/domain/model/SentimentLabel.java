package com.gogidix.aiservices.nlpprocessingservice.domain.model;

public enum SentimentLabel {
    POSITIVE("Positive", 1),
    NEUTRAL("Neutral", 0),
    NEGATIVE("Negative", -1);

    private final String displayName;
    private final int score;

    SentimentLabel(String displayName, int score) {
        this.displayName = displayName;
        this.score = score;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getScore() {
        return score;
    }

    public static SentimentLabel fromScore(double score) {
        if (score > 0.2) {
            return POSITIVE;
        } else if (score < -0.2) {
            return NEGATIVE;
        }
        return NEUTRAL;
    }
}
