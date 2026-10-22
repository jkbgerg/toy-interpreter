package interpreter.model.adt;

import java.util.ArrayList;
import java.util.List;

public class MyList<T> implements IList<T> {
    private final List<T> list = new ArrayList<>();

    @Override
    public void add(T elem) {
        list.add(elem);
    }

    @Override
    public T get(int index) {
        return list.get(index);
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public List<T> getContent() {
        return list;
    }

    @Override
    public String toString() {
        return list.toString();
    }
}