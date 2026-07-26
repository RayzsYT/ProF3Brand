package de.rayzs.prof3brand.impl.velocity;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import de.rayzs.prof3brand.api.brand.BrandProvider;
import de.rayzs.prof3brand.api.player.BrandPlayer;
import de.rayzs.prof3brand.api.player.BrandPlayerProvider;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class ImplVelocityBrandPlayerProvider implements BrandPlayerProvider {

    private final BrandProvider brandProvider;
    private final ProxyServer proxyServer;

    private HashMap<UUID, BrandPlayer> players = new HashMap<>();


    public ImplVelocityBrandPlayerProvider(final BrandProvider brandProvider, final ProxyServer proxyServer) {
        this.brandProvider = brandProvider;
        this.proxyServer = proxyServer;
    }

    @Override
    public <T> BrandPlayer convertPlayer(final T player) {
        if (player instanceof Player proxyPlayer) {
            BrandPlayer brandPlayer = players.get(proxyPlayer.getUniqueId());

            if (brandPlayer == null) {
                brandPlayer = new ImplBrandPlayer(proxyPlayer);
                this.players.put(proxyPlayer.getUniqueId(), brandPlayer);
            }

            return brandPlayer;
        }

        return null;
    }

    @Override
    public BrandPlayer getPlayerByUUID(final UUID uuid) {
        return convertPlayer(this.proxyServer.getPlayer(uuid));
    }

    @Override
    public BrandPlayer getPlayerByName(final String playerName) {
        return convertPlayer(this.proxyServer.getPlayer(playerName));
    }

    @Override
    public Collection<BrandPlayer> getPlayers() {
        return players.values();
    }

    @Override
    public void unloadPlayer(final BrandPlayer player) {
        this.players.remove(player.getUniqueId());
    }

    @Override
    public void unloadAllPlayers() {
        this.players.clear();
        this.players = null;
    }

    private class ImplBrandPlayer implements BrandPlayer {

        private final Player player;
        private final UUID uuid;
        private final String name;

        public ImplBrandPlayer(final Player player) {
            this.player = player;
            this.uuid = player.getUniqueId();
            this.name = player.getUsername();
        }

        @Override
        public UUID getUniqueId() {
            return this.uuid;
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public boolean isOnline() {
            return this.player.isActive();
        }

        @Override
        public Object getOriginObject() {
            return this.player;
        }

        @Override
        public void updateBrand(final String brand) {
            brandProvider.send(this, brand);
        }
    }
}
