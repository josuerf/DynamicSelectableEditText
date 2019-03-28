package com.goliathant.dynamicselectableedittext;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewUtils {

    public static RecyclerView setUpSeachableDialogItems(Context context, AlertDialog.Builder dialog,
                                                         RecyclerView.Adapter adapter) {

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_dynamic_search, null);
        RecyclerView recyclerView = view.findViewById(R.id.rv_search_info);
        AppCompatEditText editBusca = view.findViewById(R.id.edit_txt_search_info);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
        SearchableAdapter searchableAdapter = (SearchableAdapter) recyclerView.getAdapter();

        setFilterableEditText(editBusca, searchableAdapter);
        dialog.setView(view);

        return recyclerView;
    }

    public static void setFilterableEditText(AppCompatEditText editBusca, SearchableAdapter adapter) {
        editBusca.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() >= 2) {
                    List<Selectable> filteredList = new ArrayList<>();
                    for (Object item : adapter.getFilterableList()) {
                        if (((Selectable) item).getLabel().toLowerCase().contains(editable.toString().toLowerCase()))
                            filteredList.add((Selectable) item);
                    }
                    adapter.setItems(filteredList);
                } else if(editable.length() == 0){
                    adapter.refreshList();
                }
            }
        });
    }
}
