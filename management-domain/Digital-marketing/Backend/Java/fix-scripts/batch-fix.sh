#!/bin/bash
# Batch Fix Script - Apply all fixes to multiple services
# Usage: batch-fix.sh service1 service2 service3 ...

BASE_DIR="C:/Users/TEMP.LAPTOP-1QDBFFCA/Desktop/Gogidix-ecosystem/x-gogidix-domain/Management-domain/Digital-marketing/Backend/Java"
SCRIPTS="$BASE_DIR/fix-scripts"
MAVEN_PATH="C:/Users/TEMP.LAPTOP-1QDBFFCA/Desktop/apache-maven-3.9.12/bin/mvn"

SERVICES=("$@")

echo "========================================="
echo "BATCH FIX: ${#SERVICES[@]} services"
echo "========================================="

for SERVICE in "${SERVICES[@]}"; do
    SERVICE_DIR="$BASE_DIR/$SERVICE"

    if [ ! -d "$SERVICE_DIR" ]; then
        echo "⚠️  SKIP: $SERVICE (directory not found)"
        continue
    fi

    echo ""
    echo "-----------------------------------------"
    echo "FIXING: $SERVICE"
    echo "-----------------------------------------"

    # Apply all fix scripts
    bash "$SCRIPTS/01-create-pom.sh" "$SERVICE_DIR"
    bash "$SCRIPTS/09-create-application.sh" "$SERVICE_DIR"
    bash "$SCRIPTS/08-fix-security.sh" "$SERVICE_DIR"
    bash "$SCRIPTS/03-copy-shared.sh" "$SERVICE_DIR"
    bash "$SCRIPTS/02-fix-imports.sh" "$SERVICE_DIR"
    bash "$SCRIPTS/04-remove-kafka.sh" "$SERVICE_DIR"
    bash "$SCRIPTS/05-delete-duplicates.sh" "$SERVICE_DIR"
    bash "$SCRIPTS/06-fix-cache-keys.sh" "$SERVICE_DIR"

    # Test compilation
    echo "Testing compilation..."
    cd "$SERVICE_DIR"
    if $MAVEN_PATH clean package -Dmaven.test.skip=true -q 2>&1 | tail -1 | grep -q "BUILD SUCCESS"; then
        echo "✅ $SERVICE: COMPILED"
        ls -lh target/*.jar 2>/dev/null | awk '{print "   JAR:", $9, "("$5")"}'
    else
        echo "❌ $SERVICE: FAILED"
    fi
    cd "$BASE_DIR" > /dev/null
done

echo ""
echo "========================================="
echo "BATCH FIX COMPLETE"
echo "========================================="
