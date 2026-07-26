package de.rayzs.prof3brand.bukkit.impl;

import de.rayzs.prof3brand.api.placeholder.PlaceholderProvider;
import de.rayzs.prof3brand.api.player.BrandPlayer;
import de.rayzs.prof3brand.bukkit.hook.PluginHooks;
import de.rayzs.prof3brand.bukkit.hook.hooks.PlaceholderAPIHook;

public class ImplBukkitPlaceholderProvider implements PlaceholderProvider {

    @Override
    public String replace(final BrandPlayer player, String text) {
        if (text.charAt(0) != '%') {
            return text;
        }

        return PluginHooks.PLACEHOLDERAPI.modifyIfExist(text, (PlaceholderAPIHook hook, String str)
                -> hook.replacePlaceholders(player, str)
        );
    }
}
