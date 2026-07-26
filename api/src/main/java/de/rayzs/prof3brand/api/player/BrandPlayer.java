package de.rayzs.prof3brand.api.player;

import java.util.UUID;

public interface BrandPlayer {

    UUID getUniqueId();
    String getName();

    boolean isOnline();

    Object getOriginObject();

    void updateBrand(String brand);
}
