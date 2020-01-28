package org.nting.toolkit.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;

/** Less memory usage than HashMap, but get(), remove() and put() operations are expensive [O(n)]. */
public class SimpleMap<K, V> implements Map<K, V> {

    private final List<Entry<K, V>> entries = Lists.newLinkedList();

    @Override
    public V put(K key, V value) {
        V oldValue = remove(key);
        entries.add(new SimpleEntry<>(key, value));
        return oldValue;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        map.forEach(this::put);
    }

    @Override
    public V get(Object key) {
        return entries.stream().filter(entry -> Objects.equal(entry.getKey(), key)).findFirst().map(Entry::getValue)
                .orElse(null);
    }

    @Override
    public V remove(Object key) {
        V value = null;
        for (Iterator<Entry<K, V>> i = entries.iterator(); i.hasNext();) {
            Map.Entry<K, V> entry = i.next();
            if (Objects.equal(entry.getKey(), key)) {
                i.remove();
                if (value == null) {
                    value = entry.getValue();
                }
            }
        }
        return value;
    }

    @Override
    public void clear() {
        entries.clear();
    }

    @Override
    public boolean containsKey(Object key) {
        return entries.stream().anyMatch(entry -> Objects.equal(entry.getKey(), key));
    }

    @Override
    public boolean containsValue(Object value) {
        return entries.stream().anyMatch(entry -> Objects.equal(entry.getValue(), value));
    }

    @Override
    @Deprecated
    public Set<Entry<K, V>> entrySet() {
        throw new UnsupportedOperationException("Use entries() method instead, which returns a list.");
    }

    public List<Entry<K, V>> entries() {
        return entries;
    }

    @Override
    public boolean isEmpty() {
        return entries.isEmpty();
    }

    @Override
    public int size() {
        return entries.size();
    }

    @Override
    public Set<K> keySet() {
        return entries.stream().map(Entry::getKey).collect(Collectors.toSet());
    }

    @Override
    public Collection<V> values() {
        return entries.stream().map(Entry::getValue).collect(Collectors.toList());
    }

    private static class SimpleEntry<K, V> implements Entry<K, V> {

        private final K key;
        private V value;

        public SimpleEntry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public V setValue(V value) {
            V old = this.value;
            this.value = value;
            return old;
        }
    }
}
