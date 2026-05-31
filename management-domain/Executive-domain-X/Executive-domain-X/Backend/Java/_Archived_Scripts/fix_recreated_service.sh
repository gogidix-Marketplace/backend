#!/bin/bash

fix_service() {
    SVC_DIR=$1
    PKG=$2      # operations, financial, etc.
    CLASS=$3    # Operations, FinancialData, etc.
    COLLECTION=$4

    echo "Fixing $SVC_DIR..."
    
    cd "$SVC_DIR"
    
    # Rename all Strategy* files to appropriate names
    find src -name "*Strategy*.java" | while read file; do
        dir=$(dirname "$file")
        base=$(basename "$file")
        # Replace Strategy with CLASS in filename
        newbase=$(echo "$base" | sed "s/Strategy/$CLASS/g")
        if [[ "$base" != "$newbase" ]]; then
            mv "$file" "$dir/$newbase"
        fi
    done
    
    # Rename StrategyId files
    find src -name "*StrategyId*.java" | while read file; do
        dir=$(dirname "$file")
        base=$(basename "$file")
        newbase=$(echo "$base" | sed "s/StrategyId/${CLASS}Id/g")
        if [[ "$base" != "$newbase" ]]; then
            mv "$file" "$dir/$newbase"
        fi
    done
    
    # Fix class names inside files that still reference Strategy
    find src -name "*.java" -exec sed -i "s/StrategyQueryService/${CLASS}QueryService/g" {} \;
    find src -name "*.java" -exec sed -i "s/StrategyCommandService/${CLASS}CommandService/g" {} \;
    find src -name "*.java" -exec sed -i "s/StrategyRepository/${CLASS}Repository/g" {} \;
    find src -name "*.java" -exec sed -i "s/StrategyController/${CLASS}Controller/g" {} \;
    find src -name "*.java" -exec sed -i "s/StrategyDomainService/${CLASS}DomainService/g" {} \;
    find src -name "*.java" -exec sed -i "s/StrategyApplicationService/${CLASS}ApplicationService/g" {} \;
    find src -name "*.java" -exec sed -i "s/StrategyUserDetailsService/${CLASS}UserDetailsService/g" {} \;
    find src -name "*.java" -exec sed -i "s/MongoStrategyRepository/Mongo${CLASS}Repository/g" {} \;
    find src -name "*.java" -exec sed -i "s/StrategyMapper/${CLASS}Mapper/g" {} \;
    find src -name "*.java" -exec sed -i "s/CreateStrategyCommand/Create${CLASS}Command/g" {} \;
    find src -name "*.java" -exec sed -i "s/UpdateStrategyCommand/Update${CLASS}Command/g" {} \;
    find src -name "*.java" -exec sed -i "s/DeleteStrategyCommand/Delete${CLASS}Command/g" {} \;
    find src -name "*.java" -exec sed -i "s/StrategyDto/${CLASS}Dto/g" {} \;
    find src -name "*.java" -exec sed -i "s/StrategyAggregateDto/${CLASS}AggregateDto/g" {} \;
    find src -name "*.java" -exec sed -i "s/CreateStrategyRequest/Create${CLASS}Request/g" {} \;
    find src -name "*.java" -exec sed -i "s/GetStrategyQuery/Get${CLASS}Query/g" {} \;
    find src -name "*.java" -exec sed -i "s/ListStrategyQuery/List${CLASS}Query/g" {} \;
    
    # Fix main application class
    find src -name "*CeoStrategyServiceApplication.java" -o -name "*Application.java" | while read file; do
        if [[ "$file" == *"CeoStrategyServiceApplication"* ]]; then
            dir=$(dirname "$file")
            base=$(basename "$file")
            newbase="${CLASS}ServiceApplication.java"
            mv "$file" "$dir/$newbase"
        fi
    done
    
    find src -name "*.java" -exec sed -i "s/CeoStrategyServiceApplication/${CLASS}ServiceApplication/g" {} \;
    
    cd - > /dev/null
}

# Fix all services
cd "C:/Users/TEMP.LAPTOP-1QDBFFCA/Desktop/Gogidix-ecosystem/x-gogidix-domain/Management-domain/Executive-domain-X/Backend/Java"

fix_service "coo-operations-service" "operations" "Operations" "coo-operations-service"
fix_service "cfo-financial-consolidation-service" "financial" "FinancialData" "cfo-financial-consolidation-service"
fix_service "cto-technology-oversight-service" "technology" "Technology" "cto-technology-oversight-service"
fix_service "executive-alert-service" "alert" "Alert" "executive-alert-service"
fix_service "executive-approval-workflow-service" "workflow" "Workflow" "executive-approval-workflow-service"

echo "All services fixed!"
