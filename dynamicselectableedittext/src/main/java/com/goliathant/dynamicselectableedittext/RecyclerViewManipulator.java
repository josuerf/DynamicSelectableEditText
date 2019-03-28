package com.goliathant.dynamicselectableedittext;

import java.util.List;

/**
 * Created by Josue on 28-03-2019.
 */

public interface RecyclerViewManipulator<T extends Selectable> {
    void addAll(List<T> list);
    List<T> getItems();
}
