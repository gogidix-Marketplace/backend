#!/bin/bash

# Template service
TEMPLATE="ceo-strategy-service"

# Services to recreate (source -> dest)
declare -A SERVICES=(
    ["coo-operations"]="coo-operations-service"
    ["financial"]="cfo-financial-consolidation-service"
    ["technology"]="cto-technology-oversight-service"
    ["alert"]="executive-alert-service"
    ["workflow"]="executive-approval-workflow-service"
)

for prefix in "${!SERVICES[@]}"; do
    target="${SERVICES[$prefix]}"
    echo "Recreating $target from $TEMPLATE..."
    
    # Backup and remove target
    if [ -d "$target" ]; then
        mv "$target" "${target}-backup-$(date +%s)"
    fi
    
    # Copy template
    cp -r "$TEMPLATE" "$target"
    
    # Update package structure
    find "$target/src" -type f -name "*.java" -exec sed -i "s/\.strategy\./\.${prefix}\./g" {} \;
    find "$target/src" -type f -name "*.java" -exec sed -i "s/Strategy/${prefix^}/g" {} \;
    find "$target/src" -type f -name "*.java" -exec sed -i "s/STRATEGY/${prefix^^}/g" {} \;
    
    # Rename directories
    mv "$target/src/main/java/com/gogidix/management/executive/strategy" \
       "$target/src/main/java/com/gogidix/management/executive/$prefix" 2>/dev/null || true
    
    # Update pom.xml
    sed -i "s/ceo-strategy-service/${target}/g" "$target/pom.xml"
    sed -i "s/CEO Strategy Service/${target^}/g" "$target/pom.xml"
    
    # Remove duplicated wrong directories if any
    rm -rf "$target/src/main/java/com/gogidix/management/executive/application" 2>/dev/null || true
    rm -rf "$target/src/main/java/com/gogidix/management/executive/domain" 2>/dev/null || true
    rm -rf "$target/src/main/java/com/gogidix/management/executive/infrastructure" 2>/dev/null || true
    rm -rf "$target/src/main/java/com/gogidix/management/executive/interfaces" 2>/dev/null || true
    
    echo "$target recreated!"
done

echo "All services recreated!"
