FROM alpine:latest AS builder

WORKDIR /opt

COPY * ./

RUN apk add --update --no-cache openjdk11 maven --repository=http://dl-cdn.alpinelinux.org/alpine/edge/community \
 && mvn clean package


FROM alpine:latest

LABEL maintainer="docker@marcermarc.de"

WORKDIR /opt

COPY --from=builder /opt/target/McDockerHelper.jar .

RUN apk add --update --no-cache openjdk11 --repository=http://dl-cdn.alpinelinux.org/alpine/edge/community \
 && mkdir -p /opt/minecraft \
 && mkdir -p /mnt/minecraft

EXPOSE 25565:25565/tcp 25565:25565/udp

VOLUME ["/mnt/minecraft"]

ENTRYPOINT ["java", "-jar", "McDockerHelper.jar"]
CMD ["download", "start"]