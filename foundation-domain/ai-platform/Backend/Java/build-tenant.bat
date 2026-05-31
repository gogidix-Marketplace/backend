@echo off
cd /d "C:\Users\TEMP.LAPTOP-1QDBFFCA\Desktop\Gogidix-ecosystem\x-gogidix-domain\Foundation-domain\ai-services\Backend\Java\infrastructure-platform\tenant-service"
"C:\Users\TEMP.LAPTOP-1QDBFFCA\Desktop\apache-maven-3.9.12\bin\mvn.cmd" clean package -DskipTests > build-output.txt 2>&1
dir target
