FROM marcermarc/minecraft:jdk17 AS builder

ARG VERSION

ENV TYPE=SPIGOT

RUN java -jar McDockerHelper.jar download


FROM marcermarc/minecraft:jre17

ENV TYPE=SPIGOT

COPY --from=builder /opt/minecraft/spigot.jar /opt/minecraft/

CMD ["start"]