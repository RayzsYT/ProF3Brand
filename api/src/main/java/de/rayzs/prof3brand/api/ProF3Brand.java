package de.rayzs.prof3brand.api;

import de.rayzs.prof3brand.api.brand.BrandProvider;
import de.rayzs.prof3brand.api.config.ConfigProvider;
import de.rayzs.prof3brand.api.placeholder.PlaceholderProvider;
import de.rayzs.prof3brand.api.player.BrandPlayerProvider;
import de.rayzs.prof3brand.api.scheduler.SchedulerProvider;

public interface ProF3Brand {

    ConfigProvider getConfigProvider();
    SchedulerProvider getSchedulerProvider();
    BrandPlayerProvider getPlayerProvider();
    BrandProvider getBrandProvider();
    PlaceholderProvider getPlaceholderProvider();

    Object getPluginLoader();

    void info(final String text);
    void warn(final String text);

    void reload();
    void shutdown();
}
