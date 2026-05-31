#!/bin/bash
# Recreate coo-operations-service from working ceo-strategy-service
SOURCE="ceo-strategy-service"
TARGET="coo-operations-service-fixed"
ENTITY="Operations"
ENTITY_LOWER="operations"
ENTITY_ORIGINAL="Strategy"
ENTITY_ORIGINAL_LOWER="strategy"

rm -rf "$TARGET" 2>/dev/null
cp -r "$SOURCE" "$TARGET"

# Rename directory
find "$TARGET/src" -type d -name "$ENTITY_ORIGINAL_LOWER" | while read dir; do
    newdir=$(echo "$dir" | sed "s/$ENTITY_ORIGINAL_LOWER/$ENTITY_LOWER/g")
    mkdir -p "$newdir"
    # Move files from old to new directory
    find "$dir" -maxdepth 1 -type f -exec mv {} "$newdir/" \;
    # Remove old directory if empty
    rmdir "$dir" 2>/dev/null
done

# Fix file contents
find "$TARGET/src" -name "*.java" -exec sed -i "
s/com\.gogidix\.management\.executive\.$ENTITY_ORIGINAL_LOWER\./com.gogidix.management.executive.$ENTITY_LOWER./g
s/\bclass $ENTITY_ORIGINAL\b/class $ENTITY/g
s/\binterface $ENTITY_ORIGINAL\b/interface $ENTITY/g
s/\b$ENTITY_ORIGINAL\b\./$ENTITY./g
s/\b$ENTITY_ORIGINAL_LOWER\Id\b/$ENTITY_LOWER Id/g
s/${ENTITY_ORIGINAL}Status/${ENTITY}Status/g
s/${ENTITY_ORIGINAL}Layout/${ENTITY}Layout/g
s/${ENTITY_ORIGINAL}HealthMetrics/${ENTITY}HealthMetrics/g
s/${ENTITY_ORIGINAL}Dto/${ENTITY}Dto/g
s/${ENTITY_ORIGINAL}Query/${ENTITY}Query/g
s/${ENTITY_ORIGINAL}Command/${ENTITY}Command/g
s/${ENTITY_ORIGINAL}Repository/${ENTITY}Repository/g
s/${ENTITY_ORIGINAL}CommandService/${ENTITY}CommandService/g
s/${ENTITY_ORIGINAL}QueryService/${ENTITY}QueryService/g
s/${ENTITY_ORIGINAL}DomainService/${ENTITY}DomainService/g
s/Create${ENTITY_ORIGINAL}/Create${ENTITY}/g
s/Update${ENTITY_ORIGINAL}/Update${ENTITY}/g
s/Delete${ENTITY_ORIGINAL}/Delete${ENTITY}/g
s/List${ENTITY_ORIGINAL}s/List${ENTITY}s/g
s/Get${ENTITY_ORIGINAL}/Get${ENTITY}/g
s/ceo-strategy-service/coo-operations-service/g
s/CEO Strategy Service/COO Operations Service/g
s/Ceo${ENTITY_ORIGINAL}ServiceApplication/Coo${ENTITY}ServiceApplication/g
" {} \;

# Rename files
find "$TARGET/src" -name "*${ENTITY_ORIGINAL}*.java" | while read file; do
    newfile=$(echo "$file" | sed "s/${ENTITY_ORIGINAL}/${ENTITY}/g")
    mv "$file" "$newfile"
done

# Fix pom.xml
sed -i "
s/ceo-strategy-service/coo-operations-service/g
s/CEO Strategy Service/COO Operations Service/g
" "$TARGET/pom.xml"

echo "Recreated $TARGET"
