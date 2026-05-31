#!/bin/bash
# Automated Maven Configuration Deployment Script
# Deploys optimized Maven settings to all shared-infrastructure services

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
BASE_DIR="/mnt/c/Users/frich/Desktop/Gogidix-Technology/CLEAN-SOCIAL-ECOMMERCE-ECOSYSTEM"
SHARED_INFRA_DIR="$BASE_DIR/shared-infrastructure"

echo "🚀 [Maven Config Service] Starting deployment to all services..."
echo "📍 Base Directory: $BASE_DIR"
echo "📁 Shared Infrastructure: $SHARED_INFRA_DIR"

# Verify source files exist
if [[ ! -f "$SCRIPT_DIR/.mavenrc" ]]; then
    echo "❌ ERROR: .mavenrc not found in $SCRIPT_DIR"
    exit 1
fi

if [[ ! -f "$SCRIPT_DIR/settings.xml" ]]; then
    echo "❌ ERROR: settings.xml not found in $SCRIPT_DIR"
    exit 1
fi

# Create global Maven directory if it doesn't exist
MAVEN_HOME_DIR="$HOME/.m2"
mkdir -p "$MAVEN_HOME_DIR"

# Deploy global settings.xml
echo "📋 Deploying global Maven settings..."
cp "$SCRIPT_DIR/settings.xml" "$MAVEN_HOME_DIR/settings.xml"
echo "✅ Global settings.xml deployed to $MAVEN_HOME_DIR/settings.xml"

# Function to deploy to a service directory
deploy_to_service() {
    local service_dir="$1"
    local service_name="$(basename "$service_dir")"
    
    if [[ -d "$service_dir" ]]; then
        echo "📦 Processing: $service_name"
        
        # Deploy .mavenrc
        cp "$SCRIPT_DIR/.mavenrc" "$service_dir/.mavenrc"
        chmod +x "$service_dir/.mavenrc"
        
        # Deploy local settings.xml
        mkdir -p "$service_dir/.mvn"
        cp "$SCRIPT_DIR/settings.xml" "$service_dir/.mvn/settings.xml"
        
        echo "  ✅ $service_name: Maven configuration deployed"
        
        # Verify deployment
        if [[ -f "$service_dir/.mavenrc" && -f "$service_dir/.mvn/settings.xml" ]]; then
            echo "  ✅ $service_name: Verification passed"
            return 0
        else
            echo "  ❌ $service_name: Verification failed"
            return 1
        fi
    else
        echo "  ⚠️  $service_name: Directory not found, skipping"
        return 1
    fi
}

# Deploy to all services in shared-infrastructure
successful_deployments=0
failed_deployments=0

echo ""
echo "🔄 Deploying to shared-infrastructure services..."

for service_dir in "$SHARED_INFRA_DIR"/*; do
    if [[ -d "$service_dir" && "$(basename "$service_dir")" != "maven-config-service" ]]; then
        if deploy_to_service "$service_dir"; then
            ((successful_deployments++))
        else
            ((failed_deployments++))
        fi
    fi
done

# Deploy to all ecosystem domains
for domain in "ai-services" "centralized-dashboard" "branding" "central-configuration" "courier-services" "executive-command-center" "gogidix-corporate-website" "haulage-logistics" "management-support" "mobile-applications" "shared-libraries" "social-commerce" "unified-marketplace" "warehousing"; do
    domain_dir="$BASE_DIR/$domain"
    if [[ -d "$domain_dir" ]]; then
        echo ""
        echo "🔄 Deploying to $domain services..."
        
        for service_dir in "$domain_dir"/*; do
            if [[ -d "$service_dir" ]]; then
                if deploy_to_service "$service_dir"; then
                    ((successful_deployments++))
                else
                    ((failed_deployments++))
                fi
            fi
        done
    else
        echo ""
        echo "⚠️  Domain $domain not found at $domain_dir"
    fi
done

# Performance benchmarking setup
echo ""
echo "📊 Setting up performance benchmarking..."
cat > "$SCRIPT_DIR/benchmark-build.sh" << 'EOF'
#!/bin/bash
# Maven Build Performance Benchmark Script

SERVICE_DIR="$1"
if [[ -z "$SERVICE_DIR" || ! -d "$SERVICE_DIR" ]]; then
    echo "Usage: $0 <service-directory>"
    exit 1
fi

cd "$SERVICE_DIR"
SERVICE_NAME="$(basename "$SERVICE_DIR")"

echo "🚀 Benchmarking Maven build for: $SERVICE_NAME"
echo "📍 Directory: $SERVICE_DIR"
echo "⏰ Start time: $(date)"

# Record start time
START_TIME=$(date +%s)

# Run Maven build with time tracking
if timeout 600s mvn clean compile -q --batch-mode; then
    END_TIME=$(date +%s)
    DURATION=$((END_TIME - START_TIME))
    
    echo "✅ Build completed successfully"
    echo "⏱️  Total duration: ${DURATION} seconds"
    echo "📊 Performance: $(( 600 - DURATION )) seconds saved from timeout"
else
    END_TIME=$(date +%s)
    DURATION=$((END_TIME - START_TIME))
    
    echo "❌ Build failed or timed out"
    echo "⏱️  Duration before failure: ${DURATION} seconds"
fi

echo "🏁 End time: $(date)"
EOF

chmod +x "$SCRIPT_DIR/benchmark-build.sh"

# Summary report
echo ""
echo "📈 DEPLOYMENT SUMMARY"
echo "=================="
echo "✅ Successful deployments: $successful_deployments"
echo "❌ Failed deployments: $failed_deployments"
echo "📊 Total services processed: $((successful_deployments + failed_deployments))"

if [[ $failed_deployments -eq 0 ]]; then
    echo ""
    echo "🎉 All deployments successful!"
    echo "🚀 Maven configuration service is now active across all services"
    echo "📋 Global settings: $MAVEN_HOME_DIR/settings.xml"
    echo "🔧 Benchmark tool: $SCRIPT_DIR/benchmark-build.sh"
else
    echo ""
    echo "⚠️  Some deployments failed. Please check the logs above."
fi

echo ""
echo "🏁 Maven Configuration Service deployment completed!"