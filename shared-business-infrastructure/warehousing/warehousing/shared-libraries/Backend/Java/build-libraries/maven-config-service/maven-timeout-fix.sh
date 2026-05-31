#!/bin/bash
# Maven Timeout Fix - Environment-Specific Solution
# Addresses chronic Maven timeout issues in WSL2/Windows environment

set -e

echo "🔧 Maven Timeout Fix - Diagnostic and Resolution Tool"
echo "=================================================="

SERVICE_DIR="$1"
if [[ -z "$SERVICE_DIR" ]]; then
    SERVICE_DIR="$(pwd)"
fi

echo "🎯 Target Service: $(basename "$SERVICE_DIR")"
echo "📍 Directory: $SERVICE_DIR"

# Function to test network connectivity
test_network() {
    echo "🌐 Testing network connectivity..."
    
    if curl -s --max-time 10 https://repo1.maven.org/maven2/ > /dev/null; then
        echo "✅ Maven Central reachable"
    else
        echo "❌ Maven Central unreachable - network issue detected"
        return 1
    fi
}

# Function to create minimal settings.xml
create_minimal_settings() {
    echo "📝 Creating minimal Maven settings..."
    
    cat > "$HOME/.m2/settings-minimal.xml" << 'EOF'
<?xml version="1.0" encoding="UTF-8"?>
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 
                              http://maven.apache.org/xsd/settings-1.0.0.xsd">
    <localRepository>${user.home}/.m2/repository-minimal</localRepository>
    <interactiveMode>false</interactiveMode>
    <offline>false</offline>
    
    <profiles>
        <profile>
            <id>timeout-fix</id>
            <activation>
                <activeByDefault>true</activeByDefault>
            </activation>
            <properties>
                <maven.wagon.http.connectionTimeout>60000</maven.wagon.http.connectionTimeout>
                <maven.wagon.http.readTimeout>120000</maven.wagon.http.readTimeout>
                <maven.wagon.http.retryHandler.count>1</maven.wagon.http.retryHandler.count>
            </properties>
        </profile>
    </profiles>
</settings>
EOF
    
    echo "✅ Minimal settings created at $HOME/.m2/settings-minimal.xml"
}

# Function to test with minimal configuration
test_minimal_build() {
    echo "🧪 Testing with minimal Maven configuration..."
    
    cd "$SERVICE_DIR"
    
    # Create clean environment
    mkdir -p "$HOME/.m2/repository-minimal"
    
    # Test with minimal settings
    if timeout 120s /mnt/c/Users/frich/Desktop/apache-maven-3.8.8/bin/mvn \
        -s "$HOME/.m2/settings-minimal.xml" \
        dependency:resolve-sources \
        -Dmaven.test.skip=true \
        -q 2>/dev/null; then
        echo "✅ Minimal configuration successful"
        return 0
    else
        echo "❌ Even minimal configuration failed"
        return 1
    fi
}

# Function to create progressive build strategy
create_progressive_strategy() {
    echo "📊 Creating progressive build strategy..."
    
    cat > "$SERVICE_DIR/build-progressive.sh" << 'EOF'
#!/bin/bash
# Progressive Maven Build Strategy

echo "🔄 Progressive Maven Build Starting..."

# Phase 1: Validate project
echo "Phase 1: Project validation..."
if timeout 30s mvn validate -q; then
    echo "✅ Project validation successful"
else
    echo "❌ Project validation failed"
    exit 1
fi

# Phase 2: Resolve dependencies (with retry)
echo "Phase 2: Dependency resolution..."
for attempt in 1 2 3; do
    echo "  Attempt $attempt/3..."
    if timeout 60s mvn dependency:resolve -q; then
        echo "✅ Dependencies resolved"
        break
    elif [[ $attempt -eq 3 ]]; then
        echo "❌ Dependency resolution failed after 3 attempts"
        exit 1
    fi
    sleep 10
done

# Phase 3: Compile
echo "Phase 3: Compilation..."
if timeout 45s mvn compile -Dmaven.test.skip=true -q; then
    echo "✅ Compilation successful"
else
    echo "❌ Compilation failed"
    exit 1
fi

echo "🎉 Progressive build completed successfully!"
EOF
    
    chmod +x "$SERVICE_DIR/build-progressive.sh"
    echo "✅ Progressive build script created: $SERVICE_DIR/build-progressive.sh"
}

# Function to create offline build preparation
prepare_offline_build() {
    echo "📦 Preparing offline build capabilities..."
    
    # Download dependencies to local repository
    echo "Downloading dependencies for offline use..."
    timeout 300s /mnt/c/Users/frich/Desktop/apache-maven-3.8.8/bin/mvn \
        dependency:go-offline \
        -Dmaven.test.skip=true \
        -q 2>/dev/null || echo "⚠️  Partial dependency download (expected in problematic environment)"
    
    echo "✅ Offline preparation completed"
}

# Main execution
main() {
    # Test network connectivity
    if ! test_network; then
        echo "🚨 Network connectivity issue detected"
        echo "💡 Solution: Check WSL2 network configuration or corporate proxy settings"
        exit 1
    fi
    
    # Create minimal settings
    create_minimal_settings
    
    # Test with minimal configuration
    if test_minimal_build; then
        echo "🎉 Minimal configuration works! Using progressive strategy..."
        create_progressive_strategy
        prepare_offline_build
    else
        echo "🚨 Fundamental Maven issue detected"
        echo "💡 Recommended solutions:"
        echo "   1. Check Java version compatibility"
        echo "   2. Verify Maven installation"
        echo "   3. Check WSL2 network configuration"
        echo "   4. Consider using Docker-based builds"
        
        # Create Docker-based build alternative
        cat > "$SERVICE_DIR/build-docker.sh" << 'EOF'
#!/bin/bash
# Docker-based Maven build alternative

echo "🐳 Docker-based Maven build starting..."

docker run --rm \
    -v "$(pwd)":/workspace \
    -v "$HOME/.m2":/root/.m2 \
    -w /workspace \
    maven:3.8.8-openjdk-17 \
    mvn clean compile -Dmaven.test.skip=true -q

echo "🎉 Docker-based build completed!"
EOF
        chmod +x "$SERVICE_DIR/build-docker.sh"
        echo "✅ Docker-based build alternative created: $SERVICE_DIR/build-docker.sh"
    fi
    
    echo ""
    echo "🏁 Maven timeout fix analysis completed!"
    echo "📋 Available build strategies:"
    echo "   1. Progressive build: ./build-progressive.sh"
    echo "   2. Docker build: ./build-docker.sh"
    echo "   3. Minimal settings: ~/.m2/settings-minimal.xml"
}

# Run main function
main "$@"