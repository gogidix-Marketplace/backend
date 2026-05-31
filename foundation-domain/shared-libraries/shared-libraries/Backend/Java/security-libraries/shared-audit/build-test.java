/**
 * Simple build test to validate core classes
 */

// Test imports from main packages
import com.gogidix.infrastructure.sharedlibraries.sharedaudit.domain.*;
import com.gogidix.infrastructure.sharedlibraries.sharedaudit.application.*;
import com.gogidix.infrastructure.sharedlibraries.sharedaudit.api.dto.*;

public class BuildTest {
    public static void main(String[] args) {
        System.out.println("BUILD TEST: Testing core class instantiation");
        
        try {
            // Test domain classes
            AuditEvent.builder()
                .eventId("test-id")
                .eventType(AuditEventType.USER_AUTHENTICATION)
                .build();
            System.out.println("✅ AuditEvent class structure valid");
            
            // Test application service exists
            Class.forName("com.gogidix.infrastructure.sharedlibraries.sharedaudit.application.SharedAuditService");
            System.out.println("✅ SharedAuditService class found");
            
            // Test DTO classes
            Class.forName("com.gogidix.infrastructure.sharedlibraries.sharedaudit.api.dto.AuditEventDTO");
            System.out.println("✅ AuditEventDTO class found");
            
            System.out.println("🎯 BUILD TEST PASSED: Core project structure valid");
            
        } catch (Exception e) {
            System.err.println("❌ BUILD TEST FAILED: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}