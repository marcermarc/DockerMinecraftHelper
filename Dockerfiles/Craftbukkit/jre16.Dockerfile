FROM marcermarc/minecraft:jdk16 AS builder

ARG VERSION

ENV TYPE=CRAFTBUKKIT

RUN java -jar McDockerHelper.jar download


FROM marcermarc/minecraft:jre16

ENV TYPE=CRAFTBUKKIT

COPY --from=builder /opt/minecraft/craftbukkit.jar /opt/minecraft/

CMD ["start"]