package com.goliathant.dynamicselectableedittext;

/**
 * Created by Josue on 28-03-2019.
 */

public interface Selectable {
    String getLabel();
    boolean isSelected();
    void setSelected(boolean selected);
}
