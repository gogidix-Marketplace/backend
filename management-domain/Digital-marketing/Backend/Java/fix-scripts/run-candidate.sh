#!/bin/bash
# Candidate Service Dry Run - Complete Pipeline
# CANDIDATE: seo-service

BASE_DIR="C:/Users/TEMP.LAPTOP-1QDBFFCA/Desktop/Gogidix-ecosystem/x-gogidix-domain/Management-domain/Digital-marketing/Backend/Java"
CANDIDATE="$BASE_DIR/seo-service"
SCRIPTS="$BASE_DIR/fix-scripts"

echo "========================================="
echo "CANDIDATE DRY RUN: seo-service"
echo "========================================="

# Apply fix scripts in order
bash "$SCRIPTS/01-create-pom.sh" "$CANDIDATE"
bash "$SCRIPTS/08-fix-security.sh" "$CANDIDATE"
bash "$SCRIPTS/03-copy-shared.sh" "$CANDIDATE"
bash "$SCRIPTS/02-fix-imports.sh" "$CANDIDATE"
bash "$SCRIPTS/04-remove-kafka.sh" "$CANDIDATE"
bash "$SCRIPTS/05-delete-duplicates.sh" "$CANDIDATE"
bash "$SCRIPTS/06-fix-cache-keys.sh" "$CANDIDATE"

# Run pipeline test
bash "$SCRIPTS/07-pipeline-test.sh" "$CANDIDATE"

exit $?
