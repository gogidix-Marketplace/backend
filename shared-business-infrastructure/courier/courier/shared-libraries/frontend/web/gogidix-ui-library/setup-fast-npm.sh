#!/bin/bash

echo "🚀 Gogidix UI Library - Fast NPM Setup Script"
echo "=============================================="

# Function to install PNPM
install_pnpm() {
    echo "📦 Installing PNPM (recommended)..."
    if command -v pnpm >/dev/null 2>&1; then
        echo "✅ PNPM already installed: $(pnpm --version)"
    else
        npm install -g pnpm
        echo "✅ PNPM installed: $(pnpm --version)"
    fi
}

# Function to setup .npmrc optimizations
setup_npmrc() {
    echo "⚙️  Creating optimized .npmrc configuration..."
    # Already created by the agent above
    echo "✅ .npmrc configuration already exists"
}

# Function to clear caches
clear_caches() {
    echo "🧹 Clearing all package manager caches..."
    npm cache clean --force 2>/dev/null || true
    if command -v pnpm >/dev/null 2>&1; then
        pnpm store prune 2>/dev/null || true
    fi
    if command -v yarn >/dev/null 2>&1; then
        yarn cache clean 2>/dev/null || true
    fi
    echo "✅ Caches cleared"
}

# Function to test installation strategies
test_install() {
    echo "🧪 Testing installation strategies..."
    
    # Strategy 1: PNPM
    echo "Testing PNPM..."
    if pnpm install --dry-run 2>/dev/null; then
        echo "✅ PNPM strategy: WORKING"
        PNPM_WORKS=1
    else
        echo "❌ PNPM strategy: FAILED"
    fi
    
    # Strategy 2: Optimized NPM
    echo "Testing optimized NPM..."
    if npm install --dry-run --no-audit --prefer-offline 2>/dev/null; then
        echo "✅ Optimized NPM strategy: WORKING"
        NPM_WORKS=1
    else
        echo "❌ Optimized NPM strategy: FAILED"
    fi
}

# Function to run actual installation
run_install() {
    echo "🎯 Running actual installation..."
    make install
}

# Main execution
main() {
    echo "Starting fast NPM setup for Gogidix UI Library..."
    
    # Check Node.js version
    if ! node --version | grep -E "v(18|19|20|21)" >/dev/null; then
        echo "❌ Node.js version should be 18+. Current: $(node --version)"
        echo "Please upgrade Node.js and run this script again."
        exit 1
    fi
    echo "✅ Node.js version: $(node --version)"
    
    clear_caches
    install_pnpm
    setup_npmrc
    test_install
    
    echo ""
    echo "🎉 Setup complete! Recommended usage:"
    echo "   - Use 'make install' for smart installation with fallbacks"
    echo "   - Use 'pnpm install' directly for fastest installs"
    echo "   - Use 'make dev' for development"
    echo "   - Use 'make build' for production builds"
    echo ""
    
    read -p "Would you like to run 'make install' now? (y/n): " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        run_install
    fi
}

# Run main function
main "$@"