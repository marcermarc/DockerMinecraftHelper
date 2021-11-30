FROM marcermarc/minecraft:jre17 AS builder

ARG VERSION

ENV TYPE=FORGE

RUN java -jar McDockerHelper.jar download


FROM marcermarc/minecraft:jre17

ENV TYPE=FORGE \
    COMMAND="java -Xms%minram% -Xmx%maxram% -XX:+UseG1GC -XX:+ParallelRefProcEnabled -XX:MaxGCPauseMillis=200 -XX:+UnlockExperimentalVMOptions -XX:+DisableExplicitGC -XX:+AlwaysPreTouch -XX:G1NewSizePercent=30 -XX:G1MaxNewSizePercent=40 -XX:G1HeapRegionSize=8M -XX:G1ReservePercent=20 -XX:G1HeapWastePercent=5 -XX:G1MixedGCCountTarget=4 -XX:InitiatingHeapOccupancyPercent=15 -XX:G1MixedGCLiveThresholdPercent=90 -XX:G1RSetUpdatingPauseTimePercent=5 -XX:SurvivorRatio=32 -XX:+PerfDisableSharedMem -XX:MaxTenuringThreshold=1 -Dusing.aikars.flags=https://mcflags.emc.gs -Daikars.new.flags=true @libraries/net/minecraftforge/forge/1.17.1-37.0.61/unix_args.txt nogui"

COPY --from=builder /opt/minecraft/*.jar /opt/minecraft/
COPY --from=builder /opt/minecraft/libraries/ /opt/minecraft/libraries

CMD ["start"]