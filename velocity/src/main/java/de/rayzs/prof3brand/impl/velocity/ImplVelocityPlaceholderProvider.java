package de.rayzs.prof3brand.impl.velocity;

import de.rayzs.prof3brand.api.placeholder.PlaceholderProvider;
import de.rayzs.prof3brand.api.player.BrandPlayer;

public class ImplVelocityPlaceholderProvider implements PlaceholderProvider {

    @Override
    public String replace(final BrandPlayer player, String text) {
        if (text.charAt(0) != '%') {
            return text;
        }

        return text;
    }
}
