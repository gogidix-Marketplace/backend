#!/bin/bash
# Generate Kubernetes manifests for Warehousing Core Services

BASE_DIR="C:/Users/TEMP.LAPTOP-1QDBFFCA/Desktop/Gogidix-ecosystem/x-gogidix-domain/shared-business-infrastructure/shared-warehousing-core/k8s/warehousing-core/services"

# Array of services: name|port|component|db
declare -a SERVICES=(
    "outbound-core-service|8201|outbound|outbound"
    "fulfillment-core-service|8202|fulfillment|fulfillment"
    "picking-service|8203|picking|picking"
    "packing-service|8204|packing|packing"
    "putaway-service|8205|putaway|putaway"
    "receiving-service|8206|receiving|receiving"
    "shipping-service|8207|shipping|shipping"
    "returns-service|8208|returns|returns"
    "quality-service|8209|quality|quality"
    "inventory-core-service|8210|inventory|inventory"
    "stock-service|8211|stock|stock"
    "stock-adjustment-service|8212|stock-adjustment|stock-adjustment"
    "stock-movement-service|8213|stock-movement|stock-movement"
    "cycle-counting-service|8214|cycle-counting|cycle-counting"
    "serialization-service|8215|serialization|serialization"
    "batch-service|8216|batch|batch"
    "expiration-service|8217|expiration|expiration"
    "reorder-service|8218|reorder|reorder"
    "location-service|8220|location|location"
    "warehouse-config-service|8221|warehouse-config|warehouse-config"
    "zone-service|8222|zone|zone"
    "shelf-service|8224|shelf|shelf"
    "bin-service|8225|bin|bin"
    "self-storage-service|8230|self-storage|self-storage"
    "space-service|8231|space|space"
    "space-allocation-service|8232|space-allocation|space-allocation"
    "storage-reservation-service|8233|storage-reservation|storage-reservation"
    "tenant-config-service|8240|tenant-config|tenant-config"
    "public-booking-service|8280|public-booking|public-booking"
    "public-pricing-service|8281|public-pricing|public-pricing"
    "public-availability-service|8282|public-availability|public-availability"
)

generate_deployment() {
    local name=$1
    local port=$2
    local component=$3
    local db=$4
    local mgmt_port=$((port + 1))

    cat > "$BASE_DIR/$name/deployment.yaml" << EOF
apiVersion: apps/v1
kind: Deployment
metadata:
  name: $name
  namespace: warehousing-core
  labels:
    app: $name
    version: v1
    component: $component
    part-of: warehousing-core
spec:
  replicas: 3
  revisionHistoryLimit: 3
  selector:
    matchLabels:
      app: $name
  strategy:
    type: RollingUpdate
    rollingUpdate:
      maxSurge: 1
      maxUnavailable: 0
  template:
    metadata:
      labels:
        app: $name
        version: v1
      annotations:
        prometheus.io/scrape: "true"
        prometheus.io/port: "$mgmt_port"
        prometheus.io/path: "/actuator/prometheus"
    spec:
      serviceAccountName: warehousing-service-account
      securityContext:
        runAsNonRoot: true
        runAsUser: 1000
        fsGroup: 1000
      containers:
      - name: $name
        image: gogidix/$name:latest
        imagePullPolicy: Always
        ports:
        - name: http
          containerPort: $port
          protocol: TCP
        - name: management
          containerPort: $mgmt_port
          protocol: TCP
        env:
        - name: SPRING_PROFILES_ACTIVE
          valueFrom:
            configMapKeyRef:
              name: warehousing-common-config
              key: SPRING_PROFILES_ACTIVE
        - name: SERVER_PORT
          value: "$port"
        - name: MANAGEMENT_SERVER_PORT
          value: "$mgmt_port"
        - name: SPRING_APPLICATION_NAME
          value: "$name"
        - name: EUREKA_CLIENT_SERVICEURL_DEFAULTZONE
          valueFrom:
            configMapKeyRef:
              name: warehousing-common-config
              key: EUREKA_CLIENT_SERVICEURL_DEFAULTZONE
        - name: KAFKA_BOOTSTRAP_SERVERS
          valueFrom:
            configMapKeyRef:
              name: warehousing-common-config
              key: KAFKA_BOOTSTRAP_SERVERS
        - name: MONGODB_USERNAME
          valueFrom:
            secretKeyRef:
              name: warehousing-common-secret
              key: MONGODB_USERNAME
        - name: MONGODB_PASSWORD
          valueFrom:
            secretKeyRef:
              name: warehousing-common-secret
              key: MONGODB_PASSWORD
        - name: SPRING_DATA_MONGODB_URI
          value: "mongodb://\$(MONGODB_USERNAME):\$(MONGODB_PASSWORD)@mongodb-warehousing.mongodb.svc.cluster.local:27017/$db?authSource=admin"
        - name: SPRING_REDIS_HOST
          valueFrom:
            configMapKeyRef:
              name: warehousing-common-config
              key: REDIS_HOST
        - name: SPRING_REDIS_PORT
          valueFrom:
            configMapKeyRef:
              name: warehousing-common-config
              key: REDIS_PORT
        - name: SPRING_REDIS_PASSWORD
          valueFrom:
            secretKeyRef:
              name: redis-warehousing-secret
              key: redis-password
              optional: true
        resources:
          requests:
            memory: "256Mi"
            cpu: "250m"
          limits:
            memory: "512Mi"
            cpu: "500m"
        livenessProbe:
          httpGet:
            path: /actuator/health/liveness
            port: management
          initialDelaySeconds: 60
          periodSeconds: 15
          timeoutSeconds: 5
          failureThreshold: 3
        readinessProbe:
          httpGet:
            path: /actuator/health/readiness
            port: management
          initialDelaySeconds: 30
          periodSeconds: 10
          timeoutSeconds: 5
          failureThreshold: 3
        startupProbe:
          httpGet:
            path: /actuator/health/startup
            port: management
          initialDelaySeconds: 10
          periodSeconds: 10
          timeoutSeconds: 5
          failureThreshold: 30
        volumeMounts:
        - name: tmp
          mountPath: /tmp
      volumes:
      - name: tmp
        emptyDir: {}
      affinity:
        podAntiAffinity:
          preferredDuringSchedulingIgnoredDuringExecution:
          - weight: 100
            podAffinityTerm:
              labelSelector:
                matchExpressions:
                - key: app
                  operator: In
                  values:
                  - $name
              topologyKey: kubernetes.io/hostname
EOF
}

generate_service() {
    local name=$1
    local port=$2
    local mgmt_port=$((port + 1))

    cat > "$BASE_DIR/$name/service.yaml" << EOF
apiVersion: v1
kind: Service
metadata:
  name: $name
  namespace: warehousing-core
  labels:
    app: $name
    service: $name
spec:
  type: ClusterIP
  sessionAffinity: None
  ports:
  - name: http
    port: $port
    targetPort: http
    protocol: TCP
  - name: management
    port: $mgmt_port
    targetPort: management
    protocol: TCP
  selector:
    app: $name
---
apiVersion: v1
kind: Service
metadata:
  name: $name-headless
  namespace: warehousing-core
  labels:
    app: $name
spec:
  type: ClusterIP
  clusterIP: None
  ports:
  - name: http
    port: $port
    targetPort: http
    protocol: TCP
  selector:
    app: $name
EOF
}

generate_hpa() {
    local name=$1

    cat > "$BASE_DIR/$name/hpa.yaml" << EOF
apiVersion: autoscaling/v2
kind: HorizontalPodAutoscaler
metadata:
  name: $name-hpa
  namespace: warehousing-core
  labels:
    app: $name
spec:
  scaleTargetRef:
    apiVersion: apps/v1
    kind: Deployment
    name: $name
  minReplicas: 2
  maxReplicas: 10
  metrics:
  - type: Resource
    resource:
      name: cpu
      target:
        type: Utilization
        averageUtilization: 70
  - type: Resource
    resource:
      name: memory
      target:
        type: Utilization
        averageUtilization: 80
  behavior:
    scaleDown:
      stabilizationWindowSeconds: 300
      policies:
      - type: Percent
        value: 50
        periodSeconds: 60
    scaleUp:
      stabilizationWindowSeconds: 0
      policies:
      - type: Percent
        value: 100
        periodSeconds: 30
      - type: Pods
        value: 2
        periodSeconds: 30
      selectPolicy: Max
---
apiVersion: policy/v1
kind: PodDisruptionBudget
metadata:
  name: $name-pdb
  namespace: warehousing-core
spec:
  minAvailable: 1
  selector:
    matchLabels:
      app: $name
EOF
}

# Generate manifests for each service
for service in "${SERVICES[@]}"; do
    IFS='|' read -r name port component db <<< "$service"

    echo "Generating manifests for $name..."

    # Create directory
    mkdir -p "$BASE_DIR/$name"

    # Generate files
    generate_deployment "$name" "$port" "$component" "$db"
    generate_service "$name" "$port"
    generate_hpa "$name"

    echo "  - deployment.yaml"
    echo "  - service.yaml"
    echo "  - hpa.yaml"
done

echo ""
echo "Total services: ${#SERVICES[@]}"
echo "All manifests generated successfully!"
