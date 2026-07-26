package de.rayzs.prof3brand.api.brand;

import de.rayzs.prof3brand.api.condition.Conditions;
import de.rayzs.prof3brand.api.player.BrandPlayer;
import de.rayzs.prof3brand.api.player.BrandPlayerProvider;
import de.rayzs.prof3brand.api.scheduler.SchedulerProvider;
import de.rayzs.prof3brand.api.scheduler.SchedulerTask;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class BrandGroup {

    private static final Random RANDOM = new Random();

    private final List<BrandPlayer> players = new LinkedList<>();

    private final Conditions conditions;
    private final String brandName;
    private final String[] brands;
    private final boolean shuffle;
    private final int repeatDelay;

    private int currentBrandIndex;
    private SchedulerTask task;

    public BrandGroup(
            final SchedulerProvider schedulerProvider,
            final String brandName,
            final Conditions conditions,
            final String[] brands,
            final boolean shuffle,
            final int repeatDelay
    ) {
        this.brandName = brandName;
        this.conditions = conditions;
        this.brands = brands;
        this.shuffle = shuffle;
        this.repeatDelay = repeatDelay;

        this.currentBrandIndex = brands.length - 1;

        if (getRepeatDelay() != -1) this.task = schedulerProvider.createAsyncScheduler(task -> {
            update();

            final Iterator<BrandPlayer> iterator = players.iterator();
            final String brand = getBrand();

            while (iterator.hasNext()) {
                final BrandPlayer player = iterator.next();
                player.updateBrand(brand);
            }
        }, 0, repeatDelay);
    }

    public void addPlayer(final BrandPlayer player) {
        players.add(player);
    }

    public void removePlayer(final BrandPlayer player) {
        players.remove(player);
    }

    public boolean fulfillsConditions(final BrandPlayer player) {
        return conditions.evaluate(player);
    }

    public void update() {
        if (shuffle) {
            this.currentBrandIndex = RANDOM.nextInt(brands.length);
            return;
        }

        if (this.currentBrandIndex + 1 == brands.length) {
            this.currentBrandIndex = 0;
        } else currentBrandIndex++;
    }

    public int getRepeatDelay() {
        return brands.length <= 1 ? -1 : repeatDelay;
    }

    public void stopScheduler() {
        task.stop();
    }

    public String getBrand() {
        return this.brands[this.currentBrandIndex];
    }

    public String getBrandName() {
        return brandName;
    }
}
