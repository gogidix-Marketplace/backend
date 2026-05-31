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
	@echo "Shared-Validation Doctor: Verifying library structure..."
	@test -d "src/main/java/com/gogidix" || (echo "Missing main Java source structure" && exit 1)
	@test -f "src/main/resources/application.yml" || (echo "Missing application.yml" && exit 1)
	@test -f "src/main/java/com/gogidix/validation/SharedValidationApplication.java" || (echo "Missing main application class" && exit 1)
	@echo "Shared-Validation Doctor: ✅ Validation Library Structure Valid"

# Docker
docker:
	docker build -t shared-validation:1.0.0 .

# Clean
clean:
	./mvnw clean