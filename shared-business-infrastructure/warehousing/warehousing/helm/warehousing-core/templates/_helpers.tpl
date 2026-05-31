{{/*
===============================================================================
Warehousing Core Helm Chart - Template Helpers
===============================================================================
*/}}

{{/*
Expand the name of the chart.
*/}}
{{- define "warehousing-core.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Create a default fully qualified app name.
*/}}
{{- define "warehousing-core.fullname" -}}
{{- if .Values.fullnameOverride }}
{{- .Values.fullnameOverride | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- $name := default .Chart.Name .Values.nameOverride }}
{{- if contains $name .Release.Name }}
{{- .Release.Name | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- printf "%s-%s" .Release.Name $name | trunc 63 | trimSuffix "-" }}
{{- end }}
{{- end }}
{{- end }}

{{/*
Create chart name and version as used by the chart label.
*/}}
{{- define "warehousing-core.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Common labels
*/}}
{{- define "warehousing-core.labels" -}}
helm.sh/chart: {{ include "warehousing-core.chart" . }}
{{ include "warehousing-core.selectorLabels" . }}
{{- if .Chart.AppVersion }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
{{- end }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- range $key, $value := .Values.global.labels }}
{{ $key }}: {{ $value }}
{{- end }}
{{- end }}

{{/*
Selector labels
*/}}
{{- define "warehousing-core.selectorLabels" -}}
app.kubernetes.io/name: {{ include "warehousing-core.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
app.kubernetes.io/component: warehousing-core
{{- end }}

{{/*
Service account name
*/}}
{{- define "warehousing-core.serviceAccountName" -}}
{{- if .Values.global.serviceAccount.create }}
{{- default (include "warehousing-core.fullname" .) .Values.global.serviceAccount.name }}
{{- else }}
{{- default "default" .Values.global.serviceAccount.name }}
{{- end }}
{{- end }}

{{/*
Service labels for a specific service
*/}}
{{- define "warehousing-core.serviceLabels" -}}
{{- $serviceName := index . 0 }}
{{- $root := index . 1 }}
app.kubernetes.io/name: {{ $serviceName }}
app.kubernetes.io/instance: {{ $root.Release.Name }}
app.kubernetes.io/component: service
app.kubernetes.io/part-of: warehousing-core
{{- range $key, $value := $root.Values.global.labels }}
{{ $key }}: {{ $value }}
{{- end }}
{{- end }}

{{/*
Service selector labels for a specific service
*/}}
{{- define "warehousing-core.serviceSelectorLabels" -}}
{{- $serviceName := index . 0 }}
app.kubernetes.io/name: {{ $serviceName }}
app.kubernetes.io/instance: {{ index . 1 | default "default" }}
{{- end }}

{{/*
Deployment labels for a specific service
*/}}
{{- define "warehousing-core.deploymentLabels" -}}
{{- $serviceName := index . 0 }}
{{- $root := index . 1 }}
app.kubernetes.io/name: {{ $serviceName }}
app.kubernetes.io/instance: {{ $root.Release.Name }}
app.kubernetes.io/component: deployment
app.kubernetes.io/part-of: warehousing-core
{{- range $key, $value := $root.Values.global.labels }}
{{ $key }}: {{ $value }}
{{- end }}
{{- end }}

{{/*
Pod labels for a specific service
*/}}
{{- define "warehousing-core.podLabels" -}}
{{- $serviceName := index . 0 }}
{{- $root := index . 1 }}
app.kubernetes.io/name: {{ $serviceName }}
app.kubernetes.io/instance: {{ $root.Release.Name }}
app.kubernetes.io/component: pod
app.kubernetes.io/part-of: warehousing-core
{{- range $key, $value := $root.Values.global.labels }}
{{ $key }}: {{ $value }}
{{- end }}
{{- end }}

{{/*
Pod annotations for a specific service
*/}}
{{- define "warehousing-core.podAnnotations" -}}
{{- $root := index . 1 }}
{{- range $key, $value := $root.Values.global.annotations }}
{{ $key }}: {{ $value | quote }}
{{- end }}
{{- end }}

{{/*
Get the image repository with registry prefix
*/}}
{{- define "warehousing-core.imageRepository" -}}
{{- $serviceConfig := index . 0 }}
{{- $root := index . 1 }}
{{- if $serviceConfig.image.registry }}
{{- printf "%s/%s" $serviceConfig.image.registry $serviceConfig.image.repository }}
{{- else if $root.Values.global.imageRegistry }}
{{- printf "%s/%s" $root.Values.global.imageRegistry $serviceConfig.image.repository }}
{{- else }}
{{- $serviceConfig.image.repository }}
{{- end }}
{{- end }}

{{/*
Get the image tag
*/}}
{{- define "warehousing-core.imageTag" -}}
{{- $serviceConfig := index . 0 }}
{{- $root := index . 1 }}
{{- if $serviceConfig.image.tag }}
{{- $serviceConfig.image.tag }}
{{- else }}
{{- $root.Chart.AppVersion }}
{{- end }}
{{- end }}

{{/*
Get the image pull policy
*/}}
{{- define "warehousing-core.imagePullPolicy" -}}
{{- $serviceConfig := index . 0 }}
{{- $root := index . 1 }}
{{- if $serviceConfig.image.pullPolicy }}
{{- $serviceConfig.image.pullPolicy }}
{{- else if $root.Values.global.imagePullPolicy }}
{{- $root.Values.global.imagePullPolicy }}
{{- else }}
{{- "IfNotPresent" }}
{{- end }}
{{- end }}

{{/*
Get the MongoDB connection URI
*/}}
{{- define "warehousing-core.mongodb.uri" -}}
{{- $root := index . 0 }}
{{- $serviceName := index . 1 | default "default" }}
{{- if $root.Values.mongodb.enabled }}
{{- if $root.Values.mongodb.auth.enabled }}
{{- $user := $root.Values.mongodb.auth.rootUser | default "admin" }}
{{- $host := printf "%s-0.%s-headless.%s.svc.cluster.local:27017" (include "mongodb.fullname" $root) (include "mongodb.fullname" $root) (include "mongodb.namespace" $root) }}
mongodb://{{ $user }}:${MONGODB_PASSWORD}@{{ $host }}/warehousing_{{ $serviceName }}?authSource=admin{{- if $root.Values.mongodb.architecture }}&replicaSet={{ $root.Values.mongodb.replicaSet.name | default "rs0" }}{{- end }}
{{- else }}
mongodb://{{ $host }}/warehousing_{{ $serviceName }}
{{- end }}
{{- end }}
{{- end }}

{{/*
Get the Kafka bootstrap servers
*/}}
{{- define "warehousing-core.kafka.bootstrapServers" -}}
{{- $root := index . 0 }}
{{- if $root.Values.kafka.enabled }}
{{- range $i, $e := until (int $root.Values.kafka.controller.replicaCount) }}
{{- if $i }},{{- end }}
kafka-controller-{{ $i }}.kafka-controller-headless.{{ $root.Release.Namespace }}.svc.cluster.local:9092
{{- end }}
{{- end }}
{{- end }}

{{/*
Get the Redis host
*/}}
{{- define "warehousing-core.redis.host" -}}
{{- $root := index . 0 }}
{{- if $root.Values.redis.enabled }}
redis-master.{{ $root.Release.Namespace }}.svc.cluster.local
{{- end }}
{{- end }}

{{/*
Merge global and service-specific environment variables
*/}}
{{- define "warehousing-core.envVars" -}}
{{- $serviceConfig := index . 0 }}
{{- $root := index . 1 }}
{{- $globalEnv := dict }}
{{- /* Add global environment variables */ -}}
{{- if $root.Values.global.envVars }}
{{- range $key, $value := $root.Values.global.envVars }}
{{- $_ := set $globalEnv $key $value }}
{{- end }}
{{- end }}
{{- /* Add service-specific environment variables */ -}}
{{- if $serviceConfig.envVars }}
{{- range $key, $value := $serviceConfig.envVars }}
{{- $_ := set $globalEnv $key $value }}
{{- end }}
{{- end }}
{{- /* Add common environment variables */ -}}
{{- $_ := set $globalEnv "SPRING_PROFILES_ACTIVE" (default "cloud" (get $globalEnv "SPRING_PROFILES_ACTIVE")) }}
{{- $_ := set $globalEnv "JAVA_OPTS" (default "-XX:+UseG1GC -XX:MaxRAMPercentage=75.0" (get $globalEnv "JAVA_OPTS")) }}
{{- $globalEnv }}
{{- end }}

{{/*
Merge global and service-specific resources
*/}}
{{- define "warehousing-core.resources" -}}
{{- $serviceConfig := index . 0 }}
{{- $root := index . 1 }}
{{- $globalResources := $root.Values.global.resources }}
{{- if $serviceConfig.resources }}
{{- toYaml $serviceConfig.resources }}
{{- else }}
{{- toYaml $globalResources }}
{{- end }}
{{- end }}

{{/*
Merge global and service-specific health probes
*/}}
{{- define "warehousing-core.livenessProbe" -}}
{{- $serviceConfig := index . 0 }}
{{- $root := index . 1 }}
{{- if $serviceConfig.livenessProbe }}
{{- toYaml $serviceConfig.livenessProbe }}
{{- else }}
{{- toYaml $root.Values.global.livenessProbe }}
{{- end }}
{{- end }}

{{- define "warehousing-core.readinessProbe" -}}
{{- $serviceConfig := index . 0 }}
{{- $root := index . 1 }}
{{- if $serviceConfig.readinessProbe }}
{{- toYaml $serviceConfig.readinessProbe }}
{{- else }}
{{- toYaml $root.Values.global.readinessProbe }}
{{- end }}
{{- end }}

{{- define "warehousing-core.startupProbe" -}}
{{- $serviceConfig := index . 0 }}
{{- $root := index . 1 }}
{{- if $serviceConfig.startupProbe }}
{{- toYaml $serviceConfig.startupProbe }}
{{- else if $root.Values.global.startupProbe }}
{{- toYaml $root.Values.global.startupProbe }}
{{- end }}
{{- end }}

{{/*
Get service names list for template iteration
*/}}
{{- define "warehousing-core.services" -}}
{{- $root := . }}
{{- $services := list }}
{{- range $key, $value := $root.Values }}
{{- if or (hasSuffix "Service" $key) (hasSuffix "service" $key) }}
{{- if $value.enabled }}
{{- $services = append $services $key }}
{{- end }}
{{- end }}
{{- end }}
{{- $services }}
{{- end }}

{{/*
Convert service name to kebab-case
*/}}
{{- define "warehousing-core.serviceName" -}}
{{- $serviceName := index . 0 }}
{{- $serviceName | lower | replace "_" "-" }}
{{- end }}

{{/*
Generate unique identifier
*/}}
{{- define "warehousing-core.uniqueId" -}}
{{- printf "%s-%s" (include "warehousing-core.fullname" .) (now | date "20060102150405") | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Check if service mesh is enabled
*/}}
{{- define "warehousing-core.serviceMeshEnabled" -}}
{{- $root := . }}
{{- if $root.Values.serviceMesh }}
{{- if $root.Values.serviceMesh.enabled }}
{{- true }}
{{- end }}
{{- end }}
{{- end }}

{{/*
Get network policy egress rules
*/}}
{{- define "warehousing-core.networkPolicyEgress" -}}
{{- $root := . }}
{{- if $root.Values.networkPolicy.enabled }}
{{- $root.Values.networkPolicy.egress }}
{{- end }}
{{- end }}

{{/*
Get the full service name for a service key
*/}}
{{- define "warehousing-core.getServiceName" -}}
{{- $serviceKey := index . 0 }}
{{- $root := index . 1 }}
{{- $serviceConfig := index $root.Values $serviceKey }}
{{- $serviceConfig.name | default $serviceKey }}
{{- end }}

{{/*
Get the service port based on service type
*/}}
{{- define "warehousing-core.servicePort" -}}
{{- $serviceConfig := index . 0 }}
{{- $serviceConfig.ports.http | default 8200 }}
{{- end }}

{{/*
Get the management port for a service
*/}}
{{- define "warehousing-core.managementPort" -}}
{{- $serviceConfig := index . 0 }}
{{- $serviceConfig.ports.management | default (add1 $serviceConfig.ports.http) }}
{{- end }}
