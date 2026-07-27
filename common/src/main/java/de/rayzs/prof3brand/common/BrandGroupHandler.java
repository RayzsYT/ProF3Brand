package de.rayzs.prof3brand.common;

import de.rayzs.prof3brand.api.ProF3Brand;
import de.rayzs.prof3brand.api.brand.BrandGroup;
import de.rayzs.prof3brand.api.config.Config;
import de.rayzs.prof3brand.api.config.ConfigProvider;
import de.rayzs.prof3brand.api.placeholder.PlaceholderProvider;
import de.rayzs.prof3brand.api.player.BrandPlayer;
import de.rayzs.prof3brand.api.scheduler.SchedulerProvider;
import de.rayzs.prof3brand.common.impl.brand.ImplBrandGroup;
import de.rayzs.prof3brand.common.impl.condition.ImplConditions;

import java.util.*;

public class BrandGroupHandler {

    private final ProF3Brand instance;
    private final SchedulerProvider schedulerProvider;
    private final PlaceholderProvider placeholderProvider;

    private final Map<UUID, BrandGroup> groups = new HashMap<>();
    private final Config config;

    private BrandGroup[] brandGroups;


    public BrandGroupHandler(
            final ProF3Brand instance,
            final SchedulerProvider schedulerProvider,
            final PlaceholderProvider placeholderProvider,
            final Config config
    ) {
        this.instance = instance;
        this.schedulerProvider = schedulerProvider;
        this.placeholderProvider = placeholderProvider;
        this.config = config;

        reload();
    }

    public void reload() {
        final long startTime = System.currentTimeMillis();

        this.groups.clear();
        if (this.brandGroups != null) for (BrandGroup brandGroup : this.brandGroups) {
            brandGroup.stopScheduler();
        }


        if (!this.config.exist()) {
            ConfigProvider.exportResourceFile(this.getClass(), "brands.yml");
        }

        this.config.reload();


        final List<BrandGroup> brandGroupsWithoutConditions = new ArrayList<>();
        final List<BrandGroup> brandGroupsWithConditions = new ArrayList<>();

        for (String key : this.config.getKeys(false)) {
            if (key.contains(".")) {
                continue;
            }

            final String conditions = (String) this.config.get(key, "condition");
            final List<String> brands = (ArrayList<String>) this.config.get(key, "brands");
            final boolean shuffle = (boolean) this.config.get(key, "shuffle");
            final int repeatDelay = (int) this.config.get(key, "repeat-delay");


            final BrandGroup brandGroup = new ImplBrandGroup(
                    this.schedulerProvider,
                    key,
                    new ImplConditions(this.placeholderProvider, conditions),
                    brands.toArray(new String[0]),
                    shuffle,
                    repeatDelay
            );

            (conditions.isEmpty() ? brandGroupsWithoutConditions : brandGroupsWithConditions).add(brandGroup);
        }


        final BrandGroup[] brandGroups = new BrandGroup[brandGroupsWithConditions.size() + brandGroupsWithoutConditions.size()];

        int i = 0;
        for (final BrandGroup group : brandGroupsWithConditions)
            brandGroups[i++] = group;

        for (final BrandGroup group : brandGroupsWithoutConditions)
            brandGroups[i++] = group;

        this.brandGroups = brandGroups;


        this.instance.info("Loaded " + brandGroups.length + " brand groups in total! (" + (System.currentTimeMillis() - startTime) + "ms)");
    }

    public void removePlayer(final BrandPlayer player) {
        final BrandGroup brandGroup = this.groups.remove(player.getUniqueId());
        if (brandGroup != null) brandGroup.removePlayer(player);
    }

    public void sendCurrentBrandToPlayer(final BrandPlayer player) {
        final BrandGroup brandGroup = this.groups.get(player.getUniqueId());
        if (brandGroup != null) {
            player.updateBrand(brandGroup.getBrand());
        }
    }

    public void reevaluatePlayerBrandGroups(final BrandPlayer player) {
        if (!player.isOnline()) {
            removePlayer(player);
            return;
        }


        final BrandGroup currentGroup = groups.get(player.getUniqueId());

        for (final BrandGroup brandGroup : this.brandGroups) {
            if (brandGroup.fulfillsConditions(player)) {

                if (brandGroup == currentGroup) {
                    return;
                }

                final BrandGroup prevGroup = groups.put(player.getUniqueId(), brandGroup);
                if (prevGroup != null) prevGroup.removePlayer(player);


                player.updateBrand(brandGroup.getBrand());
                brandGroup.addPlayer(player);

                return;
            }
        }

        removePlayer(player);
    }
}
