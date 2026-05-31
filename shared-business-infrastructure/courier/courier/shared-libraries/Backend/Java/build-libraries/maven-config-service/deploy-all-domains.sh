#!/bin/bash
# Comprehensive Maven Configuration Deployment for All 15 Ecosystem Domains
# Ensures every Java service across the entire ecosystem gets optimized Maven settings

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
BASE_DIR="/mnt/c/Users/frich/Desktop/Gogidix-Technology/CLEAN-SOCIAL-ECOMMERCE-ECOSYSTEM"

echo "🚀 [Comprehensive Maven Config] Deploying to ALL 15 ecosystem domains..."
echo "📍 Base Directory: $BASE_DIR"
echo "🕐 Start Time: $(date)"

# Track deployment statistics
total_services=0
successful_deployments=0
failed_deployments=0

# Deploy function
deploy_maven_config() {
    local target_dir="$1"
    local service_name="$(basename "$target_dir")"
    
    if [[ -d "$target_dir" ]]; then
        # Deploy .mavenrc for Unix/Linux
        cp "$SCRIPT_DIR/.mavenrc" "$target_dir/.mavenrc" 2>/dev/null || true
        chmod +x "$target_dir/.mavenrc" 2>/dev/null || true
        
        # Deploy Windows version
        cp "$SCRIPT_DIR/mavenrc_pre.bat" "$target_dir/mavenrc_pre.bat" 2>/dev/null || true
        
        # Deploy local settings.xml
        mkdir -p "$target_dir/.mvn" 2>/dev/null || true
        cp "$SCRIPT_DIR/settings.xml" "$target_dir/.mvn/settings.xml" 2>/dev/null || true
        
        # Verify deployment
        if [[ -f "$target_dir/.mavenrc" && -f "$target_dir/.mvn/settings.xml" ]]; then
            echo "    ✅ $service_name"
            ((successful_deployments++))
            return 0
        else
            echo "    ❌ $service_name (verification failed)"
            ((failed_deployments++))
            return 1
        fi
    else
        echo "    ⚠️  $service_name (directory not found)"
        ((failed_deployments++))
        return 1
    fi
}

# Define all 15 domains
domains=(
    "ai-services"
    "centralized-dashboard"  
    "branding"
    "central-configuration"
    "courier-services"
    "executive-command-center"
    "gogidix-corporate-website"
    "haulage-logistics"
    "management-support"
    "mobile-applications"
    "shared-libraries"
    "social-commerce"
    "shared-infrastructure"
    "unified-marketplace"
    "warehousing"
)

# Process each domain
for domain in "${domains[@]}"; do
    domain_path="$BASE_DIR/$domain"
    
    if [[ -d "$domain_path" ]]; then
        echo ""
        echo "🔄 Processing Domain: $domain"
        echo "📁 Path: $domain_path"
        
        # Count services in this domain
        service_count=0
        for service_dir in "$domain_path"/*; do
            if [[ -d "$service_dir" ]]; then
                ((service_count++))
                ((total_services++))
            fi
        done
        
        echo "📊 Services found: $service_count"
        
        # Deploy to each service
        for service_dir in "$domain_path"/*; do
            if [[ -d "$service_dir" ]]; then
                deploy_maven_config "$service_dir"
            fi
        done
        
        echo "✅ Domain $domain completed"
    else
        echo ""
        echo "⚠️  Domain $domain not found at $domain_path"
    fi
done

# Final summary
echo ""
echo "📈 COMPREHENSIVE DEPLOYMENT SUMMARY"
echo "=================================="
echo "🔢 Total domains processed: ${#domains[@]}"
echo "🔢 Total services found: $total_services"
echo "✅ Successful deployments: $successful_deployments"
echo "❌ Failed deployments: $failed_deployments"
echo "📊 Success rate: $(( (successful_deployments * 100) / total_services ))%"
echo "🕐 End Time: $(date)"

if [[ $failed_deployments -eq 0 ]]; then
    echo ""
    echo "🎉 PERFECT DEPLOYMENT! All services configured successfully!"
    echo "🚀 Maven Configuration Service is now active across the entire ecosystem"
    echo "📋 Global settings: ~/.m2/settings.xml"
    echo "🏆 Enterprise-wide Maven optimization complete"
else
    echo ""
    echo "⚠️  Deployment completed with some issues. Check logs above for details."
fi

echo ""
echo "🏁 Comprehensive Maven Configuration deployment finished!"