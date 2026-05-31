.PHONY: dev build test docker clean verify

# Development
dev:
	./mvnw spring-boot:run

# Build
build:
	./mvnw clean package -DskipTests

# Test
test:
	./mvnw clean test

# Verify (doctor check)
verify:
	./mvnw clean compile test-compile
	@echo "Shared-Audit Doctor: Verifying structure..."
	@test -d "src/main/java/com/gogidix/infrastructure/sharedlibraries/sharedaudit/api" || (echo "Missing api layer" && exit 1)
	@test -d "src/main/java/com/gogidix/infrastructure/sharedlibraries/sharedaudit/application" || (echo "Missing application layer" && exit 1)
	@test -d "src/main/java/com/gogidix/infrastructure/sharedlibraries/sharedaudit/domain" || (echo "Missing domain layer" && exit 1)
	@test -f "src/main/resources/application.yml" || (echo "Missing application.yml" && exit 1)
	@echo "Shared-Audit Doctor: OK"

# Docker
docker:
	docker build -t shared-audit:1.0.0 .

# Clean
clean:
	./mvnw clean