package de.rayzs.prof3brand.velocity.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.event.player.ServerPreConnectEvent;
import com.velocitypowered.api.proxy.Player;
import de.rayzs.prof3brand.api.ProF3Brand;
import de.rayzs.prof3brand.api.player.BrandPlayer;
import de.rayzs.prof3brand.api.player.BrandPlayerProvider;
import de.rayzs.prof3brand.common.BrandGroupHandler;
import de.rayzs.prof3brand.common.impl.ImplProF3Brand;

public class PlayerListener {

    private final ImplProF3Brand instance;
    private final BrandPlayerProvider playerProvider;
    private final BrandGroupHandler brandGroupHandler;

    public PlayerListener(final ProF3Brand instance) {
        this.instance = (ImplProF3Brand) instance;
        this.playerProvider = instance.getPlayerProvider();
        this.brandGroupHandler = this.instance.getBrandGroupHandler();
    }

    @Subscribe
    public void onServerPreConnect(final ServerPreConnectEvent event) {
        final Player proxyPlayer = event.getPlayer();
        final BrandPlayer player = this.playerProvider.convertPlayer(proxyPlayer);

        player.updateBrand("");
        this.brandGroupHandler.reevaluatePlayerBrandGroups(player);
    }

    @Subscribe
    public void onServerSwitch(final ServerConnectedEvent event) {
        final Player proxyPlayer = event.getPlayer();
        final BrandPlayer player = this.playerProvider.convertPlayer(proxyPlayer);

        this.brandGroupHandler.sendCurrentBrandToPlayer(player);
    }

    @Subscribe
    public void onDisconnect(final DisconnectEvent event) {
        final Player proxyPlayer = event.getPlayer();
        final BrandPlayer player = this.playerProvider.convertPlayer(proxyPlayer);

        this.playerProvider.unloadPlayer(player);
        this.brandGroupHandler.removePlayer(player);
    }
}
