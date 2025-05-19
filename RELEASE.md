/
/
/
/
/
/
/
/
/
Get current latest version
```sh
curl -s "https://registry.hub.docker.com/v2/repositories/shykhov/money-glitch.services.backtest/tags?page_size=100"\
 | grep -o '"name":"[0-9]*\.[0-9]*\.[0-9]*"' \
 | grep -o '[0-9]*\.[0-9]*\.[0-9]*' \
 | head -1

```
Release (!!change version before release!!)
```sh
    ./gradlew clean
    ./gradlew bootJar
```

```sh
    docker buildx build \
    --platform linux/amd64 \
    -t shykhov/money-glitch.services.backtest:0.0.331 . \
    --push
```







```shell
kubectl port-forward svc/backtest-db-service 5439:5432 -n application
```
