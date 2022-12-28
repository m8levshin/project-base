Command to install loki:
```
helm repo add grafana https://grafana.github.io/helm-charts
helm install loki-stack grafana/loki-stack -n monitoring --create-namespace -f helm-config.yml
```