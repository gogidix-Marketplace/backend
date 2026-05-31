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
{{- range .Values.global -}}
{{- $_ := set $.root "namespace" .namespace -}}
{{- end -}}

{{/*
Expand the name of the chart.
*/}}
{{- define "event-driven-architecture.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{/*
Create a default fully qualified app name.
*/}}
{{- define "event-driven-architecture.fullname" -}}
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
{{- define "event-driven-architecture.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{/*
Common labels
*/}}
{{- define "event-driven-architecture.labels" -}}
helm.sh/chart: {{ include "event-driven-architecture.chart" . }}
{{ include "event-driven-architecture.selectorLabels" . }}
{{- if .Chart.AppVersion -}}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
{{- end -}}
{{- end -}}

{{/*
Selector labels
*/}}
{{- define "event-driven-architecture.selectorLabels" -}}
{{- if .Values.global -}}
{{- $_ := set . "component" .Values.global.component -}}
{{- end -}}
app.kubernetes.io/name: {{ include "event-driven-architecture.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end -}}

{{/*
Service selector labels for specific service
*/}}
{{- define "event-driven-architecture.serviceSelectorLabels" -}}
app.kubernetes.io/name: {{ .name }}
app.kubernetes.io/instance: {{ $.Release.Name }}
{{- end -}}

{{/*
Create the name of the service account to use
*/}}
{{- define "event-driven-architecture.serviceAccountName" -}}
{{- if .Values.serviceAccount.create -}}
{{- default (include "event-driven-architecture.fullname" .) .Values.serviceAccount.name -}}
{{- else -}}
{{- default "default" .Values.serviceAccount.name -}}
{{- end -}}
{{- end -}}

{{/*
Get the namespace
*/}}
{{- define "event-driven-architecture.namespace" -}}
{{- if .Values.global -}}
{{- .Values.global.namespace -}}
{{- else -}}
{{- .Release.Namespace -}}
{{- end -}}
{{- end -}}

{{/*
Create a fully qualified service name
*/}}
{{- define "event-driven-architecture.serviceName" -}}
{{- printf "%s-%s" (include "event-driven-architecture.fullname" $) .name | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{/*
Create service labels
*/}}
{{- define "event-driven-architecture.serviceLabels" -}}
{{- $serviceContext := index .Values .serviceKey -}}
helm.sh/chart: {{ include "event-driven-architecture.chart" $ }}
app.kubernetes.io/name: {{ $serviceContext.name }}
app.kubernetes.io/instance: {{ $.Release.Name }}
app.kubernetes.io/managed-by: {{ $.Release.Service }}
{{- if $.Chart.AppVersion -}}
app.kubernetes.io/version: {{ $.Chart.AppVersion | quote }}
{{- end -}}
app.kubernetes.io/component: {{ .serviceKey }}
app.kubernetes.io/part-of: event-driven-architecture
{{- end -}}
