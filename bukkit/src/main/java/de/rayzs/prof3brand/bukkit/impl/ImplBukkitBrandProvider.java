package de.rayzs.prof3brand.bukkit.impl;

import de.rayzs.prof3brand.api.ProF3BrandProvider;
import de.rayzs.prof3brand.api.brand.BrandProvider;
import de.rayzs.prof3brand.api.player.BrandPlayer;
import de.rayzs.prof3brand.api.utils.PacketUtils;
import de.rayzs.prof3brand.api.utils.VersionHelper;
import de.rayzs.prof3brand.bukkit.netty.BukkitPacketAnalyzer;
import io.netty.channel.Channel;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.Messenger;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;

public class ImplBukkitBrandProvider implements BrandProvider {


    private final String channelName = VersionHelper.isBefore(1, 13) ? "MC|Brand" : "minecraft:brand";
    private final Plugin plugin = (Plugin) ProF3BrandProvider.get().getPluginLoader();

    private Class<?> brandPayloadClass, clientBoundCustomPacketPayloadPacketClass, customPacketPayloadPacketClass;


    public ImplBukkitBrandProvider() {
        try {
            brandPayloadClass = Class.forName("net.minecraft.network.protocol.common.custom.BrandPayload");
            clientBoundCustomPacketPayloadPacketClass = Class.forName("net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket");
            customPacketPayloadPacketClass = Class.forName("net.minecraft.network.protocol.common.custom.CustomPacketPayload");


            final Server server = Bukkit.getServer();
            final Messenger messenger = server.getMessenger();

            final Method method = messenger.getClass().getDeclaredMethod("addToOutgoing", Plugin.class, String.class);
            method.invoke(messenger, plugin, channelName);
            messenger.registerOutgoingPluginChannel(plugin, channelName);

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }


    @Override
    public void send(final BrandPlayer player, final String brandText) {
        if (! (player instanceof Player bukkitPlayer)) {
            return;
        }

        if (!VersionHelper.isAtLeast(1, 20, 6)) {
            final PacketUtils.BrandManipulate serverBrand = new PacketUtils.BrandManipulate(brandText);
            bukkitPlayer.sendPluginMessage(plugin, channelName, serverBrand.getBytes());
            return;
        }

        try {
            final Channel channel = BukkitPacketAnalyzer.getPlayerChannel(bukkitPlayer);

            if (channel == null) {
                return;
            }

            final Object brandPayloadObj = brandPayloadClass
                    .getDeclaredConstructor(String.class)
                    .newInstance(brandText);

            final Object customPacketPayloadPacket = clientBoundCustomPacketPayloadPacketClass
                    .getDeclaredConstructor(customPacketPayloadPacketClass)
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
