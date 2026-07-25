package de.rayzs.prof3brand.impl.velocity;

import de.rayzs.prof3brand.api.placeholder.PlaceholderProvider;
import de.rayzs.prof3brand.api.player.BrandPlayer;

public class ImplVelocityPlaceholderProvider implements PlaceholderProvider {

    @Override
    public String replace(BrandPlayer player, String text) {
        return text;
    }
}
