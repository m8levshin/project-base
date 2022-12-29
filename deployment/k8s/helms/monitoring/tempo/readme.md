Command to install loki:
```
helm repo add grafana https://grafana.github.io/helm-charts
helm install tempo grafana/tempo -n monitoring --create-namespace -f helm-config.yml
```