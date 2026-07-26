package de.rayzs.prof3brand.bukkit.impl;

import de.rayzs.prof3brand.api.placeholder.PlaceholderProvider;
import de.rayzs.prof3brand.api.player.BrandPlayer;
import de.rayzs.prof3brand.bukkit.hook.PluginHooks;
import de.rayzs.prof3brand.bukkit.hook.hooks.PlaceholderAPIHook;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class ImplBukkitPlaceholderProvider implements PlaceholderProvider {

    @Override
    public String replace(final BrandPlayer player, String text) {
        if (! (player.getOriginObject() instanceof Player bukkitPlayer)) {
            return text;
        }

        final String playerName = bukkitPlayer.getName();
        final World world = bukkitPlayer.getWorld();

        final String worldName = world.getName();
        final String gameMode = bukkitPlayer.getGameMode().name();
        final String opped = bukkitPlayer.isOp() ? "true" : "false";

        text = text
                .replace("%player%", playerName)
                .replace("%world%", worldName)
                .replace("%gamemode%", gameMode)
                .replace("%opped%", opped);

        return PluginHooks.PLACEHOLDERAPI.modifyIfExist(text, (PlaceholderAPIHook hook, String str)
                -> hook.replacePlaceholders(player, str)
        );
    }
}
