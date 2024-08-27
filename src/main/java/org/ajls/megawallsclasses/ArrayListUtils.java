package org.ajls.megawallsclasses;

import java.util.ArrayList;

public class ArrayListUtils {

    public static ArrayList removeArrayListIndexes(ArrayList arrayList, ArrayList<Integer> indexes) {
        int removed = 0; //elements removed count
        for (int i = 0; i < indexes.size(); i++) {
            arrayList.remove(indexes.get(i) - removed);
            removed++;
        }
        return arrayList;
    }
}
