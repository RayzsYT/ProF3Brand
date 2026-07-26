package de.rayzs.prof3brand.bukkit.impl;

import de.rayzs.prof3brand.api.ProF3BrandProvider;
import de.rayzs.prof3brand.api.player.BrandPlayer;
import de.rayzs.prof3brand.api.player.BrandPlayerProvider;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public class ImplBukkitBrandPlayerProvider implements BrandPlayerProvider {

    private HashMap<UUID, BrandPlayer> players = new HashMap<>();

    @Override
    public <T> BrandPlayer convertPlayer(final T player) {
        if (player instanceof Player bukkitPlayer) {
            BrandPlayer brandPlayer = players.get(bukkitPlayer.getUniqueId());

            if (brandPlayer == null) {
                brandPlayer = new ImplBrandPlayer(bukkitPlayer);
                this.players.put(bukkitPlayer.getUniqueId(), brandPlayer);
            }

            return brandPlayer;
        }

        return null;
    }

    @Override
    public BrandPlayer getPlayerByUUID(final UUID uuid) {
        return convertPlayer(Bukkit.getPlayer(uuid));
    }

    @Override
    public BrandPlayer getPlayerByName(final String playerName) {
        return convertPlayer(Bukkit.getPlayer(playerName));
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

    private static class ImplBrandPlayer implements BrandPlayer {

        private final Player player;
        private final UUID uuid;
        private final String name;

        public ImplBrandPlayer(final Player player) {
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
        public Object getOriginObject() {
            return this.player;
        }

        @Override
        public void updateBrand(final String brand) {
            ProF3BrandProvider.get().getBrandProvider().send(this, brand);
        }
    }
}
