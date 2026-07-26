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
import de.rayzs.prof3brand.common.impl.ImplProF3Brand;
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
                getLogger(),
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
