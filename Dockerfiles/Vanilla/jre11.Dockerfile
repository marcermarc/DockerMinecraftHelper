FROM marcermarc/minecraft:jre11

ARG VERSION

ENV TYPE=VANILLA

RUN java -jar McDockerHelper.jar download

CMD ["start"]