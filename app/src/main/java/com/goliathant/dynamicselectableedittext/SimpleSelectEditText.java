package com.goliathant.dynamicselectableedittext;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

/**
 * Created by Josue on 28-03-2019.
 */

public class SimpleSelectEditText<T extends Selectable> extends DynamicSelectableEditText<T> implements DynamicAlertDialog {

    protected SimpleSelectEditText(Context context) {
        super(context);
    }

    protected SimpleSelectEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected SimpleSelectEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void buildAlertDialog(View view) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(view.getContext());
        alertBuilder.setTitle(mHint);
        alertBuilder.setPositiveButton(R.string.alert_cancel, null);
        if (mHasSearch) {
            Context context = alertBuilder.getContext();
            RecyclerView recyclerView = RecyclerViewUtils.setUpSeachableDialogItems(context,
                    alertBuilder, new RVSearchAdapter(context, (List<Selectable>) mItems,
                            SearchType.SIMPLE_SELECT, mSearchOffset), null);

            setUpOnScrollChangeListener(recyclerView);

            AlertDialog dialog = alertBuilder.create();
            dialog.show();
            RVSearchAdapter adapter = (RVSearchAdapter) recyclerView.getAdapter();
            adapter.setOnRefreshList(this);
            adapter.refreshList();
            adapter.setOnItemClickListener((v, selectedIndex) -> {
                setTextOfSelectedItem(adapter.getSelectedItem());
                dialog.dismiss();
            });

        } else {
            alertBuilder.setItems(mListableItems, (dialogInterface, selectedIndex) -> setTextOfSelectedIndex(selectedIndex));
            alertBuilder.create().show();
        }
    }
}