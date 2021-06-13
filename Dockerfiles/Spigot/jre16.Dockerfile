FROM marcermarc/minecraft:jdk16 AS builder

ARG VERSION

ENV TYPE=SPIGOT

RUN java -jar McDockerHelper.jar download


FROM marcermarc/minecraft:jre16

ENV TYPE=SPIGOT

COPY --from=builder /opt/minecraft/spigot.jar /opt/minecraft/

CMD ["start"]