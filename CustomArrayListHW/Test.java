package CustomArrayListHW;

import java.util.Comparator;
import java.util.Random;

public class Test {

    public static void main(String[] args) {

        CustomArrayList<CustomArrayList<Integer>> list = new CustomArrayList<>();
        System.out.println(list.isEmpty());
        int amount = 10;
        for (int i = 0; i < amount; i++) {
            Random random = new Random();
            int size = random.nextInt(24);
            CustomArrayList<Integer> subList = new CustomArrayList<>();
            for (int j = 0; j < size; j++) {
                subList.add(random.nextInt(100));
            }
            list.add(subList);
            System.out.println(subList.capacity());
        }
        System.out.println(list);
        System.out.println(list.isEmpty());
        Comparator<CustomArrayList<Integer>> comparator = (a, b) -> {
            if (a.capacity() > b.capacity()) {
                return 1;
            } else if (a.capacity() < b.capacity()) {
                return -1;
            } else {
                return 0;
            }
        };
        list.sort(comparator);
        System.out.println(list);
        for (int i = 0; i < list.size(); i++) {
            CustomArrayList<Integer> subList = list.get(i);
            System.out.println(subList.capacity());
        }
    }

}
