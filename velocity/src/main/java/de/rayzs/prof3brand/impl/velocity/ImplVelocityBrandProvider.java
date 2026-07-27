package de.rayzs.prof3brand.impl.velocity;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import de.rayzs.prof3brand.api.brand.BrandProvider;
import de.rayzs.prof3brand.api.placeholder.PlaceholderProvider;
import de.rayzs.prof3brand.api.player.BrandPlayer;
import de.rayzs.prof3brand.api.utils.PacketUtils;
import io.netty.buffer.ByteBuf;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.lang.reflect.Method;

public class ImplVelocityBrandProvider implements BrandProvider {

    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    private final PlaceholderProvider placeholderProvider;

    private Class<?> pluginMessagePacketClass, minecraftConnectionClass, connectedPlayerConnectionClass;
    private Method connectionMethod;


    public ImplVelocityBrandProvider(final PlaceholderProvider placeholderProvider) {
        this.placeholderProvider = placeholderProvider;

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
    public void send(final BrandPlayer player, String brandText) {
        if (! (player.getOriginObject() instanceof Player proxyPlayer)) return;

        brandText = applyColors(brandText + "&r");

        try {
            final Object connectedPlayerObj = connectedPlayerConnectionClass.cast(player.getOriginObject());
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

    private String applyColors(final String text) {
        // Implementation using MiniMessage some other time...
        return text.replace("&", "§");
    }
}
