package de.rayzs.prof3brand.api.brand;

import de.rayzs.prof3brand.api.condition.Conditions;
import de.rayzs.prof3brand.api.player.BrandPlayer;

import java.util.Random;

public class BrandGroup {

    private static final Random RANDOM = new Random();

    private final Conditions conditions;
    private final String brandName;
    private final String[] brands;
    private final boolean shuffle;

    private int currentBrandIndex;


    public BrandGroup(final String brandName, final Conditions conditions, final String[] brands, final boolean shuffle) {
        this.brandName = brandName;
        this.conditions = conditions;
        this.brands = brands;
        this.shuffle = shuffle;

        this.currentBrandIndex = brands.length;
    }

    public boolean fulfillsConditions(final BrandPlayer player) {
        return conditions.evaluate(player);
    }

    public void update() {
        if (shuffle) {
            this.currentBrandIndex = RANDOM.nextInt(brands.length);
            return;
        }

        if (++this.currentBrandIndex == this.brands.length) {
            this.currentBrandIndex = 0;
        }
    }

    public String getBrand() {
        return this.brands[this.currentBrandIndex];
    }

    public String getBrandName() {
        return brandName;
    }
}
