package de.rayzs.prof3brand.api.brand;

import de.rayzs.prof3brand.api.player.BrandPlayer;

public interface BrandGroup {

    void addPlayer(final BrandPlayer player);
    void removePlayer(final BrandPlayer player);

    boolean fulfillsConditions(final BrandPlayer player);
    void stopScheduler();
    void update();

    String getBrandName();
    String getBrand();

    int getRepeatDelay();
}
