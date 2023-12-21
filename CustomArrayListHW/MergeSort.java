package CustomArrayListHW;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;

public class MergeSort {

    public static <T> T[] sort(T[] array, Comparator<? super T> comparator) {
        if (array.length > 1) {
            T[] left = Arrays.copyOfRange(array, 0, array.length / 2);
            T[] right = Arrays.copyOfRange(array, array.length / 2, array.length);
            left = sort(left, comparator);
            right = sort(right, comparator);
            array = merge(left, right, comparator);
        }
        return array;
    }

    private static <T> T[] merge(T[] left, T[] right, Comparator<? super T> comparator) {
        T[] result = (T[]) Array.newInstance(left[0].getClass(), left.length + right.length);
        int i = 0, j = 0, k = 0;
        while (i < left.length && j < right.length) {
            if (comparator.compare(left[i], right[j]) < 0) {
                result[k++] = left[i++];
            } else {
                result[k++] = right[j++];
            }
        }
        while (i < left.length) {
            result[k++] = left[i++];
        }
        while (j < right.length) {
            result[k++] = right[j++];
        }
        return result;
    }
}