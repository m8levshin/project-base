Command to install jaeger:
```
helm repo add jaegertracing https://jaegertracing.github.io/helm-charts
helm install jaeger jaegertracing/jaeger -n monitoring --create-namespace -f helm-config.yml
```