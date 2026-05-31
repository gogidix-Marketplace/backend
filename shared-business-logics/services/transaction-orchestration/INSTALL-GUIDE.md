# Install Maven on Windows (Java 17 Already Installed)

## Quick Method 1: Automatic Installation (Recommended)

**Double-click: `install-maven-only.bat`**

This will:
- Automatically download Maven 3.9.6
- Install to standard location
- Configure environment variables
- Auto-detect your Java 17 installation

**Note**: Right-click and select "Run as administrator" for best results.

---

## Quick Method 2: Assisted Installation

**Step 1**: Download Maven
- Go to: https://maven.apache.org/download.cgi
- Download: `apache-maven-3.9.6-bin.zip`

**Step 2**: Extract Maven
- Right-click the zip file
- Extract to: `C:\Program Files\apache-maven-3.9.6`

**Step 3**: Configure Environment Variables
- Double-click `setup-environment.bat`
- This will automatically set MAVEN_HOME and PATH
- Restart your terminal when prompted

---

## Manual Installation (If automated methods fail)

### Install Apache Maven

1. **Download**: https://maven.apache.org/download.cgi
2. Download: `apache-maven-3.9.6-bin.zip` (or latest)
3. Extract the zip file
4. Move extracted folder to: `C:\Program Files\apache-maven-3.9.6`

### Set Environment Variables

1. Press `Win + X` and select **System**
2. Click **Advanced system settings**
3. Click **Environment Variables**
4. Under **System variables**, click **New**:

   **Variable name**: `MAVEN_HOME`
   **Variable value**: `C:\Program Files\apache-maven-3.9.6`

5. Find the **Path** variable and click **Edit**
6. Click **New** and add:
   - `%MAVEN_HOME%\bin`

7. Click **OK** to save all dialogs

8. **IMPORTANT**: Close and reopen all terminal windows

---

## Verify Installation

Open a **new** command prompt or terminal and run:

```bash
mvn -version
```

Expected output:
```
Apache Maven 3.9.6
Maven home: C:\Program Files\apache-maven-3.9.6
Java version: 17.0.x
```

Also verify Java:

```bash
java -version
```

Expected output:
```
java version "17.x.x"
Java(TM) SE Runtime Environment ...
```

---

## Troubleshooting

### "mvn is not recognized"

- MAVEN_HOME not set correctly
- Maven not extracted to right location
- PATH doesn't include `%MAVEN_HOME%\bin`
- Close and reopen terminal

### Wrong Java version

- Project requires Java 17+
- Check `java -version` shows 17 or higher
- Set JAVA_HOME to point to Java 17 directory

### Access Denied

- Run installer as Administrator
- Right-click → "Run as administrator"

---

## Next Steps

Once Maven is installed:

1. Open a new terminal
2. Verify with: `mvn -version`
3. Verify Java: `java -version` (should be 17+)
4. Run: `start-all-services.bat`
5. Open: http://localhost:3000

---

## System Requirements

- **Java**: 17+ (already installed ✓)
- **Maven**: 3.9+
- **Node.js**: 20+ (already installed ✓)
- **Memory**: 4GB+ recommended
- **Disk**: 2GB free space
- Download `apache-maven-3.9.6-bin.zip` (or latest version)
- Extract to: `C:\Program Files\apache-maven-3.9.6`
- Right-click the zip → "Extract All..."

**Step 4**: Configure Environment Variables
- Double-click `setup-environment.bat`
- This will automatically set JAVA_HOME, MAVEN_HOME, and PATH
- Restart your terminal when prompted

---

## Manual Installation (If automated methods fail)

### Install Java JDK 21

1. **Download**: https://www.oracle.com/java/technologies/downloads/#java21
2. Select: **Windows x64 Installer**
3. Run the installer
4. Install to: `C:\Program Files\Java\jdk-21` (default)

### Install Apache Maven

1. **Download**: https://maven.apache.org/download.cgi
2. Download: `apache-maven-3.9.6-bin.zip` (or latest)
3. Extract the zip file
4. Move extracted folder to: `C:\Program Files\apache-maven-3.9.6`

### Set Environment Variables

1. Press `Win + X` and select **System**
2. Click **Advanced system settings**
3. Click **Environment Variables**
4. Under **System variables**, click **New**:

   **Variable name**: `JAVA_HOME`
   **Variable value**: `C:\Program Files\Java\jdk-21`

5. Click **New** again:

   **Variable name**: `MAVEN_HOME`
   **Variable value**: `C:\Program Files\apache-maven-3.9.6`

6. Find the **Path** variable and click **Edit**
7. Click **New** and add:
   - `%JAVA_HOME%\bin`
   - `%MAVEN_HOME%\bin`

8. Click **OK** to save all dialogs

9. **IMPORTANT**: Close and reopen all terminal windows

---

## Verify Installation

Open a **new** command prompt or terminal and run:

```bash
java -version
```

Expected output:
```
java version "21.x.x"
Java(TM) SE Runtime Environment ...
```

```bash
mvn -version
```

Expected output:
```
Apache Maven 3.9.6
Maven home: C:\Program Files\apache-maven-3.9.6
Java version: 21.x.x
```

---

## Troubleshooting

### "java is not recognized"

- Environment variables not set correctly
- Close and reopen terminal
- Verify JAVA_HOME and PATH in System Properties

### "mvn is not recognized"

- MAVEN_HOME not set correctly
- Maven not extracted to right location
- PATH doesn't include `%MAVEN_HOME%\bin`
- Close and reopen terminal

### Wrong Java version

- Make sure you downloaded JDK 21 (not JDK 17 or 11)
- Uninstall old Java versions if needed
- Check JAVA_HOME points to JDK 21 folder

### Access Denied

- Run installer as Administrator
- Right-click → "Run as administrator"

---

## Next Steps

Once Java and Maven are installed:

1. Open a new terminal
2. Verify with: `java -version` and `mvn -version`
3. Run: `start-all-services.bat`
4. Open: http://localhost:3000

---

## Alternative: Using Portable Versions

If you don't want to install system-wide:

1. Download portable Java (ZIP version)
2. Download Maven (ZIP)
3. Extract to any folder (e.g., `C:\DevTools`)
4. Add `C:\DevTools\jdk-21\bin` and `C:\DevTools\maven\bin` to PATH
5. No admin rights required for PATH changes to user variables

This approach doesn't require administrator privileges.
