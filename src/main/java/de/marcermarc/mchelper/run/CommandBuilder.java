package de.marcermarc.mchelper.run;

import de.marcermarc.mchelper.Configuration;

import java.io.File;

public class CommandBuilder {
    private final static String XMS = "-Xms";
    private final static String XMX = "-Xmx";
    private final static String MINRAM = "%minram%";
    private final static String MAXRAM = "%maxram%";
    private final static String EXECUTABLE = "%executable%";
    private final static String SPACE = " ";

    private CommandBuilder() {
    }

    public static String[] buildCommand(Configuration config) {
        String command = config.getCommand();

        if (command == null) {
            command = getCommand(config);
        }

        if (command.contains(MINRAM)) {
            command = command.replace(MINRAM, config.getMinRam());
        }
        if (command.contains(MAXRAM)) {
            command = command.replace(MAXRAM, config.getMaxRam());
        }

        if (command.contains(EXECUTABLE)) {
            File exec = new File(config.getMcDir(), config.getMcExec());
            command = command.replace(EXECUTABLE, exec.getAbsolutePath());
        }

        return command.split(SPACE);
    }

    public static String getCommand(Configuration config) {
        StringBuilder builder = new StringBuilder("java ");

        String jvmArgs = config.getJvmArgs();
        if (!jvmArgs.contains(XMS)) {
            builder.append(XMS).append(config.getMinRam()).append(SPACE);
        }
        if (!jvmArgs.contains(XMX)) {
            builder.append(XMX).append(config.getMaxRam()).append(SPACE);
        }

        return builder
                .append(jvmArgs)
                .append(SPACE)
                .append(config.getMcStartArgs())
                .append(SPACE)
                .append(config.getMcProgramArgs())
                .toString();
    }
}
