FROM marcermarc/minecraft:jre17

ARG VERSION

ENV TYPE=VANILLA

RUN java -jar McDockerHelper.jar download

CMD ["start"]