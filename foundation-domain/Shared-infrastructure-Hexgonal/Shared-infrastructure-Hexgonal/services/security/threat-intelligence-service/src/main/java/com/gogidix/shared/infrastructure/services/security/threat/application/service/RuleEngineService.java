package com.gogidix.shared.infrastructure.services.security.threat.application.service;

import com.gogidix.shared.infrastructure.services.security.threat.domain.model.ThreatIndicator.ThreatSeverity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Rule-based threat detection engine service.
 *
 * <p>Implements common detection rules:
 * - SQL injection prevention
 * - XSS (cross-site scripting) prevention
 * - Path traversal prevention
 * - Command injection prevention
 * - Malformed input prevention
 * - Rate limiting detection
 * - Suspicious activity detection
 * </p>
 */
@Service
@Transactional
public class RuleEngineService {

    private static final Logger logger = LoggerFactory.getLogger(RuleEngineService.class);

    // Rule storage with thread-safe cache
    private final Map<String, Rule> rules = new ConcurrentHashMap<>();

    // Pre-configured detection patterns
    private static final List<DetectionPattern> DETECTION_PATTERNS = List.of(
        // SQL Injection - detect common SQL injection patterns
        new DetectionPattern("SQL_INJECTION", ThreatSeverity.CRITICAL,
            Pattern.compile("('|(\\-\\-)|(;)|(\")|(\\|)|(\\*)|(or)|(&nbsp;)|(%27)|(%2A)|(union)|(select)|(insert)|(update)|(delete)|(drop)|(exec)|(script)|(alert)|(eval)|(%3B)|(%7C)|(\\$)|(&lt;)|(&gt;)", Pattern.CASE_INSENSITIVE)),

        // XSS - detect script tags and event handlers
        new DetectionPattern("XSS_PREVENTION", ThreatSeverity.HIGH,
            Pattern.compile("(<script[^>]*>)|(javascript:)|(onerror\\s*=)|(onload\\s*=)|(eval\\s*\\()|(expression\\s*:)|(onclick)|(onmouseover)|(onfocus)|(onblur)|(onkeydown)|(onkeypress)", Pattern.CASE_INSENSITIVE)),

        // Path Traversal - detect directory traversal attempts
        new DetectionPattern("PATH_TRAVERSAL", ThreatSeverity.CRITICAL,
            Pattern.compile("(\\.\\.)|(%2e%2e)|(%252e%252e)|(~)|(\\.\\.[/\\\\])|(/\\.\\.)|([/\\\\]\\.\\.[/\\\\])|(\\.\\.[/\\\\]+)", Pattern.CASE_INSENSITIVE)),

        // Command Injection - detect shell command patterns
        new DetectionPattern("COMMAND_INJECTION", ThreatSeverity.CRITICAL,
            Pattern.compile("(;)|(&)|(\\|)|(`)|(\\$\\()|(\\${)|(\\|\\|)|(>>)|(&gt;)|(wget)|(curl)|(nc)|(netcat)|(bash)|(sh)|(cmd)|(powershell)|(eval\\s*\\()", Pattern.CASE_INSENSITIVE))
    );

    // Rate limiting patterns for detecting rapid/burst requests
    private static final Pattern RATE_LIMITING_PATTERN = Pattern.compile(
        "(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\s*,\\s*){5,}|(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\s+){10,}"
    );

    // Suspicious SSH key pattern detection
    private static final Pattern SSH_KEY_PATTERN = Pattern.compile(
        "((ssh-rsa)|(ssh-ed25519)|(ecdsa-sha2))\\s+[A-Za-z0-9+/=]{100,}\\s+"
    );

    // Large base64 encoded data (potential payload smuggling)
    private static final Pattern BASE64_PAYLOAD_PATTERN = Pattern.compile(
        "[A-Za-z0-9+/=]{100,}\\s*[A-Za-z0-9+/=]{100,}"
    );

    /**
     * Evaluate content against all registered rules.
     *
     * @param content The content to evaluate
     * @return List of matched rules with their threat levels
     */
    public List<RuleEvaluation> evaluateContent(String content) {
        List<RuleEvaluation> results = new ArrayList<>();

        if (content == null || content.isBlank()) {
            return results;
        }

        // Check pre-configured detection patterns
        for (DetectionPattern detectionPattern : DETECTION_PATTERNS) {
            Matcher matcher = detectionPattern.pattern().matcher(content);
            if (matcher.find()) {
                String detectedValue = matcher.group();
                results.add(new RuleEvaluation(
                    true,
                    detectionPattern.name(),
                    detectionPattern.severity(),
                    "Detected " + detectionPattern.name() + " pattern",
                    detectedValue
                ));
            }
        }

        // Check for rate limiting patterns
        Matcher rateLimitMatcher = RATE_LIMITING_PATTERN.matcher(content);
        if (rateLimitMatcher.find()) {
            results.add(new RuleEvaluation(
                true,
                "RATE_LIMITING",
                ThreatSeverity.MEDIUM,
                "Detected potential rate limiting or request burst pattern",
                rateLimitMatcher.group()
            ));
        }

        // Check for SSH key patterns
        Matcher sshKeyMatcher = SSH_KEY_PATTERN.matcher(content);
        if (sshKeyMatcher.find()) {
            results.add(new RuleEvaluation(
                true,
                "SSH_KEY_DETECTED",
                ThreatSeverity.HIGH,
                "Detected potential SSH key in content",
                sshKeyMatcher.group()
            ));
        }

        // Check for large base64 payloads
        Matcher base64Matcher = BASE64_PAYLOAD_PATTERN.matcher(content);
        if (base64Matcher.find()) {
            String match = base64Matcher.group();
            if (match.length() > 200) {
                results.add(new RuleEvaluation(
                    true,
                    "LARGE_PAYLOAD",
                    ThreatSeverity.MEDIUM,
                    "Detected large base64 encoded payload",
                    match.substring(0, Math.min(50, match.length())) + "..."
                ));
            }
        }

        // Check dynamically registered rules
        for (Rule rule : rules.values()) {
            if (rule.matches(content)) {
                results.add(new RuleEvaluation(
                    true,
                    rule.getName(),
                    rule.getThreatLevel(),
                    "Custom rule matched",
                    content.substring(0, Math.min(100, content.length()))
                ));
            }
        }

        return results;
    }

    /**
     * Evaluate content and return the highest severity threat found.
     *
     * @param content The content to evaluate
     * @return Highest severity threat, or null if no threats detected
     */
    public ThreatSeverity evaluateForHighestSeverity(String content) {
        List<RuleEvaluation> results = evaluateContent(content);
        if (results.isEmpty()) {
            return null;
        }

        ThreatSeverity highest = ThreatSeverity.LOW;
        for (RuleEvaluation result : results) {
            if (result.getThreatLevel().ordinal() > highest.ordinal()) {
                highest = result.getThreatLevel();
            }
        }
        return highest;
    }

    /**
     * Check if content contains any threats.
     *
     * @param content The content to check
     * @return true if any threat is detected
     */
    public boolean containsThreat(String content) {
        return !evaluateContent(content).isEmpty();
    }

    /**
     * Add a new detection rule to the engine.
     *
     * @param ruleName The rule name
     * @param rule The rule definition
     * @return true if added successfully
     */
    public boolean addRule(String ruleName, Rule rule) {
        rules.put(ruleName, rule);
        logger.info("Detection rule added: {}", ruleName);
        return true;
    }

    /**
     * Remove a rule from the engine.
     *
     * @param ruleName The rule name to remove
     * @return true if rule was removed
     */
    public boolean removeRule(String ruleName) {
        Rule removed = rules.remove(ruleName);
        if (removed != null) {
            logger.info("Detection rule removed: {}", ruleName);
            return true;
        }
        return false;
    }

    /**
     * Get all registered rule names.
     *
     * @return List of rule names
     */
    public List<String> getRuleNames() {
        return new ArrayList<>(rules.keySet());
    }

    /**
     * Rule definition for custom detection rules.
     */
    public interface Rule {
        String getName();
        ThreatSeverity getThreatLevel();
        boolean matches(String content);
    }

    /**
     * SQL Injection detection rule.
     */
    public static class SqlInjectionRule implements Rule {
        private static final String NAME = "SQL_INJECTION";
        private static final Pattern PATTERN = Pattern.compile(
                "('|(\\-\\-)|(;)|(\")|(\\|)|(\\*)|(or)|(\\s+or\\s+)|(union\\s+select)|(exec\\s+)",
                Pattern.CASE_INSENSITIVE);

        @Override
        public String getName() {
            return NAME;
        }

        @Override
        public ThreatSeverity getThreatLevel() {
            return ThreatSeverity.CRITICAL;
        }

        @Override
        public boolean matches(String content) {
            if (content == null || content.isBlank()) {
                return false;
            }
            return PATTERN.matcher(content).find();
        }
    }

    /**
     * XSS (Cross-site scripting) detection rule.
     */
    public static class XssRule implements Rule {
        private static final String NAME = "XSS_PREVENTION";
        private static final List<String> XSS_PATTERNS = Arrays.asList(
                "<script", "javascript:", "onerror", "onload", "eval(",
                "expression:", "onclick", "onmouseover", "onfocus"
        );

        @Override
        public String getName() {
            return NAME;
        }

        @Override
        public ThreatSeverity getThreatLevel() {
            return ThreatSeverity.HIGH;
        }

        @Override
        public boolean matches(String content) {
            if (content == null || content.isBlank()) {
                return false;
            }

            String lowerContent = content.toLowerCase();
            for (String pattern : XSS_PATTERNS) {
                if (lowerContent.contains(pattern.toLowerCase())) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * Path Traversal detection rule.
     */
    public static class PathTraversalRule implements Rule {
        private static final String NAME = "PATH_TRAVERSAL";
        private static final Pattern PATTERN = Pattern.compile(
                "(\\.\\.)|(%2e%2e)|(%252e%252e)|(~)|(\\.\\.[/\\\\])|(/\\.\\.)|([/\\\\]\\.\\.[/\\\\])",
                Pattern.CASE_INSENSITIVE);

        @Override
        public String getName() {
            return NAME;
        }

        @Override
        public ThreatSeverity getThreatLevel() {
            return ThreatSeverity.CRITICAL;
        }

        @Override
        public boolean matches(String content) {
            if (content == null || content.isBlank()) {
                return false;
            }
            return PATTERN.matcher(content).find();
        }
    }

    /**
     * Command Injection detection rule.
     */
    public static class CommandInjectionRule implements Rule {
        private static final String NAME = "COMMAND_INJECTION";
        private static final Pattern PATTERN = Pattern.compile(
                "(;)|(&)|(\\|)|(`)|(\\$\\()|(\\${)|(\\|\\|)|(>>)|(&gt;)",
                Pattern.CASE_INSENSITIVE);

        @Override
        public String getName() {
            return NAME;
        }

        @Override
        public ThreatSeverity getThreatLevel() {
            return ThreatSeverity.CRITICAL;
        }

        @Override
        public boolean matches(String content) {
            if (content == null || content.isBlank()) {
                return false;
            }
            return PATTERN.matcher(content).find();
        }
    }

    /**
     * Result record for rule evaluation.
     */
    public static class RuleEvaluation {
        private final boolean match;
        private final String ruleName;
        private final ThreatSeverity threatLevel;
        private final String message;
        private final String detectedPattern;

        public RuleEvaluation(boolean match, String ruleName, ThreatSeverity threatLevel,
                           String message, String detectedPattern) {
            this.match = match;
            this.ruleName = ruleName;
            this.threatLevel = threatLevel;
            this.message = message;
            this.detectedPattern = detectedPattern;
        }

        public boolean isMatch() {
            return match;
        }

        public String getRuleName() {
            return ruleName;
        }

        public ThreatSeverity getThreatLevel() {
            return threatLevel;
        }

        public String getMessage() {
            return message;
        }

        public String getDetectedPattern() {
            return detectedPattern;
        }
    }

    /**
     * Internal record for detection patterns.
     */
    private record DetectionPattern(String name, ThreatSeverity severity, Pattern pattern) {
    }
}
