#!/bin/bash

# Fix System-Admin POMs script
BASE_DIR="c:/Users/TEMP.LAPTOP-1QDBFFCA/Desktop/Gogidix-ecosystem/x-gogidix-domain/Management-domain/System-Administrator/Backend/Java"

SERVICES=(
    "access-control-service"
    "access-request-service"
    "alert-management-service"
    "audit-service"
    "compliance-service"
    "configuration-service"
    "deployment-service"
    "environment-service"
    "infrastructure-monitoring-service"
    "performance-metrics-service"
    "security-monitoring-service"
    "user-provisioning-service"
    "vulnerability-scanning-service"
    "incident-management-service"
)

for SERVICE in "${SERVICES[@]}"; do
    POM_FILE="$BASE_DIR/$SERVICE/pom.xml"
    if [ -f "$POM_FILE" ]; then
        echo "Processing $SERVICE..."

        # Read the artifactId from current POM
        ARTIFACT_ID=$(grep -m 1 "<artifactId>" "$POM_FILE" | sed 's/.*<artifactId>\(.*\)<\/artifactId>.*/\1/')

        # Create new POM with Spring Boot parent
        cat > "$POM_FILE" << EOF
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.0</version>
        <relativePath/>
    </parent>
    <groupId>com.gogidix.sysadmin</groupId>
    <artifactId>$ARTIFACT_ID</artifactId>
    <version>1.0.0</version>
    <name>$ARTIFACT_ID</name>
    <description>Hexagonal SaaS service for System Administrator</description>
    <properties>
        <java.version>17</java.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-mongodb</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.security</groupId>
            <artifactId>spring-security-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.mockito</groupId>
            <artifactId>mockito-junit-jupiter</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.awaitility</groupId>
            <artifactId>awaitility</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.jacoco</groupId>
                <artifactId>jacoco-maven-plugin</artifactId>
                <version>0.8.11</version>
                <executions>
                    <execution>
                        <goals>
                            <goal>prepare-agent</goal>
                        </goals>
                    </execution>
                    <execution>
                        <id>report</id>
                        <phase>test</phase>
                        <goals>
                            <goal>report</goal>
                        </goals>
                    </execution>
                    <execution>
                        <id>check</id>
                        <goals>
                            <goal>check</goal>
                        </goals>
                        <configuration>
                            <rules>
                                <rule>
                                    <element>PACKAGE</element>
                                    <limits>
                                        <limit>
                                            <counter>LINE</counter>
                                            <value>COVEREDRATIO</value>
                                            <minimum>0.85</minimum>
                                        </limit>
                                        <limit>
                                            <counter>BRANCH</counter>
                                            <value>COVEREDRATIO</value>
                                            <minimum>0.75</minimum>
                                        </limit>
                                    </limits>
                                </rule>
                            </rules>
                            <haltOnFailure>false</haltOnFailure>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
</project>
EOF
        echo "Fixed $SERVICE"
    fi
done

echo "All System-Admin POMs fixed!"
