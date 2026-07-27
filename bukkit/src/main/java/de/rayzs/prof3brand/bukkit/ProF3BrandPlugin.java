package de.rayzs.prof3brand.bukkit;

import de.rayzs.prof3brand.api.ProF3Brand;
import de.rayzs.prof3brand.api.ProF3BrandProvider;
import de.rayzs.prof3brand.api.brand.BrandProvider;
import de.rayzs.prof3brand.api.placeholder.PlaceholderProvider;
import de.rayzs.prof3brand.api.player.BrandPlayerProvider;
import de.rayzs.prof3brand.api.scheduler.SchedulerProvider;
import de.rayzs.prof3brand.api.utils.VersionHelper;
import de.rayzs.prof3brand.bukkit.impl.*;
import de.rayzs.prof3brand.bukkit.listener.PlayerListener;
import de.rayzs.prof3brand.bukkit.netty.BukkitPacketAnalyzer;
import de.rayzs.prof3brand.common.impl.ImplProF3Brand;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class ProF3BrandPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        VersionHelper.initialize(Bukkit.getBukkitVersion());

        final PlaceholderProvider placeholderProvider = new ImplBukkitPlaceholderProvider();
        final BrandProvider brandProvider = new ImplBukkitBrandProvider(placeholderProvider, this);
        final BrandPlayerProvider brandPlayerProvider = new ImplBukkitBrandPlayerProvider(brandProvider);
        final SchedulerProvider schedulerProvider = VersionHelper.isFolia()
                ? new ImplFoliaSchedulerProvider(this)
                : new ImplBukkitSchedulerProvider(this);

        final ProF3Brand api = new ImplProF3Brand(
                this,
                getLogger(),
                schedulerProvider,
                brandPlayerProvider,
                brandProvider,
                placeholderProvider
        );
        ProF3BrandProvider.set(api);

        final PluginManager manager = getServer().getPluginManager();
        manager.registerEvents(new PlayerListener(api), this);

        Bukkit.getOnlinePlayers().forEach(bukkitPlayer -> {
            BukkitPacketAnalyzer.getPlayerChannel(bukkitPlayer);
            brandPlayerProvider.convertPlayer(bukkitPlayer);
        });


        getCommand("prof3brand").setExecutor((sender, command, label, args) -> {
            sender.sendMessage("Reloading brands.yml...");
            api.reload();
            sender.sendMessage("Successfully reloaded brand.yml!");

            return true;
        });
    }

    @Override
    public void onDisable() {
        ProF3BrandProvider.get().shutdown();
        BukkitPacketAnalyzer.unloadAllPlayerChannels();
        HandlerList.unregisterAll(this);
    }
}
