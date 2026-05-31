# shared-utilities Setup Guide

## Prerequisites
- Java 17+
- Maven 3.8+
- Spring Boot 3.x

## Installation
Add to your pom.xml:
```xml
<dependency>
    <groupId>com.gogidix.ecosystem.shared</groupId>
    <artifactId>shared-utilities</artifactId>
    <version>${shared.libraries.version}</version>
</dependency>
```

## Configuration
Configure in application.yml:
```yaml
gogidix:
  utilities:
    enabled: true
```
