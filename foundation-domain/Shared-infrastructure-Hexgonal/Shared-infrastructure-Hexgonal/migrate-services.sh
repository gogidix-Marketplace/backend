#!/bin/bash

# Script to migrate and refactor packages for Shared-infrastructure-Hexgonal
# This script copies services from the old location and updates package declarations

SERVICES_DIR="C:/Users/TEMP.LAPTOP-1QDBFFCA/Desktop/Gogidix-ecosystem/x-gogidix-domain/Foundation-domain/shared-infrastructure/Backend/Java"
TARGET_DIR="C:/Users/TEMP.LAPTOP-1QDBFFCA/Desktop/Gogidix-ecosystem/x-gogidix-domain/Foundation-domain/Shared-infrastructure-Hexgonal/services"

# Phase 1: Social Media Integration Service
echo "Migrating social-media-integration-service..."

SRC_SERVICES="social-media-integration-service"
TARGET_CAT="communication/social-media-integration-service"
OLD_PKG="com.gogidix.sharedinfrastructure.socialmedia"
NEW_PKG="com.gogidix.shared.infrastructure.services.communication.socialmedia"

# Create target directory structure
mkdir -p "$TARGET_DIR/$TARGET_CAT/src/main/java/com/gogidix/shared/infrastructure/services/communication/socialmedia"
mkdir -p "$TARGET_DIR/$TARGET_CAT/src/main/java/com/gogidix/shared/infrastructure/services/communication/socialmedia/domain/model"
mkdir -p "$TARGET_DIR/$TARGET_CAT/src/main/java/com/gogidix/shared/infrastructure/services/communication/socialmedia/domain/port/in"
mkdir -p "$TARGET_DIR/$TARGET_CAT/src/main/java/com/gogidix/shared/infrastructure/services/communication/socialmedia/domain/port/out"
mkdir -p "$TARGET_DIR/$TARGET_CAT/src/main/java/com/gogidix/shared/infrastructure/services/communication/socialmedia/application/dto/request"
mkdir -p "$TARGET_DIR/$TARGET_CAT/src/main/java/com/gogidix/shared/infrastructure/services/communication/socialmedia/application/dto/response"
mkdir -p "$TARGET_DIR/$TARGET_CAT/src/main/java/com/gogidix/shared/infrastructure/services/communication/socialmedia/application/mapper"
mkdir -p "$TARGET_DIR/$TARGET_CAT/src/main/java/com/gogidix/shared/infrastructure/services/communication/socialmedia/application/service"
mkdir -p "$TARGET_DIR/$TARGET_CAT/src/main/java/com/gogidix/shared/infrastructure/services/communication/socialmedia/infrastructure/config"
mkdir -p "$TARGET_DIR/$TARGET_CAT/src/main/java/com/gogidix/shared/infrastructure/services/communication/socialmedia/infrastructure/persistence"
mkdir -p "$TARGET_DIR/$TARGET_CAT/src/main/java/com/gogidix/shared/infrastructure/services/communication/socialmedia/adapter"
mkdir -p "$TARGET_DIR/$TARGET_CAT/src/main/java/com/gogidix/shared/infrastructure/services/communication/socialmedia/interfaces/rest"
mkdir -p "$TARGET_DIR/$TARGET_CAT/src/main/resources"

# Copy source files
cp -r "$SERVICES_DIR/$SRC_SERVICES/src/"* "$TARGET_DIR/$TARGET_CAT/src/" 2>/dev/null

echo "Migration structure created. Package renaming needs to be done manually or with IDE refactoring tools."
echo "Use Find/Replace in files:"
echo "  OLD: $OLD_PKG"
echo "  NEW: $NEW_PKG"
