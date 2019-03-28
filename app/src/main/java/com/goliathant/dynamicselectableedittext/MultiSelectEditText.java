package com.goliathant.dynamicselectableedittext;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Josue on 28-03-2019.
 */

public class MultiSelectEditText<T extends Selectable> extends DynamicSelectableEditText<T> implements DynamicAlertDialog{

    private String dialogTitle;

    protected MultiSelectEditText(Context context) {
        super(context);
    }

    protected MultiSelectEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected MultiSelectEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void buildAlertDialog(View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getContext());
        alertDialog.setNegativeButton("Cancelar", (dialogInterface, i) -> dialogInterface.dismiss());
        alertDialog.setTitle(dialogTitle);

        RecyclerView rvDialogBusca = RecyclerViewUtils.setUpSeachableDialogItems(parent, alertDialog,
                new ChipViewBuscaRvAdapter(parent, rvList), null);

        ChipViewBuscaRvAdapter buscaRvAdapter = (ChipViewBuscaRvAdapter) rvDialogBusca.getAdapter();
        buscaRvAdapter.setOnRefreshList(onRefreshContext);
        if(buscaRvAdapter.removeJaSelecionados(((ChipViewRvAdapter)chipRvAdapter).getItems())){
            rvDialogBusca.post(() -> buscaRvAdapter.notifyDataSetChanged());
        }

        setUpOnScrollChangeListener(rvDialogBusca);
        alertDialog.setPositiveButton("Adicionar", ((dialogInterface, i) ->
                ((ChipViewRvAdapter) chipRvAdapter).addAll(((ChipViewBuscaRvAdapter) rvDialogBusca.getAdapter()).getSelectedItems())));

        alertDialog.create().show();
    }


    @Override
    protected void setAlertItems(AlertDialog.Builder alertBuilder) {

    }
}
