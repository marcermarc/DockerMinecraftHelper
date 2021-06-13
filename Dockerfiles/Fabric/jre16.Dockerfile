FROM marcermarc/minecraft:jre16 AS builder

ARG VERSION

ENV TYPE=FABRIC

RUN java -jar McDockerHelper.jar download


FROM marcermarc/minecraft:jre16

ENV TYPE=FABRIC

COPY --from=builder /opt/minecraft/ /opt/minecraft/

CMD ["start"]