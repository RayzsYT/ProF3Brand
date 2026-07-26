package de.rayzs.prof3brand.bungee.impl;

import de.rayzs.prof3brand.api.brand.BrandProvider;
import de.rayzs.prof3brand.api.placeholder.PlaceholderProvider;
import de.rayzs.prof3brand.api.player.BrandPlayer;
import de.rayzs.prof3brand.api.utils.PacketUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.protocol.ProtocolConstants;

import javax.management.MBeanRegistration;

public class ImplBungeeBrandProvider implements BrandProvider {

    private final PlaceholderProvider placeholderProvider;

    public ImplBungeeBrandProvider(final PlaceholderProvider placeholderProvider) {
        this.placeholderProvider = placeholderProvider;
    }

    @Override
    public void send(final BrandPlayer player, String brandText) {
        if (! (player.getOriginObject() instanceof ProxiedPlayer proxyPlayer)) {
            return;
        }

        brandText = ChatColor.translateAlternateColorCodes('&', this.placeholderProvider.replace(player, brandText));

        final PacketUtils.BrandManipulate serverBrand = new PacketUtils.BrandManipulate(brandText);
        final String brand = proxyPlayer.getPendingConnection().getVersion() >= ProtocolConstants.MINECRAFT_1_13
                ? "minecraft:brand" : "MC|Brand";

        proxyPlayer.sendData(brand, serverBrand.getBytes());
    }
}
