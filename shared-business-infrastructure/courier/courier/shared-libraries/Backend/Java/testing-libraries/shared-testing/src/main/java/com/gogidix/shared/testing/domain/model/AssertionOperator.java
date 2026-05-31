package com.gogidix.shared.testing.domain.model;

import java.util.Objects;

/**
 * Enumeration of assertion operators for test comparisons.
 */
public enum AssertionOperator {
    
    EQUALS("==", "Equals") {
        @Override
        public boolean evaluate(Object actual, Object expected) {
            return Objects.equals(actual, expected);
        }
    },
    
    NOT_EQUALS("!=", "Not Equals") {
        @Override
        public boolean evaluate(Object actual, Object expected) {
            return !Objects.equals(actual, expected);
        }
    },
    
    GREATER_THAN(">", "Greater Than") {
        @Override
        public boolean evaluate(Object actual, Object expected) {
            return compareNumbers(actual, expected) > 0;
        }
    },
    
    GREATER_THAN_OR_EQUAL(">=", "Greater Than or Equal") {
        @Override
        public boolean evaluate(Object actual, Object expected) {
            return compareNumbers(actual, expected) >= 0;
        }
    },
    
    LESS_THAN("<", "Less Than") {
        @Override
        public boolean evaluate(Object actual, Object expected) {
            return compareNumbers(actual, expected) < 0;
        }
    },
    
    LESS_THAN_OR_EQUAL("<=", "Less Than or Equal") {
        @Override
        public boolean evaluate(Object actual, Object expected) {
            return compareNumbers(actual, expected) <= 0;
        }
    },
    
    CONTAINS("contains", "Contains") {
        @Override
        public boolean evaluate(Object actual, Object expected) {
            if (actual instanceof String actualStr && expected instanceof String expectedStr) {
                return actualStr.contains(expectedStr);
            }
            if (actual instanceof java.util.Collection<?> collection) {
                return collection.contains(expected);
            }
            return false;
        }
    },
    
    NOT_CONTAINS("not contains", "Not Contains") {
        @Override
        public boolean evaluate(Object actual, Object expected) {
            return !CONTAINS.evaluate(actual, expected);
        }
    },
    
    STARTS_WITH("starts with", "Starts With") {
        @Override
        public boolean evaluate(Object actual, Object expected) {
            if (actual instanceof String actualStr && expected instanceof String expectedStr) {
                return actualStr.startsWith(expectedStr);
            }
            return false;
        }
    },
    
    ENDS_WITH("ends with", "Ends With") {
        @Override
        public boolean evaluate(Object actual, Object expected) {
            if (actual instanceof String actualStr && expected instanceof String expectedStr) {
                return actualStr.endsWith(expectedStr);
            }
            return false;
        }
    },
    
    MATCHES_REGEX("matches", "Matches Regex") {
        @Override
        public boolean evaluate(Object actual, Object expected) {
            if (actual instanceof String actualStr && expected instanceof String pattern) {
                return actualStr.matches(pattern);
            }
            return false;
        }
    },
    
    IN_RANGE("in range", "In Range") {
        @Override
        public boolean evaluate(Object actual, Object expected) {
            if (!(expected instanceof java.util.Map<?, ?> range)) {
                return false;
            }
            
            Object min = range.get("min");
            Object max = range.get("max");
            
            if (min == null || max == null) {
                return false;
            }
            
            return compareNumbers(actual, min) >= 0 && compareNumbers(actual, max) <= 0;
        }
    },
    
    SIZE_EQUALS("size ==", "Size Equals") {
        @Override
        public boolean evaluate(Object actual, Object expected) {
            int actualSize = getSize(actual);
            if (expected instanceof Integer expectedSize) {
                return actualSize == expectedSize;
            }
            return false;
        }
    },
    
    HAS_LENGTH("length ==", "Has Length") {
        @Override
        public boolean evaluate(Object actual, Object expected) {
            if (actual instanceof String actualStr && expected instanceof Integer expectedLength) {
                return actualStr.length() == expectedLength;
            }
            return false;
        }
    },
    
    IS_EMPTY("is empty", "Is Empty") {
        @Override
        public boolean evaluate(Object actual, Object expected) {
            return getSize(actual) == 0;
        }
    },
    
    IS_NOT_EMPTY("is not empty", "Is Not Empty") {
        @Override
        public boolean evaluate(Object actual, Object expected) {
            return getSize(actual) > 0;
        }
    };
    
    private final String symbol;
    private final String displayName;
    
    AssertionOperator(String symbol, String displayName) {
        this.symbol = symbol;
        this.displayName = displayName;
    }
    
    /**
     * Gets the operator symbol.
     */
    public String getSymbol() {
        return symbol;
    }
    
    /**
     * Gets the display name.
     */
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Evaluates the assertion with actual and expected values.
     */
    public abstract boolean evaluate(Object actual, Object expected);
    
    /**
     * Compares two numbers, handling different numeric types.
     */
    protected static int compareNumbers(Object actual, Object expected) {
        if (actual instanceof Number actualNum && expected instanceof Number expectedNum) {
            double actualDouble = actualNum.doubleValue();
            double expectedDouble = expectedNum.doubleValue();
            return Double.compare(actualDouble, expectedDouble);
        }
        
        // Try to parse strings as numbers
        if (actual instanceof String actualStr && expected instanceof String expectedStr) {
            try {
                double actualDouble = Double.parseDouble(actualStr);
                double expectedDouble = Double.parseDouble(expectedStr);
                return Double.compare(actualDouble, expectedDouble);
            } catch (NumberFormatException e) {
                // Fall back to string comparison
                return actualStr.compareTo(expectedStr);
            }
        }
        
        // If one is a number and one is a string, try to convert
        if (actual instanceof Number actualNum && expected instanceof String expectedStr) {
            try {
                double expectedDouble = Double.parseDouble(expectedStr);
                return Double.compare(actualNum.doubleValue(), expectedDouble);
            } catch (NumberFormatException e) {
                return -1; // Number is "less than" non-numeric string
            }
        }
        
        if (actual instanceof String actualStr && expected instanceof Number expectedNum) {
            try {
                double actualDouble = Double.parseDouble(actualStr);
                return Double.compare(actualDouble, expectedNum.doubleValue());
            } catch (NumberFormatException e) {
                return 1; // Non-numeric string is "greater than" number
            }
        }
        
        // Default to string comparison
        return String.valueOf(actual).compareTo(String.valueOf(expected));
    }
    
    /**
     * Gets the size/length of an object.
     */
    protected static int getSize(Object obj) {
        if (obj == null) return 0;
        if (obj instanceof String s) return s.length();
        if (obj instanceof java.util.Collection<?> c) return c.size();
        if (obj instanceof java.util.Map<?, ?> m) return m.size();
        if (obj.getClass().isArray()) {
            return java.lang.reflect.Array.getLength(obj);
        }
        return 1; // Single object has size 1
    }
    
    /**
     * Finds the best operator for comparing two specific types.
     */
    public static AssertionOperator suggestOperator(Object actual, Object expected) {
        if (actual == null || expected == null) {
            return EQUALS;
        }
        
        if (actual instanceof Number && expected instanceof Number) {
            return EQUALS; // Could also suggest GREATER_THAN, etc.
        }
        
        if (actual instanceof String && expected instanceof String) {
            return EQUALS; // Could also suggest CONTAINS, STARTS_WITH, etc.
        }
        
        if (actual instanceof java.util.Collection<?>) {
            return SIZE_EQUALS;
        }
        
        return EQUALS; // Default fallback
    }
    
    /**
     * Gets operators suitable for numeric comparisons.
     */
    public static AssertionOperator[] getNumericOperators() {
        return new AssertionOperator[]{
            EQUALS, NOT_EQUALS, GREATER_THAN, GREATER_THAN_OR_EQUAL, 
            LESS_THAN, LESS_THAN_OR_EQUAL, IN_RANGE
        };
    }
    
    /**
     * Gets operators suitable for string comparisons.
     */
    public static AssertionOperator[] getStringOperators() {
        return new AssertionOperator[]{
            EQUALS, NOT_EQUALS, CONTAINS, NOT_CONTAINS, STARTS_WITH, 
            ENDS_WITH, MATCHES_REGEX, HAS_LENGTH, IS_EMPTY, IS_NOT_EMPTY
        };
    }
    
    /**
     * Gets operators suitable for collection comparisons.
     */
    public static AssertionOperator[] getCollectionOperators() {
        return new AssertionOperator[]{
            EQUALS, NOT_EQUALS, CONTAINS, NOT_CONTAINS, 
            SIZE_EQUALS, IS_EMPTY, IS_NOT_EMPTY
        };
    }
}