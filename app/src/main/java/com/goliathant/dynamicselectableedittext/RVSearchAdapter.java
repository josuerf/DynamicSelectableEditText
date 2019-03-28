package com.goliathant.dynamicselectableedittext;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RVSearchAdapter extends RecyclerView.Adapter<RVSearchAdapter.RVSearchViewHolder>
        implements SearchableAdapter<Selectable> {

    private final Context mContext;
    private final SearchType mSearchType;
    private final int mItemsHoldingLimit;
    private final List<Selectable> mItems;
    private final List<Selectable> mFirstItems;
    private final List<Selectable> mFilteredItems;

    private String mSelectedItem;
    private OnRefreshList mOnRefreshList;
    private List<Selectable> mSelectedItems;
    private FilterableEditTextItemClickListener mClickListener;

    public RVSearchAdapter(Context context, List<Selectable> items, SearchType searchType, int itemsHoldingLimit) {
        this.mContext = context;
        this.mSearchType = searchType;
        this.mItemsHoldingLimit = itemsHoldingLimit;

        this.mFirstItems = new ArrayList<>(items);
        this.mFilteredItems = new ArrayList<>(items);
        this.mItems = new ArrayList<>(items.size() > 10 ? items.subList(0, 10) : items);

        if(mSearchType == SearchType.MULTI_SELECT)
            mSelectedItems = new ArrayList<>();
    }

    @NonNull
    @Override
    public RVSearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (mSearchType) {
            case MULTI_SELECT:
                return new MultiSelectViewHolder(LayoutInflater.from(mContext)
                        .inflate(R.layout.item_multi_select, parent, false));
            case SIMPLE_SELECT:
                return new SimpleSelectViewHolder(LayoutInflater.from(mContext)
                        .inflate(R.layout.item_simple_select, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RVSearchViewHolder holder, int position) {
        holder.bind(mItems.get(position));
    }

    public void setOnItemClickListener(FilterableEditTextItemClickListener clickListener) {
        this.mClickListener = clickListener;
    }

    public void setSelectedItem(String item) {
        this.mSelectedItem = item;
    }

    public String getSelectedItem() {
        return this.mSelectedItem;
    }

    @Override
    public int getItemCount() {
        return this.mItems.size();
    }

    @Override
    public void setItems(List<Selectable> items) {
        this.mFilteredItems.clear();
        this.mFilteredItems.addAll(items);
        this.mItems.clear();
        this.mItems.addAll(items.size() > mItemsHoldingLimit ? items.subList(0, mItemsHoldingLimit) : items);
        this.notifyDataSetChanged();
    }

    public List<Selectable> getFilterableList() {
        return this.mFilteredItems;
    }

    public void addItem(Selectable item, int position) {
        this.mItems.add(position, item);
    }

    @Override
    public List<Selectable> getItems() {
        return this.mItems;
    }

    @Override
    public void refreshList() {
        if (mOnRefreshList != null)
            mOnRefreshList.onRefreshList();
        setItems(this.mFirstItems);
    }

    public void setOnRefreshList(OnRefreshList onRefreshList) {
        this.mOnRefreshList = onRefreshList;
    }

    public void addSelectedItem(Selectable item) {
        this.mSelectedItems.add(item);
    }

    public void removeSelectedItem(Selectable item) {
        this.mSelectedItems.remove(item);
    }

    abstract class RVSearchViewHolder extends RecyclerView.ViewHolder {

        public RVSearchViewHolder(View itemView) {
            super(itemView);
        }

        abstract void bind(Selectable object);
    }

    class SimpleSelectViewHolder extends RVSearchViewHolder {

        private TextView txtItem;

        public SimpleSelectViewHolder(View itemView) {
            super(itemView);
            txtItem = itemView.findViewById(R.id.txt_rv_search_item);
        }

        @Override
        void bind(Selectable object) {
            txtItem.setText(object.getLabel());
            txtItem.setOnClickListener(v -> {
                setSelectedItem(object.getLabel());
                mClickListener.onClick(v, getAdapterPosition());
            });
        }
    }

    class MultiSelectViewHolder extends RVSearchViewHolder {

        private CheckBox cbxItem;

        public MultiSelectViewHolder(View itemView) {
            super(itemView);
            cbxItem = itemView.findViewById(R.id.item_multi_select);
        }

        @Override
        void bind(Selectable object) {
            cbxItem.setText(object.getLabel());
            cbxItem.setOnCheckedChangeListener((compoundButton, isChecked) -> {
                if (isChecked) {
                    addSelectedItem(object);
                } else {
                    removeSelectedItem(object);
                }
            });
        }
    }
}
