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
{{- define "transaction-orchestration.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{/*
Create a default fully qualified app name.
*/}}
{{- define "transaction-orchestration.fullname" -}}
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
{{- define "transaction-orchestration.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{/*
Common labels
*/}}
{{- define "transaction-orchestration.labels" -}}
helm.sh/chart: {{ include "transaction-orchestration.chart" . }}
{{ include "transaction-orchestration.selectorLabels" . }}
{{- if .Chart.AppVersion -}}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
{{- end -}}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end -}}

{{/*
Selector labels
*/}}
{{- define "transaction-orchestration.selectorLabels" -}}
{{- if .Values.global -}}
{{- $_ := set . "component" .Values.global.component -}}
{{- end -}}
app.kubernetes.io/name: {{ include "transaction-orchestration.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end -}}

{{/*
Service selector labels for specific service
*/}}
{{- define "transaction-orchestration.serviceSelectorLabels" -}}
app.kubernetes.io/name: {{ include "transaction-orchestration.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
app.kubernetes.io/component: {{ .component }}
{{- end -}}

{{/*
Create the name of the service account to use
*/}}
{{- define "transaction-orchestration.serviceAccountName" -}}
{{- if .Values.serviceAccount.create -}}
{{- default (include "transaction-orchestration.fullname" .) .Values.serviceAccount.name -}}
{{- else -}}
{{- default "default" .Values.serviceAccount.name -}}
{{- end -}}
{{- end -}}

{{/*
Get the namespace
*/}}
{{- define "transaction-orchestration.namespace" -}}
{{- if .Values.global -}}
{{- .Values.global.namespace -}}
{{- else -}}
{{- .Release.Namespace -}}
{{- end -}}
{{- end -}}

{{/*
Create a fully qualified service name
*/}}
{{- define "transaction-orchestration.serviceName" -}}
{{- printf "%s-%s" (include "transaction-orchestration.fullname" .) .serviceName | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{/*
Create image pull secret reference
*/}}
{{- define "transaction-orchestration.imagePullSecrets" -}}
{{- if .Values.global.imagePullSecrets -}}
{{- toYaml .Values.global.imagePullSecrets -}}
{{- end -}}
{{- end -}}

{{/*
Create service port
*/}}
{{- define "transaction-orchestration.servicePort" -}}
{{- default .Values.servicePort .Values.global.servicePort -}}
{{- end -}}

{{/*
Create management port
*/}}
{{- define "transaction-orchestration.managementPort" -}}
{{- default .Values.managementPort .Values.global.managementPort -}}
{{- end -}}

{{/*
Determine if HPA should be enabled
*/}}
{{- define "transaction-orchestration.hpaEnabled" -}}
{{- if .Values.hpa.enabled -}}
{{- .Values.hpa.enabled -}}
{{- else -}}
{{- if hasKey . "hpa" -}}
{{- .Values.hpa.enabled -}}
{{- else -}}
{{- $.Values.hpa.enabled -}}
{{- end -}}
{{- end -}}
{{- end -}}

{{/*
Get service image
*/}}
{{- define "transaction-orchestration.image" -}}
{{- $registry := .Values.global.imageRegistry -}}
{{- $tag := .Values.global.imageTag -}}
{{- if .Values.image.registry -}}
{{- $registry = .Values.image.registry -}}
{{- end -}}
{{- if .Values.image.tag -}}
{{- $tag = .Values.image.tag -}}
{{- end -}}
{{- printf "%s/%s:%s" $registry .serviceName $tag -}}
{{- end -}}
