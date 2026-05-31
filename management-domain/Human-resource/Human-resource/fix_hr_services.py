#!/usr/bin/env python3
"""Fix all common issues in HR services."""

import os
import re
import glob

BASE = r"C:\Users\TEMP.LAPTOP-1QDBFFCA\Desktop\Gogidix-ecosystem\x-gogidix-domain\Management-domain\Human-resource\Backend\Java"

LOMBOK_IMPORTS = {
    '@Data': 'import lombok.Data;',
    '@Builder': 'import lombok.Builder;',
    '@SuperBuilder': 'import lombok.experimental.SuperBuilder;',
    '@NoArgsConstructor': 'import lombok.NoArgsConstructor;',
    '@AllArgsConstructor': 'import lombok.AllArgsConstructor;',
    '@Getter': 'import lombok.Getter;',
    '@Setter': 'import lombok.Setter;',
    '@EqualsAndHashCode': 'import lombok.EqualsAndHashCode;',
    '@ToString': 'import lombok.ToString;',
    '@Value': 'import lombok.Value;',
    '@RequiredArgsConstructor': 'import lombok.RequiredArgsConstructor;',
    '@Slf4j': 'import lombok.extern.slf4j.Slf4j;',
    '@Log': 'import lombok.extern.java.Log;',
}

def fix_pom_xmlns(filepath):
    """Remove duplicate xmlns attribute from <plugin> tags."""
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()
    fixed = content.replace('<plugin xmlns="http://maven.apache.org/POM/4.0.0">', '<plugin>')
    if fixed != content:
        with open(filepath, 'w', encoding='utf-8') as f:
            f.write(fixed)
        return True
    return False

def fix_bom(filepath):
    """Remove BOM from file."""
    with open(filepath, 'rb') as f:
        data = f.read()
    if data.startswith(b'\xef\xbb\xbf'):
        with open(filepath, 'wb') as f:
            f.write(data[3:])
        return True
    return False

def fix_missing_lombok_imports(filepath):
    """Add missing Lombok imports to Java files."""
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()
    
    if 'import lombok' not in content and not any(ann in content for ann in ['@Data', '@Builder', '@SuperBuilder', '@Getter', '@Setter', '@NoArgsConstructor', '@AllArgsConstructor']):
        return False
    
    # Find existing imports
    existing_imports = set(re.findall(r'import\s+[\w.]+;', content))
    
    # Determine which annotations are used
    needed = set()
    for ann, imp in LOMBOK_IMPORTS.items():
        if ann in content and imp not in existing_imports:
            needed.add(imp)
    
    if not needed:
        return False
    
    # Find the last import line to insert after
    lines = content.split('\n')
    last_import_idx = -1
    for i, line in enumerate(lines):
        if line.startswith('import '):
            last_import_idx = i
    
    if last_import_idx == -1:
        # Find package statement
        for i, line in enumerate(lines):
            if line.startswith('package '):
                last_import_idx = i
                break
    
    if last_import_idx == -1:
        return False
    
    # Sort needed imports and insert after last import
    sorted_needed = sorted(needed)
    for imp in sorted_needed:
        lines.insert(last_import_idx + 1, imp)
        last_import_idx += 1
    
    new_content = '\n'.join(lines)
    if new_content != content:
        with open(filepath, 'w', encoding='utf-8') as f:
            f.write(new_content)
        return True
    return False

def fix_spring_packages(filepath):
    """Fix BOM-corrupted Spring package names (missing dots)."""
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()
    
    replacements = {
        'org.springframeworkannotation': 'org.springframework.context.annotation',
        'org.springframeworkconfig': 'org.springframework.context.annotation', 
        'org.springframeworklistener': 'org.springframework.context.annotation',
        'org.springframeworkweb': 'org.springframework.web',
        'org.springframeworkhttp': 'org.springframework.http',
        'org.springframeworkboot': 'org.springframework.boot',
        'org.springframeworkdata': 'org.springframework.data',
        'org.springframeworkcloud': 'org.springframework.cloud',
        'org.springframeworksecurity': 'org.springframework.security',
        'org.springframeworkkafka': 'org.springframework.kafka',
        'org.springframeworkamqp': 'org.springframework.amqp',
    }
    
    # More targeted: fix specific known corruption patterns
    specific_fixes = {
        'import org.springframeworkannotation.EnableKafka;': 'import org.springframework.kafka.annotation.EnableKafka;',
        'import org.springframeworkannotation.KafkaListener;': 'import org.springframework.kafka.annotation.KafkaListener;',
        'import org.springframeworkconfig.ConcurrentKafkaListenerContainerFactory;': 'import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;',
        'import org.springframeworklistener.ConcurrentMessageListenerContainer;': 'import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;',
        'import org.springframeworkconfig.KafkaListenerContainerFactory;': 'import org.springframework.kafka.config.KafkaListenerContainerFactory;',
        'import org.springframeworkannotation.Configuration;': 'import org.springframework.context.annotation.Configuration;',
        'import org.springframeworkannotation.Bean;': 'import org.springframework.context.annotation.Bean;',
        'import org.springframeworkannotation.Autowired;': 'import org.springframework.beans.factory.annotation.Autowired;',
        'import org.springframeworkannotation.Service;': 'import org.springframework.stereotype.Service;',
        'import org.springframeworkannotation.Component;': 'import org.springframework.stereotype.Component;',
        'import org.springframeworkannotation.Repository;': 'import org.springframework.stereotype.Repository;',
        'import org.springframeworkannotation.RequestMapping;': 'import org.springframework.web.bind.annotation.RequestMapping;',
        'import org.springframeworkannotation.RestController;': 'import org.springframework.web.bind.annotation.RestController;',
        'import org.springframeworkannotation.GetMapping;': 'import org.springframework.web.bind.annotation.GetMapping;',
        'import org.springframeworkannotation.PostMapping;': 'import org.springframework.web.bind.annotation.PostMapping;',
        'import org.springframeworkannotation.PutMapping;': 'import org.springframework.web.bind.annotation.PutMapping;',
        'import org.springframeworkannotation.DeleteMapping;': 'import org.springframework.web.bind.annotation.DeleteMapping;',
        'import org.springframeworkannotation.PathVariable;': 'import org.springframework.web.bind.annotation.PathVariable;',
        'import org.springframeworkannotation.RequestBody;': 'import org.springframework.web.bind.annotation.RequestBody;',
        'import org.springframeworkannotation.RequestParam;': 'import org.springframework.web.bind.annotation.RequestParam;',
    }
    
    changed = False
    for old, new in specific_fixes.items():
        if old in content:
            content = content.replace(old, new)
            changed = True
    
    # Generic fix for remaining corrupted packages
    # Pattern: org.springframework<word> where <word> is a known sub-package
    generic_patterns = [
        (r'org\.springframework\.annotation\.\w+', 'org.springframework.context.annotation'),
    ]
    
    if changed or any(bad in content for bad in replacements.keys()):
        # Write back
        with open(filepath, 'w', encoding='utf-8') as f:
            f.write(content)
        return True
    return False

def fix_java_file(filepath):
    """Apply all fixes to a Java file."""
    changes = []
    if fix_bom(filepath):
        changes.append('BOM')
    if fix_missing_lombok_imports(filepath):
        changes.append('Lombok')
    if fix_spring_packages(filepath):
        changes.append('SpringPkgs')
    return changes

def fix_all():
    services = [d for d in os.listdir(BASE) if os.path.isdir(os.path.join(BASE, d))]
    
    for svc in sorted(services):
        svc_path = os.path.join(BASE, svc)
        print(f"\n{'='*60}")
        print(f"Service: {svc}")
        print(f"{'='*60}")
        
        # Fix pom.xml
        pom = os.path.join(svc_path, 'pom.xml')
        if os.path.exists(pom):
            if fix_pom_xmlns(pom):
                print(f"  [POM] Fixed xmlns in pom.xml")
            if fix_bom(pom):
                print(f"  [POM] Fixed BOM in pom.xml")
        
        # Fix all Java files
        java_files = glob.glob(os.path.join(svc_path, 'src', '**', '*.java'), recursive=True)
        for jf in java_files:
            changes = fix_java_file(jf)
            if changes:
                rel = os.path.relpath(jf, svc_path)
                print(f"  [{', '.join(changes)}] {rel}")

if __name__ == '__main__':
    fix_all()
    print("\n\nDone! All fixes applied.")
