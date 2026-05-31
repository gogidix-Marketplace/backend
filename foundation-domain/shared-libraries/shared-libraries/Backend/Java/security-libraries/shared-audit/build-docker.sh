#!/bin/bash
# Docker-based Maven build alternative

echo "🐳 Docker-based Maven build starting..."

docker run --rm \
    -v "$(pwd)":/workspace \
    -v "$HOME/.m2":/root/.m2 \
    -w /workspace \
    maven:3.9-openjdk-17 \
    mvn clean compile -Dmaven.test.skip=true -q

echo "🎉 Docker-based build completed!"
