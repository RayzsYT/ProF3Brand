package de.rayzs.prof3brand.bukkit.listener;

import de.rayzs.prof3brand.api.ProF3Brand;
import de.rayzs.prof3brand.api.ProF3BrandProvider;
import de.rayzs.prof3brand.api.player.BrandPlayer;
import de.rayzs.prof3brand.api.player.BrandPlayerProvider;
import de.rayzs.prof3brand.bukkit.netty.BukkitPacketAnalyzer;
import de.rayzs.prof3brand.bukkit.impl.ImplBukkitBrandProvider;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    private final ProF3Brand instance = ProF3BrandProvider.get();
    private final BrandPlayerProvider playerProvider = instance.getPlayerProvider();
    private final ImplBukkitBrandProvider brandProvider = (ImplBukkitBrandProvider) instance.getBrandProvider();

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        final Player bukkitPlayer = event.getPlayer();
        final BrandPlayer player = playerProvider.convertPlayer(bukkitPlayer);

        brandProvider.preparePlayer(bukkitPlayer);

        // Load player channel to cache.
        BukkitPacketAnalyzer.getPlayerChannel(bukkitPlayer);
    }

    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent event) {
        final Player bukkitPlayer = event.getPlayer();

        // Load player channel to cache.
        BukkitPacketAnalyzer.unloadPlayerChannel(bukkitPlayer);
    }
}
