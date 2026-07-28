package de.rayzs.prof3brand.api.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TripleMap<K, V, E> {

    private final Map<K, V> fstValMap = new HashMap<>();
    private final Map<K, E> sndValMap = new HashMap<>();


    public V putFst(K key, V fstVal) {
        return fstValMap.put(key, fstVal);
    }

    public E putSnd(K key, E sndVal) {
        return sndValMap.put(key, sndVal);
    }

    public boolean removeFst(K key, V fstVal) {
        return fstValMap.remove(key, fstVal);
    }

    public boolean removeSnd(K key, E sndVal) {
        return sndValMap.remove(key, sndVal);
    }

    public boolean containsFst(K key) {
        return fstValMap.containsKey(key);
    }

    public boolean containsSnd(K key) {
        return sndValMap.containsKey(key);
    }

    public V getFst(K key) {
        return fstValMap.get(key);
    }

    public E getSnd(K key) {
        return sndValMap.get(key);
    }

    public Set<K> keySet() {
        return fstValMap.keySet();
    }
}
