package de.rayzs.prof3brand.common;

import de.rayzs.prof3brand.api.brand.BrandGroup;
import de.rayzs.prof3brand.api.condition.Conditions;
import de.rayzs.prof3brand.api.config.Config;
import de.rayzs.prof3brand.api.config.ConfigProvider;
import de.rayzs.prof3brand.api.placeholder.PlaceholderProvider;
import de.rayzs.prof3brand.api.player.BrandPlayer;
import de.rayzs.prof3brand.api.scheduler.SchedulerProvider;

import java.util.*;

public class BrandGroupHandler {

    private final SchedulerProvider schedulerProvider;
    private final PlaceholderProvider placeholderProvider;

    private final Map<UUID, BrandGroup> groups = new HashMap<>();
    private final Config config;

    private BrandGroup[] brandGroups;


    public BrandGroupHandler(
            final SchedulerProvider schedulerProvider,
            final PlaceholderProvider placeholderProvider,
            final Config config
    ) {
        this.schedulerProvider = schedulerProvider;
        this.placeholderProvider = placeholderProvider;
        this.config = config;

        reload();
    }

    public void reload() {
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


            final BrandGroup brandGroup = new BrandGroup(
                    schedulerProvider,
                    key,
                    new Conditions(placeholderProvider, conditions),
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
    }

    public void reevaluatePlayerBrandGroups(final BrandPlayer player) {
        if (!player.isOnline()) {
            final BrandGroup brandGroup = groups.remove(player.getUniqueId());
            if (brandGroup != null) brandGroup.removePlayer(player);

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

        final BrandGroup prevGroup = groups.remove(player.getUniqueId());
        prevGroup.removePlayer(player);
    }
}
