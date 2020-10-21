package de.marcermarc.mchelper.download.vanilla;

import de.marcermarc.mchelper.Controller;
import de.marcermarc.mchelper.Util;
import de.marcermarc.mchelper.download.BaseDownload;
import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Download extends BaseDownload {
    private static final String VANILLA_META_URL = "https://launchermeta.mojang.com/mc/game/version_manifest.json";

    public Download(Controller controller) {
        super(controller);
    }

    @Override
    public void start() throws Exception {
        Path path = Paths.get(controller.getConfig().getMcDir(), controller.getConfig().getMcExec());

        Util.downloadFile(getDownloadUrl(), path);
    }

    private String getDownloadUrl() throws Exception {
        String vanillaMeta = Util.downloadString(VANILLA_META_URL);

        JSONObject vanillaMetaJson = new JSONObject(vanillaMeta);

        String version = controller.getConfig().getVersion();
        if (version == null) {
            version = vanillaMetaJson.getJSONObject("latest").getString("release");
        }

        String versionJsonUrl = null;
        JSONArray versions = vanillaMetaJson.getJSONArray("versions");
        for (int i = 0; i <= versions.length(); i++) {
            JSONObject value = versions.getJSONObject(i);
            if (version.equals(value.getString("id"))) {
                versionJsonUrl = value.getString("url");
            }
        }

        String versionMeta = Util.downloadString(versionJsonUrl);

        JSONObject versionJson = new JSONObject(versionMeta);

        return versionJson.getJSONObject("downloads").getJSONObject("server").getString("url");
    }

    @Override
    protected String[] getBuildDependencies() {
        return null;
    }
}
