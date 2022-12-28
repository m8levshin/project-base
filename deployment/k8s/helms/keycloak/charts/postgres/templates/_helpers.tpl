{{ define "keycloak-pg.secret-name" -}}
{{.Release.Name}}-pg-db-credential
{{- end }}


{{ define "keycloak-pg.service-name" -}}
{{.Release.Name}}-pg-service
{{- end }}