package interpreter.model.adt;

import java.util.List;


public interface IList<T> {
    void add(T elem);
    T get(int index);
    int size();
    List<T> getContent();
}