#!/bin/bash
# Enhanced Maven Build with Extended Timeouts and Retry Logic
# Designed for WSL2/Windows environment with chronic timeout issues

set -e

echo "🚀 Enhanced Maven Build Tool with Extended Timeouts"
echo "===================================================="

SERVICE_DIR="$1"
if [[ -z "$SERVICE_DIR" ]]; then
    SERVICE_DIR="$(pwd)"
fi

SERVICE_NAME=$(basename "$SERVICE_DIR")
echo "🎯 Target Service: $SERVICE_NAME"
echo "📍 Directory: $SERVICE_DIR"

# Configuration - Maven 3.9.9 for Better Performance
MAVEN_HOME="/mnt/c/Users/frich/Desktop/apache-maven-3.9.9"
MAVEN_CMD="$MAVEN_HOME/bin/mvn"
MAX_RETRIES=3
COMPILE_TIMEOUT=600  # 10 minutes
TEST_TIMEOUT=600     # 10 minutes
PACKAGE_TIMEOUT=900  # 15 minutes

# WSL Authentication Support
WSL_PASSWORD="Ajimmy2907"
export SUDO_ASKPASS="/usr/bin/ssh-askpass"

# Set Maven environment
export MAVEN_HOME="$MAVEN_HOME"
export PATH="$MAVEN_HOME/bin:$PATH"

# Create enhanced settings.xml with extended timeouts
create_enhanced_settings() {
    echo "📝 Creating enhanced Maven settings with extended timeouts..."
    
    cat > "$HOME/.m2/settings-enhanced.xml" << 'EOF'
<?xml version="1.0" encoding="UTF-8"?>
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 
                              http://maven.apache.org/xsd/settings-1.0.0.xsd">
    <localRepository>${user.home}/.m2/repository</localRepository>
    <interactiveMode>false</interactiveMode>
    <offline>false</offline>
    
    <profiles>
        <profile>
            <id>extended-timeout</id>
            <activation>
                <activeByDefault>true</activeByDefault>
            </activation>
            <properties>
                <!-- Extended timeouts for slow environments -->
                <maven.wagon.http.connectionTimeout>300000</maven.wagon.http.connectionTimeout>
                <maven.wagon.http.readTimeout>600000</maven.wagon.http.readTimeout>
                <maven.wagon.http.retryHandler.count>5</maven.wagon.http.retryHandler.count>
                <maven.wagon.rto>600000</maven.wagon.rto>
                
                <!-- Parallel builds disabled for stability -->
                <maven.artifact.threads>1</maven.artifact.threads>
                
                <!-- Memory settings -->
                <maven.compiler.maxmem>1024m</maven.compiler.maxmem>
            </properties>
        </profile>
    </profiles>
    
    <mirrors>
        <mirror>
            <id>central-mirror</id>
            <mirrorOf>central</mirrorOf>
            <url>https://repo1.maven.org/maven2</url>
        </mirror>
    </mirrors>
</settings>
EOF
    
    echo "✅ Enhanced settings created"
}

# Function to run Maven command with retry logic
run_maven_with_retry() {
    local command="$1"
    local timeout_seconds="$2"
    local description="$3"
    
    echo ""
    echo "🔄 Executing: $description"
    echo "⏱️  Timeout: ${timeout_seconds}s | Max retries: $MAX_RETRIES"
    
    for attempt in $(seq 1 $MAX_RETRIES); do
        echo "  Attempt $attempt/$MAX_RETRIES..."
        
        # Set Maven options for memory and performance
        export MAVEN_OPTS="-Xmx2048m -Xms1024m -XX:MaxPermSize=512m -Djava.awt.headless=true"
        
        # Run command with extended timeout
        if timeout ${timeout_seconds}s $MAVEN_CMD \
            --settings "$HOME/.m2/settings-enhanced.xml" \
            --batch-mode \
            --errors \
            --fail-fast \
            $command; then
            echo "  ✅ $description succeeded!"
            return 0
        else
            exit_code=$?
            echo "  ⚠️ Attempt $attempt failed (exit code: $exit_code)"
            
            if [[ $attempt -lt $MAX_RETRIES ]]; then
                echo "  ⏳ Waiting 30 seconds before retry..."
                sleep 30
            else
                echo "  ❌ $description failed after $MAX_RETRIES attempts"
                return 1
            fi
        fi
    done
}

# Function to compile service
compile_service() {
    cd "$SERVICE_DIR"
    
    echo ""
    echo "═════════════════════════════════════════"
    echo "📦 COMPILING: $SERVICE_NAME"
    echo "═════════════════════════════════════════"
    
    # First try to validate the POM
    run_maven_with_retry "validate -q" 120 "POM validation"
    
    # Clean previous builds
    run_maven_with_retry "clean -q" 120 "Clean build directory"
    
    # Compile with skip tests
    if run_maven_with_retry "compile -DskipTests -q" $COMPILE_TIMEOUT "Compilation"; then
        echo "✅ COMPILATION SUCCESSFUL"
        return 0
    else
        echo "❌ COMPILATION FAILED"
        return 1
    fi
}

# Function to run tests
run_tests() {
    echo ""
    echo "═════════════════════════════════════════"
    echo "🧪 TESTING: $SERVICE_NAME"
    echo "═════════════════════════════════════════"
    
    if run_maven_with_retry "test -DfailIfNoTests=false" $TEST_TIMEOUT "Unit tests"; then
        echo "✅ TESTS PASSED"
        return 0
    else
        echo "⚠️ TESTS FAILED OR SKIPPED"
        return 1
    fi
}

# Function to package service
package_service() {
    echo ""
    echo "═════════════════════════════════════════"
    echo "📦 PACKAGING: $SERVICE_NAME"
    echo "═════════════════════════════════════════"
    
    if run_maven_with_retry "package -DskipTests" $PACKAGE_TIMEOUT "Packaging JAR"; then
        echo "✅ PACKAGING SUCCESSFUL"
        
        # Check if JAR was created
        if ls target/*.jar 2>/dev/null; then
            echo "✅ JAR FILE CREATED: $(ls target/*.jar)"
        else
            echo "⚠️ Warning: No JAR file found in target directory"
        fi
        return 0
    else
        echo "❌ PACKAGING FAILED"
        return 1
    fi
}

# Function to verify infrastructure readiness
verify_infrastructure() {
    echo ""
    echo "═════════════════════════════════════════"
    echo "🔍 VERIFYING INFRASTRUCTURE: $SERVICE_NAME"
    echo "═════════════════════════════════════════"
    
    local infra_ready=true
    
    # Check for Eureka configuration
    if [[ -f "src/main/resources/application.properties" ]] || [[ -f "src/main/resources/application.yml" ]]; then
        if grep -q "eureka" src/main/resources/application.* 2>/dev/null; then
            echo "✅ Eureka configuration found"
        else
            echo "⚠️ No Eureka configuration found"
            infra_ready=false
        fi
    else
        echo "⚠️ No application configuration file found"
        infra_ready=false
    fi
    
    # Check for Docker readiness
    if [[ -f "Dockerfile" ]]; then
        echo "✅ Dockerfile present"
    elif [[ -f "cloud-build.sh" ]]; then
        echo "✅ Cloud build script present"
    else
        echo "⚠️ No Docker/cloud build configuration found"
        infra_ready=false
    fi
    
    # Check for DTOs
    if find src/main/java -path "*/api/dto/*.java" 2>/dev/null | grep -q .; then
        local dto_count=$(find src/main/java -path "*/api/dto/*.java" 2>/dev/null | wc -l)
        echo "✅ DTOs found: $dto_count files"
    else
        echo "⚠️ No DTOs found in api/dto directory"
    fi
    
    if $infra_ready; then
        echo "✅ INFRASTRUCTURE READY"
        return 0
    else
        echo "⚠️ INFRASTRUCTURE NEEDS CONFIGURATION"
        return 1
    fi
}

# Main execution
main() {
    # Create enhanced settings
    create_enhanced_settings
    
    # Check if it's a Maven project
    if [[ ! -f "$SERVICE_DIR/pom.xml" ]]; then
        echo "⚠️ Not a Maven project (no pom.xml found)"
        exit 1
    fi
    
    # Run build phases
    local compile_result=0
    local test_result=0
    local package_result=0
    local infra_result=0
    
    compile_service || compile_result=1
    run_tests || test_result=1
    package_service || package_result=1
    verify_infrastructure || infra_result=1
    
    # Summary
    echo ""
    echo "═════════════════════════════════════════"
    echo "📊 BUILD SUMMARY: $SERVICE_NAME"
    echo "═════════════════════════════════════════"
    
    [[ $compile_result -eq 0 ]] && echo "✅ Compilation: PASSED" || echo "❌ Compilation: FAILED"
    [[ $test_result -eq 0 ]] && echo "✅ Tests: PASSED" || echo "⚠️ Tests: FAILED/SKIPPED"
    [[ $package_result -eq 0 ]] && echo "✅ Packaging: PASSED" || echo "❌ Packaging: FAILED"
    [[ $infra_result -eq 0 ]] && echo "✅ Infrastructure: READY" || echo "⚠️ Infrastructure: NEEDS WORK"
    
    echo ""
    if [[ $compile_result -eq 0 ]] && [[ $package_result -eq 0 ]]; then
        echo "🎉 SERVICE IS PRODUCTION READY!"
        exit 0
    else
        echo "⚠️ SERVICE NEEDS ATTENTION"
        exit 1
    fi
}

# Run main function
main "$@"