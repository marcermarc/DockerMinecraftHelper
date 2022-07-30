FROM marcermarc/minecraft:jre11 AS builder

ARG VERSION

ENV TYPE=FORGE

RUN java -jar McDockerHelper.jar download


FROM marcermarc/minecraft:jre11

ENV TYPE=FORGE

COPY --from=builder /opt/minecraft/*.jar /opt/minecraft/
COPY --from=builder /opt/minecraft/libraries/ /opt/minecraft/libraries

CMD ["start"]