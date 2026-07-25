package de.rayzs.prof3brand.impl.common.config;

import de.rayzs.prof3brand.api.config.Config;
import de.rayzs.prof3brand.api.config.ConfigProvider;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ImplConfigProvider implements ConfigProvider {

    private final String defaultFolderPath = "plugins/ProF3Brand";
    private final Map<String, Config> configs = new HashMap<>();


    // Create default files if the folder does not exist yet.
    public ImplConfigProvider() {
        final File folder = new File(defaultFolderPath);

        if (folder.isDirectory()) {
            return;
        }

        if (!folder.mkdirs()) {
            throw new RuntimeException("Failed to create ControlPlayer folder! (" + defaultFolderPath + ")");
        }

        // TO-DO:
        // Create implementation to load all files from inside project
        // into the plugins/ProF3Brand folder.
    }

    @Override
    public Config getOrCreate(final String fileName) {
        return getOrCreate(null, fileName);
    }

    @Override
    public Config getOrCreate(final String filePath, final String fileName) {
        final String path = defaultFolderPath + (filePath != null ? ("/" + filePath) : "");
        final String id = path + "/" + fileName;

        Config config = configs.get(id);
        if (config != null) {
            return config;
        }

        config = new ImplConfig(path, fileName);

        configs.put(id, config);
        return config;
    }
}