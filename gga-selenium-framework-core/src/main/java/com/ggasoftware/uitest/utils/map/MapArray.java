package com.ggasoftware.uitest.utils.map;


import com.ggasoftware.uitest.utils.LinqUtils;
import com.ggasoftware.uitest.utils.linqInterfaces.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static com.ggasoftware.uitest.utils.Timer.ignoreException;
import static com.ggasoftware.uitest.utils.LinqUtils.select;


/**
 * Created by Roman_Iovlev on 6/3/2015.
 */
public class MapArray<K, V> implements Collection<KeyValue<K,V>> {
    public List<KeyValue<K,V>> pairs;

    public MapArray() { pairs = new ArrayList<>(); }
    public MapArray(K key, V value) {
        this();
        add(key, value);
    }
    public <T> MapArray(Collection<T> collection, FuncTT<T, K> key, FuncTT<T, V> value) throws Exception {
        this();
        try {
            for (T t : collection)
                add(key.invoke(t), value.invoke(t));
        } catch (Exception ex) { throw new Exception("Can't init MapArray"); }
    }
    public MapArray(int count, FuncTT<Integer, K> key, FuncTT<Integer, V> value) throws Exception {
        this();
        try {
            for (int i = 0; i < count; i++)
                add(key.invoke(i), value.invoke(i));
        } catch (Exception ex) { throw new Exception("Can't init MapArray"); }
    }
    public boolean add(K key, V value) {
        if (keys().contains(key))
            return false;
        pairs.add(new KeyValue<>(key, value));
        return true;
    }

    public V get(K key) {
        KeyValue<K, V> first = null;
        try { first = LinqUtils.first(pairs, pair -> pair.key.equals(key));
        } catch (Exception ignore) {}
        return (first != null) ? first.value : null;
    }
    public KeyValue<K,V> get(int index) {
        if (index < 0) index = pairs.size() + index;
        if (index < 0) return null;
        return (pairs.size() > index)
            ? pairs.get(index)
            : null;
    }
    public K key(int index) {
        return get(index).key;
    }
    public V value(int index) {
        return get(index).value;
    }
    public Collection<K> keys() {
        return ignoreException(() -> select(pairs, pair -> pair.key));
    }
    public Collection<V> values() {
        return ignoreException(() -> select(pairs, pair -> pair.value));
    }

    public int size() { return pairs.size(); }
    public int count() {
        return size();
    }
    public boolean isEmpty() { return size() == 0; }
    public boolean any() { return size() > 0; }

    public KeyValue<K,V> last() {
        return get(size() - 1);
    }
    public KeyValue<K,V> first() {
        return get(0);
    }

    public boolean contains(Object o) {
        return values().contains(o);
    }

    public Iterator<KeyValue<K, V>> iterator() {
        return pairs.iterator();
    }

    public Object[] toArray() {
        return pairs.toArray();
    }

    public <T> T[] toArray(T[] a) {
        return pairs.toArray(a);
    }

    public boolean add(KeyValue<K, V> kvKeyValue) {
        return pairs.add(kvKeyValue);
    }

    public boolean remove(Object o) {
        boolean isRemoved = false;
        for (KeyValue<K, V> kv : pairs)
            if (kv.value.equals(o)) {
                pairs.remove(kv);
                isRemoved = true;
            }
        return isRemoved;
    }

    public boolean containsAll(Collection<?> c) {
        for(Object o : c)
            if (!contains(o))
                return false;
        return true;
    }

    public boolean addAll(Collection<? extends KeyValue<K, V>> c) {
        for(KeyValue<K, V> o : c)
            add(o);
        return true;
    }

    public boolean removeAll(Collection<?> c) {
        for(Object o : c)
            if (!remove(o))
                return false;
        return true;
    }

    public boolean retainAll(Collection<?> c) {
        for(KeyValue kv : pairs)
            if (!c.contains(kv))
                if (!remove(kv))
                    return false;
        return true;
    }

    public void clear() {
        pairs.clear();
    }
}
