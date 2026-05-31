{{/*
Expand the name of the chart.
*/}}
{{- define "ai-services.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Create a default fully qualified app name.
*/}}
{{- define "ai-services.fullname" -}}
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
{{- define "ai-services.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Common labels
*/}}
{{- define "ai-services.labels" -}}
helm.sh/chart: {{ include "ai-services.chart" . }}
{{ include "ai-services.selectorLabels" . }}
{{- if .Chart.AppVersion }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
{{- end }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}

{{/*
Selector labels
*/}}
{{- define "ai-services.selectorLabels" -}}
app.kubernetes.io/name: {{ include "ai-services.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end }}

{{/*
Create the name of the service account to use
*/}}
{{- define "ai-services.serviceAccountName" -}}
{{- if .Values.serviceAccount.create }}
{{- default (include "ai-services.fullname" .) .Values.serviceAccount.name }}
{{- else }}
{{- default "default" .Values.serviceAccount.name }}
{{- end }}
{{- end }}

{{/*
Service name for a specific service
*/}}
{{- define "ai-services.serviceName" -}}
{{- printf "%s.%s.svc.cluster.local" . (include "ai-services.namespace" .) }}
{{- end }}

{{/*
Get the namespace
*/}}
{{- define "ai-services.namespace" -}}
{{- default .Release.Namespace .Values.namespace.name }}
{{- end }}

{{/*
Service specific labels
*/}}
{{- define "ai-services.serviceLabels" -}}
app: {{ .name }}
team: {{ .team }}
app.kubernetes.io/name: {{ .name }}
app.kubernetes.io/component: service
app.kubernetes.io/part-of: ai-services
{{- end }}

{{/*
Service specific selector labels
*/}}
{{- define "ai-services.serviceSelectorLabels" -}}
app: {{ .name }}
team: {{ .team }}
{{- end }}

{{/*
Check if a service is enabled
*/}}
{{- define "ai-services.serviceEnabled" -}}
{{- if hasKey . "enabled" }}
{{- .enabled }}
{{- else }}
{{- true }}
{{- end }}
{{- end }}

{{/*
Get service image
*/}}
{{- define "ai-services.serviceImage" -}}
{{- if .image }}
{{- if .image.repository }}
{{- .image.repository }}:{{ .image.tag | default "latest" }}
{{- else }}
{{- printf "%s/%s:%s" (default "gogidix" .global.imageRegistry) .name (default "1.0.0" .image.tag) }}
{{- end }}
{{- else }}
{{- printf "gogidix/%s:1.0.0" .name }}
{{- end }}
{{- end }}

{{/*
Get service port
*/}}
{{- define "ai-services.servicePort" -}}
{{- default 8080 .port }}
{{- end }}

{{/*
Java Options
*/}}
{{- define "ai-services.javaOpts" -}}
{{- if .javaOpts }}
{{- .javaOpts }}
{{- else if .global.javaOpts }}
{{- .global.javaOpts }}
{{- else }}
-Xms512m -Xmx1024m -XX:+UseG1GC -XX:MaxGCPauseMillis=200
{{- end }}
{{- end }}

{{/*
Merge global env with service specific env
*/}}
{{- define "ai-services.serviceEnv" -}}
{{- $globalEnv := .global.env | default dict }}
{{- $serviceEnv := .env | default dict }}
{{- $merged := merge $serviceEnv $globalEnv }}
{{- toYaml $merged }}
{{- end }}

{{/*
Get resource requests
*/}}
{{- define "ai-services.resourceRequests" -}}
{{- if .resources }}
{{- if .resources.requests }}
{{- toYaml .resources.requests }}
{{- else if .global.resources }}
{{- toYaml .global.resources.requests }}
{{- else }}
memory: "768Mi"
cpu: "250m"
{{- end }}
{{- else }}
memory: "768Mi"
cpu: "250m"
{{- end }}
{{- end }}

{{/*
Get resource limits
*/}}
{{- define "ai-services.resourceLimits" -}}
{{- if .resources }}
{{- if .resources.limits }}
{{- toYaml .resources.limits }}
{{- else if .global.resources }}
{{- toYaml .global.resources.limits }}
{{- else }}
memory: "1536Mi"
cpu: "1000m"
{{- end }}
{{- else }}
memory: "1536Mi"
cpu: "1000m"
{{- end }}
{{- end }}

{{/*
Get security context
*/}}
{{- define "ai-services.podSecurityContext" -}}
{{- if .securityContext }}
{{- toYaml .securityContext }}
{{- else if .global.podSecurityContext }}
{{- toYaml .global.podSecurityContext }}
{{- else }}
runAsNonRoot: true
runAsUser: 1000
fsGroup: 1000
{{- end }}
{{- end }}

{{/*
Get container security context
*/}}
{{- define "ai-services.containerSecurityContext" -}}
{{- if .containerSecurityContext }}
{{- toYaml .containerSecurityContext }}
{{- else if .global.securityContext }}
{{- toYaml .global.securityContext }}
{{- else }}
allowPrivilegeEscalation: false
readOnlyRootFilesystem: false
capabilities:
  drop:
    - ALL
seccompProfile:
  type: RuntimeDefault
{{- end }}
{{- end }}

{{/*
Get image pull policy
*/}}
{{- define "ai-services.imagePullPolicy" -}}
{{- if .imagePullPolicy }}
{{- .imagePullPolicy }}
{{- else if .global.imagePullPolicy }}
{{- .global.imagePullPolicy }}
{{- else }}
Always
{{- end }}
{{- end }}

{{/*
Get probe configuration
*/}}
{{- define "ai-services.livenessProbe" -}}
{{- if and .probes .probes.liveness .probes.liveness.enabled }}
httpGet:
  path: {{ .probes.liveness.path | default "/actuator/health/liveness" }}
  port: {{ .port | default 8080 }}
initialDelaySeconds: {{ .probes.liveness.initialDelaySeconds | default 60 }}
periodSeconds: {{ .probes.liveness.periodSeconds | default 15 }}
timeoutSeconds: {{ .probes.liveness.timeoutSeconds | default 5 }}
failureThreshold: {{ .probes.liveness.failureThreshold | default 3 }}
{{- end }}
{{- end }}

{{/*
Get readiness probe configuration
*/}}
{{- define "ai-services.readinessProbe" -}}
{{- if and .probes .probes.readiness .probes.readiness.enabled }}
httpGet:
  path: {{ .probes.readiness.path | default "/actuator/health/readiness" }}
  port: {{ .port | default 8080 }}
initialDelaySeconds: {{ .probes.readiness.initialDelaySeconds | default 45 }}
periodSeconds: {{ .probes.readiness.periodSeconds | default 10 }}
timeoutSeconds: {{ .probes.readiness.timeoutSeconds | default 5 }}
failureThreshold: {{ .probes.readiness.failureThreshold | default 3 }}
{{- end }}
{{- end }}

{{/*
Get startup probe configuration
*/}}
{{- define "ai-services.startupProbe" -}}
{{- if and .probes .probes.startup .probes.startup.enabled }}
httpGet:
  path: {{ .probes.startup.path | default "/actuator/health/startup" }}
  port: {{ .port | default 8080 }}
initialDelaySeconds: {{ .probes.startup.initialDelaySeconds | default 10 }}
periodSeconds: {{ .probes.startup.periodSeconds | default 10 }}
timeoutSeconds: {{ .probes.startup.timeoutSeconds | default 5 }}
failureThreshold: {{ .probes.startup.failureThreshold | default 18 }}
{{- end }}
{{- end }}

{{/*
Get prometheus annotations
*/}}
{{- define "ai-services.prometheusAnnotations" -}}
{{- if and .monitoring .monitoring.enabled }}
prometheus.io/scrape: "true"
prometheus.io/port: "{{ .port | default 8080 }}"
prometheus.io/path: {{ .monitoring.prometheus.path | default "/actuator/prometheus" }}
{{- end }}
{{- end }}

{{/*
Get node selector
*/}}
{{- define "ai-services.nodeSelector" -}}
{{- if .nodeSelector }}
{{- toYaml .nodeSelector }}
{{- end }}
{{- end }}

{{/*
Get tolerations
*/}}
{{- define "ai-services.tolerations" -}}
{{- if .tolerations }}
{{- toYaml .tolerations }}
{{- end }}
{{- end }}

{{/*
Get affinity
*/}}
{{- define "ai-services.affinity" -}}
{{- if .affinity }}
{{- toYaml .affinity }}
{{- else }}
{{- if and .global (not .affinity) }}
podAntiAffinity:
  preferredDuringSchedulingIgnoredDuringExecution:
    - weight: 100
      podAffinityTerm:
        labelSelector:
          matchExpressions:
            - key: app
              operator: In
              values:
                - {{ .name }}
        topologyKey: kubernetes.io/hostname
{{- end }}
{{- end }}
{{- end }}

{{/*
Get HPA configuration
*/}}
{{- define "ai-services.hpaEnabled" -}}
{{- and .hpa .hpa.enabled }}
{{- end }}

{{/*
Get PDB configuration
*/}}
{{- define "ai-services.pdbEnabled" -}}
{{- and .pdb .pdb.enabled }}
{{- end }}

{{/*
Get ingress enabled
*/}}
{{- define "ai-services.ingressEnabled" -}}
{{- if and .ingress }}
{{- if hasKey .ingress "enabled" }}
{{- .ingress.enabled }}
{{- else }}
{{- true }}
{{- end }}
{{- else }}
{{- false }}
{{- end }}
{{- end }}

{{/*
Team label mapping
*/}}
{{- define "ai-services.teamLabel" -}}
{{- .team | default "team-ai-platform" }}
{{- end }}

{{/*
Cost center label mapping
*/}}
{{- define "ai-services.costCenter" -}}
{{- .costCenter | default "engineering-ai" }}
{{- end }}
