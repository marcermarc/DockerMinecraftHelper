FROM marcermarc/minecraft:jre16

ARG VERSION

ENV TYPE=VANILLA

RUN java -jar McDockerHelper.jar download

CMD ["start"]