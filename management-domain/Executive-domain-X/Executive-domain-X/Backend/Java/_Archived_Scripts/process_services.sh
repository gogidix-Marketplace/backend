#!/bin/bash

# ceo-strategy-service
cd ceo-strategy-service
mkdir -p src/main/java/com/gogidix/management/executive/strategy
find src/main/java -name "*.java" -type f | while read f; do
  newpath=$(echo "$f" | sed 's|com/gogidix/management/executive/|com/gogidix/management/executive/strategy/|')
  mkdir -p "$(dirname "$newpath")"
  mv "$f" "$newpath"
done
find src/main/java/com/gogidix/management/executive/strategy -name "*.java" -type f | while read f; do
  sed -i 's/package com\.gogidix\.management\.executive\./package com.gogidix.management.executive.strategy./g' "$f"
  sed -i 's/import com\.gogidix\.management\.executive\./import com.gogidix.management.executive.strategy./g' "$f"
done
find src/test/java -name "*.java" -type f 2>/dev/null | while read f; do
  newpath=$(echo "$f" | sed 's|com/gogidix/management/executive/|com/gogidix/management/executive/strategy/|')
  mkdir -p "$(dirname "$newpath")"
  mv "$f" "$newpath"
done
find src/test/java/com/gogidix/management/executive/strategy -name "*.java" -type f 2>/dev/null | while read f; do
  sed -i 's/package com\.gogidix\.management\.executive\./package com.gogidix.management.executive.strategy./g' "$f"
  sed -i 's/import com\.gogidix\.management\.executive\./import com.gogidix.management.executive.strategy./g' "$f"
done
cd ..
