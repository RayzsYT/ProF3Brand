package de.rayzs.prof3brand.impl.velocity;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import de.rayzs.prof3brand.api.placeholder.PlaceholderProvider;
import de.rayzs.prof3brand.api.player.BrandPlayer;

import java.util.Optional;

public class ImplVelocityPlaceholderProvider implements PlaceholderProvider {

    @Override
    public String replace(final BrandPlayer player, String text) {
        if (player.getOriginObject() instanceof Player proxyPlayer) {
            final Optional<ServerConnection> serverOptional = proxyPlayer.getCurrentServer();
            final String playerName = proxyPlayer.getUsername();

            String serverName = "";
            int serverPlayerCount = 0;

            if (serverOptional.isPresent()) {
                final ServerConnection connection = serverOptional.get();
                final RegisteredServer server = connection.getServer();
                final ServerInfo info = connection.getServerInfo();

                serverName = info.getName();
                serverPlayerCount = server.getPlayersConnected().size();
            }

            text = text
                    .replace("%player%", playerName)
                    .replace("%server_name%", serverName)
                    .replace("%server_count%", String.valueOf(serverPlayerCount));
        }

        return text;
    }
}
