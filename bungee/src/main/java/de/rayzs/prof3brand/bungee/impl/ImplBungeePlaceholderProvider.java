package de.rayzs.prof3brand.bungee.impl;

import de.rayzs.prof3brand.api.placeholder.PlaceholderProvider;
import de.rayzs.prof3brand.api.player.BrandPlayer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;

public class ImplBungeePlaceholderProvider implements PlaceholderProvider {

    @Override
    public String replace(final BrandPlayer player, String text) {
        if (player.getOriginObject() instanceof ProxiedPlayer proxyPlayer) {
            final String playerName = proxyPlayer.getName();

            final Server server = proxyPlayer.getServer();
            final String serverName = server != null ? server.getInfo().getName() : "";

            final int serverPlayerCount = server != null ? server.getInfo().getPlayers().size() : 0;


            text = text
                    .replace("%player%", playerName)
                    .replace("%server_name%", serverName)
                    .replace("%server_count%", String.valueOf(serverPlayerCount));
        }

        return text;
    }
}
