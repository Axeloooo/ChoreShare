# Kubernetes manifests

## Secrets

The deployments read credentials from a Secret named `choreshare-secrets`, which is
**not** stored in this repo. Create it once per cluster before applying the manifests
(Jenkins applies every `k8s/*.yml`, so don't add a Secret manifest with real values here):

```bash
kubectl create secret generic choreshare-secrets \
  --from-literal=JWT_SECRET="$(openssl rand -base64 48)" \
  --from-literal=EUREKA_USERNAME=eureka \
  --from-literal=EUREKA_PASSWORD="$(openssl rand -hex 24)" \
  --from-literal=MAIL_USERNAME=choresync@gmail.com \
  --from-literal=MAIL_PASSWORD='<gmail app password>' \
  --from-literal=PGADMIN_DEFAULT_PASSWORD="$(openssl rand -base64 24)"
```

| Key | Used by |
| --- | --- |
| `JWT_SECRET` | api-gateway, auth-service (must be the same value; base64, >= 256 bits) |
| `EUREKA_USERNAME`, `EUREKA_PASSWORD` | registry-service (basic auth) and every Eureka client's `defaultZone`. Keep the password URL-safe (hex). |
| `MAIL_USERNAME`, `MAIL_PASSWORD` | email-service |
| `PGADMIN_DEFAULT_PASSWORD` | pgadmin |

## Internal-only tools

Eureka, pgAdmin and Zipkin are not on the public ingress. Reach them with a port-forward:

```bash
kubectl port-forward svc/pgadmin-svc 5050:5050
kubectl port-forward svc/zipkin-svc 9411:9411
kubectl port-forward statefulset/eureka 8761:8761
```
