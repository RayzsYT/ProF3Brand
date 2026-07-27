package de.rayzs.prof3brand.common.impl;

import de.rayzs.prof3brand.api.ProF3Brand;
import de.rayzs.prof3brand.api.brand.BrandProvider;
import de.rayzs.prof3brand.api.config.ConfigProvider;
import de.rayzs.prof3brand.api.placeholder.PlaceholderProvider;
import de.rayzs.prof3brand.api.player.BrandPlayer;
import de.rayzs.prof3brand.api.player.BrandPlayerProvider;
import de.rayzs.prof3brand.api.scheduler.SchedulerProvider;
import de.rayzs.prof3brand.common.BrandGroupHandler;
import de.rayzs.prof3brand.common.impl.config.ImplConfigProvider;

import java.util.logging.Logger;

public class ImplProF3Brand implements ProF3Brand {

    private final Object pluginLoader;
    private final Logger logger;

    private final BrandGroupHandler brandGroupHandler;

    private ConfigProvider configProvider;
    private SchedulerProvider schedulerProvider;
    private BrandPlayerProvider playerProvider;
    private BrandProvider brandProvider;
    private PlaceholderProvider placeholderProvider;

    public ImplProF3Brand(
            final Object pluginLoader,
            final Logger logger,
            final SchedulerProvider schedulerProvider,
            final BrandPlayerProvider playerProvider,
            final BrandProvider brandProvider,
            final PlaceholderProvider placeholderProvider
    ) {

        this.pluginLoader = pluginLoader;
        this.logger = logger;

        this.configProvider = new ImplConfigProvider();
        this.schedulerProvider = schedulerProvider;
        this.playerProvider = playerProvider;
        this.brandProvider = brandProvider;
        this.placeholderProvider = placeholderProvider;


        this.brandGroupHandler = new BrandGroupHandler(
                schedulerProvider,
                placeholderProvider,
                configProvider.getOrCreate("brands")
        );


        this.schedulerProvider.createAsyncScheduler(task -> {
            for (final BrandPlayer player : this.playerProvider.getPlayers()) {
                this.brandGroupHandler.reevaluatePlayerBrandGroups(player);
            }
        }, 20, 20);

    }

    @Override
    public ConfigProvider getConfigProvider() {
        return this.configProvider;
    }

    @Override
    public SchedulerProvider getSchedulerProvider() {
        return this.schedulerProvider;
    }

    @Override
    public BrandPlayerProvider getPlayerProvider() {
        return this.playerProvider;
    }

    @Override
    public PlaceholderProvider getPlaceholderProvider() {
        return this.placeholderProvider;
    }

    @Override
    public BrandProvider getBrandProvider() {
        return this.brandProvider;
    }

    public BrandGroupHandler getBrandGroupHandler() {
        return this.brandGroupHandler;
    }

    @Override
    public Object getPluginLoader() {
        return this.pluginLoader;
    }

    @Override
    public void info(String text) {
        this.logger.info(text);
    }

    @Override
    public void warn(String text) {
        this.logger.warning(text);
    }

    @Override
    public void reload() {
        this.brandGroupHandler.reload();
    }

    @Override
    public void shutdown() {
        this.playerProvider.unloadAllPlayers();

        this.playerProvider = null;
        this.schedulerProvider = null;
        this.configProvider = null;
    }
}
