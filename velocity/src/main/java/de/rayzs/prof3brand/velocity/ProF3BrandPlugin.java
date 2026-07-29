package de.rayzs.prof3brand.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.proxy.ProxyServer;
import de.rayzs.prof3brand.api.ProF3BrandProvider;
import de.rayzs.prof3brand.api.brand.BrandProvider;
import de.rayzs.prof3brand.api.placeholder.PlaceholderProvider;
import de.rayzs.prof3brand.api.player.BrandPlayerProvider;
import de.rayzs.prof3brand.api.scheduler.SchedulerProvider;
import de.rayzs.prof3brand.api.utils.VersionHelper;
import de.rayzs.prof3brand.common.impl.ImplProF3Brand;
import de.rayzs.prof3brand.impl.velocity.ImplVelocityBrandPlayerProvider;
import de.rayzs.prof3brand.impl.velocity.ImplVelocityBrandProvider;
import de.rayzs.prof3brand.impl.velocity.ImplVelocityPlaceholderProvider;
import de.rayzs.prof3brand.impl.velocity.ImplVelocitySchedulerProvider;
import de.rayzs.prof3brand.velocity.listener.PlayerListener;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.logging.Logger;

@Plugin(
        id = "prof3brand",
        name = "ProF3Brand",
        version = "1.0.1",
        url = "https://modrinth.com/plugin/prof3brand",
        description = "Fully customize the F3 server brand to your personal liking.",
        authors = {"Rayzs_YT"}
)
public class ProF3BrandPlugin {

    private ProF3BrandPlugin instance;
    private ProxyServer server;
    private Logger logger;
    private EventManager manager;

    @Inject
    public ProF3BrandPlugin(final ProxyServer server, final Logger logger) {
        this.instance = this;
        this.server = server;
        this.logger = logger;

        this.manager = server.getEventManager();
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        VersionHelper.initialize(null);

        final PlaceholderProvider placeholderProvider = new ImplVelocityPlaceholderProvider();
        final BrandProvider brandProvider = new ImplVelocityBrandProvider(placeholderProvider);
        final BrandPlayerProvider brandPlayerProvider = new ImplVelocityBrandPlayerProvider(brandProvider, server);
        final SchedulerProvider schedulerProvider = new ImplVelocitySchedulerProvider(this, server);

        final ImplProF3Brand api = new ImplProF3Brand(
                this,
                this.logger,
                schedulerProvider,
                brandPlayerProvider,
                brandProvider,
                placeholderProvider
        );
        ProF3BrandProvider.set(api);


        manager.register(this, new PlayerListener(api));

        final PluginContainer pluginContainer = this.server.getPluginManager().getPlugin("prof3brand").get();
        final String pluginVersion = pluginContainer.getDescription().getVersion().get();


        this.server.getCommandManager().register("prof3brand", new SimpleCommand() {
            @Override
            public void execute(final Invocation invocation) {
                final CommandSource source = invocation.source();
                source.sendMessage(MiniMessage.miniMessage().deserialize("Reloading brands.yml..."));
                api.reload();
                source.sendMessage(MiniMessage.miniMessage().deserialize("Successfully reloaded brand.yml!"));
            }

            @Override
            public boolean hasPermission(final Invocation invocation) {
                return invocation.source().hasPermission("prof3brand.use");
            }
        }, "probrand", "brand", "f3brand");
    }
}
