package de.rayzs.prof3brand.bukkit.listener;

import de.rayzs.prof3brand.api.ProF3Brand;
import de.rayzs.prof3brand.api.player.BrandPlayer;
import de.rayzs.prof3brand.api.player.BrandPlayerProvider;
import de.rayzs.prof3brand.bukkit.netty.BukkitPacketAnalyzer;
import de.rayzs.prof3brand.bukkit.impl.ImplBukkitBrandProvider;
import de.rayzs.prof3brand.common.BrandGroupHandler;
import de.rayzs.prof3brand.common.impl.ImplProF3Brand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    private final ImplProF3Brand instance;
    private final BrandPlayerProvider playerProvider;
    private final ImplBukkitBrandProvider brandProvider;
    private final BrandGroupHandler brandGroupHandler;

    public PlayerListener(final ProF3Brand instance) {
        this.instance = (ImplProF3Brand) instance;
        this.playerProvider = instance.getPlayerProvider();
        this.brandProvider = (ImplBukkitBrandProvider) instance.getBrandProvider();
        this.brandGroupHandler = this.instance.getBrandGroupHandler();
    }

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        final Player bukkitPlayer = event.getPlayer();
        final BrandPlayer player = this.playerProvider.convertPlayer(bukkitPlayer);

        player.updateBrand("");

        this.brandProvider.preparePlayer(bukkitPlayer);

        // Load player channel to cache.
        BukkitPacketAnalyzer.getPlayerChannel(bukkitPlayer);
        this.brandGroupHandler.reevaluatePlayerBrandGroups(player);
    }

    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent event) {
        final Player bukkitPlayer = event.getPlayer();
        final BrandPlayer player = this.playerProvider.convertPlayer(bukkitPlayer);

        // Load player channel to cache.
        BukkitPacketAnalyzer.unloadPlayerChannel(bukkitPlayer);
        this.playerProvider.unloadPlayer(player);
        this.brandGroupHandler.removePlayer(player);
    }
}
