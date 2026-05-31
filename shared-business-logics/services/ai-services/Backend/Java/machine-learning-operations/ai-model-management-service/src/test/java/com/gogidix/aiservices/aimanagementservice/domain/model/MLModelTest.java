package com.gogidix.aiservices.aimanagementservice.domain.model;

import com.gogidix.aiservices.aimanagementservice.shared.exception.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for MLModel domain model.
 */
@DisplayName("MLModel Domain Model Tests")
class MLModelTest {

    private static final String TENANT_ID = "tenant-123";
    private static final String MODEL_NAME = "recommendation-model";
    private static final ModelFramework FRAMEWORK = ModelFramework.TENSORFLOW;
    private static final String VERSION = "1.0.0";
    private static final String ARTIFACT_URL = "s3://models/recommendation-v1.pkl";

    @Nested
    @DisplayName("Creation Tests")
    class CreationTests {

        @Test
        @DisplayName("Should create model with valid parameters")
        void shouldCreateModelWithValidParameters() {
            MLModel model = new MLModel(TENANT_ID, MODEL_NAME, FRAMEWORK, VERSION, ARTIFACT_URL);

            assertNotNull(model.getId());
            assertEquals(TENANT_ID, model.getTenantId());
            assertEquals(MODEL_NAME, model.getModelName());
            assertEquals(FRAMEWORK, model.getFramework());
            assertEquals(VERSION, model.getVersion());
            assertEquals(ARTIFACT_URL, model.getModelArtifactUrl());
            assertEquals(com.gogidix.aiservices.aimanagementservice.domain.model.ModelStatus.REGISTERED, model.getStatus());
            assertNotNull(model.getRegisteredAt());
        }

        @Test
        @DisplayName("Should throw exception when tenantId is null")
        void shouldThrowWhenTenantIdIsNull() {
            assertThrows(NullPointerException.class,
                    () -> new MLModel(null, MODEL_NAME, FRAMEWORK, VERSION, ARTIFACT_URL));
        }
    }

    @Nested
    @DisplayName("Deployment Tests")
    class DeploymentTests {

        @Test
        @DisplayName("Should deploy registered model")
        void shouldDeployRegisteredModel() {
            MLModel model = new MLModel(TENANT_ID, MODEL_NAME, FRAMEWORK, VERSION, ARTIFACT_URL);

            model.deploy();
            model.completeDeployment();

            assertEquals(com.gogidix.aiservices.aimanagementservice.domain.model.ModelStatus.DEPLOYED, model.getStatus());
            assertNotNull(model.getDeployedAt());
        }

        @Test
        @DisplayName("Should throw when deploying deployed model")
        void shouldThrowWhenDeployingDeployedModel() {
            MLModel model = new MLModel(TENANT_ID, MODEL_NAME, FRAMEWORK, VERSION, ARTIFACT_URL);
            model.deploy();
            model.completeDeployment();

            assertThrows(IllegalStateException.class, model::deploy);
        }

        @Test
        @DisplayName("Should undeploy deployed model")
        void shouldUndeployDeployedModel() {
            MLModel model = new MLModel(TENANT_ID, MODEL_NAME, FRAMEWORK, VERSION, ARTIFACT_URL);
            model.deploy();
            model.completeDeployment();

            model.undeploy();
            model.completeUndeployment();

            assertEquals(com.gogidix.aiservices.aimanagementservice.domain.model.ModelStatus.UNDEPLOYED, model.getStatus());
            assertNull(model.getDeployedAt());
        }
    }

    @Nested
    @DisplayName("Validation Tests")
    class ValidationTests {

        @Test
        @DisplayName("Should validate valid model")
        void shouldValidateValidModel() {
            MLModel model = new MLModel(TENANT_ID, MODEL_NAME, FRAMEWORK, VERSION, ARTIFACT_URL);

            assertDoesNotThrow(model::validate);
        }

        @Test
        @DisplayName("Should throw when tenantId is blank")
        void shouldThrowWhenTenantIdIsBlank() {
            MLModel model = new MLModel("  ", MODEL_NAME, FRAMEWORK, VERSION, ARTIFACT_URL);

            assertThrows(ValidationException.class, model::validate);
        }

        @Test
        @DisplayName("Should throw when modelName is blank")
        void shouldThrowWhenModelNameIsBlank() {
            MLModel model = new MLModel(TENANT_ID, "  ", FRAMEWORK, VERSION, ARTIFACT_URL);

            assertThrows(ValidationException.class, model::validate);
        }
    }
}
