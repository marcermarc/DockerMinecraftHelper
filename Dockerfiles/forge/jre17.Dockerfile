FROM marcermarc/minecraft:jre17 AS builder

ARG VERSION

ENV TYPE=FORGE

RUN java -jar McDockerHelper.jar download


FROM marcermarc/minecraft:jre17

ENV TYPE=FORGE

COPY --from=builder /opt/minecraft/ /opt/minecraft/

CMD ["start"]