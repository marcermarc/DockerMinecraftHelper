package de.marcermarc.mchelper.download;

import de.marcermarc.mchelper.Controller;
import de.marcermarc.mchelper.Util;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
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

        new ProcessBuilder("java", "-jar", jarPath.toString(), "--rev", controller.getConfig().getVersion())
                .directory(TEMP_DIRECTORY)
                .inheritIO()
                .start()
                .waitFor(60, TimeUnit.MINUTES);
    }
}
