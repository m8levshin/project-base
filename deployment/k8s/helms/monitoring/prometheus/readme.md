Command to install prometheus:
```
helm repo add prometheus-community https://prometheus-community.github.io/helm
helm install prometheus -n monitoring --create-namespace  prometheus-community/prometheus -f helm-config.yml
```