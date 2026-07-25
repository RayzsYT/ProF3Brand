package de.rayzs.prof3brand.impl.bungee;

import de.rayzs.prof3brand.api.placeholder.PlaceholderProvider;
import de.rayzs.prof3brand.api.player.BrandPlayer;

public class ImplBungeePlaceholderProvider implements PlaceholderProvider {

    @Override
    public String replace(BrandPlayer player, String text) {
        return text;
    }
}
