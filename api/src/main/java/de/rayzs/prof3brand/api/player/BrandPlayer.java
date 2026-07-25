package de.rayzs.prof3brand.api.player;

import java.util.UUID;

public interface BrandPlayer {

    UUID getUniqueId();
    String getName();

    Object getOriginObject();

    void updateBrand(String brand);
}
