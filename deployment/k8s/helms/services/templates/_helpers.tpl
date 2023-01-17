{{ define "core-config.name" -}}
{{ .Release.Name }}-core-config
{{- end }}

{{ define "core-secret.name" -}}
{{ .Release.Name }}-core-secret
{{- end }}