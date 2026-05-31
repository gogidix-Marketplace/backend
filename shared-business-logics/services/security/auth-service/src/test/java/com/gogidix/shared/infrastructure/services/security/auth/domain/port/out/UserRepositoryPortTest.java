package com.gogidix.shared.infrastructure.services.security.auth.domain.port.out;

import com.gogidix.shared.infrastructure.services.security.auth.domain.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for UserRepositoryPort interface contract.
 * Tests the interface definition and expected behavior.
 */
@DisplayName("UserRepositoryPort Interface Tests")
class UserRepositoryPortTest {

    @Test
    @DisplayName("Should have save method in interface")
    void shouldHaveSaveMethodInInterface() {
        // Given & When & Then
        assertDoesNotThrow(() -> {
            UserRepositoryPort.class.getMethod("save", User.class);
        });
    }

    @Test
    @DisplayName("Should have findByIdAndTenantId method in interface")
    void shouldHaveFindByIdAndTenantIdMethodInInterface() {
        // Given & When & Then
        assertDoesNotThrow(() -> {
            UserRepositoryPort.class.getMethod("findByIdAndTenantId", String.class, String.class);
        });
    }

    @Test
    @DisplayName("Should have findByUsernameAndTenantId method in interface")
    void shouldHaveFindByUsernameAndTenantIdMethodInInterface() {
        // Given & When & Then
        assertDoesNotThrow(() -> {
            UserRepositoryPort.class.getMethod("findByUsernameAndTenantId", String.class, String.class);
        });
    }

    @Test
    @DisplayName("Should have findByEmailAndTenantId method in interface")
    void shouldHaveFindByEmailAndTenantIdMethodInInterface() {
        // Given & When & Then
        assertDoesNotThrow(() -> {
            UserRepositoryPort.class.getMethod("findByEmailAndTenantId", String.class, String.class);
        });
    }

    @Test
    @DisplayName("Should have existsByUsernameAndTenantId method in interface")
    void shouldHaveExistsByUsernameAndTenantIdMethodInInterface() {
        // Given & When & Then
        assertDoesNotThrow(() -> {
            UserRepositoryPort.class.getMethod("existsByUsernameAndTenantId", String.class, String.class);
        });
    }

    @Test
    @DisplayName("Should have existsByEmailAndTenantId method in interface")
    void shouldHaveExistsByEmailAndTenantIdMethodInInterface() {
        // Given & When & Then
        assertDoesNotThrow(() -> {
            UserRepositoryPort.class.getMethod("existsByEmailAndTenantId", String.class, String.class);
        });
    }

    @Test
    @DisplayName("Should have delete method in interface")
    void shouldHaveDeleteMethodInInterface() {
        // Given & When & Then
        assertDoesNotThrow(() -> {
            UserRepositoryPort.class.getMethod("delete", User.class);
        });
    }

    @Test
    @DisplayName("Should be an interface")
    void shouldBeAnInterface() {
        // Then
        assertTrue(UserRepositoryPort.class.isInterface());
    }

    @Test
    @DisplayName("Should have correct return type for save method")
    void shouldHaveCorrectReturnTypeForSaveMethod() throws NoSuchMethodException {
        // When
        Class<?> returnType = UserRepositoryPort.class.getMethod("save", User.class).getReturnType();

        // Then
        assertEquals(User.class, returnType);
    }

    @Test
    @DisplayName("Should have correct return type for findByIdAndTenantId method")
    void shouldHaveCorrectReturnTypeForFindByIdAndTenantIdMethod() throws NoSuchMethodException {
        // When
        Class<?> returnType = UserRepositoryPort.class.getMethod("findByIdAndTenantId", String.class, String.class).getReturnType();

        // Then
        assertEquals(Optional.class, returnType);
    }

    @Test
    @DisplayName("Should have correct return type for existsByUsernameAndTenantId method")
    void shouldHaveCorrectReturnTypeForExistsByUsernameAndTenantIdMethod() throws NoSuchMethodException {
        // When
        Class<?> returnType = UserRepositoryPort.class.getMethod("existsByUsernameAndTenantId", String.class, String.class).getReturnType();

        // Then
        assertEquals(boolean.class, returnType);
    }

    @Test
    @DisplayName("Should have correct return type for existsByEmailAndTenantId method")
    void shouldHaveCorrectReturnTypeForExistsByEmailAndTenantIdMethod() throws NoSuchMethodException {
        // When
        Class<?> returnType = UserRepositoryPort.class.getMethod("existsByEmailAndTenantId", String.class, String.class).getReturnType();

        // Then
        assertEquals(boolean.class, returnType);
    }

    @Test
    @DisplayName("Should have void return type for delete method")
    void shouldHaveVoidReturnTypeForDeleteMethod() throws NoSuchMethodException {
        // When
        Class<?> returnType = UserRepositoryPort.class.getMethod("delete", User.class).getReturnType();

        // Then
        assertEquals(void.class, returnType);
    }

    @Test
    @DisplayName("Should create mock implementation")
    void shouldCreateMockImplementation() {
        // Given & When
        UserRepositoryPort mockPort = new UserRepositoryPort() {
            @Override
            public User save(User user) {
                return user;
            }

            @Override
            public Optional<User> findByIdAndTenantId(String userId, String tenantId) {
                return Optional.empty();
            }

            @Override
            public Optional<User> findByUsernameAndTenantId(String username, String tenantId) {
                return Optional.empty();
            }

            @Override
            public Optional<User> findByEmailAndTenantId(String email, String tenantId) {
                return Optional.empty();
            }

            @Override
            public boolean existsByUsernameAndTenantId(String username, String tenantId) {
                return false;
            }

            @Override
            public boolean existsByEmailAndTenantId(String email, String tenantId) {
                return false;
            }

            @Override
            public void delete(User user) {
            }
        };

        // Then
        assertNotNull(mockPort);
        assertDoesNotThrow(() -> mockPort.delete(new User()));
    }

    @Test
    @DisplayName("Should verify interface is public")
    void shouldVerifyInterfaceIsPublic() {
        // Then
        assertTrue(java.lang.reflect.Modifier.isPublic(UserRepositoryPort.class.getModifiers()));
    }

    @Test
    @DisplayName("Should have exactly 7 methods")
    void shouldHaveExactlySevenMethods() {
        // When
        int methodCount = UserRepositoryPort.class.getDeclaredMethods().length;

        // Then
        assertEquals(7, methodCount, "UserRepositoryPort should have exactly 7 methods");
    }
}
