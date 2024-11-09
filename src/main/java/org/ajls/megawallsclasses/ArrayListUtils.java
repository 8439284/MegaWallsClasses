package org.ajls.megawallsclasses;

import java.util.ArrayList;

public class ArrayListUtils {

    public static ArrayList removeArrayListSequentialIndexes(ArrayList arrayList, ArrayList<Integer> indexes) {
        int removed = 0; //elements removed count
        for (int i = 0; i < indexes.size(); i++) {
            arrayList.remove(indexes.get(i) - removed);
            removed++;
        }
        return arrayList;
    }

    public static ArrayList removeArrayListIndexes(ArrayList arrayList, ArrayList<Integer> indexes) {
        return arrayList;
    }
    public static ArrayList removeArrayListIndexesClone(ArrayList arrayList, ArrayList<Integer> indexes) {
//        arrayList.getClass();
        ArrayList newArrayList = new ArrayList();
        for (int i = 0; i < arrayList.size(); i++) {
            if (!indexes.contains(i)) {
                newArrayList.add(arrayList.get(i));
            }
        }
        return newArrayList;
    }
}
