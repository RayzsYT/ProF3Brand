package de.rayzs.prof3brand.bungee.impl;

import de.rayzs.prof3brand.api.brand.BrandProvider;
import de.rayzs.prof3brand.api.player.BrandPlayer;
import de.rayzs.prof3brand.api.player.BrandPlayerProvider;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class ImplBungeeBrandPlayerProvider implements BrandPlayerProvider {

    private final BrandProvider brandProvider;

    private HashMap<UUID, BrandPlayer> players = new HashMap<>();


    public ImplBungeeBrandPlayerProvider(final BrandProvider brandProvider) {
        this.brandProvider = brandProvider;
    }

    @Override
    public <T> BrandPlayer convertPlayer(final T player) {
        if (player instanceof ProxiedPlayer proxyPlayer) {
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
        return convertPlayer(ProxyServer.getInstance().getPlayer(uuid));
    }

    @Override
    public BrandPlayer getPlayerByName(final String playerName) {
        return convertPlayer(ProxyServer.getInstance().getPlayer(playerName));
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

        private final ProxiedPlayer player;
        private final UUID uuid;
        private final String name;

        public ImplBrandPlayer(final ProxiedPlayer player) {
            this.player = player;
            this.uuid = player.getUniqueId();
            this.name = player.getName();
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
            return this.player.isConnected();
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
