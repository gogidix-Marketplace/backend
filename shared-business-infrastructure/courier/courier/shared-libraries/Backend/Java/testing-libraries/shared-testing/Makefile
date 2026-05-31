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
	@echo "Shared-Testing Doctor: Verifying library structure..."
	@test -d "src/main/java/com/gogidix" || (echo "Missing main Java source structure" && exit 1)
	@test -f "src/main/resources/application.yml" || (echo "Missing application.yml" && exit 1)
	@test -f "src/main/java/com/gogidix/testing/SharedTestingApplication.java" || (echo "Missing main application class" && exit 1)
	@echo "Shared-Testing Doctor: ✅ Testing Library Structure Valid"

# Docker
docker:
	docker build -t shared-testing:1.0.0 .

# Clean
clean:
	./mvnw clean