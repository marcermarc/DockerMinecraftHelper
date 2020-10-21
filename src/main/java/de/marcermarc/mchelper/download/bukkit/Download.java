package de.marcermarc.mchelper.download.bukkit;

import de.marcermarc.mchelper.Controller;
import de.marcermarc.mchelper.download.BuildToolsBaseDownload;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Download extends BuildToolsBaseDownload {
    private static final String FILE_START = "bukkit";
    private static final String FILE_END = ".jar";

    public Download(Controller controller) {
        super(controller);
    }

    @Override
    public void start() throws Exception {
        downloadAndRunBuildtools();

        File[] files = TEMP_DIRECTORY.listFiles((file, filename) -> filename.startsWith(FILE_START) && filename.endsWith(FILE_END));

        if (files == null || files.length != 1) {
            throw new Exception("Bukkit jar not found");
        }

        Files.move(files[0].toPath(), Paths.get(controller.getConfig().getMcDir(), controller.getConfig().getMcExec()));
    }
}

