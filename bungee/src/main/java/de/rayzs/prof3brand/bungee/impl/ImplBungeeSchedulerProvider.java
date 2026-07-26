package de.rayzs.prof3brand.bungee.impl;

import de.rayzs.prof3brand.api.scheduler.SchedulerProvider;
import de.rayzs.prof3brand.api.scheduler.SchedulerTask;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.TaskScheduler;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class ImplBungeeSchedulerProvider implements SchedulerProvider {

    private final Plugin plugin;
    private final TaskScheduler taskScheduler;

    public ImplBungeeSchedulerProvider(final Plugin plugin) {
        this.plugin = plugin;
        this.taskScheduler = ProxyServer.getInstance().getScheduler();
    }


    // Sync schedulers

    @Override
    public SchedulerTask createScheduler(final Consumer<SchedulerTask> scheduler) {
        final SchedulerTask schedulerTask = new SchedulerTask();

        this.taskScheduler.schedule(plugin, () -> {
            if (!schedulerTask.isRunning()) {
                return;
            }

            scheduler.accept(schedulerTask);
        }, 0, TimeUnit.MILLISECONDS);

        return schedulerTask;
    }

    @Override
    public SchedulerTask createScheduler(final Consumer<SchedulerTask> scheduler, final long delay) {
        final SchedulerTask schedulerTask = new SchedulerTask();

        this.taskScheduler.schedule(plugin, () -> {
            if (!schedulerTask.isRunning()) {
                return;
            }

            scheduler.accept(schedulerTask);
        }, delay * 50, TimeUnit.MILLISECONDS);

        return schedulerTask;
    }

    @Override
    public SchedulerTask createScheduler(final Consumer<SchedulerTask> scheduler, final long delay, final long period) {
        final SchedulerTask schedulerTask = new SchedulerTask();

        this.taskScheduler.schedule(plugin, () -> {
            if (!schedulerTask.isRunning()) {
                return;
            }

            scheduler.accept(schedulerTask);
        }, delay * 50, period * 50, TimeUnit.MILLISECONDS);

        return schedulerTask;
    }


    // Async schedulers

    @Override
    public SchedulerTask createAsyncScheduler(final Consumer<SchedulerTask> scheduler) {
        final SchedulerTask schedulerTask = new SchedulerTask();

        this.taskScheduler.runAsync(plugin, () -> {
            if (!schedulerTask.isRunning()) {
                return;
            }

            scheduler.accept(schedulerTask);
        });

        return schedulerTask;
    }

    @Override
    public SchedulerTask createAsyncScheduler(final Consumer<SchedulerTask> scheduler, final long delay) {
        final SchedulerTask schedulerTask = new SchedulerTask();

        this.taskScheduler.schedule(plugin, () -> {
            if (!schedulerTask.isRunning()) {
                return;
            }

            scheduler.accept(schedulerTask);
        }, delay * 50, TimeUnit.MILLISECONDS);

        return schedulerTask;
    }

    @Override
    public SchedulerTask createAsyncScheduler(final Consumer<SchedulerTask> scheduler, final long delay, final long period) {
        final SchedulerTask schedulerTask = new SchedulerTask();

        this.taskScheduler.schedule(plugin, () -> {
            if (!schedulerTask.isRunning()) {
                return;
            }

            scheduler.accept(schedulerTask);
        }, delay * 50, period * 50, TimeUnit.MILLISECONDS);

        return schedulerTask;
    }
}