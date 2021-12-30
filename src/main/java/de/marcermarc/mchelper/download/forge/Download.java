package de.marcermarc.mchelper.download.forge;

import de.marcermarc.mchelper.Controller;
import de.marcermarc.mchelper.Util;
import de.marcermarc.mchelper.download.BaseDownload;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class Download extends BaseDownload {
    private static final String FORGE_META_URL = "https://files.minecraftforge.net/maven/net/minecraftforge/forge/maven-metadata.json";
    private static final String VERSION_REPLACE = "%version%";
    private static final String FORGE_DOWNLOAD_URL = "https://files.minecraftforge.net/maven/net/minecraftforge/forge/" + VERSION_REPLACE + "/forge-" + VERSION_REPLACE + "-installer.jar";
    private static final String FORGE_INSTALLER_FILENAME = "installer/forge-installer.jar";
    private static final Path FORGE_RESULT_PATH = Paths.get(TEMP_PATH, "result");
    private static final String FILE_START = "forge-";
    private static final String FILE_END = ".jar";
    private static final String LIBRARIES_FOLDER = "libraries";
    private static final String UNIX_ARGS_FILENAME = "unix_args.txt";
    private static final String ORIG_UNIX_AGRS_SUBPATH = LIBRARIES_FOLDER + "/net/minecraftforge/forge/" + VERSION_REPLACE + "/" + UNIX_ARGS_FILENAME;

    public Download(Controller controller) {
        super(controller);
    }

    @Override
    public void start() throws Exception {
        Path installerPath = Paths.get(TEMP_PATH, FORGE_INSTALLER_FILENAME);
        Files.createDirectory(installerPath.getParent());
        Files.createDirectory(FORGE_RESULT_PATH);

        System.out.println("Get Version...");

        String version = getVersion();

        System.out.println("Download Forge Version '" + version + "'...");

        Util.downloadFile(FORGE_DOWNLOAD_URL.replace(VERSION_REPLACE, version), installerPath);

        System.out.println("Install Forge Version '" + version + "'...");

        new ProcessBuilder("java", "-jar", installerPath.toString(), "--installServer")
                .directory(FORGE_RESULT_PATH.toFile())
                .inheritIO()
                .start()
                .waitFor(5, TimeUnit.MINUTES);

        File[] files = FORGE_RESULT_PATH.toFile().listFiles((file, filename) -> filename.endsWith(FILE_END));

        for (File file : files) {
            if (file.getName().startsWith(FILE_START)) {
                Files.move(file.toPath(), Paths.get(controller.getConfig().getMcDir(), controller.getConfig().getMcExec()));
            } else {
                Files.move(file.toPath(), Paths.get(controller.getConfig().getMcDir(), file.getName()));
            }
        }

        // Move Forge libraries
        Files.move(Paths.get(FORGE_RESULT_PATH.toString(), LIBRARIES_FOLDER), Paths.get(controller.getConfig().getMcDir(), LIBRARIES_FOLDER));

        writeArgsFile(version);
    }

    private String getVersion() throws Exception {
        String forgeMeta = Util.downloadString(FORGE_META_URL);

        JSONObject forgeMetaJson = new JSONObject(forgeMeta);

        String version = controller.getConfig().getVersion();
        if (version == null) {
            Iterator<String> iterator = forgeMetaJson.keys();
            for (String v = iterator.next(); iterator.hasNext(); v = iterator.next()) {
                if (version == null || Util.compareVersions(v, version, "\\.") > 0) {
                    version = v;
                }
            }
        }

        JSONArray subversions = forgeMetaJson.getJSONArray(version);

        String subversion = controller.getConfig().getSubversion();
        if (subversion == null) {
            for (int i = 0; i < subversions.length(); i++) {
                String v = subversions.getString(i);

                if (subversion == null || Util.compareVersions(v, subversion, "\\.|-") > 0) {
                    subversion = v;
                }
            }
        } else {
            for (int i = 0; i < subversions.length(); i++) {
                String v = subversions.getString(i);

                if (v.contains(subversion)) {
                    subversion = v;
                    break;
                }
            }
        }

        return subversion;
    }

    private void writeArgsFile(String version) throws IOException {
        Path origFilePath = Paths.get(controller.getConfig().getMcDir(), ORIG_UNIX_AGRS_SUBPATH.replace(VERSION_REPLACE, version));

        // old forge version don't use this method
        if (Files.exists(origFilePath)) {
            String absoluteLibariesPath = Paths.get(controller.getConfig().getMcDir(), LIBRARIES_FOLDER).toString();
            Path newFilePath = Paths.get(controller.getConfig().getMcDir(), UNIX_ARGS_FILENAME);

            try (Stream<String> stream = Files.lines(origFilePath)) {
                Files.write(
                        newFilePath,
                        (Iterable<String>) stream.map(x -> x.replace(LIBRARIES_FOLDER, absoluteLibariesPath))::iterator,
                        StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
            }
        }
    }

    @Override
    protected String[] getBuildDependencies() {
        return null;
    }
}
