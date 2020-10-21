package de.marcermarc.mchelper.download.fabric;

import de.marcermarc.mchelper.Controller;
import de.marcermarc.mchelper.Util;
import de.marcermarc.mchelper.download.BaseDownload;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Download extends BaseDownload {
    private static final String FABRIC_INSTALLER_URL = "https://jenkins.modmuss50.me/job/FabricMC/job/fabric-installer/job/master/lastSuccessfulBuild/artifact/build/libs/fabric*.jar/*zip*/fabric-installer.zip";
    private static final String FABRIC_INSTALLER_FILENAME = "fabric-installer.jar";
    private static final String FABRIC_RESULT_FILENAME = "fabric-server-launch.jar";

    public Download(Controller controller) {
        super(controller);
    }

    public void start() throws Exception {
        Path jarPath = Paths.get(TEMP_PATH, FABRIC_INSTALLER_FILENAME);
        cleanPreviousVersion();

        System.out.println("Download and extract fabric-installer...");

        Util.downloadAndExtractFile(FABRIC_INSTALLER_URL, jarPath);

        System.out.println("Run fabric-installer...");

        new ProcessBuilder(getInstallerCommand(jarPath.toString()))
                .directory(TEMP_DIRECTORY)
                .inheritIO()
                .start()
                .waitFor(5, TimeUnit.MINUTES);

        String execName = controller.getConfig().getMcExec();
        if (!FABRIC_RESULT_FILENAME.equals(execName)) {
            Files.move(
                    Paths.get(controller.getConfig().getMcDir(), FABRIC_RESULT_FILENAME),
                    Paths.get(controller.getConfig().getMcDir(), controller.getConfig().getMcExec())
            );
        }
    }

    private void cleanPreviousVersion() throws IOException {
        Path fabricFolder = Paths.get(controller.getConfig().getMcDir(), ".fabric");

        Util.deleteDirectory(fabricFolder);
    }

    private List<String> getInstallerCommand(final String jarPath) {
        List<String> command = new ArrayList<>();
        command.add("java");
        command.add("-jar");
        command.add(jarPath);
        command.add("server");
        command.add("-downloadMinecraft");
        command.add("-dir");
        command.add(controller.getConfig().getMcDir());

        String version = controller.getConfig().getVersion();
        if (version != null) {
            command.add("-mcversion");
            command.add(version);
        }

        String subversion = controller.getConfig().getSubversion();
        if (subversion != null) {
            command.add("-loader");
            command.add(subversion);
        }

        return command;
    }

    @Override
    protected String[] getBuildDependencies() {
        return null;
    }
}
