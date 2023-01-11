FROM alpine:latest AS builder

WORKDIR /opt

COPY pom.xml ./
COPY src ./src/

RUN apk add --update --no-cache openjdk11 maven --repository=http://dl-cdn.alpinelinux.org/alpine/edge/community \
 && mvn compile package


FROM azul/zulu-openjdk-alpine:11-latest

LABEL maintainer="docker@marcermarc.de"

WORKDIR /opt

COPY --from=builder /opt/target/McDockerHelper.jar .

RUN mkdir -p /opt/minecraft \
 && mkdir -p /mnt/minecraft

EXPOSE 25565:25565/tcp 25565:25565/udp

VOLUME ["/mnt/minecraft"]

ENTRYPOINT ["java", "-jar", "McDockerHelper.jar"]
CMD ["download", "start"]