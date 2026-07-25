package de.rayzs.prof3brand.common;

import de.rayzs.prof3brand.api.brand.BrandGroup;
import de.rayzs.prof3brand.api.condition.Conditions;
import de.rayzs.prof3brand.api.config.Config;
import de.rayzs.prof3brand.api.player.BrandPlayer;

import java.util.*;

public class BrandGroupHandler {

    private final Map<UUID, BrandGroup> groups = new HashMap<>();
    private final Config config;

    private BrandGroup[] brandGroups;


    public BrandGroupHandler(final Config config) {
        this.config = config;

        reload();
    }

    public void reload() {
        final List<BrandGroup> brandGroups = new ArrayList<>();

        for (String key : this.config.getKeys(false)) {
            if (!key.contains(".")) {
                continue;
            }

            final String conditions = (String) this.config.get(key, "condition");
            final List<String> brands = (ArrayList<String>) this.config.get(key, "brands");
            final boolean shuffle = (boolean) this.config.get(key, "shuffle");


            final BrandGroup brandGroup = new BrandGroup(
                    key,
                    new Conditions(conditions),
                    brands.toArray(new String[0]),
                    shuffle
            );

            brandGroups.add(brandGroup);
        }

        this.brandGroups = brandGroups.toArray(new BrandGroup[0]);
    }

    public void reevaluatePlayerBrandGroups(final BrandPlayer player) {
        boolean found = false;

        for (final BrandGroup brandGroup : this.brandGroups) {
            if (brandGroup.fulfillsConditions(player)) {
                found = true;
                groups.put(player.getUniqueId(), brandGroup);
            }
        }

        if (!found) groups.remove(player.getUniqueId());
    }
}
