package de.rayzs.prof3brand.api.brand;

import de.rayzs.prof3brand.api.player.BrandPlayer;
import de.rayzs.prof3brand.api.utils.PacketUtils;

public interface BrandProvider {
    void send(final BrandPlayer player, final String brandText);
}
