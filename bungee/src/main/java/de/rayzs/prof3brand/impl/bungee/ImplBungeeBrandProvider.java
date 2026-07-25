package de.rayzs.prof3brand.impl.bungee;

import de.rayzs.prof3brand.api.brand.BrandProvider;
import de.rayzs.prof3brand.api.player.BrandPlayer;
import de.rayzs.prof3brand.api.utils.PacketUtils;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import net.md_5.bungee.protocol.ProtocolConstants;

public class ImplBungeeBrandProvider implements BrandProvider {

    private ScheduledTask task;

    @Override
    public void send(final BrandPlayer player, final String brandText) {
        if (! (player.getOriginObject() instanceof ProxiedPlayer proxiedPlayer)) {
            return;
        }

        final PacketUtils.BrandManipulate serverBrand = new PacketUtils.BrandManipulate(brandText);
        final String brand = proxiedPlayer.getPendingConnection().getVersion() >= ProtocolConstants.MINECRAFT_1_13
                ? "minecraft:brand" : "MC|Brand";

        proxiedPlayer.sendData(brand, serverBrand.getBytes());
    }
}
