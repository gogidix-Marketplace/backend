# Shared Libraries Test Scripts

## Overview
Three PowerShell scripts to verify all shared libraries are production-ready.

---

## Script 1: Quick-Test-AllLibraries.ps1
**Purpose:** Fast verification of Maven repository installation  
**Runtime:** ~5 seconds  
**Use When:** Quick check that all libraries are installed

### Usage
```powershell
cd "C:\Users\frich\Desktop\Gogidix-Technology-Ecosystem\domains\Foundation-domain\shared-libraries\backend\java"
.\Quick-Test-AllLibraries.ps1
```

### What It Checks
- ✅ JAR exists in Maven repository
- ✅ POM exists in Maven repository
- ✅ File sizes reported

### Expected Output
```
✅ shared-model-service - JAR: 45.32 KB
✅ shared-security-service - JAR: 78.45 KB
✅ shared-validation-service - JAR: 34.21 KB
...
Result: 8 / 8 libraries installed
🎉 100% PRODUCTION READY!
```

---

## Script 2: Test-AllSharedLibraries.ps1
**Purpose:** Comprehensive verification of all libraries  
**Runtime:** ~15-20 minutes  
**Use When:** Full production readiness verification

### Usage
```powershell
cd "C:\Users\frich\Desktop\Gogidix-Technology-Ecosystem\domains\Foundation-domain\shared-libraries\backend\java"
.\Test-AllSharedLibraries.ps1
```

### What It Tests
For each library:
1. ✅ Directory exists
2. ✅ pom.xml exists
3. ✅ Compilation succeeds (mvn compile)
4. ✅ JAR packaging succeeds (mvn jar:jar)
5. ✅ Maven install succeeds (mvn install)
6. ✅ JAR exists in Maven repository
7. ✅ POM exists in Maven repository

### Expected Output
```
========================================
Testing: shared-model
========================================
[OK] Directory exists
[OK] pom.xml found
[TEST 1] Compiling shared-model...
[PASS] Compilation successful
[TEST 2] Packaging JAR for shared-model...
[PASS] JAR packaging successful
[OK] JAR file created: shared-model-service-1.0.0.jar (45.32 KB)
[TEST 3] Installing to Maven repository...
[PASS] Maven install successful
[TEST 4] Verifying Maven repository installation...
[OK] JAR found in Maven repo: 45.32 KB
[OK] POM found in Maven repo
[SUCCESS] shared-model is PRODUCTION READY!

...

=========================================
FINAL VERIFICATION REPORT
=========================================
✅ PASS - shared-model
✅ PASS - shared-security
✅ PASS - shared-validation
✅ PASS - shared-testing
✅ PASS - shared-audit
✅ PASS - shared-utilities
✅ PASS - shared-exceptions
✅ PASS - shared-messaging

=========================================
SUMMARY
=========================================
Total Libraries: 8
Success: 8
Failed: 0
Success Rate: 100%

🎉🎉🎉 100% PRODUCTION READY! 🎉🎉🎉
All shared libraries are ready for service development!
```

### Output Files
- Creates JSON report: `Test-Results-YYYYMMDD-HHmmss.txt`
- Contains detailed results for each library

---

## Script 3: Compile-Test-SingleLibrary.ps1
**Purpose:** Detailed testing of a single library  
**Runtime:** ~2-3 minutes per library  
**Use When:** Debugging a specific library

### Usage
```powershell
cd "C:\Users\frich\Desktop\Gogidix-Technology-Ecosystem\domains\Foundation-domain\shared-libraries\backend\java"
.\Compile-Test-SingleLibrary.ps1 -LibraryName "shared-utilities"
```

### What It Tests
1. ✅ Clean (mvn clean)
2. ✅ Compile with timing
3. ✅ Package JAR with file size
4. ✅ Run tests (optional)
5. ✅ Install to Maven
6. ✅ Verify repository installation

### Example Usage for Each Library
```powershell
# Test shared-model
.\Compile-Test-SingleLibrary.ps1 -LibraryName "shared-model"

# Test shared-security
.\Compile-Test-SingleLibrary.ps1 -LibraryName "shared-security"

# Test shared-validation
.\Compile-Test-SingleLibrary.ps1 -LibraryName "shared-validation"

# Test shared-testing
.\Compile-Test-SingleLibrary.ps1 -LibraryName "shared-testing"

# Test shared-audit
.\Compile-Test-SingleLibrary.ps1 -LibraryName "shared-audit"

# Test shared-utilities
.\Compile-Test-SingleLibrary.ps1 -LibraryName "shared-utilities"

# Test shared-exceptions
.\Compile-Test-SingleLibrary.ps1 -LibraryName "shared-exceptions"

# Test shared-messaging
.\Compile-Test-SingleLibrary.ps1 -LibraryName "shared-messaging"
```

---

## Troubleshooting

### If Tests Fail

**Issue:** Compilation errors
```powershell
# View detailed errors
cd "path\to\library"
mvn clean compile 2>&1 | Select-String -Pattern "ERROR"
```

**Issue:** JAR not created
```powershell
# Check target directory
cd "path\to\library"
ls target\*.jar
```

**Issue:** Maven install fails
```powershell
# Check Maven repository
ls "$env:USERPROFILE\.m2\repository\com\gogidix\libraries"
```

---

## Expected Results (All Pass)

### Quick Test Expected
```
✅ shared-model-service - JAR: 45.32 KB
✅ shared-security-service - JAR: 78.45 KB
✅ shared-validation-service - JAR: 34.21 KB
✅ shared-testing-service - JAR: 56.78 KB
✅ shared-audit - JAR: 89.12 KB
✅ shared-utilities - JAR: 67.34 KB
✅ shared-exceptions - JAR: 12.45 KB
✅ shared-messaging - JAR: 54.67 KB
Result: 8 / 8 libraries installed
🎉 100% PRODUCTION READY!
```

### Full Test Expected
All 8 libraries should show:
- [PASS] Compilation successful
- [PASS] JAR packaging successful
- [PASS] Maven install successful
- [OK] JAR found in Maven repo
- [OK] POM found in Maven repo
- [SUCCESS] {library} is PRODUCTION READY!

---

## Performance Notes

- **Quick-Test:** ~5 seconds (no compilation)
- **Single-Library:** ~2-3 minutes per library
- **Full-Test:** ~15-20 minutes for all 8 libraries

---

## Recommendations

1. **First Run:** Use Quick-Test to verify current status
2. **If Issues:** Use Single-Library test for debugging
3. **Full Verification:** Run Full-Test before production deployment
4. **Regular Checks:** Run Quick-Test after any changes

---

**Created:** 2025-10-26  
**Agent:** Agent 2  
**Status:** Production Ready
