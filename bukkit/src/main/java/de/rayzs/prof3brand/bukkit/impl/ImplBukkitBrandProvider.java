package de.rayzs.prof3brand.bukkit.impl;

import de.rayzs.prof3brand.api.brand.BrandProvider;
import de.rayzs.prof3brand.api.placeholder.PlaceholderProvider;
import de.rayzs.prof3brand.api.player.BrandPlayer;
import de.rayzs.prof3brand.api.utils.PacketUtils;
import de.rayzs.prof3brand.api.utils.VersionHelper;
import de.rayzs.prof3brand.bukkit.netty.BukkitPacketAnalyzer;
import io.netty.channel.Channel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.Messenger;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;

public class ImplBukkitBrandProvider implements BrandProvider {


    private final String channelName = VersionHelper.isBefore(1, 13) ? "MC|Brand" : "minecraft:brand";
    private final PlaceholderProvider placeholderProvider;
    private final Plugin plugin;

    private Class<?> brandPayloadClass, clientBoundCustomPacketPayloadPacketClass, customPacketPayloadPacketClass;


    public ImplBukkitBrandProvider(final PlaceholderProvider placeholderProvider, final Plugin plugin) {
        this.placeholderProvider = placeholderProvider;
        this.plugin = plugin;

        try {
            brandPayloadClass = Class.forName("net.minecraft.network.protocol.common.custom.BrandPayload");
            clientBoundCustomPacketPayloadPacketClass = Class.forName("net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket");
            customPacketPayloadPacketClass = Class.forName("net.minecraft.network.protocol.common.custom.CustomPacketPayload");


            final Server server = Bukkit.getServer();
            final Messenger messenger = server.getMessenger();

            final Method method = messenger.getClass().getDeclaredMethod("addToOutgoing", Plugin.class, String.class);
            method.setAccessible(true);

            method.invoke(messenger, plugin, channelName);
            messenger.registerOutgoingPluginChannel(plugin, channelName);

            method.setAccessible(false);

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }


    @Override
    public void send(final BrandPlayer player, String brandText) {

        if (! (player.getOriginObject() instanceof Player bukkitPlayer)) {
            return;
        }

        brandText = ChatColor.translateAlternateColorCodes('&', this.placeholderProvider.replace(player, brandText));

        if (!VersionHelper.isAtLeast(1, 20, 6)) {
            final PacketUtils.BrandManipulate serverBrand = new PacketUtils.BrandManipulate(brandText);
            bukkitPlayer.sendPluginMessage(this.plugin, this.channelName, serverBrand.getBytes());
            return;
        }

        try {
            final Channel channel = BukkitPacketAnalyzer.getPlayerChannel(bukkitPlayer);

            if (channel == null) {
                return;
            }

            final Object brandPayloadObj = this.brandPayloadClass
                    .getDeclaredConstructor(String.class)
                    .newInstance(brandText);

            final Object customPacketPayloadPacket = this.clientBoundCustomPacketPayloadPacketClass
                    .getDeclaredConstructor(this.customPacketPayloadPacketClass)
                    .newInstance(brandPayloadObj);

            channel.pipeline().writeAndFlush(customPacketPayloadPacket);

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void preparePlayer(final Player player) {
        if (VersionHelper.isAtLeast(1, 21, 7)) {
            return;
        }

        try {
            final Field channelsField = player.getClass().getDeclaredField("channels");
            channelsField.setAccessible(true);

            final Set<String> channels = (Set<String>) channelsField.get(player);
            channels.add(channelName);

            channelsField.setAccessible(false);

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
