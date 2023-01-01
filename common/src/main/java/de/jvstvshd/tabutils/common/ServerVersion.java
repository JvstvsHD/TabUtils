package de.jvstvshd.tabutils.common;

import java.util.Arrays;

public enum ServerVersion {

    /**
     * Minecraft 1.8.8
     */
    v1_8_R3,
    /**
     * Minecraft 1.18-1.18.1
     */
    v1_18_R1,
    /**
     * Minecraft 1.18.2
     */
    v1_18_R2,
    /**
     * Minecraft 1.19-1.19.2
     */
    v1_19_R1,
    /**
     * Minecraft 1.19.3
     */
    v1_19_R2;

    public static ServerVersion determine() {
        try {
            String version = org.bukkit.Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
            return Arrays.stream(values()).filter(serverVersion -> serverVersion.name().equals(version)).findFirst().orElseThrow(UnsupportedServerVersionException::new);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new UnsupportedServerVersionException(e);
        }
    }
}
