package de.rayzs.prof3brand.api.player;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

public interface BrandPlayerProvider {

    <T> BrandPlayer convertPlayer(T player);

    BrandPlayer getPlayerByUUID(UUID uuid);
    BrandPlayer getPlayerByName(String playerName);

    Collection<BrandPlayer> getPlayers();

    void unloadPlayer(BrandPlayer player);
    void unloadAllPlayers();
}
