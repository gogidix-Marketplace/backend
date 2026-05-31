#!/bin/bash
# Script to populate or remove empty directories

BASE_DIR="./Backend/Java"

# Create README files for empty Java package directories to prevent git from removing them
find "$BASE_DIR" -type d -empty | while read dir; do
    # Check if it's a Java package directory (contains 'java' in path and is a valid package structure)
    if echo "$dir" | grep -q "java/com/gogidix"; then
        # Create .gitkeep to preserve directory structure
        touch "$dir/.gitkeep"
        echo "Created .gitkeep in: $dir"
    fi
done

# Find and count remaining empty directories
remaining_empty=$(find "$BASE_DIR" -type d -empty | wc -l)
echo "Remaining empty directories: $remaining_empty"

# List them if any
if [ "$remaining_empty" -gt 0 ]; then
    echo "Remaining empty directories:"
    find "$BASE_DIR" -type d -empty
fi
