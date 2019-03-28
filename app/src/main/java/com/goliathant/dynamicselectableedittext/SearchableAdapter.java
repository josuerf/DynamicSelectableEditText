package com.goliathant.dynamicselectableedittext;

import java.util.List;

/**
 * Created by Josue on 28-03-2019.
 */

public interface SearchableAdapter<T> {
    void setItems(List<T> items);
    List<T> getItems();
    List<T> getFilterableList();
    void refreshList();
}
