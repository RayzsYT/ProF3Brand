package de.rayzs.prof3brand.bukkit.hook;

import de.rayzs.prof3brand.api.ProF3BrandProvider;
import org.bukkit.Bukkit;

import java.util.function.Consumer;

public enum PluginHooks {

    PLACEHOLDERAPI(
            "PlaceholderAPI",
            "de.rayzs.prof3brand.bukkit.hook.hooks.PlaceholderAPIHook"
    );



    private Hook hookObj;

    PluginHooks(final String pluginName, final String hookClassPath) {
        final boolean enabled = Bukkit.getPluginManager().isPluginEnabled(pluginName);

        if (!enabled) {
            this.hookObj = null;
            return;
        }

        try {
            final Class<? extends Hook> hookClass = Class.forName(hookClassPath).asSubclass(Hook.class);
            this.hookObj = hookClass.newInstance();
            this.hookObj.start();

            ProF3BrandProvider.get().info("Successfully hooked into " + pluginName + "! ");
        } catch (final Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Takes and execute a consumer if the hook exists. If the hook does not exist, it will do nothing.
     *
     * @param consumer the consumer to execute if the hook exists.
     * @param <T> the type of the hook.
     */
    public <T extends Hook> void executeIfExist(final Consumer<T> consumer) {
        if (this.hookObj == null) {
            return;
        }

        consumer.accept((T) hookObj);
    }

    public <T extends Hook, V> V modifyIfExist(final V input, final HookTask<T, V> consumer) {
        if (this.hookObj == null) {
            return input;
        }

        return consumer.apply((T) hookObj, input);
    }
}