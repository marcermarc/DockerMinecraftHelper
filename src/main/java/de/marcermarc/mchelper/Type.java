package de.marcermarc.mchelper;

import de.marcermarc.mchelper.download.BaseDownload;

public enum Type {
    BUKKIT("bukkit.jar"),
    CRAFTBUKKIT("craftbukkit.jar"),
    FABRIC("fabric-server-launch.jar.jar"),
    FORGE("forge-server.jar"),
    PAPER("paper.jar"),
    SPIGOT("spigot.jar"),
    VANILLA("minecraft-server.jar");

    private final String defaultMcExec;

    Type(String defaultMcExec) {
        this.defaultMcExec = defaultMcExec;
    }

    public String getDefaultMcExec() {
        return defaultMcExec;
    }

    public BaseDownload getDownloader(Controller controller) {
        switch (this) {
            case BUKKIT:
                return new de.marcermarc.mchelper.download.bukkit.Download(controller);
            case CRAFTBUKKIT:
                return new de.marcermarc.mchelper.download.craftbukkit.Download(controller);
            case FABRIC:
                return new de.marcermarc.mchelper.download.fabric.Download(controller);
            case FORGE:
                return new de.marcermarc.mchelper.download.forge.Download(controller);
            case PAPER:
                return new de.marcermarc.mchelper.download.paper.Download(controller);
            case SPIGOT:
                return new de.marcermarc.mchelper.download.spigot.Download(controller);
            case VANILLA:
                return new de.marcermarc.mchelper.download.vanilla.Download(controller);
        }
        return null;
    }
}
