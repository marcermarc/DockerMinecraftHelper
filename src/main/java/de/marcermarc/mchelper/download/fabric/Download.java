package de.marcermarc.mchelper.download.fabric;

import de.marcermarc.mchelper.Controller;
import de.marcermarc.mchelper.Type;
import de.marcermarc.mchelper.Util;
import de.marcermarc.mchelper.download.BaseDownload;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Download extends BaseDownload {
    private static final String VERSION_REPLACE = "%version%";
    private static final String FABRIC_METADATA_URL = "https://maven.fabricmc.net/net/fabricmc/fabric-installer/maven-metadata.xml";
    private static final String FABRIC_INSTALLER_URL = "https://maven.fabricmc.net/net/fabricmc/fabric-installer/" + VERSION_REPLACE + "/fabric-installer-" + VERSION_REPLACE + ".jar";
    private static final String FABRIC_INSTALLER_FILENAME = "fabric-installer.jar";
    private static final String FABRIC_RESULT_FILENAME = Type.FABRIC.getDefaultMcExec();

    public Download(Controller controller) {
        super(controller);
    }

    public void start() throws Exception {
        Path jarPath = Paths.get(TEMP_PATH, FABRIC_INSTALLER_FILENAME);
        cleanPreviousVersion();

        System.out.println("Get latest installer version");

        String installerVersion = getVersionFromMetadata();

        System.out.println("Download and extract fabric-installer...");

        Util.downloadFile(FABRIC_INSTALLER_URL.replace(VERSION_REPLACE, installerVersion), jarPath);

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

    private String getVersionFromMetadata() throws Exception {
        URL sourceUrl = new URL(FABRIC_METADATA_URL);

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(sourceUrl.openStream());

        doc.getDocumentElement().normalize();

        NodeList nodesVersioning = doc.getElementsByTagName("versioning");

        Element element = (Element) nodesVersioning.item(0);

        return element.getElementsByTagName("release").item(0).getTextContent();
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
