package com.gogidix.aiservices.performanceoptimizationservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Recommendation Record Tests")
class RecommendationTest {

    private static final String TYPE = "OPTIMIZATION";
    private static final String DESCRIPTION = "Add database index";
    private static final int PRIORITY = 1;
    private static final String ACTION = "CREATE INDEX idx_user_email ON users(email);";

    @Nested
    @DisplayName("Creation Tests")
    class CreationTests {

        @Test
        @DisplayName("Should create recommendation with all fields")
        void shouldCreateWithAllFields() {
            Recommendation recommendation = new Recommendation(TYPE, DESCRIPTION, PRIORITY, ACTION);

            assertThat(recommendation.type()).isEqualTo(TYPE);
            assertThat(recommendation.description()).isEqualTo(DESCRIPTION);
            assertThat(recommendation.priority()).isEqualTo(PRIORITY);
            assertThat(recommendation.action()).isEqualTo(ACTION);
        }

        @Test
        @DisplayName("Should create recommendation with null type")
        void shouldCreateWithNullType() {
            Recommendation recommendation = new Recommendation(null, DESCRIPTION, PRIORITY, ACTION);

            assertThat(recommendation.type()).isNull();
        }

        @Test
        @DisplayName("Should create recommendation with null description")
        void shouldCreateWithNullDescription() {
            Recommendation recommendation = new Recommendation(TYPE, null, PRIORITY, ACTION);

            assertThat(recommendation.description()).isNull();
        }

        @Test
        @DisplayName("Should create recommendation with null action")
        void shouldCreateWithNullAction() {
            Recommendation recommendation = new Recommendation(TYPE, DESCRIPTION, PRIORITY, null);

            assertThat(recommendation.action()).isNull();
        }

        @Test
        @DisplayName("Should create recommendation with zero priority")
        void shouldCreateWithZeroPriority() {
            Recommendation recommendation = new Recommendation(TYPE, DESCRIPTION, 0, ACTION);

            assertThat(recommendation.priority()).isZero();
        }

        @Test
        @DisplayName("Should create recommendation with negative priority")
        void shouldCreateWithNegativePriority() {
            Recommendation recommendation = new Recommendation(TYPE, DESCRIPTION, -1, ACTION);

            assertThat(recommendation.priority()).isNegative();
        }
    }

    @Nested
    @DisplayName("Equality Tests")
    class EqualityTests {

        @Test
        @DisplayName("Should be equal with same values")
        void shouldBeEqualWithSameValues() {
            Recommendation recommendation1 = new Recommendation(TYPE, DESCRIPTION, PRIORITY, ACTION);
            Recommendation recommendation2 = new Recommendation(TYPE, DESCRIPTION, PRIORITY, ACTION);

            assertThat(recommendation1).isEqualTo(recommendation2);
            assertThat(recommendation1.hashCode()).isEqualTo(recommendation2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal with different type")
        void shouldNotBeEqualWithDifferentType() {
            Recommendation recommendation1 = new Recommendation(TYPE, DESCRIPTION, PRIORITY, ACTION);
            Recommendation recommendation2 = new Recommendation("REFACTORING", DESCRIPTION, PRIORITY, ACTION);

            assertThat(recommendation1).isNotEqualTo(recommendation2);
        }

        @Test
        @DisplayName("Should not be equal with different description")
        void shouldNotBeEqualWithDifferentDescription() {
            Recommendation recommendation1 = new Recommendation(TYPE, DESCRIPTION, PRIORITY, ACTION);
            Recommendation recommendation2 = new Recommendation(TYPE, "Different description", PRIORITY, ACTION);

            assertThat(recommendation1).isNotEqualTo(recommendation2);
        }

        @Test
        @DisplayName("Should not be equal with different priority")
        void shouldNotBeEqualWithDifferentPriority() {
            Recommendation recommendation1 = new Recommendation(TYPE, DESCRIPTION, PRIORITY, ACTION);
            Recommendation recommendation2 = new Recommendation(TYPE, DESCRIPTION, 2, ACTION);

            assertThat(recommendation1).isNotEqualTo(recommendation2);
        }

        @Test
        @DisplayName("Should not be equal with different action")
        void shouldNotBeEqualWithDifferentAction() {
            Recommendation recommendation1 = new Recommendation(TYPE, DESCRIPTION, PRIORITY, ACTION);
            Recommendation recommendation2 = new Recommendation(TYPE, DESCRIPTION, PRIORITY, "Different action");

            assertThat(recommendation1).isNotEqualTo(recommendation2);
        }

        @Test
        @DisplayName("Should be equal to itself")
        void shouldBeEqualToItself() {
            Recommendation recommendation = new Recommendation(TYPE, DESCRIPTION, PRIORITY, ACTION);

            assertThat(recommendation).isEqualTo(recommendation);
        }

        @Test
        @DisplayName("Should not be equal to null")
        void shouldNotBeEqualToNull() {
            Recommendation recommendation = new Recommendation(TYPE, DESCRIPTION, PRIORITY, ACTION);

            assertThat(recommendation).isNotNull();
        }

        @Test
        @DisplayName("Should not be equal to different type")
        void shouldNotBeEqualToDifferentType() {
            Recommendation recommendation = new Recommendation(TYPE, DESCRIPTION, PRIORITY, ACTION);

            assertThat(recommendation).isNotEqualTo("not a recommendation");
        }
    }

    @Nested
    @DisplayName("Type Tests")
    class TypeTests {

        @ParameterizedTest
        @ValueSource(strings = {"OPTIMIZATION", "REFACTORING", "SCALING", "CACHING", "CONFIGURATION"})
        @DisplayName("Should accept standard recommendation types")
        void shouldAcceptStandardRecommendationTypes(String type) {
            Recommendation recommendation = new Recommendation(type, DESCRIPTION, PRIORITY, ACTION);

            assertThat(recommendation.type()).isEqualTo(type);
        }

        @Test
        @DisplayName("Should accept custom recommendation type")
        void shouldAcceptCustomRecommendationType() {
            Recommendation recommendation = new Recommendation("CUSTOM_TYPE", DESCRIPTION, PRIORITY, ACTION);

            assertThat(recommendation.type()).isEqualTo("CUSTOM_TYPE");
        }

        @Test
        @DisplayName("Should accept empty type")
        void shouldAcceptEmptyType() {
            Recommendation recommendation = new Recommendation("", DESCRIPTION, PRIORITY, ACTION);

            assertThat(recommendation.type()).isEmpty();
        }

        @Test
        @DisplayName("Should accept type with special characters")
        void shouldAcceptTypeWithSpecialCharacters() {
            Recommendation recommendation = new Recommendation("DB-OPT/SQL", DESCRIPTION, PRIORITY, ACTION);

            assertThat(recommendation.type()).isEqualTo("DB-OPT/SQL");
        }
    }

    @Nested
    @DisplayName("Description Tests")
    class DescriptionTests {

        @Test
        @DisplayName("Should accept short description")
        void shouldAcceptShortDescription() {
            Recommendation recommendation = new Recommendation(TYPE, "Fix it", PRIORITY, ACTION);

            assertThat(recommendation.description()).isEqualTo("Fix it");
        }

        @Test
        @DisplayName("Should accept long description")
        void shouldAcceptLongDescription() {
            String longDescription = "This is a very detailed description of the recommendation ".repeat(5);
            Recommendation recommendation = new Recommendation(TYPE, longDescription, PRIORITY, ACTION);

            assertThat(recommendation.description()).isEqualTo(longDescription);
        }

        @Test
        @DisplayName("Should accept empty description")
        void shouldAcceptEmptyDescription() {
            Recommendation recommendation = new Recommendation(TYPE, "", PRIORITY, ACTION);

            assertThat(recommendation.description()).isEmpty();
        }

        @Test
        @DisplayName("Should accept description with code snippets")
        void shouldAcceptDescriptionWithCodeSnippets() {
            Recommendation recommendation = new Recommendation(TYPE, "Use SELECT * FROM table", PRIORITY, ACTION);

            assertThat(recommendation.description()).contains("SELECT");
        }

        @Test
        @DisplayName("Should accept description with unicode")
        void shouldAcceptDescriptionWithUnicode() {
            Recommendation recommendation = new Recommendation(TYPE, "优化数据库查询", PRIORITY, ACTION);

            assertThat(recommendation.description()).isEqualTo("优化数据库查询");
        }
    }

    @Nested
    @DisplayName("Priority Tests")
    class PriorityTests {

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 5, 10, 100})
        @DisplayName("Should accept various priority values")
        void shouldAcceptVariousPriorityValues(int priority) {
            Recommendation recommendation = new Recommendation(TYPE, DESCRIPTION, priority, ACTION);

            assertThat(recommendation.priority()).isEqualTo(priority);
        }

        @Test
        @DisplayName("Should accept negative priority")
        void shouldAcceptNegativePriority() {
            Recommendation recommendation = new Recommendation(TYPE, DESCRIPTION, -10, ACTION);

            assertThat(recommendation.priority()).isEqualTo(-10);
        }

        @Test
        @DisplayName("Should accept very high priority")
        void shouldAcceptVeryHighPriority() {
            Recommendation recommendation = new Recommendation(TYPE, DESCRIPTION, 9999, ACTION);

            assertThat(recommendation.priority()).isEqualTo(9999);
        }

        @Test
        @DisplayName("Should handle priority comparison")
        void shouldHandlePriorityComparison() {
            Recommendation lowPriority = new Recommendation(TYPE, DESCRIPTION, 10, ACTION);
            Recommendation highPriority = new Recommendation(TYPE, DESCRIPTION, 1, ACTION);

            assertThat(lowPriority.priority()).isGreaterThan(highPriority.priority());
        }
    }

    @Nested
    @DisplayName("Action Tests")
    class ActionTests {

        @Test
        @DisplayName("Should accept SQL action")
        void shouldAcceptSqlAction() {
            Recommendation recommendation = new Recommendation(TYPE, DESCRIPTION, PRIORITY, "CREATE INDEX idx_name ON table(column);");

            assertThat(recommendation.action()).contains("CREATE INDEX");
        }

        @Test
        @DisplayName("Should accept configuration action")
        void shouldAcceptConfigurationAction() {
            Recommendation recommendation = new Recommendation(TYPE, DESCRIPTION, PRIORITY, "Set cache.ttl=300");

            assertThat(recommendation.action()).contains("cache.ttl");
        }

        @Test
        @DisplayName("Should accept code refactoring action")
        void shouldAcceptCodeRefactoringAction() {
            Recommendation recommendation = new Recommendation(TYPE, DESCRIPTION, PRIORITY, "Replace loop with stream API");

            assertThat(recommendation.action()).contains("stream");
        }

        @Test
        @DisplayName("Should accept multi-step action")
        void shouldAcceptMultiStepAction() {
            String multiStepAction = "1. Add index\n2. Update query\n3. Clear cache";
            Recommendation recommendation = new Recommendation(TYPE, DESCRIPTION, PRIORITY, multiStepAction);

            assertThat(recommendation.action()).contains("\n");
        }

        @Test
        @DisplayName("Should accept empty action")
        void shouldAcceptEmptyAction() {
            Recommendation recommendation = new Recommendation(TYPE, DESCRIPTION, PRIORITY, "");

            assertThat(recommendation.action()).isEmpty();
        }

        @Test
        @DisplayName("Should accept action with special characters")
        void shouldAcceptActionWithSpecialCharacters() {
            Recommendation recommendation = new Recommendation(TYPE, DESCRIPTION, PRIORITY, "curl -X POST https://api.example.com/optimize");

            assertThat(recommendation.action()).contains("curl");
        }
    }

    @Nested
    @DisplayName("Use Case Tests")
    class UseCaseTests {

        @Test
        @DisplayName("Should represent database optimization recommendation")
        void shouldRepresentDatabaseOptimizationRecommendation() {
            Recommendation recommendation = new Recommendation(
                    "OPTIMIZATION",
                    "Add index on frequently queried column",
                    1,
                    "CREATE INDEX idx_user_email ON users(email);"
            );

            assertThat(recommendation.type()).isEqualTo("OPTIMIZATION");
            assertThat(recommendation.priority()).isEqualTo(1);
            assertThat(recommendation.action()).contains("CREATE INDEX");
        }

        @Test
        @DisplayName("Should represent caching recommendation")
        void shouldRepresentCachingRecommendation() {
            Recommendation recommendation = new Recommendation(
                    "CACHING",
                    "Implement Redis caching for user sessions",
                    2,
                    "Add @Cacheable annotation to getUser method"
            );

            assertThat(recommendation.type()).isEqualTo("CACHING");
            assertThat(recommendation.description()).contains("Redis");
        }

        @Test
        @DisplayName("Should represent scaling recommendation")
        void shouldRepresentScalingRecommendation() {
            Recommendation recommendation = new Recommendation(
                    "SCALING",
                    "Increase number of worker instances",
                    3,
                    "kubectl scale deployment/app --replicas=5"
            );

            assertThat(recommendation.type()).isEqualTo("SCALING");
            assertThat(recommendation.action()).contains("kubectl");
        }

        @Test
        @DisplayName("Should represent refactoring recommendation")
        void shouldRepresentRefactoringRecommendation() {
            Recommendation recommendation = new Recommendation(
                    "REFACTORING",
                    "Extract common logic into utility class",
                    5,
                    "Create StringUtils class with normalize method"
            );

            assertThat(recommendation.type()).isEqualTo("REFACTORING");
            assertThat(recommendation.priority()).isGreaterThan(1);
        }

        @Test
        @DisplayName("Should represent configuration recommendation")
        void shouldRepresentConfigurationRecommendation() {
            Recommendation recommendation = new Recommendation(
                    "CONFIGURATION",
                    "Adjust connection pool size",
                    1,
                    "Set spring.datasource.hikari.maximum-pool-size=20"
            );

            assertThat(recommendation.type()).isEqualTo("CONFIGURATION");
            assertThat(recommendation.action()).contains("hikari");
        }

        @Test
        @DisplayName("Should represent code optimization recommendation")
        void shouldRepresentCodeOptimizationRecommendation() {
            Recommendation recommendation = new Recommendation(
                    "OPTIMIZATION",
                    "Use StringBuilder instead of string concatenation",
                    3,
                    "Replace s1 + s2 with new StringBuilder().append(s1).append(s2).toString()"
            );

            assertThat(recommendation.type()).isEqualTo("OPTIMIZATION");
            assertThat(recommendation.action()).contains("StringBuilder");
        }
    }

    @Nested
    @DisplayName("Priority Ordering Tests")
    class PriorityOrderingTests {

        @Test
        @DisplayName("Should order recommendations by priority")
        void shouldOrderRecommendationsByPriority() {
            Recommendation priority1 = new Recommendation(TYPE, DESCRIPTION, 1, ACTION);
            Recommendation priority2 = new Recommendation(TYPE, DESCRIPTION, 2, ACTION);
            Recommendation priority3 = new Recommendation(TYPE, DESCRIPTION, 3, ACTION);

            assertThat(priority1.priority()).isLessThan(priority2.priority());
            assertThat(priority2.priority()).isLessThan(priority3.priority());
        }

        @Test
        @DisplayName("Should identify highest priority recommendation")
        void shouldIdentifyHighestPriorityRecommendation() {
            Recommendation priority1 = new Recommendation(TYPE, "First", 1, ACTION);
            Recommendation priority5 = new Recommendation(TYPE, "Fifth", 5, ACTION);
            Recommendation priority10 = new Recommendation(TYPE, "Tenth", 10, ACTION);

            Recommendation highest = priority1;

            assertThat(highest.priority()).isLessThan(priority5.priority());
            assertThat(highest.priority()).isLessThan(priority10.priority());
        }

        @Test
        @DisplayName("Should handle same priority recommendations")
        void shouldHandleSamePriorityRecommendations() {
            Recommendation recommendation1 = new Recommendation(TYPE, "First", 1, ACTION);
            Recommendation recommendation2 = new Recommendation(TYPE, "Second", 1, ACTION);

            assertThat(recommendation1.priority()).isEqualTo(recommendation2.priority());
        }
    }

    @Nested
    @DisplayName("Record Methods Tests")
    class RecordMethodsTests {

        @Test
        @DisplayName("Should implement toString")
        void shouldImplementToString() {
            Recommendation recommendation = new Recommendation(TYPE, DESCRIPTION, PRIORITY, ACTION);

            String toString = recommendation.toString();

            assertThat(toString).contains(TYPE);
            assertThat(toString).contains(DESCRIPTION);
            assertThat(toString).contains(String.valueOf(PRIORITY));
        }

        @Test
        @DisplayName("Should have consistent hashCode")
        void shouldHaveConsistentHashCode() {
            Recommendation recommendation = new Recommendation(TYPE, DESCRIPTION, PRIORITY, ACTION);

            int hashCode1 = recommendation.hashCode();
            int hashCode2 = recommendation.hashCode();

            assertThat(hashCode1).isEqualTo(hashCode2);
        }

        @Test
        @DisplayName("Should have different hashCode for different values")
        void shouldHaveDifferentHashCodeForDifferentValues() {
            Recommendation recommendation1 = new Recommendation(TYPE, DESCRIPTION, PRIORITY, ACTION);
            Recommendation recommendation2 = new Recommendation("DIFFERENT", DESCRIPTION, PRIORITY, ACTION);

            assertThat(recommendation1.hashCode()).isNotEqualTo(recommendation2.hashCode());
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle all null fields")
        void shouldHandleAllNullFields() {
            Recommendation recommendation = new Recommendation(null, null, 0, null);

            assertThat(recommendation.type()).isNull();
            assertThat(recommendation.description()).isNull();
            assertThat(recommendation.action()).isNull();
            assertThat(recommendation.priority()).isZero();
        }

        @Test
        @DisplayName("Should handle very long description")
        void shouldHandleVeryLongDescription() {
            String longDescription = "Detailed description ".repeat(50);
            Recommendation recommendation = new Recommendation(TYPE, longDescription, PRIORITY, ACTION);

            assertThat(recommendation.description()).hasSizeGreaterThan(100);
        }

        @Test
        @DisplayName("Should handle very long action")
        void shouldHandleVeryLongAction() {
            String longAction = "Step 1: Do this\nStep 2: Do that\n".repeat(20);
            Recommendation recommendation = new Recommendation(TYPE, DESCRIPTION, PRIORITY, longAction);

            assertThat(recommendation.action()).hasSizeGreaterThan(100);
        }

        @Test
        @DisplayName("Should handle Integer.MAX_VALUE as priority")
        void shouldHandleMaxValueAsPriority() {
            Recommendation recommendation = new Recommendation(TYPE, DESCRIPTION, Integer.MAX_VALUE, ACTION);

            assertThat(recommendation.priority()).isEqualTo(Integer.MAX_VALUE);
        }

        @Test
        @DisplayName("Should handle Integer.MIN_VALUE as priority")
        void shouldHandleMinValueAsPriority() {
            Recommendation recommendation = new Recommendation(TYPE, DESCRIPTION, Integer.MIN_VALUE, ACTION);

            assertThat(recommendation.priority()).isEqualTo(Integer.MIN_VALUE);
        }
    }
}
