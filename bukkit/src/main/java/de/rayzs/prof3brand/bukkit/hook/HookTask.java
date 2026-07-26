package de.rayzs.prof3brand.bukkit.hook;

@FunctionalInterface
public interface HookTask<T, V> {
    V apply(T t, V v);
}