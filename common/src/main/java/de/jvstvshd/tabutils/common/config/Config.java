package de.jvstvshd.tabutils.common.config;

import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.CustomClassLoaderConstructor;
import org.yaml.snakeyaml.introspector.BeanAccess;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;

/**
 * The configuration class which holds the actual configuration is mainly used to load or save the configuration.
 */
public class Config {

    private final Yaml yaml = yaml();

    /**
     * A {@link Path} leading to the file where the configuration lays.
     */
    private final Path path;
    private ConfigData configData = new ConfigData();

    /**
     * Creates a new configuration instance.
     *
     * @param path the path to the configuration file.
     * @see Config#Config(JavaPlugin)
     */
    public Config(Path path) {
        this.path = path;
        yaml.setBeanAccess(BeanAccess.FIELD);
    }

    /**
     * Creates a new configuration instance from the {@code plugin}'s data folder.
     *
     * @param plugin the plugin to which this configuration belongs.
     * @see Config#Config(Path)
     */
    public Config(JavaPlugin plugin) {
        this(plugin.getDataFolder().toPath().resolve("config.yml"));
    }

    /**
     * Loads the config from the given {@link #path}.
     *
     * @throws IOException if an I/O error occurs.
     * @see Files#newInputStream(Path, OpenOption...)
     */
    public void load() throws IOException {
        configData = yaml.loadAs(Files.newInputStream(path), ConfigData.class);
    }

    /**
     * Saves the config to the given {@link #path}.
     *
     * @throws IOException if an I/O error occurs.
     * @see Files#write(Path, byte[], OpenOption...)
     */
    public void save() throws IOException {
        Files.write(path, yaml.dump(configData).getBytes());
    }

    public ConfigData configData() {
        return configData;
    }

    public Config setConfigData(ConfigData configData) {
        this.configData = configData;
        return this;
    }

    private Yaml yaml() {
        DumperOptions options = new DumperOptions();
        options.setIndent(2);
        options.setPrettyFlow(true);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

        Representer representer = new Representer();
        representer.addClassTag(ConfigData.class, Tag.MAP);

        Yaml yaml = new Yaml(new CustomClassLoaderConstructor(ConfigData.class.getClassLoader()), representer, options);
        yaml.setBeanAccess(BeanAccess.FIELD);
        return yaml;
    }

    public Path path() {
        return path;
    }

}