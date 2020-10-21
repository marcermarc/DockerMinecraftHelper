package de.marcermarc.mchelper.download.paper;

import de.marcermarc.mchelper.Controller;
import de.marcermarc.mchelper.Util;
import de.marcermarc.mchelper.download.BaseDownload;
import org.json.JSONObject;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Download extends BaseDownload {
    private static final String VERSION_REPLACE = "%version%";
    private static final String BUILD_REPLACE = "%build%";
    private static final String PAPER_API_VERSIONS_URL = "https://papermc.io/api/v1/paper";
    private static final String PAPER_API_BUILD_URL = "https://papermc.io/api/v1/paper/" + VERSION_REPLACE;
    private static final String PAPER_API_DOWNLOAD_URL = "https://papermc.io/api/v1/paper/" + VERSION_REPLACE + "/" + BUILD_REPLACE + "/download";

    public Download(Controller controller) {
        super(controller);
    }

    @Override
    public void start() throws Exception {
        Path path = Paths.get(controller.getConfig().getMcDir(), controller.getConfig().getMcExec());

        Util.downloadFile(getDownloadUrl(), path);
    }

    private String getDownloadUrl() throws Exception {
        String version = controller.getConfig().getVersion();
        if (version == null) {
            String paperApiVersions = Util.downloadString(PAPER_API_VERSIONS_URL);
            JSONObject paperApiVersionsJson = new JSONObject(paperApiVersions);

            version = paperApiVersionsJson.getJSONArray("versions").getString(0);
        }

        String subversion = controller.getConfig().getSubversion();
        if (subversion == null) {
            String paperBuildVersions = Util.downloadString(PAPER_API_BUILD_URL.replace(BUILD_REPLACE, version));
            JSONObject paperBuildVersionsJson = new JSONObject(paperBuildVersions);

            subversion = paperBuildVersionsJson.getJSONObject("builds").getString("latest");
        }

        return PAPER_API_DOWNLOAD_URL.replace(VERSION_REPLACE, version).replace(BUILD_REPLACE, subversion);
    }

    @Override
    protected String[] getBuildDependencies() {
        return null;
    }
}
