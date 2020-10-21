package de.marcermarc.mchelper.run;

import de.marcermarc.mchelper.Configuration;

import java.io.File;

public class CommandBuilder {
    private final static String MINRAM = "%minram%";
    private final static String MAXRAM = "%maxram%";
    private final static String EXECUTABLE = "%executable%";

    private CommandBuilder() {
    }

    public static String[] buildCommand(Configuration config) {
        String command = config.getCommand();

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

        return command.split(" ");
    }
}
