package com.goliathant.dynamicselectableedittext;

import java.util.List;

/**
 * Created by Josue on 28-03-2019.
 */

public interface RecyclerViewManipulator {
    void addAll(List<Selectable> list);
    List<Selectable> getItems();
}
