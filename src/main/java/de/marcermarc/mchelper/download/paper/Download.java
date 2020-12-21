package de.marcermarc.mchelper.download.paper;

import de.marcermarc.mchelper.Controller;
import de.marcermarc.mchelper.Util;
import de.marcermarc.mchelper.download.BaseDownload;
import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Download extends BaseDownload {
    private static final String VERSION_REPLACE = "%version%";
    private static final String BUILD_REPLACE = "%build%";
    private static final String DOWNLAOD_REPLACE = "%download%";
    private static final String PAPER_API_VERSIONS_URL = "https://papermc.io/api/v2/projects/paper";
    private static final String PAPER_API_BUILD_URL = "https://papermc.io/api/v2/projects/paper/versions/" + VERSION_REPLACE;
    private static final String PAPER_API_BUILD_INFO_URL = "https://papermc.io/api/v2/projects/paper/versions/" + VERSION_REPLACE + "/builds/" + BUILD_REPLACE;
    private static final String PAPER_API_DOWNLOAD_URL = "https://papermc.io/api/v2/projects/paper/versions/" + VERSION_REPLACE + "/builds/" + BUILD_REPLACE + "/downloads/" + DOWNLAOD_REPLACE;

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

            JSONArray versions = paperApiVersionsJson.getJSONArray("versions");
            for (int i = 0; i < versions.length(); i++) {
                String v = versions.getString(i);

                if (version == null || Util.compareVersions(v, version, "\\.") > 0) {
                    version = v;
                }
            }
        }

        String subversion = controller.getConfig().getSubversion();
        if (subversion == null) {
            String paperBuildVersions = Util.downloadString(PAPER_API_BUILD_URL.replace(VERSION_REPLACE, version));
            JSONObject paperBuildVersionsJson = new JSONObject(paperBuildVersions);

            JSONArray subversions = paperBuildVersionsJson.getJSONArray("builds");
            subversion = Integer.toString(subversions.toList().stream().mapToInt(x -> (int) x).max().getAsInt());
        }

        String paperBuildInfo = Util.downloadString(PAPER_API_BUILD_INFO_URL.replace(VERSION_REPLACE, version).replace(BUILD_REPLACE, subversion));
        JSONObject paperBuildInfoJson = new JSONObject(paperBuildInfo);
        String downloadFilename = paperBuildInfoJson.getJSONObject("downloads").getJSONObject("application").getString("name");

        return PAPER_API_DOWNLOAD_URL.replace(VERSION_REPLACE, version).replace(BUILD_REPLACE, subversion).replace(DOWNLAOD_REPLACE, downloadFilename);
    }

    @Override
    protected String[] getBuildDependencies() {
        return null;
    }
}
