package de.rayzs.prof3brand.api.placeholder;

import de.rayzs.prof3brand.api.player.BrandPlayer;

public interface PlaceholderProvider {

    String replace(BrandPlayer player, String text);
}
