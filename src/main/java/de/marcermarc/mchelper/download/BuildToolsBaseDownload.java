package de.marcermarc.mchelper.download;

import de.marcermarc.mchelper.Controller;
import de.marcermarc.mchelper.Type;
import de.marcermarc.mchelper.Util;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public abstract class BuildToolsBaseDownload extends BaseDownload {
    private static final String BUILDTOOLS_URL = "https://hub.spigotmc.org/jenkins/job/BuildTools/lastSuccessfulBuild/artifact/target/BuildTools.jar";
    private static final String BUILDTOOLS_FILENAME = "BuildTools.jar";

    public BuildToolsBaseDownload(Controller controller) {
        super(controller);
    }

    @Override
    protected String[] getBuildDependencies() {
        return new String[]{"git"};
    }

    protected void downloadAndRunBuildtools() throws IOException, InterruptedException {
        Path jarPath = Paths.get(TEMP_PATH, BUILDTOOLS_FILENAME);

        System.out.println("Download BuildTools...");

        Util.downloadFile(BUILDTOOLS_URL, jarPath);

        System.out.println("Run BuildTools...");

        new ProcessBuilder(getInstallerCommand(jarPath.toString()))
                .directory(TEMP_DIRECTORY)
                .inheritIO()
                .start()
                .waitFor(60, TimeUnit.MINUTES);
    }

    private List<String> getInstallerCommand(final String jarPath) {
        List<String> command = new ArrayList<>();
        command.add("java");
        command.add("-jar");
        command.add(jarPath);

        String version = controller.getConfig().getVersion();
        if (version != null) {
            command.add("--rev");
            command.add(version);
        }

        if (controller.getConfig().getType() == Type.CRAFTBUKKIT) {
            command.add("--compile");
            command.add("craftbukkit");
        }

        return command;
    }
}
