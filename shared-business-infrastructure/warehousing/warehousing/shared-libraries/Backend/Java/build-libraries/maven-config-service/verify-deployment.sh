#!/bin/bash
# Quick verification script to check Maven Configuration Service deployment status

BASE_DIR="/mnt/c/Users/frich/Desktop/Gogidix-Technology/CLEAN-SOCIAL-ECOMMERCE-ECOSYSTEM"

echo "🔍 Maven Configuration Service Deployment Verification"
echo "====================================================="
echo ""

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

total_services=0
configured_services=0

for domain in "${domains[@]}"; do
    domain_path="$BASE_DIR/$domain"
    
    if [[ -d "$domain_path" ]]; then
        echo "📁 Domain: $domain"
        
        for service_dir in "$domain_path"/*; do
            if [[ -d "$service_dir" ]]; then
                service_name="$(basename "$service_dir")"
                ((total_services++))
                
                if [[ -f "$service_dir/.mavenrc" && -f "$service_dir/.mvn/settings.xml" ]]; then
                    echo "  ✅ $service_name (configured)"
                    ((configured_services++))
                else
                    echo "  ⚠️  $service_name (not configured)"
                fi
            fi
        done
    fi
done

echo ""
echo "📊 VERIFICATION SUMMARY"
echo "======================="
echo "🔢 Total services: $total_services"
echo "✅ Configured services: $configured_services"
echo "⚠️  Pending services: $((total_services - configured_services))"
echo "📈 Configuration rate: $(( (configured_services * 100) / total_services ))%"

if [[ $configured_services -eq $total_services ]]; then
    echo ""
    echo "🎉 ALL SERVICES CONFIGURED! Maven optimization is ecosystem-wide!"
else
    echo ""
    echo "🚧 Deployment in progress or incomplete. Run deploy-all-domains.sh if needed."
fi