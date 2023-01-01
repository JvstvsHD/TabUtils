package de.jvstvshd.tabutils.common.config;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.HashMap;
import java.util.Map;

/**
 * A class containing the actual config data.
 */
public class ConfigData implements ConfigurationSerializable {

    /**
     * Whether to modify the ping in the tablist. If set to true, all outgoing Player Info Update packets will be modified so that the ping is set to {@link #ping}
     *
     * @see <a href="https://wiki.vg/Protocol#Player_Info_Update">Player Info Update packet at wiki.vg</a>
     */
    private boolean modifyPing = false;

    /**
     * Which value should be displayed to the client as ping. This is only taken into account if {@link #modifyPing} is set to true.
     */
    private int ping = -1;

    public ConfigData(boolean modifyPing, int ping) {
        this.modifyPing = modifyPing;
        this.ping = ping;
    }

    public ConfigData() {
    }

    public static ConfigData deserialize(Map<String, Object> map) {
        return new ConfigData((boolean) map.get("modifyPing"), (int) map.get("ping"));
    }

    /**
     * Whether to modify a player's ping in the tablist. If set to true, all outgoing Player Info Update packets will be modified so that the ping is set to {@link #ping}
     *
     * @return true if the ping should be modified, false otherwise.
     * @see <a href="https://wiki.vg/Protocol#Player_Info_Update">Player Info Update packet at wiki.vg</a>
     */
    public boolean getModifyPing() {
        return modifyPing;
    }

    /**
     * Sets whether to modify the ping in the tablist. If set to true, all outgoing Player Info Update packets will be modified so that the ping is set to {@link #ping}
     *
     * @param modifyPing whether to modify the ping in the tablist.
     * @return this instance for chaining.
     */
    public ConfigData setModifyPing(boolean modifyPing) {
        this.modifyPing = modifyPing;
        return this;
    }

    /**
     * Which value should be displayed to the client as ping. This is only taken into account if {@link #modifyPing} is set to true.
     *
     * @return the ping to be displayed to the client.
     */
    public int getPing() {
        return ping;
    }

    /**
     * Sets which value should be displayed to the client as ping. This is only taken into account if {@link #modifyPing} is set to true.
     *
     * @param ping the ping to be displayed to the client.
     * @return this instance for chaining.
     */
    public ConfigData setPing(int ping) {
        this.ping = ping;
        return this;
    }

    @Override
    public Map<String, Object> serialize() {
        return new HashMap<String, Object>() {
            {
                put("modifyPing", modifyPing);
                put("ping", ping);
            }
        };
    }
}