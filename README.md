This is a smaller reproducer for [issue #4759](https://github.com/quarkusio/quarkus/issues/47259).

## Steps to reproduce

1. Start the server (requires Docker for mariadb container)

```bash
mvn quarkus:dev
```

2. Execute the requests:

Let's start with a working one.
```shell
curl --request POST \
--url http://localhost:8080/test/1 \
  --header 'Api-Key: 1234567890abcdef' \
  --header 'Content-Type: application/json'
```
It should say: it works!

Now, the next request is supposed to work too; it works on versions prior to `3.18`.
```shell
curl --request POST \
--url http://localhost:8080/test/2 \
  --header 'Api-Key: 1234567890abcdef' \
  --header 'Content-Type: application/json'
```

See the source code [here](src/main/java/org/acme/reproducer/TestResource.java).