package de.rayzs.prof3brand.bukkit;

import de.rayzs.prof3brand.api.ProF3Brand;
import de.rayzs.prof3brand.api.ProF3BrandProvider;
import de.rayzs.prof3brand.api.brand.BrandProvider;
import de.rayzs.prof3brand.api.placeholder.PlaceholderProvider;
import de.rayzs.prof3brand.api.player.BrandPlayerProvider;
import de.rayzs.prof3brand.api.scheduler.SchedulerProvider;
import de.rayzs.prof3brand.api.utils.VersionHelper;
import de.rayzs.prof3brand.bukkit.listener.PlayerListener;
import de.rayzs.prof3brand.bukkit.netty.BukkitPacketAnalyzer;
import de.rayzs.prof3brand.impl.bukkit.*;
import de.rayzs.prof3brand.impl.common.ImplProF3Brand;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ProF3BrandPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        VersionHelper.initialize(Bukkit.getBukkitVersion());

        final BrandPlayerProvider brandPlayerProvider = new ImplBukkitBrandPlayerProvider();
        final BrandProvider brandProvider = new ImplBukkitBrandProvider();
        final SchedulerProvider schedulerProvider = VersionHelper.isFolia()
                ? new ImplFoliaSchedulerProvider(this)
                : new ImplBukkitSchedulerProvider(this);

        final PlaceholderProvider placeholderProvider = new ImplBukkitPlaceholderProvider();

        final ProF3Brand api = new ImplProF3Brand(
                this,
                schedulerProvider,
                brandPlayerProvider,
                brandProvider,
                placeholderProvider
        );
        ProF3BrandProvider.set(api);

        final PluginManager manager = getServer().getPluginManager();
        manager.registerEvents(new PlayerListener(), this);

        Bukkit.getOnlinePlayers().forEach(BukkitPacketAnalyzer::getPlayerChannel);
    }

    @Override
    public void onDisable() {
        ProF3BrandProvider.get().shutdown();
        BukkitPacketAnalyzer.unloadAllPlayerChannels();
        HandlerList.unregisterAll(this);
    }
}
