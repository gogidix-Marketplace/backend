#!/usr/bin/env python3
"""
Test Coverage Analysis Script for Shared Infrastructure Hexagonal Domain
Analyzes all services for test coverage and implementation gaps
"""

import os
import re
import json
from pathlib import Path
from datetime import datetime
from collections import defaultdict

DOMAIN_PATH = r"C:\Users\TEMP.LAPTOP-1QDBFFCA\Desktop\Gogidix-ecosystem\x-gogidix-domain\Foundation-domain\shared-infrastructure-hexgonal"

class ServiceAnalyzer:
    def __init__(self, domain_path):
        self.domain_path = Path(domain_path)
        self.services = {}
        self.gaps = []

    def find_all_services(self):
        """Find all service directories"""
        service_dirs = []
        for root, dirs, files in os.walk(self.domain_path):
            if 'src' in dirs:
                rel_path = os.path.relpath(root, self.domain_path)
                # Filter to service directories
                parts = Path(rel_path).parts
                if len(parts) >= 2 and any(p in ['services', 'core-infrastructure', 'core-tenancy'] for p in parts):
                    service_dirs.append(root)
        return list(set(service_dirs))

    def analyze_service(self, service_path):
        """Analyze a single service"""
        service_path = Path(service_path)
        service_name = self._extract_service_name(service_path)

        main_files = list(service_path.rglob("src/main/java/**/*.java"))
        test_files = list(service_path.rglob("src/test/java/**/*.java"))

        # Extract class names from main files
        main_classes = set()
        for f in main_files:
            class_name = self._extract_class_name(f.relative_to(service_path))
            if class_name:
                main_classes.add(class_name)

        # Extract test classes and what they test
        test_coverage = {}
        untested_classes = set()

        for f in test_files:
            test_content = f.read_text(encoding='utf-8', errors='ignore')
            test_class = self._extract_class_name(f.relative_to(service_path))
            tested_classes = self._find_tested_classes(test_content)

            if test_class:
                test_coverage[test_class] = {
                    'file': str(f.relative_to(service_path)),
                    'methods': self._extract_test_methods(test_content),
                    'tested_classes': tested_classes
                }

        # Find untested classes
        for main_class in main_classes:
            if not main_class.endswith('Test'):
                is_tested = False
                for test_info in test_coverage.values():
                    if main_class in test_info['tested_classes'] or \
                       main_class.replace('.java', '') + 'Test' in test_coverage:
                        is_tested = True
                        break
                if not is_tested:
                    untested_classes.add(main_class)

        # Find implementation gaps
        gaps = self._find_gaps(main_files, service_path)

        return {
            'name': service_name,
            'path': str(service_path.relative_to(self.domain_path)),
            'main_files': len(main_files),
            'test_files': len(test_files),
            'main_classes': list(main_classes),
            'test_coverage': test_coverage,
            'untested_classes': list(untested_classes),
            'gaps': gaps,
            'coverage_percentage': self._calculate_coverage(main_classes, test_coverage)
        }

    def _extract_service_name(self, service_path):
        """Extract service name from path"""
        parts = service_path.parts
        for i, part in enumerate(parts):
            if 'service' in part.lower() or part == 'config-server' or part == 'core-tenancy':
                return part
        return service_path.name

    def _extract_class_name(self, file_path):
        """Extract class name from file path"""
        parts = file_path.parts
        if 'src' in parts:
            idx = parts.index('src')
            if idx + 3 < len(parts):
                return '/'.join(parts[idx+3:])
        return str(file_path)

    def _find_tested_classes(self, test_content):
        """Find which classes are tested by a test file"""
        tested = set()
        # Look for imports and references
        imports = re.findall(r'import\s+([^\;]+);', test_content)
        for imp in imports:
            if '.domain.model.' in imp or '.domain.port.' in imp:
                class_name = imp.split('.')[-1]
                tested.add(class_name)
        return list(tested)

    def _extract_test_methods(self, test_content):
        """Extract test method names and scenarios"""
        methods = re.findall(r'@Test\s*\n\s*@DisplayName\("([^"]+)"\)\s*\n\s*void\s+(\w+)\(', test_content)
        return [{'name': m[1], 'scenario': m[0]} for m in methods]

    def _find_gaps(self, main_files, service_path):
        """Find implementation gaps in source files"""
        gaps = []
        gap_patterns = {
            'TODO': r'//\s*TODO\b',
            'FIXME': r'//\s*FIXME\b',
            'PLACEHOLDER': r'//\s*Placeholder\b',
            'NotImplemented': r'throw new.*NotImplemented',
            'Empty Method': r'\{\s*\}',  # Empty methods
        }

        for f in main_files:
            try:
                content = f.read_text(encoding='utf-8', errors='ignore')
                for gap_type, pattern in gap_patterns.items():
                    matches = re.finditer(pattern, content, re.IGNORECASE)
                    for match in matches:
                        line_num = content[:match.start()].count('\n') + 1
                        gaps.append({
                            'type': gap_type,
                            'file': str(f.relative_to(service_path)),
                            'line': line_num,
                            'content': match.group().strip()
                        })
            except Exception as e:
                pass

        return gaps

    def _calculate_coverage(self, main_classes, test_coverage):
        """Calculate test coverage percentage"""
        if not main_classes:
            return 100.0

        tested = 0
        for cls in main_classes:
            for test_info in test_coverage.values():
                if cls.replace('.java', '') in str(test_info):
                    tested += 1
                    break

        return round((tested / len(main_classes)) * 100, 2)

    def generate_report(self):
        """Generate complete test coverage report"""
        services = []
        all_gaps = []

        # Core modules
        core_tenancy = self.domain_path / 'core-tenancy'
        if core_tenancy.exists():
            services.append(self.analyze_service(core_tenancy))

        core_infra = self.domain_path / 'core-infrastructure'
        if core_infra.exists():
            for subdir in core_infra.iterdir():
                if subdir.is_dir() and (subdir.name.endswith('-service') or subdir.name.endswith('-module')):
                    services.append(self.analyze_service(subdir))

        # Services
        services_dir = self.domain_path / 'services'
        if services_dir.exists():
            for category in services_dir.iterdir():
                if category.is_dir():
                    for service in category.iterdir():
                        if service.is_dir() and (service.name.endswith('-service') or service.name == 'config-server' or service.name == 'message-broker'):
                            services.append(self.analyze_service(service))

        # Collect all gaps
        for service in services:
            for gap in service['gaps']:
                gap['service'] = service['name']
                all_gaps.append(gap)

        return {
            'domain': 'shared-infrastructure-hexgonal',
            'generated_at': datetime.now().isoformat(),
            'total_services': len(services),
            'services': services,
            'total_gaps': len(all_gaps),
            'all_gaps': all_gaps,
            'summary': self._generate_summary(services)
        }

    def _generate_summary(self, services):
        """Generate summary statistics"""
        total_main = sum(s['main_files'] for s in services)
        total_tests = sum(s['test_files'] for s in services)
        total_untested = sum(len(s['untested_classes']) for s in services)
        avg_coverage = sum(s['coverage_percentage'] for s in services) / len(services) if services else 0

        return {
            'total_main_files': total_main,
            'total_test_files': total_tests,
            'total_untested_classes': total_untested,
            'average_coverage': round(avg_coverage, 2),
            'services_with_full_coverage': len([s for s in services if s['coverage_percentage'] == 100]),
            'services_with_no_tests': len([s for s in services if s['test_files'] == 0])
        }

if __name__ == '__main__':
    analyzer = ServiceAnalyzer(DOMAIN_PATH)
    report = analyzer.generate_report()

    # Save JSON report
    json_path = os.path.join(DOMAIN_PATH, 'docs', 'test-analysis', 'implementation-gaps.json')
    os.makedirs(os.path.dirname(json_path), exist_ok=True)
    with open(json_path, 'w') as f:
        json.dump(report, f, indent=2)

    print(f"Report saved to {json_path}")
    print(f"Total services: {report['total_services']}")
    print(f"Average coverage: {report['summary']['average_coverage']}%")
