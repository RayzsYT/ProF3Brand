package de.rayzs.prof3brand.bukkit.netty;

import de.rayzs.prof3brand.api.utils.VersionHelper;
import io.netty.channel.*;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class BukkitPacketAnalyzer {

    private static final ConcurrentHashMap<UUID, Channel> CACHED_CHANNELS = new ConcurrentHashMap<>();


    public static Channel getPlayerChannel(final Player player) {
        try {

            Channel channel = CACHED_CHANNELS.get(player.getUniqueId());

            if (channel == null) {
                channel = findPlayerChannel(player);
                CACHED_CHANNELS.put(player.getUniqueId(), channel);
            }

            return channel;

        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return null;
    }

    public static void unloadPlayerChannel(final Player player) {
        CACHED_CHANNELS.remove(player.getUniqueId());
    }

    public static void unloadAllPlayerChannels() {
        CACHED_CHANNELS.clear();
    }


    private static Object getPlayerConnection(Player player) throws Exception {
        final Object entityPlayer = player.getClass().getMethod("getHandle").invoke(player);

        for (final Field declaredField : entityPlayer.getClass().getDeclaredFields()) {
            if (declaredField.getType().getSimpleName().endsWith("PlayerConnection")) {
                return declaredField.get(entityPlayer);
            }
        }

        return null;
    }

    private static Channel findPlayerChannel(Player player) throws Exception {
        Object channelObject = null;

        if (VersionHelper.getSoftware().isPaperBased() && VersionHelper.isAtLeast(1, 20, 6)) {
            for (final Method declaredMethod : player.getClass().getDeclaredMethods()) {
                if (declaredMethod.getReturnType().getSimpleName().equalsIgnoreCase("ServerPlayer")) {
                    final Object serverPlayerObj = declaredMethod.invoke(player);
                    final Object serverGamePacketListenerImplObj = serverPlayerObj.getClass().getDeclaredField("connection").get(serverPlayerObj);
                    final Object connectionObj = serverGamePacketListenerImplObj.getClass().getSuperclass().getDeclaredField("connection").get(serverGamePacketListenerImplObj);

                    for (final Field declaredField : connectionObj.getClass().getDeclaredFields()) {
                        if (declaredField.getType().getSimpleName().endsWith("Channel")) {
                            channelObject = declaredField.get(connectionObj);
                        }
                    }
                }
            }

        } else {

            final Object playerConnection = getPlayerConnection(player);
            final Object networkManager = findPlayerNetworkManager(playerConnection);

            for (final Field declaredField : networkManager.getClass().getDeclaredFields()) {
                if (declaredField.getType().getSimpleName().endsWith("Channel")) {
                    channelObject = declaredField.get(networkManager);
                }
            }
        }

        return channelObject instanceof Channel channel ? channel : null;
    }

    private static Object findPlayerNetworkManager(final Object playerConnection) throws Exception {
        final Class<?> clazz = VersionHelper.isAfter(1, 20, 2)
                ? playerConnection.getClass().getSuperclass()
                : playerConnection.getClass();

        for (final Field declaredField : clazz.getDeclaredFields()) {
            if (declaredField.getType().getSimpleName().endsWith("NetworkManager")) {
                return declaredField.get(playerConnection);
            }
        }

        return null;
    }
}
