#!/bin/bash
# Complete Pipeline Test Script
SERVICE_DIR="$1"
MAVEN_PATH="C:/Users/TEMP.LAPTOP-1QDBFFCA/Desktop/apache-maven-3.9.12/bin/mvn"

echo "========================================="
echo "PIPELINE TEST: $(basename $SERVICE_DIR)"
echo "========================================="

cd "$SERVICE_DIR"

# Step 1: Clean
echo "[1/5] Clean..."
$MAVEN_PATH clean -q

# Step 2: Compile
echo "[2/5] Compile..."
if $MAVEN_PATH compile -q 2>&1 | tail -1 | grep -q "BUILD SUCCESS"; then
    echo "      ✅ Compile: SUCCESS"
else
    echo "      ❌ Compile: FAILED"
    $MAVEN_PATH compile 2>&1 | grep -E "ERROR|cannot find" | head -3
    exit 1
fi

# Step 3: Package (skip tests)
echo "[3/5] Package..."
if $MAVEN_PATH package -DskipTests -q 2>&1 | tail -1 | grep -q "BUILD SUCCESS"; then
    echo "      ✅ Package: SUCCESS"
else
    echo "      ❌ Package: FAILED"
    exit 1
fi

# Step 4: Verify JAR exists
if [ -f "target/*.jar" ]; then
    echo "[4/5] JAR: ✅ EXISTS"
    ls -lh target/*.jar | awk '{print "      Size:", $5}'
else
    echo "[4/5] JAR: ❌ NOT FOUND"
    exit 1
fi

# Step 5: Summary
echo "[5/5] Pipeline: ✅ COMPLETE"
echo "========================================="

cd - > /dev/null
return 0
