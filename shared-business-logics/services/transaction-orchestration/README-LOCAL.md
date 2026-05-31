# Quick Start - Local Development

## System Status

✅ **Java 17**: Already installed
✅ **Node.js 25.4.0**: Already installed
⚠️ **Maven**: Needs to be installed

## One-Click Setup

1. **Install Maven** (Right-click → Run as Administrator):
   ```
   install-maven-only.bat
   ```

2. **Start All Services**:
   ```
   start-all-services.bat
   ```

3. **Open Dashboard**:
   ```
   http://localhost:3000
   ```

## What's Required

- **Java 17+**: ✓ Already installed
- **Maven 3.9+**: ⚠️ Needs installation
- **Node.js 20+**: ✓ Already installed (v25.4.0)

## Installation Scripts

- `install-maven-only.bat` - Install Maven only (Java 17 already present)
- `start-all-services.bat` - Start all 5 services + frontend
- `stop-all-services.bat` - Stop all services

## Service Ports

- Audit Trail: http://localhost:8081
- Onboarding: http://localhost:8082
- Progress Step: http://localhost:8083
- Status Broadcast: http://localhost:8084
- Monitoring: http://localhost:8085
- Web Dashboard: http://localhost:3000

## Quick Verification

After installing Maven, verify:

```bash
java -version   # Should show 17+
mvn -version    # Should show 3.9+
```

## Documentation

- `INSTALL-GUIDE.md` - Detailed Maven installation guide
- `LOCAL-SETUP.md` - Comprehensive local setup documentation
- `QUICKSTART.md` - Quick reference guide

## Next Steps

1. Run `install-maven-only.bat` (as Administrator)
2. Close and reopen terminal
3. Run `start-all-services.bat`
4. Open http://localhost:3000 in your browser

That's it! 🚀
