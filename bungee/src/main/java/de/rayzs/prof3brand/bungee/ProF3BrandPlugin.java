package de.rayzs.prof3brand.bungee;

import de.rayzs.prof3brand.api.ProF3Brand;
import de.rayzs.prof3brand.api.ProF3BrandProvider;
import de.rayzs.prof3brand.api.brand.BrandProvider;
import de.rayzs.prof3brand.api.placeholder.PlaceholderProvider;
import de.rayzs.prof3brand.api.player.BrandPlayerProvider;
import de.rayzs.prof3brand.api.scheduler.SchedulerProvider;
import de.rayzs.prof3brand.api.utils.VersionHelper;
import de.rayzs.prof3brand.bungee.impl.ImplBungeeBrandPlayerProvider;
import de.rayzs.prof3brand.bungee.impl.ImplBungeeBrandProvider;
import de.rayzs.prof3brand.bungee.impl.ImplBungeePlaceholderProvider;
import de.rayzs.prof3brand.bungee.impl.ImplBungeeSchedulerProvider;
import de.rayzs.prof3brand.bungee.listener.PlayerListener;
import de.rayzs.prof3brand.common.impl.ImplProF3Brand;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

public class ProF3BrandPlugin extends Plugin {

    @Override
    public void onEnable() {
        VersionHelper.initialize(null);

        final PlaceholderProvider placeholderProvider = new ImplBungeePlaceholderProvider();
        final BrandProvider brandProvider = new ImplBungeeBrandProvider(placeholderProvider);
        final BrandPlayerProvider brandPlayerProvider = new ImplBungeeBrandPlayerProvider(brandProvider);
        final SchedulerProvider schedulerProvider = new ImplBungeeSchedulerProvider(this);

        final ProF3Brand api = new ImplProF3Brand(
                this,
                getLogger(),
                schedulerProvider,
                brandPlayerProvider,
                brandProvider,
                placeholderProvider
        );
        ProF3BrandProvider.set(api);


        final PluginManager manager = ProxyServer.getInstance().getPluginManager();
        manager.registerListener(this, new PlayerListener(api));
    }

    @Override
    public void onDisable() {

    }
}
