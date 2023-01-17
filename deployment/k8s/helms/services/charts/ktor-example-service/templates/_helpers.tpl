{{ define "ktor-example-service-config.name" -}}
{{ .Chart.Name }}-service-config
{{- end }}


{{ define "ktor-example-service-secret.name" -}}
{{ .Chart.Name }}-service-config
{{- end }}