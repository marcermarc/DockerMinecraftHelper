package de.marcermarc.mchelper.download;

import de.marcermarc.mchelper.Controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class BaseDownload {
    public static final String TEMP_PATH = "/tmp";
    public static final File TEMP_DIRECTORY = new File(TEMP_PATH);

    protected final Controller controller;
    private boolean softwareInstalled = false;

    public BaseDownload(Controller controller) {
        this.controller = controller;
    }

    public abstract void start() throws Exception;

    public final void installBuildDependencies() throws IOException, InterruptedException {
        String[] packages = getBuildDependencies();

        if (packages == null) {
            System.out.println("No additional packages needed.");
            return;
        } else if (softwareInstalled) {
            System.out.println("Additional packages already installed.");
        }

        System.out.println("Install additional packages...");

        List<String> commandline = Stream.concat(
                Stream.builder()
                        .add("apk")
                        .add("add")
                        .add("--virtual")
                        .add(".build-dependencies")
                        .build(),
                Stream.of(packages))
                .map(x -> (String) x)
                .collect(Collectors.toList());

        new ProcessBuilder(commandline)
                .inheritIO()
                .start()
                .waitFor(60, TimeUnit.SECONDS);

        System.out.println("Installation successful");
    }

    public final void uninstallBuildDependencies() throws IOException, InterruptedException {
        if (!softwareInstalled) {
            return;
        }

        new ProcessBuilder("apk", "del", ".build-dependencies")
                .inheritIO()
                .start()
                .waitFor(60, TimeUnit.SECONDS);

        softwareInstalled = false;
    }

    /**
     * null, when no packages are needed
     */
    protected abstract String[] getBuildDependencies();
}
