package de.rayzs.prof3brand.impl.velocity;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import de.rayzs.prof3brand.api.brand.BrandProvider;
import de.rayzs.prof3brand.api.player.BrandPlayer;
import de.rayzs.prof3brand.api.utils.PacketUtils;
import io.netty.buffer.ByteBuf;

import java.lang.reflect.Method;

public class ImplVelocityBrandProvider implements BrandProvider {

    private final ProxyServer server;
    private Class<?> pluginMessagePacketClass, minecraftConnectionClass, connectedPlayerConnectionClass;
    private Method connectionMethod;


    public ImplVelocityBrandProvider(final ProxyServer server) {
        this.server = server;

        try {
            this.pluginMessagePacketClass = Class.forName("com.velocitypowered.proxy.protocol.packet.PluginMessagePacket");
            this.minecraftConnectionClass = Class.forName("com.velocitypowered.proxy.connection.MinecraftConnection");
            this.connectedPlayerConnectionClass = Class.forName("com.velocitypowered.proxy.connection.client.ConnectedPlayer");
            this.connectionMethod = connectedPlayerConnectionClass.getDeclaredMethod("getConnection");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }


    @Override
    public void send(final BrandPlayer player, final String brandText) {
        if (! (player.getOriginObject() instanceof Player proxyPlayer)) return;

        try {
            final Object connectedPlayerObj = connectedPlayerConnectionClass.cast(player);
            final Object minecraftConnectionObj = connectionMethod.invoke(connectedPlayerObj);

            final PacketUtils.BrandManipulate serverBrand = new PacketUtils.BrandManipulate(brandText, false);
            final String brand = proxyPlayer.getProtocolVersion().getProtocol() >= 393
                    ? "minecraft:brand" : "MC|Brand";


            final Object pluginMessagePacket = pluginMessagePacketClass
                    .getConstructor(String.class, ByteBuf.class)
                    .newInstance(brand, serverBrand.getByteBuf());

            final Method writeMethod = minecraftConnectionClass.getDeclaredMethod("write", Object.class);
            writeMethod.invoke(minecraftConnectionObj, pluginMessagePacket);

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
