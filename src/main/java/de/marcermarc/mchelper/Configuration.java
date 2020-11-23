package de.marcermarc.mchelper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Configuration {
    private static final String WORK_DIR = "workdir";
    private static final String MC_DIR = "mcdir";
    private static final String MC_EXEC = "mcexecutable";
    private static final String COMMAND = "command";
    private static final String MIN_RAM = "minram";
    private static final String MAX_RAM = "maxram";
    private static final String RESTART_INTERVAL = "restartinterval";
    private static final String STOP_TIMEOUT = "stoptimeout";
    private static final String VERSION = "version";
    private static final String SUBVERSION = "subversion";
    private static final String TYPE = "type";
    private static final String EULA = "eula";

    private static final String LATEST = "latest";

    private final Map<String, String> source = new HashMap<>();

    public Configuration() {
        put(WORK_DIR, "/mnt/minecraft");
        put(MC_DIR, "/opt/minecraft");
        put(MC_EXEC, null);
        put(COMMAND, "java -jar %executable% nogui");
        put(MIN_RAM, "4G");
        put(MAX_RAM, "4G");
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
}
