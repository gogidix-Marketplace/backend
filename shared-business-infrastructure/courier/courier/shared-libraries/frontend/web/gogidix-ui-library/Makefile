.PHONY: install dev build test lint storybook clean verify docker

# Install dependencies with multiple fallback options
install:
	@echo "Gogidix UI Library: Installing dependencies..."
	@echo "Trying PNPM (recommended)..."
	@pnpm install 2>/dev/null || ( \
		echo "PNPM failed, trying optimized NPM..." && \
		npm install --no-audit --prefer-offline --maxsockets=1 --fetch-retry-maxtimeout=600000 2>/dev/null || ( \
			echo "Standard NPM failed, trying with registry fallback..." && \
			npm install --registry https://registry.yarnpkg.com/ --no-audit --fetch-retry-maxtimeout=600000 2>/dev/null || ( \
				echo "Registry fallback failed, trying Yarn..." && \
				yarn install --network-timeout 600000 2>/dev/null || ( \
					echo "All package managers failed. Check network connection and try manual installation." && \
					exit 1 \
				) \
			) \
		) \
	)

# Development with hot reload
dev:
	@command -v pnpm >/dev/null 2>&1 && pnpm run dev || npm run dev

# Build library
build:
	@command -v pnpm >/dev/null 2>&1 && pnpm run build || npm run build

# Test
test:
	@command -v pnpm >/dev/null 2>&1 && pnpm run test || npm run test

# Lint
lint:
	@command -v pnpm >/dev/null 2>&1 && pnpm run lint || npm run lint

# Storybook
storybook:
	@command -v pnpm >/dev/null 2>&1 && pnpm run storybook || npm run storybook

# Verify (doctor check)
verify:
	@echo "Gogidix UI Library Doctor: Verifying structure..."
	@test -f "package.json" || (echo "Missing package.json" && exit 1)
	@test -f "tsconfig.json" || (echo "Missing tsconfig.json" && exit 1)
	@test -d "src" || (echo "Missing src directory" && exit 1)
	@test -f "rollup.config.js" || (echo "Missing rollup.config.js" && exit 1)
	@command -v node >/dev/null 2>&1 || (echo "Node.js not installed" && exit 1)
	@node --version | grep -E "v(18|19|20|21)" >/dev/null || (echo "Node.js version should be 18+" && exit 1)
	@echo "Gogidix UI Library Doctor: ✅ React/TypeScript Library Structure Valid"

# Clean
clean:
	rm -rf node_modules dist .rollup.cache
	@command -v pnpm >/dev/null 2>&1 && pnpm store prune || npm cache clean --force

# Docker build
docker:
	docker build -t gogidix-ui-library:1.0.0 .

# Setup PNPM (recommended)
setup-pnpm:
	@echo "Installing PNPM globally..."
	npm install -g pnpm
	@echo "PNPM installed. Use 'make install' to install dependencies with PNPM."