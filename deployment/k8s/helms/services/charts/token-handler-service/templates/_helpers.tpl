{{ define "token-handler-service-config.name" -}}
{{ .Chart.Name }}-service-config
{{- end }}


{{ define "token-handler-service-secret.name" -}}
{{ .Chart.Name }}-service-config
{{- end }}