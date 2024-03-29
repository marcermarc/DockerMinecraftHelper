FROM marcermarc/minecraft:jre17 AS builder

ARG VERSION

ENV TYPE=PAPER

RUN java -jar McDockerHelper.jar download


FROM marcermarc/minecraft:jre17

ENV TYPE=PAPER

COPY --from=builder /opt/minecraft/paper.jar /opt/minecraft/

CMD ["start"]