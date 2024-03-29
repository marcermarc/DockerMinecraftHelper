package de.marcermarc.mchelper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Configuration {
    private static final String AIKAR_ARGS = // source: https://mcflags.emc.gs/
            "-XX:+UseG1GC -XX:+ParallelRefProcEnabled -XX:MaxGCPauseMillis=200 -XX:+UnlockExperimentalVMOptions " +
                    "-XX:+DisableExplicitGC -XX:+AlwaysPreTouch -XX:G1NewSizePercent=30 -XX:G1MaxNewSizePercent=40 " +
                    "-XX:G1HeapRegionSize=8M -XX:G1ReservePercent=20 -XX:G1HeapWastePercent=5 " +
                    "-XX:G1MixedGCCountTarget=4 -XX:InitiatingHeapOccupancyPercent=15 " +
                    "-XX:G1MixedGCLiveThresholdPercent=90 -XX:G1RSetUpdatingPauseTimePercent=5 -XX:SurvivorRatio=32 " +
                    "-XX:+PerfDisableSharedMem -XX:MaxTenuringThreshold=1 " +
                    "-Dusing.aikars.flags=https://mcflags.emc.gs -Daikars.new.flags=true";

    private static final String WORK_DIR = "workdir";
    private static final String MC_DIR = "mcdir";
    private static final String MC_EXEC = "mcexecutable";
    private static final String COMMAND = "command";
    private static final String MIN_RAM = "minram";
    private static final String MAX_RAM = "maxram";
    private static final String RESTART_INTERVAL = "restartinterval";
    private static final String RESTART_CRON = "restartcron";
    private static final String STOP_TIMEOUT = "stoptimeout";
    private static final String VERSION = "version";
    private static final String SUBVERSION = "subversion";
    private static final String TYPE = "type";
    private static final String EULA = "eula";
    private static final String JVM_ARGS = "jvmargs";
    private static final String MC_START_ARGS = "mcstartargs";
    private static final String MC_PROGRAM_ARGS = "mcargs";

    private static final String LATEST = "latest";

    private final Map<String, String> source = new HashMap<>();

    public Configuration() {
        put(WORK_DIR, "/mnt/minecraft");
        put(MC_DIR, "/opt/minecraft");
        put(MC_EXEC, null);
        put(COMMAND, null); // Replaced with JVM_ARGS, MC_START_ARGS and MC_PROGRAM_ARGS
        put(JVM_ARGS, AIKAR_ARGS);
        put(MC_START_ARGS, "-jar %executable%");
        put(MC_PROGRAM_ARGS, "nogui");
        put(MIN_RAM, "6G");
        put(MAX_RAM, "6G");
        put(RESTART_INTERVAL, "-1");
        put(STOP_TIMEOUT, "60");
        put(VERSION, null);
        put(SUBVERSION, null);
        put(TYPE, "VANILLA");
        put(EULA, "false");
    }

    public void initWithEnvironment() {
        for (Map.Entry<String, String> ent : System.getenv().entrySet()) {
            if (ent.getValue() != null && !ent.getValue().trim().isEmpty()) {
                put(ent.getKey(), ent.getValue());
            }
        }
    }

    public String put(String key, String value) {
        return source.put(formatToConfigKey(key), value);
    }

    /**
     * Convert {@literal key} to a key of the {@link Configuration} map.
     * <p>
     * The keys are only lowercase letters.
     */
    private String formatToConfigKey(String key) {
        return key.codePoints()
                .filter(Character::isLetter)
                .map(Character::toLowerCase)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public String getWorkDir() {
        return source.get(WORK_DIR);
    }

    public String getMcDir() {
        return source.get(MC_DIR);
    }

    public String getMcExec() {
        String result = source.get(MC_EXEC);
        if (result == null) {
            return getType().getDefaultMcExec();
        }
        return result;
    }

    public String getCommand() {
        return source.get(COMMAND);
    }

    public String getJvmArgs() {
        return source.get(JVM_ARGS);
    }

    public String getMcStartArgs() {
        return source.get(MC_START_ARGS);
    }

    public void setMcStartArgs(String value) {
        source.put(MC_START_ARGS, value);
    }

    public String getMcProgramArgs() {
        return source.get(MC_PROGRAM_ARGS);
    }

    public String getMinRam() {
        return source.get(MIN_RAM);
    }

    public String getMaxRam() {
        return source.get(MAX_RAM);
    }

    /**
     * in minutes
     */
    public int getRestartInterval() {
        String restartInterval = source.get(RESTART_INTERVAL);

        if (Util.isNumeric(restartInterval)) {
            return Integer.parseInt(restartInterval);
        }

        return -1;
    }

    /**
     * In seconds
     */
    public int getStopTimeout() {
        String stopTimeout = source.get(STOP_TIMEOUT);

        if (Util.isNumeric(stopTimeout)) {
            return Integer.parseInt(stopTimeout);
        }

        return 60; //Default value
    }

    public String getVersion() {
        String version = source.get(VERSION);

        if (LATEST.equalsIgnoreCase(version)) {
            return null;
        }

        return version;
    }

    public String getSubversion() {
        return source.get(SUBVERSION);
    }

    public Type getType() {
        String type = source.get(TYPE);

        return Arrays.stream(Type.values())
                .filter(x -> x.name().equalsIgnoreCase(type))
                .findFirst()
                .orElse(Type.VANILLA);
    }

    public boolean getEula() {
        String eula = source.get(EULA);

        return Boolean.toString(true).equalsIgnoreCase(eula);
    }

    public String getRestartCron() {
        return source.get(RESTART_CRON);
    }
}
