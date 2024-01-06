package CustomArrayListHW;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.NoSuchElementException;

public class CustomArrayList<T> {

    private final int DEFAULT_CAPACITY = 16;
    private final int GROWTH_THRESHOLD = DEFAULT_CAPACITY / 4;
    private Object[] array = new Object[DEFAULT_CAPACITY];
    private int cursor = 0;

    public void add(T item) {
        if (cursor == array.length - GROWTH_THRESHOLD) {
            grow();
        }
        array[cursor++] = item;
    }

    public void addAll(Collection<? extends T> collection) {
        for (T item : collection) {
            add(item);
        }
    }

    public void clear() {
        array = new Object[DEFAULT_CAPACITY];
        cursor = 0;
    }

    public T get(int index) {
        if (index < 0 || index >= cursor) {
            throw new IndexOutOfBoundsException();
        }
        return (T) array[index];
    }

    public boolean isEmpty() {
        return cursor == 0;
    }

    public void remove(int index) {
        if (index < 0 || index >= cursor) {
            throw new IndexOutOfBoundsException();
        }
        System.arraycopy(array, index + 1, array, index, cursor - index - 1);
    }

    public void remove(T item) {
        if (cursor == 0) {
            throw new NoSuchElementException("List is empty");
        }
        for (int i = 0; i < cursor; i++) {
            if (array[i].equals(item)) {
                remove(i);
                break;
            }
        }
        throw new NoSuchElementException("Item not found");
    }

    public void sort(Comparator<? super T> comparator) {
        if (cursor == 0) {
            throw new NoSuchElementException("List is empty");
        }
        T[] arrayToSort = (T[]) Arrays.copyOf(this.array, cursor);
        T[] sortedArray = MergeSort.sort(arrayToSort, comparator);
        System.arraycopy(sortedArray, 0, this.array, 0, cursor);
    }

    public int size() {
        return cursor;
    }

    public int capacity() {
        return array.length;
    }

    public String toString() {
        if (cursor == 0) {
            return "[]";
        }
        String result = "[";
        for (int i = 0; i < cursor-1; i++) {
            result += array[i] + ", ";
        }
        return result + array[cursor-1] + "]";
    }

    private void grow() {
        Object[] newArray = new Object[array.length * 2];
        System.arraycopy(array, 0, newArray, 0, array.length);
        array = newArray;
    }

}
