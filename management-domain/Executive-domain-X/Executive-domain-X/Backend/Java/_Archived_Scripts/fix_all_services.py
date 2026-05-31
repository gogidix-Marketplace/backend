#!/usr/bin/env python3
"""
Fix all remaining Executive domain services
Corrects package imports, class names, and field references
"""

import os
import re
from pathlib import Path

# Service configurations: (service_dir, entity_name, entity_class)
SERVICES = [
    ("ceo-approval-service", "approval", "Approval"),
    ("ceo-strategy-service", "strategy", "Strategy"),
    ("cfo-financial-consolidation-service", "financial", "FinancialData"),
    ("coo-operations-service", "operations", "Operations"),
    ("cto-technology-oversight-service", "technology", "Technology"),
    ("executive-alert-service", "alert", "Alert"),
    ("executive-approval-workflow-service", "workflow", "Workflow"),
    ("executive-audit-service", "audit", "Audit"),
]

BASE_DIR = Path(r"C:\Users\TEMP.LAPTOP-1QDBFFCA\Desktop\Gogidix-ecosystem\x-gogidix-domain\Management-domain\Executive-domain-X\Backend\Java")

def fix_service(service_dir, entity_name, entity_class):
    """Fix a single service"""
    service_path = BASE_DIR / service_dir
    if not service_path.exists():
        print(f"Skipping {service_dir} - not found")
        return False

    print(f"\n{'='*60}")
    print(f"Fixing {service_dir} (entity: {entity_class})")
    print(f"{'='*60}")

    src_path = service_path / "src" / "main" / "java"
    if not src_path.exists():
        print(f"No src/main/java found in {service_dir}")
        return False

    # Find all Java files
    java_files = list(src_path.rglob("*.java"))
    print(f"Found {len(java_files)} Java files")

    fixed_count = 0
    for java_file in java_files:
        try:
            with open(java_file, 'r', encoding='utf-8') as f:
                content = f.read()

            original_content = content
            rel_path = java_file.relative_to(src_path)

            # Fix 1: Wrong package imports (double folder name in package)
            # e.g., com.gogidix.management.executive.approval.approval -> com.gogidix.management.executive.approval.domain.model
            content = re.sub(
                rf'import com\.gogidix\.management\.executive\.{entity_name}\.{entity_name}\.',
                f'import com.gogidix.management.executive.{entity_name}.domain.model.',
                content
            )
            content = re.sub(
                rf'import com\.gogidix\.management\.executive\.{entity_name}\.application\.command\.',
                f'import com.gogidix.management.executive.{entity_name}.application.command.',
                content
            )
            content = re.sub(
                rf'import com\.gogidix\.management\.executive\.{entity_name}\.application\.query\.',
                f'import com.gogidix.management.executive.{entity_name}.application.query.',
                content
            )
            content = re.sub(
                rf'import com\.gogidix\.management\.executive\.{entity_name}\.application\.dto\.',
                f'import com.gogidix.management.executive.{entity_name}.application.dto.',
                content
            )
            content = re.sub(
                rf'import com\.gogidix\.management\.executive\.{entity_name}\.domain\.repository\.',
                f'import com.gogidix.management.executive.{entity_name}.domain.repository.',
                content
            )
            content = re.sub(
                rf'import com\.gogidix\.management\.executive\.{entity_name}\.domain\.service\.',
                f'import com.gogidix.management.executive.{entity_name}.domain.service.',
                content
            )
            content = re.sub(
                rf'import com\.gogidix\.management\.executive\.{entity_name}\.domain\.model\.',
                f'import com.gogidix.management.executive.{entity_name}.domain.model.',
                content
            )
            content = re.sub(
                rf'import com\.gogidix\.management\.executive\.{entity_name}\.infrastructure\.',
                f'import com.gogidix.management.executive.{entity_name}.infrastructure.',
                content
            )

            # Fix 2: DTO imports from wrong package
            content = re.sub(
                rf'import com\.gogidix\.management\.executive\.{entity_name}\.(\w+Dto)',
                f'import com.gogidix.management.executive.{entity_name}.application.dto.\\1',
                content
            )

            # Fix 3: Domain model imports from wrong package
            for model_name in [entity_class, 'BaseEntity', 'KpiWidget', 'PerformanceBenchmark', 'ExecutiveSummary', 'DataFeed', f'{entity_class}Data', f'{entity_class}Id']:
                content = re.sub(
                    rf'import com\.gogidix\.management\.executive\.{entity_name}\.{model_name}',
                    f'import com.gogidix.management.executive.{entity_name}.domain.model.{model_name}',
                    content
                )

            # Fix 4: Repository imports from wrong package
            content = re.sub(
                rf'import com\.gogidix\.management\.executive\.{entity_name}\.([A-Z]\w*Repository)',
                f'import com.gogidix.management.executive.{entity_name}.domain.repository.\\1',
                content
            )

            # Fix 5: Dashboard* class names -> Entity* class names
            if entity_class != "Dashboard":
                content = re.sub(r'class DashboardUserDetailsService', f'class {entity_class}UserDetailsService', content)
                content = re.sub(r'interface DashboardDomainService', f'interface {entity_class}DomainService', content)
                content = re.sub(r'DashboardUserDetailsService\.class', f'{entity_class}UserDetailsService.class', content)
                content = re.sub(r'new DashboardUserDetailsService\(', f'new {entity_class}UserDetailsService(', content)

                # Fix record types
                content = re.sub(r'record DashboardLayout', f'record {entity_class}Layout', content)
                content = re.sub(r'record DashboardHealthMetrics', f'record {entity_class}HealthMetrics', content)
                content = re.sub(r'DashboardLayout ', f'{entity_class}Layout ', content)
                content = re.sub(r'DashboardHealthMetrics ', f'{entity_class}HealthMetrics ', content)

                # Fix method names
                content = re.sub(r'validateDashboardForCreation', f'validate{entity_class}ForCreation', content)
                content = re.sub(r'validateDashboardForUpdate', f'validate{entity_class}ForUpdate', content)
                content = re.sub(r'canPublish\(Dashboard dashboard', f'canPublish({entity_class} {entity_name.lower()}', content)
                content = re.sub(r'canDelete\(Dashboard dashboard', f'canDelete({entity_class} {entity_name.lower()}', content)
                content = re.sub(r'calculateLayout\(Dashboard dashboard', f'calculateLayout({entity_class} {entity_name.lower()}', content)

            # Fix 6: Wrong package declarations
            content = re.sub(
                rf'package com\.gogidix\.management\.executive\.{entity_name}\.application\.',
                f'package com.gogidix.management.executive.{entity_name}.application.',
                content
            )
            content = re.sub(
                rf'package com\.gogidix\.management\.executive\.{entity_name}\.domain\.',
                f'package com.gogidix.management.executive.{entity_name}.domain.',
                content
            )

            # Write back if changed
            if content != original_content:
                with open(java_file, 'w', encoding='utf-8') as f:
                    f.write(content)
                fixed_count += 1

        except Exception as e:
            print(f"Error processing {rel_path}: {e}")

    print(f"Fixed {fixed_count} files in {service_dir}")
    return True

def main():
    print("Executive Domain Services - Batch Fix")
    print("="*60)

    for service_dir, entity_name, entity_class in SERVICES:
        fix_service(service_dir, entity_name, entity_class)

    print("\n" + "="*60)
    print("Batch fix complete!")
    print("="*60)

if __name__ == "__main__":
    main()
