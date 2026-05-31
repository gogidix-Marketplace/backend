#!/usr/bin/env python3
"""
Recreate broken Executive-domain services from working ceo-strategy-service template
"""
import os
import shutil
import re
import time
from pathlib import Path

BASE_DIR = Path("C:/Users/TEMP.LAPTOP-1QDBFFCA/Desktop/Gogidix-ecosystem/x-gogidix-domain/Management-domain/Executive-domain-X/Backend/Java")
TEMPLATE = "ceo-strategy-service"

# Service configuration: (package_name, service_name, display_name, collection_name)
SERVICES = [
    ("operations", "coo-operations-service", "COO Operations Service", "coo-operations-service"),
    ("financial", "cfo-financial-consolidation-service", "CFO Financial Consolidation Service", "cfo-financial-consolidation-service"),
    ("technology", "cto-technology-oversight-service", "CTO Technology Oversight Service", "cto-technology-oversight-service"),
    ("alert", "executive-alert-service", "Executive Alert Service", "executive-alert-service"),
    ("workflow", "executive-approval-workflow-service", "Executive Approval Workflow Service", "executive-approval-workflow-service"),
]

def backup_and_remove(service_path):
    """Backup existing service directory"""
    if service_path.exists():
        backup_path = Path(f"{service_path}-backup-{int(time.time())}")
        shutil.move(str(service_path), str(backup_path))
        print(f"  Backed up existing to {backup_path.name}")

def copy_template(target_name):
    """Copy template to target directory"""
    template_path = BASE_DIR / TEMPLATE
    target_path = BASE_DIR / target_name
    shutil.copytree(template_path, target_path)
    print(f"  Copied template to {target_name}")
    return target_path

def update_java_files(target_path, package, class_name, collection):
    """Update all Java files with new package and class names"""
    java_files = list(target_path.rglob("*.java"))

    # Strategy -> class name (e.g., Operations, Financial, etc.)
    # strategy -> package name (e.g., operations, financial, etc.)
    old_package = "strategy"
    old_class = "Strategy"

    for java_file in java_files:
        content = java_file.read_text(encoding='utf-8', errors='ignore')

        # Replace package paths
        content = re.sub(r'\.strategy\.', f'.{package}.', content)
        content = re.sub(r'com\.gogidix\.management\.executive\.strategy\.',
                        f'com.gogidix.management.executive.{package}.', content)

        # Replace class names (Strategy -> Operations, etc.)
        content = re.sub(r'\bStrategy\b', class_name, content)
        content = re.sub(r'\bSTRATEGY\b', class_name.upper(), content)

        # Replace KpiWidget with appropriate widget class
        content = re.sub(r'KpiWidget', f'{class_name}.Widget', content)

        # Replace dashboard/dashboards references
        content = re.sub(r'\bdashboard\b', package.lower() + 's', content)
        content = re.sub(r'\bdashboards\b', package.lower() + 's', content)
        content = re.sub(r'\bDashboard\b', class_name, content)

        # Replace collection name
        content = re.sub(r'collection = "ceo-strategy-service"', f'collection = "{collection}"', content)

        java_file.write_text(content, encoding='utf-8')

    print(f"  Updated {len(java_files)} Java files")

def rename_directories(target_path, package):
    """Rename strategy directory to new package name"""
    old_dir = target_path / "src/main/java/com/gogidix/management/executive/strategy"
    new_dir = target_path / f"src/main/java/com/gogidix/management/executive/{package}"

    if old_dir.exists():
        shutil.move(str(old_dir), str(new_dir))
        print(f"  Renamed directory: strategy -> {package}")

def update_pom(target_path, service_name, display_name):
    """Update pom.xml with new service name"""
    pom_path = target_path / "pom.xml"
    content = pom_path.read_text(encoding='utf-8', errors='ignore')

    content = re.sub(r'ceo-strategy-service', service_name, content)
    content = re.sub(r'CEO Strategy Service', display_name, content)

    pom_path.write_text(content, encoding='utf-8')
    print(f"  Updated pom.xml")

def cleanup_wrong_dirs(target_path, package):
    """Remove any incorrectly created directories"""
    base = target_path / "src/main/java/com/gogidix/management/executive"

    for dir_name in ["application", "domain", "infrastructure", "interfaces"]:
        wrong_dir = base / dir_name
        if wrong_dir.exists() and not wrong_dir.is_symlink():
            # Check if it's the wrong one (not the package-specific one)
            package_dir = base / package
            if package_dir.exists():
                # Remove the wrong one
                shutil.rmtree(str(wrong_dir))
                print(f"  Removed wrong directory: {dir_name}")

def recreate_service(package, service_name, display_name, collection):
    """Recreate a single service"""
    print(f"\n{'='*60}")
    print(f"Recreating: {service_name}")
    print(f"  Package: {package}")
    print(f"  Class: {package.capitalize()}")
    print(f"{'='*60}")

    class_name = package.capitalize()
    target_path = BASE_DIR / service_name

    # Backup existing
    backup_and_remove(target_path)

    # Copy template
    copy_template(service_name)

    # Update files
    update_java_files(target_path, package, class_name, collection)

    # Rename directories
    rename_directories(target_path, package)

    # Update pom.xml
    update_pom(target_path, service_name, display_name)

    # Cleanup wrong directories
    cleanup_wrong_dirs(target_path, package)

    print(f"  ✓ {service_name} recreated successfully!")

def main():
    os.chdir(BASE_DIR)

    for package, service_name, display_name, collection in SERVICES:
        try:
            recreate_service(package, service_name, display_name, collection)
        except Exception as e:
            print(f"  ✗ Error recreating {service_name}: {e}")

    print(f"\n{'='*60}")
    print("All services recreated!")
    print(f"{'='*60}")

if __name__ == "__main__":
    main()
