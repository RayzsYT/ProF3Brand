package de.rayzs.prof3brand.api.brand;

import de.rayzs.prof3brand.api.player.BrandPlayer;

public interface BrandProvider {
    void send(final BrandPlayer player, String brandText);
}
