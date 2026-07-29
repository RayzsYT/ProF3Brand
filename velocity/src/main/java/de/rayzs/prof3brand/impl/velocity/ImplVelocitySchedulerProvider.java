package de.rayzs.prof3brand.impl.velocity;

import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.scheduler.Scheduler;
import de.rayzs.prof3brand.api.scheduler.SchedulerProvider;
import de.rayzs.prof3brand.api.scheduler.SchedulerTask;
import de.rayzs.prof3brand.velocity.ProF3BrandPlugin;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class ImplVelocitySchedulerProvider implements SchedulerProvider {

    private final ProF3BrandPlugin plugin;
    private final ProxyServer proxyServer;
    private final Scheduler taskScheduler;

    public ImplVelocitySchedulerProvider(final ProF3BrandPlugin plugin, final ProxyServer proxyServer) {
        this.plugin = plugin;
        this.proxyServer = proxyServer;
        this.taskScheduler = proxyServer.getScheduler();
    }


    // Sync schedulers

    @Override
    public SchedulerTask createScheduler(final Consumer<SchedulerTask> scheduler) {
        final SchedulerTask schedulerTask = new SchedulerTask();

        this.taskScheduler.buildTask(plugin, () -> {
            if (!schedulerTask.isRunning()) {
                return;
            }

            scheduler.accept(schedulerTask);
        }).schedule();

        return schedulerTask;
    }

    @Override
    public SchedulerTask createScheduler(final Consumer<SchedulerTask> scheduler, final long delay) {
        final SchedulerTask schedulerTask = new SchedulerTask();

        this.taskScheduler.buildTask(plugin, () -> {
            if (!schedulerTask.isRunning()) {
                return;
            }

            scheduler.accept(schedulerTask);
        }).delay(delay * 50, TimeUnit.MILLISECONDS)
                .schedule();

        return schedulerTask;
    }

    @Override
    public SchedulerTask createScheduler(final Consumer<SchedulerTask> scheduler, final long delay, final long period) {
        final SchedulerTask schedulerTask = new SchedulerTask();

        this.taskScheduler.buildTask(plugin, () -> {
            if (!schedulerTask.isRunning()) {
                return;
            }

            scheduler.accept(schedulerTask);
        }).delay(delay * 50, TimeUnit.MILLISECONDS)
                .repeat(delay * 50, TimeUnit.MILLISECONDS)
                .schedule();

        return schedulerTask;
    }


    // Async schedulers

    @Override
    public SchedulerTask createAsyncScheduler(final Consumer<SchedulerTask> scheduler) {
        final SchedulerTask schedulerTask = new SchedulerTask();

        this.taskScheduler.buildTask(plugin, () -> {
            if (!schedulerTask.isRunning()) {
                return;
            }

            scheduler.accept(schedulerTask);
        }).schedule();

        return schedulerTask;
    }

    @Override
    public SchedulerTask createAsyncScheduler(final Consumer<SchedulerTask> scheduler, final long delay) {
        final SchedulerTask schedulerTask = new SchedulerTask();

        this.taskScheduler.buildTask(plugin, () -> {
                    if (!schedulerTask.isRunning()) {
                        return;
                    }

                    scheduler.accept(schedulerTask);
                }).delay(delay * 50, TimeUnit.MILLISECONDS)
                .schedule();

        return schedulerTask;
    }

    @Override
    public SchedulerTask createAsyncScheduler(final Consumer<SchedulerTask> scheduler, final long delay, final long period) {
        final SchedulerTask schedulerTask = new SchedulerTask();

        this.taskScheduler.buildTask(plugin, () -> {
                    if (!schedulerTask.isRunning()) {
                        return;
                    }

                    scheduler.accept(schedulerTask);
                }).delay(delay * 50, TimeUnit.MILLISECONDS)
                .repeat(delay * 50, TimeUnit.MILLISECONDS)
                .schedule();

        return schedulerTask;
    }
}