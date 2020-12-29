package de.marcermarc.mchelper.run;

import de.marcermarc.mchelper.Controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class BeforeStart {
    private static final String EULA_FILE = "eula.txt";

    protected final Controller controller;

    public BeforeStart(Controller controller) {
        this.controller = controller;
    }

    public void run() throws Exception {
        testEula();
    }

    private void testEula() throws IOException {
        if (!controller.getConfig().getEula()) {
            return; // without configure it, it should not do anything
        }

        System.out.println("Eula environment parameter is set to true. If you not agree to the minecraft eula stop this server.");
        System.out.println("Test and prepare eula file.");

        Path eulaPath = Paths.get(controller.getConfig().getWorkDir(), EULA_FILE);

        List<String> currentContent;
        if (Files.exists(eulaPath)) {
            currentContent = Files.readAllLines(eulaPath);

            for (int i = currentContent.size() - 1; i >= 0; i--) {
                if (currentContent.get(i).startsWith("eula=true")) {
                    System.out.println("Eula-file is already ok.");
                    return; // if eula is already set to true, it is ok
                } else if (currentContent.get(i).startsWith("eula=")) {
                    currentContent.remove(i); // remove this line, to remove eula=false or other things in this line
                }
            }
        } else {
            currentContent = new ArrayList<>();
        }

        currentContent.add(0, "#This file was created/modified by the docker McHelper with using env variable 'eula'.");
        currentContent.add("eula=true");

        Files.write(eulaPath, currentContent, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE, StandardOpenOption.CREATE);

        System.out.println("Eula-file modified/created.");
    }
}
