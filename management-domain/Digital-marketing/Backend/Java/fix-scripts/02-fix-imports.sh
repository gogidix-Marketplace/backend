#!/bin/bash
# Script 2: Fix package imports
SERVICE_DIR="$1"

# Fix shared package imports
find "$SERVICE_DIR/src" -name "*.java" -exec sed -i 's/com\.gogidix\.marketing\.shared/com.gogidix.digitalmarketing.shared/g' {} \;
find "$SERVICE_DIR/src" -name "*.java" -exec sed -i 's/com\.gogidix\.management\.shared\.infrastructure\.persistence/com.gogidix.digitalmarketing.shared.infrastructure.persistence/g' {} \;
find "$SERVICE_DIR/src" -name "*.java" -exec sed -i 's/import com\.gogidix\..*\.BaseEntity/import com.gogidix.digitalmarketing.shared.domain.BaseEntity/g' {} \;
find "$SERVICE_DIR/src" -name "*.java" -exec sed -i 's/import com\.gogidix\..*\.BaseRepository/import com.gogidix.digitalmarketing.shared.infrastructure.persistence.BaseRepository/g' {} \;

echo "Fixed imports in: $SERVICE_DIR"
