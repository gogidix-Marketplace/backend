#!/usr/bin/env python3
"""
Script to customize all 9 executive services from blueprint
"""
import os
import re
import shutil
from pathlib import Path

# Define all services with their configurations
SERVICES = [
    {
        "name": "ceo-analytics-service",
        "package": "com.gogidix.management.executive.analytics",
        "main_class": "CeoAnalyticsServiceApplication",
        "entity": "Analytics",
        "entity_plural": "Analytics",
        "repository": "AnalyticsRepository",
        "command_service": "AnalyticsCommandService",
        "query_service": "AnalyticsQueryService",
        "controller": "AnalyticsController"
    },
    {
        "name": "ceo-approval-service",
        "package": "com.gogidix.management.executive.approval",
        "main_class": "CeoApprovalServiceApplication",
        "entity": "Approval",
        "entity_plural": "Approvals",
        "repository": "ApprovalRepository",
        "command_service": "ApprovalCommandService",
        "query_service": "ApprovalQueryService",
        "controller": "ApprovalController"
    },
    {
        "name": "ceo-strategy-service",
        "package": "com.gogidix.management.executive.strategy",
        "main_class": "CeoStrategyServiceApplication",
        "entity": "Strategy",
        "entity_plural": "Strategies",
        "repository": "StrategyRepository",
        "command_service": "StrategyCommandService",
        "query_service": "StrategyQueryService",
        "controller": "StrategyController"
    },
    {
        "name": "cfo-financial-consolidation-service",
        "package": "com.gogidix.management.executive.financial",
        "main_class": "CfoFinancialConsolidationServiceApplication",
        "entity": "FinancialData",
        "entity_plural": "FinancialData",
        "repository": "FinancialDataRepository",
        "command_service": "FinancialDataCommandService",
        "query_service": "FinancialDataQueryService",
        "controller": "FinancialDataController"
    },
    {
        "name": "coo-operations-service",
        "package": "com.gogidix.management.executive.operations",
        "main_class": "CooOperationsServiceApplication",
        "entity": "Operations",
        "entity_plural": "Operations",
        "repository": "OperationsRepository",
        "command_service": "OperationsCommandService",
        "query_service": "OperationsQueryService",
        "controller": "OperationsController"
    },
    {
        "name": "cto-technology-oversight-service",
        "package": "com.gogidix.management.executive.technology",
        "main_class": "CtoTechnologyOversightServiceApplication",
        "entity": "Technology",
        "entity_plural": "Technology",
        "repository": "TechnologyRepository",
        "command_service": "TechnologyCommandService",
        "query_service": "TechnologyQueryService",
        "controller": "TechnologyController"
    },
    {
        "name": "executive-alert-service",
        "package": "com.gogidix.management.executive.alert",
        "main_class": "ExecutiveAlertServiceApplication",
        "entity": "Alert",
        "entity_plural": "Alerts",
        "repository": "AlertRepository",
        "command_service": "AlertCommandService",
        "query_service": "AlertQueryService",
        "controller": "AlertController"
    },
    {
        "name": "executive-approval-workflow-service",
        "package": "com.gogidix.management.executive.workflow",
        "main_class": "ExecutiveApprovalWorkflowServiceApplication",
        "entity": "Workflow",
        "entity_plural": "Workflows",
        "repository": "WorkflowRepository",
        "command_service": "WorkflowCommandService",
        "query_service": "WorkflowQueryService",
        "controller": "WorkflowController"
    },
    {
        "name": "executive-audit-service",
        "package": "com.gogidix.management.executive.audit",
        "main_class": "ExecutiveAuditServiceApplication",
        "entity": "Audit",
        "entity_plural": "Audits",
        "repository": "AuditRepository",
        "command_service": "AuditCommandService",
        "query_service": "AuditQueryService",
        "controller": "AuditController"
    }
]

BASE_DIR = Path(r"C:\Users\TEMP.LAPTOP-1QDBFFCA\Desktop\Gogidix-ecosystem\x-gogidix-domain\Management-domain\Executive-domain-X\Backend\Java")
BLUEPRINT_DIR = Path(r"C:\Users\TEMP.LAPTOP-1QDBFFCA\Desktop\Gogidix-ecosystem\x-gogidix-domain\Management-domain\Executive-domain\Backend\Java\executive-dashboard-service")

OLD_PACKAGE = "com.gogidix.management.executive"
OLD_MAIN_CLASS = "ExecutiveDashboardServiceApplication"
OLD_ENTITY = "Dashboard"


def delete_shared_folder(service_dir):
    """Delete the Shared folder if it exists"""
    shared_dir = service_dir / "Shared"
    if shared_dir.exists():
        shutil.rmtree(shared_dir)
        print(f"  Deleted Shared folder")


def update_pom_xml(service_dir, service):
    """Update pom.xml with service-specific values"""
    pom_path = service_dir / "pom.xml"
    if not pom_path.exists():
        return

    content = pom_path.read_text(encoding='utf-8')

    # Update artifactId
    content = re.sub(
        r'<artifactId>executive-dashboard-service</artifactId>',
        f'<artifactId>{service["name"]}</artifactId>',
        content
    )

    # Update groupId
    content = re.sub(
        r'<groupId>com\.gogidix\.executive</groupId>',
        '<groupId>com.gogidix.management.executive</groupId>',
        content
    )

    # Update name
    name_display = service["name"].replace("-", " ").title()
    content = re.sub(
        r'<name>Executive Dashboard Service</name>',
        f'<name>{name_display}</name>',
        content
    )

    pom_path.write_text(content, encoding='utf-8')
    print(f"  Updated pom.xml")


def customize_java_file(file_path, service):
    """Customize a single Java file"""
    content = file_path.read_text(encoding='utf-8')

    original_content = content

    # Update package declaration
    content = re.sub(
        rf'package {re.escape(OLD_PACKAGE)}\.',
        f'package {service["package"]}.',
        content
    )

    # Update import statements
    content = re.sub(
        rf'import {re.escape(OLD_PACKAGE)}\.',
        f'import {service["package"]}.',
        content
    )

    # Update main class name
    if OLD_MAIN_CLASS in content:
        content = content.replace(OLD_MAIN_CLASS, service["main_class"])

    # Update entity-related class names
    content = content.replace(f'{OLD_ENTITY}Repository', service["repository"])
    content = content.replace(f'{OLD_ENTITY}CommandService', service["command_service"])
    content = content.replace(f'{OLD_ENTITY}QueryService', service["query_service"])
    content = content.replace(f'{OLD_ENTITY}Controller', service["controller"])

    # Update entity class name in specific contexts
    content = re.sub(
        rf'\b{re.escape(OLD_ENTITY)}\b',
        service["entity"],
        content
    )

    # Special handling for KpiWidget - keep as is
    content = re.sub(
        rf'{re.escape(service["entity"])}Widget',
        'KpiWidget',
        content
    )

    # Fix document collection names
    content = re.sub(
        r'collection = "executive_dashboards"',
        f'collection = "{service["name"]}"',
        content
    )

    # Fix scans
    content = re.sub(
        rf'basePackages = "{re.escape(OLD_PACKAGE)}"',
        f'basePackages = "{service["package"]}"',
        content
    )

    if content != original_content:
        file_path.write_text(content, encoding='utf-8')
        return True
    return False


def rename_java_file(file_path, old_name, new_name):
    """Rename a Java file"""
    if old_name in file_path.name:
        new_path = file_path.parent / file_path.name.replace(old_name, new_name)
        file_path.rename(new_path)
        return new_path
    return file_path


def customize_service(service):
    """Customize a single service"""
    print(f"\nCustomizing {service['name']}...")
    service_dir = BASE_DIR / service["name"]

    if not service_dir.exists():
        print(f"  ERROR: Service directory {service_dir} does not exist")
        return False

    # Delete Shared folder
    delete_shared_folder(service_dir)

    # Update pom.xml
    update_pom_xml(service_dir, service)

    # Process all Java files
    src_dir = service_dir / "src"
    if src_dir.exists():
        for java_file in src_dir.rglob("*.java"):
            customize_java_file(java_file, service)

            # Rename files if needed
            if OLD_ENTITY in java_file.name:
                new_file = rename_java_file(java_file, OLD_ENTITY, service["entity"])
                print(f"  Renamed: {java_file.name} -> {new_file.name}")

            if OLD_MAIN_CLASS in java_file.name:
                new_file = rename_java_file(java_file, OLD_MAIN_CLASS, service["main_class"])
                print(f"  Renamed: {java_file.name} -> {new_file.name}")

    print(f"  Customized {service['name']}")
    return True


def main():
    """Main function to customize all services"""
    print("Starting customization of all 9 executive services...")

    for service in SERVICES:
        customize_service(service)

    print("\n" + "=" * 60)
    print("Customization complete!")
    print("=" * 60)


if __name__ == "__main__":
    main()
