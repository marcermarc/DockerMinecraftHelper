package de.marcermarc.mchelper.modpack.curseforge;

public class Modpack extends BaseModpack {

    public static String searchUrl = "https://addons-ecs.forgesvc.net/api/v2/addon/search?gameId=432&searchFilter={slug}&pageSize=1";
    public static String filesUrl = "https://addons-ecs.forgesvc.net/api/v2/addon/{addonID}/files";
    public static String downloadUrl = "https://addons-ecs.forgesvc.net/api/v2/addon/{addonID}/file/{fileID}/download-url";

    public static String slugParam = "{slug}";
    public static String addonIDParam = "{addonID}";
    public static String fileIDParam = "{fileID}";


}
