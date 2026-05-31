{{- /*
Copyright 2026 Gogidix

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/ -}}

{{/* vim: set filetype=mustache: */}}

{{/*
Expand the name of the chart.
*/}}
{{- define "centralized-dashboard.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{/*
Create a default fully qualified app name.
*/}}
{{- define "centralized-dashboard.fullname" -}}
{{- if .Values.fullnameOverride -}}
{{- .Values.fullnameOverride | trunc 63 | trimSuffix "-" -}}
{{- else -}}
{{- $name := default .Chart.Name .Values.nameOverride -}}
{{- if contains $name .Release.Name -}}
{{- .Release.Name | trunc 63 | trimSuffix "-" -}}
{{- else -}}
{{- printf "%s-%s" .Release.Name $name | trunc 63 | trimSuffix "-" -}}
{{- end -}}
{{- end -}}
{{- end -}}

{{/*
Create chart name and version as used by the chart label.
*/}}
{{- define "centralized-dashboard.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{/*
Common labels
*/}}
{{- define "centralized-dashboard.labels" -}}
helm.sh/chart: {{ include "centralized-dashboard.chart" . }}
{{ include "centralized-dashboard.selectorLabels" . }}
{{- if .Chart.AppVersion -}}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
{{- end -}}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end -}}

{{/*
Selector labels
*/}}
{{- define "centralized-dashboard.selectorLabels" -}}
app.kubernetes.io/name: {{ include "centralized-dashboard.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end -}}

{{/*
Service labels
*/}}
{{- define "centralized-dashboard.serviceLabels" -}}
{{- include "centralized-dashboard.selectorLabels" . -}}
{{- if .Values.commonLabels -}}
{{ toYaml .Values.commonLabels }}
{{- end -}}
{{- end -}}

{{/*
Create the name of the service account to use
*/}}
{{- define "centralized-dashboard.serviceAccountName" -}}
{{- if .Values.serviceAccount.create -}}
{{- default (include "centralized-dashboard.fullname" .) .Values.serviceAccount.name -}}
{{- else -}}
{{- default "default" .Values.serviceAccount.name -}}
{{- end -}}
{{- end -}}

{{/*
Get the namespace
*/}}
{{- define "centralized-dashboard.namespace" -}}
{{- if .Values.global -}}
{{- .Values.global.namespace -}}
{{- else -}}
{{- .Release.Namespace -}}
{{- end -}}
{{- end -}}

{{/*
Get the image registry
*/}}
{{- define "centralized-dashboard.imageRegistry" -}}
{{- if .Values.global -}}
{{- .Values.global.imageRegistry -}}
{{- else -}}
{{- "ghcr.io/gogidix" -}}
{{- end -}}
{{- end -}}

{{/*
Get the image tag
*/}}
{{- define "centralized-dashboard.imageTag" -}}
{{- if .Values.global -}}
{{- .Values.global.imageTag -}}
{{- else -}}
{{- "latest" -}}
{{- end -}}
{{- end -}}

{{/*
Get the image pull policy
*/}}
{{- define "centralized-dashboard.imagePullPolicy" -}}
{{- if .Values.global -}}
{{- .Values.global.imagePullPolicy -}}
{{- else -}}
{{- "IfNotPresent" -}}
{{- end -}}
{{- end -}}

{{/*
Create a fully qualified service name for a specific component
*/}}
{{- define "centralized-dashboard.serviceName" -}}
{{- printf "%s-%s" (include "centralized-dashboard.fullname" .) .name | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{/*
Create environment variable list from service config
*/}}
{{- define "centralized-dashboard.envVars" -}}
{{- range $key, $value := .envVars -}}
- name: {{ $key }}
  value: {{ $value | quote }}
{{- end -}}
{{- if .Values.global.database.host }}
- name: SPRING_DATASOURCE_URL
  value: {{ printf "jdbc:postgresql://%s:%d/%s" .Values.global.database.host (.Values.global.database.port | int) .Values.database.name | quote }}
- name: SPRING_DATASOURCE_USERNAME
  value: {{ .Values.database.username | quote }}
{{- end }}
{{- if .Values.global.redis.host }}
- name: SPRING_REDIS_HOST
  value: {{ .Values.global.redis.host | quote }}
- name: SPRING_REDIS_PORT
  value: {{ .Values.global.redis.port | quote }}
{{- end }}
{{- if .Values.global.kafka.bootstrapServers }}
- name: SPRING_KAFKA_BOOTSTRAP_SERVERS
  value: {{ .Values.global.kafka.bootstrapServers | quote }}
{{- end }}
{{- end -}}
