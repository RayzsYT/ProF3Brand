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
        this.repeatDelay = brands.length == 0 ? -1 : brands.length == 1
                ? (repeatDelay <= 0 ? (this.brands[0].contains("%") ? 10 : -1) : repeatDelay)
                : (repeatDelay == -1 ? -1 : repeatDelay <= 0 ? 10 : repeatDelay);

        this.currentBrandIndex = brands.length - 1;

        if (this.repeatDelay != -1) this.task = schedulerProvider.createAsyncScheduler(task -> {
            update();

            final Iterator<BrandPlayer> iterator = this.players.iterator();
            final String brand = getBrand();

            while (iterator.hasNext()) {
                final BrandPlayer player = iterator.next();
                player.updateBrand(brand);
            }
        }, 1, this.repeatDelay);
    }

    public void addPlayer(final BrandPlayer player) {
        this.players.add(player);
    }

    public void removePlayer(final BrandPlayer player) {
        this.players.remove(player);
    }

    public boolean fulfillsConditions(final BrandPlayer player) {
        return this.conditions.evaluate(player);
    }

    public void update() {
        if (shuffle) {
            this.currentBrandIndex = RANDOM.nextInt(this.brands.length);
            return;
        }

        if (this.currentBrandIndex + 1 == brands.length) {
            this.currentBrandIndex = 0;
        } else this.currentBrandIndex++;
    }

    public int getRepeatDelay() {
        return this.repeatDelay;
    }

    public void stopScheduler() {
        if (this.task != null) this.task.stop();
    }

    public String getBrand() {
        return this.brands[this.currentBrandIndex];
    }

    public String getBrandName() {
        return this.brandName;
    }
}
