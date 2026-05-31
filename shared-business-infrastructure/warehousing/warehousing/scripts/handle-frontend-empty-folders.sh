#!/bin/bash
# Script to populate remaining empty frontend directories

# Handle frontend empty directories
find ./Frontends -type d -empty | while read dir; do
    # Create .gitkeep to preserve directory structure
    touch "$dir/.gitkeep"
    echo "Created .gitkeep in: $dir"
done

# Handle the vendor-sync-service directory with braces
vendor_dir="./Backend/Java/Vendor/vendor-sync-service"
if [ -d "$vendor_dir" ]; then
    echo "Vendor sync service directory exists, skipping"
else
    # The braces in the name indicate a pattern, not an actual directory
    echo "Skipping pattern directory: ./Backend/Java/Vendor/{vendor-sync-service}"
fi

echo "Frontend empty folders handled successfully"
