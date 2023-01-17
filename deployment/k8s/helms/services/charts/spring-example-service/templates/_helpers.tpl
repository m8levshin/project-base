{{ define "spring-example-service-config.name" -}}
{{ .Chart.Name }}-service-config
{{- end }}


{{ define "spring-example-service-secret.name" -}}
{{ .Chart.Name }}-service-config
{{- end }}