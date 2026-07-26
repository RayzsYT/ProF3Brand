package de.rayzs.prof3brand.bukkit.hook.hooks;

import de.rayzs.prof3brand.api.player.BrandPlayer;
import de.rayzs.prof3brand.bukkit.hook.Hook;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlaceholderAPIHook implements Hook {

    @Override
    public void start() {}

    public String replacePlaceholders(final BrandPlayer player, @NotNull String text) {
        if (player.getOriginObject() instanceof Player bukkitPlayer) {
            return PlaceholderAPI.setPlaceholders(bukkitPlayer, text);
        }

        return text;
    }
}