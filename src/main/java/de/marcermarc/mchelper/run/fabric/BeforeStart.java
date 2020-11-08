package de.marcermarc.mchelper.run.fabric;

import de.marcermarc.mchelper.Controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;


public class BeforeStart extends de.marcermarc.mchelper.run.BeforeStart {
    private final static String CONFIG_FILE_NAME = "fabric-server-launcher.properties";
    private final static String CONFIG_FILE_CONTENT = "serverJar=/opt/minecraft/server.jar";

    public BeforeStart(Controller controller) {
        super(controller);
    }

    @Override
    public void run() throws Exception {
        super.run();

        Path configFile = Paths.get(controller.getConfig().getWorkDir(), CONFIG_FILE_NAME);

        if (!Files.exists(configFile)) {
            Files.write(configFile, CONFIG_FILE_CONTENT.getBytes(), StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
        }

    }
}
