package de.marcermarc.mchelper.modpack.curseforge;

public enum ReleaseType {
    ALPHA(3),
    BETA(2),
    RELEASE(1);

    private final int value;

    ReleaseType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
