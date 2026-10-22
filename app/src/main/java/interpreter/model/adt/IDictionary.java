package interpreter.model.adt;

import interpreter.exceptions.ADTException;
import java.util.Map;


public interface IDictionary<K, V> {
    boolean isDefined(K key);
    void put(K key, V value);
    void update(K key, V value) throws ADTException;
    V lookup(K key) throws ADTException;
    Map<K, V> getContent();
}