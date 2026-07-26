package de.rayzs.prof3brand.common.impl.config;

import de.rayzs.prof3brand.api.config.Config;
import de.rayzs.prof3brand.common.impl.config.yaml.Configuration;
import de.rayzs.prof3brand.common.impl.config.yaml.ConfigurationProvider;
import de.rayzs.prof3brand.common.impl.config.yaml.YamlConfiguration;

import java.io.File;
import java.util.Collection;

public class ImplConfig implements Config {

    private final String filePath, fileName;

    private File file;
    private Configuration configuration;

    public ImplConfig(final String filePath, final String fileName) {
        this.fileName = fileName;
        this.filePath = filePath;

        load();
    }

    private void load() {
        try {
            this.file = new File(filePath, fileName + ".yml");
            this.configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void reload() {
        load();
    }

    @Override
    public void save() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, file);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public Config set(final String path, final String target, final Object object) {
        this.configuration.set(
                ((path != null) ? (path + ".") : "") + target,
                object instanceof String ? ((String) object).replace("§", "&") : object
        );

        return this;
    }

    @Override
    public ImplConfig set(final String target, final Object object) {
        set(null, target, object);
        return this;
    }

    @Override
    public ImplConfig setAndSave(final String path, final String target, final Object object) {
        set(path, target, object);
        save();

        return this;
    }

    @Override
    public ImplConfig setAndSave(final String target, final Object object) {
        set(target, object);
        save();

        return this;
    }

    @Override
    public <T> T getOrSet(final String path, final String target, final T t) {
        T result = (T) get(path, target);

        if (result != null) {
            return result;
        }

        set(path, target, t);
        save();

        return (T) get(path, target);
    }

    @Override
    public <T> T getOrSet(final String target, final T t) {
        T result = (T) get(target);

        if (result != null) {
            return result;
        }

        set(target, t);
        save();

        return (T) get(target);
    }

    @Override
    public Object get(final String target) {
        return get(null, target);
    }

    @Override
    public Object get(final String path, final String target) {
        Object object = this.configuration.get(((path != null) ? (path + ".") : "") + target);

        if (object instanceof String str) {
            return str.replace("&", "§");
        }

        return object;
    }

    @Override
    public Collection<String> getKeys(final boolean deep) {
        return this.configuration.getKeys();
    }

    @Override
    public Collection<String> getKeys(final String section, final boolean deep) {
        return this.configuration.getSection(section).getKeys();
    }
}