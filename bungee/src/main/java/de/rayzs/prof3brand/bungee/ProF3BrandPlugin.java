package de.rayzs.prof3brand.bungee;

import de.rayzs.prof3brand.api.ProF3Brand;
import de.rayzs.prof3brand.api.ProF3BrandProvider;
import de.rayzs.prof3brand.api.brand.BrandProvider;
import de.rayzs.prof3brand.api.placeholder.PlaceholderProvider;
import de.rayzs.prof3brand.api.player.BrandPlayerProvider;
import de.rayzs.prof3brand.api.scheduler.SchedulerProvider;
import de.rayzs.prof3brand.api.utils.VersionHelper;
import de.rayzs.prof3brand.impl.bungee.ImplBungeeBrandPlayerProvider;
import de.rayzs.prof3brand.impl.bungee.ImplBungeeBrandProvider;
import de.rayzs.prof3brand.impl.bungee.ImplBungeePlaceholderProvider;
import de.rayzs.prof3brand.impl.bungee.ImplBungeeSchedulerProvider;
import de.rayzs.prof3brand.impl.common.ImplProF3Brand;
import net.md_5.bungee.api.plugin.Plugin;

public class ProF3BrandPlugin extends Plugin {

    @Override
    public void onEnable() {
        VersionHelper.initialize(null);

        final BrandPlayerProvider brandPlayerProvider = new ImplBungeeBrandPlayerProvider();
        final BrandProvider brandProvider = new ImplBungeeBrandProvider();
        final SchedulerProvider schedulerProvider = new ImplBungeeSchedulerProvider(this);

        final PlaceholderProvider placeholderProvider = new ImplBungeePlaceholderProvider();

        final ProF3Brand api = new ImplProF3Brand(
                this,
                schedulerProvider,
                brandPlayerProvider,
                brandProvider,
                placeholderProvider
        );
        ProF3BrandProvider.set(api);
    }

    @Override
    public void onDisable() {

    }
}
