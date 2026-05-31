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
	@echo "Shared-Exceptions Doctor: Verifying structure..."
	@test -d "src/main/java/com/gogidix/exceptions/api" || (echo "Missing api layer" && exit 1)
	@test -d "src/main/java/com/gogidix/exceptions/application" || (echo "Missing application layer" && exit 1)
	@test -d "src/main/java/com/gogidix/exceptions/domain" || (echo "Missing domain layer" && exit 1)
	@test -d "src/main/java/com/gogidix/exceptions/infrastructure" || (echo "Missing infrastructure layer" && exit 1)
	@test -f "src/main/resources/application.yml" || (echo "Missing application.yml" && exit 1)
	@echo "Shared-Exceptions Doctor: OK"

# Docker
docker:
	docker build -t shared-exceptions:1.0.0 .

# Clean
clean:
	./mvnw clean