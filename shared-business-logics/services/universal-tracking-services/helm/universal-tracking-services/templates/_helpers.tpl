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
{{- define "universal-tracking-services.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{/*
Create a default fully qualified app name.
*/}}
{{- define "universal-tracking-services.fullname" -}}
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
{{- define "universal-tracking-services.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{/*
Common labels
*/}}
{{- define "universal-tracking-services.labels" -}}
helm.sh/chart: {{ include "universal-tracking-services.chart" . }}
{{ include "universal-tracking-services.selectorLabels" . }}
{{- if .Chart.AppVersion -}}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
{{- end -}}
{{- end -}}

{{/*
Selector labels
*/}}
{{- define "universal-tracking-services.selectorLabels" -}}
{{- if .Values.global -}}
{{- $_ := set . "component" .Values.global.component -}}
{{- end -}}
app.kubernetes.io/name: {{ include "universal-tracking-services.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end -}}

{{/*
Create the name of the service account to use
*/}}
{{- define "universal-tracking-services.serviceAccountName" -}}
{{- if .Values.serviceAccount.create -}}
{{- default (include "universal-tracking-services.fullname" .) .Values.serviceAccount.name -}}
{{- else -}}
{{- default "default" .Values.serviceAccount.name -}}
{{- end -}}
{{- end -}}

{{/*
Get the namespace
*/}}
{{- define "universal-tracking-services.namespace" -}}
{{- if .Values.global -}}
{{- .Values.global.namespace -}}
{{- else -}}
{{- .Release.Namespace -}}
{{- end -}}
{{- end -}}
