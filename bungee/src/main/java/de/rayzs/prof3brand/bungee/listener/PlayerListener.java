package de.rayzs.prof3brand.bungee.listener;

import de.rayzs.prof3brand.api.ProF3Brand;
import de.rayzs.prof3brand.api.player.BrandPlayer;
import de.rayzs.prof3brand.api.player.BrandPlayerProvider;
import de.rayzs.prof3brand.common.BrandGroupHandler;
import de.rayzs.prof3brand.common.impl.ImplProF3Brand;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PlayerListener implements Listener {

    private final ImplProF3Brand instance;
    private final BrandPlayerProvider playerProvider;
    private final BrandGroupHandler brandGroupHandler;

    public PlayerListener(final ProF3Brand instance) {
        this.instance = (ImplProF3Brand) instance;
        this.playerProvider = instance.getPlayerProvider();
        this.brandGroupHandler = this.instance.getBrandGroupHandler();
    }

    @EventHandler
    public void onPostLogin(final PostLoginEvent event) {
        final ProxiedPlayer proxyPlayer = event.getPlayer();
        final BrandPlayer player = this.playerProvider.convertPlayer(proxyPlayer);

        player.updateBrand("");
        this.brandGroupHandler.reevaluatePlayerBrandGroups(player);
    }

    @EventHandler
    public void onServerSwitch(final ServerSwitchEvent event) {
        final ProxiedPlayer proxyPlayer = event.getPlayer();
        final BrandPlayer player = this.playerProvider.convertPlayer(proxyPlayer);

        this.brandGroupHandler.sendCurrentBrandToPlayer(player);
    }

    @EventHandler
    public void onDisconnect(final PlayerDisconnectEvent event) {
        final ProxiedPlayer proxiedPlayer = event.getPlayer();
        final BrandPlayer player = this.playerProvider.convertPlayer(proxiedPlayer);

        this.playerProvider.unloadPlayer(player);
        this.brandGroupHandler.removePlayer(player);
    }
}
