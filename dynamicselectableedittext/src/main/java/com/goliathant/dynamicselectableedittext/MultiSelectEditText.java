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

public class MultiSelectEditText<T extends Selectable> extends DynamicSelectableEditText<T> implements DynamicAlertDialog {

    private RecyclerViewManipulator placeHolderRecyclerView;

    protected MultiSelectEditText(Context context) {
        super(context);
    }

    protected MultiSelectEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected MultiSelectEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setPlaceHolderRecyclerView(RecyclerViewManipulator placeHolderRecyclerView) {
        this.placeHolderRecyclerView = placeHolderRecyclerView;
    }

    @Override
    public void buildAlertDialog(View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setNegativeButton(R.string.alert_cancel, (dialogInterface, i) -> dialogInterface.dismiss());
        alertDialog.setTitle(mHint);

        RecyclerView rvSearchDialog = RecyclerViewUtils.setUpSeachableDialogItems(getContext(), alertDialog,
                new RVSearchAdapter(getContext(), (List<Selectable>) mItems,
                        SearchType.MULTI_SELECT, mSearchOffset));

        RVSearchAdapter searchRVAdapter = (RVSearchAdapter) rvSearchDialog.getAdapter();
        searchRVAdapter.setOnRefreshList(this);
        if(placeHolderRecyclerView == null) {
            throw new RuntimeException(getResources().getString(R.string.error_placeholder_null));
        } else {
            if (searchRVAdapter.removeEquals(placeHolderRecyclerView.getItems())) {
                rvSearchDialog.post(() -> searchRVAdapter.notifyDataSetChanged());
            }

            setUpOnScrollChangeListener(rvSearchDialog);
            alertDialog.setPositiveButton(R.string.alert_add, ((dialogInterface, i) ->
                    placeHolderRecyclerView.addAll(((RVSearchAdapter) rvSearchDialog.getAdapter()).getSelectedItems())));
            alertDialog.create().show();
        }
    }
}
