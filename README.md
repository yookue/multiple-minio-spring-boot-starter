# Multiple Minio Spring Boot Starter

Spring Boot application integrates multiple `minio` quickly, which is a high performance object Storage.

## Quickstart

- Import dependencies

```xml
    <dependency>
        <groupId>com.yookue.springstarter</groupId>
        <artifactId>multiple-minio-spring-boot-starter</artifactId>
        <version>LATEST</version>
    </dependency>
```

> By default, this starter will auto take effect, you can turn it off by `spring.multiple-minio.enabled = false`

- Configure Spring Boot `application.yml` with prefix `spring.multiple-minio`

```yml
spring:
    multiple-minio:
        primary:
            host: '192.168.0.1'
            port: 9000
            secure-http: true
            access-key: 'foo1'
            secret-key: 'bar1'
            bucket-name: 'app1'
        secondary:
            host: '192.168.0.2'
            port: 9000
            secure-http: true
            access-key: 'foo2'
            secret-key: 'bar2'
            bucket-name: 'app2'
        tertiary:
            host: '192.168.0.3'
            port: 9000
            secure-http: true
            access-key: 'foo3'
            secret-key: 'bar3'
            bucket-name: 'app3'
```

> This starter supports 3 `MinioClient` at most. (Three strikes and you're out)

- Configure your beans with the following bean by `@Autowired`/`@Resource` annotation, combined with `@Qualifier` annotation (take `primary` as an example)

| Bean Type   | Qualifier                                  |
|-------------|--------------------------------------------|
| MinioClient | PrimaryMinioAutoConfiguration.MINIO_CLIENT |

## Document

- Github: https://github.com/yookue/multiple-minio-spring-boot-starter
- Minio homepage: https://min.io
- Minio github: https://github.com/minio/minio

## Requirement

- jdk 1.8+

## License

This project is under the [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0)

See the `NOTICE.txt` file for required notices and attributions.

## Donation

You like this package? Then [donate to Yookue](https://yookue.com/public/donate) to support the development.

## Website

- Yookue: https://yookue.com
