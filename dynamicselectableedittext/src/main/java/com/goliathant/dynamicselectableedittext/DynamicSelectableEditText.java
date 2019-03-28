package com.goliathant.dynamicselectableedittext;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import java.util.List;

/**
 * Created by Josue on 28-03-2019.
 */

public abstract class DynamicSelectableEditText<T extends Selectable> extends AppCompatEditText implements OnRefreshList {

    protected List<T> mItems;
    protected T mSelectedItem;
    protected int mSelectedIndex;
    protected int mSearchInit = 0;
    protected int mSearchOffset = 10;
    protected String[] mListableItems;
    protected final CharSequence mHint;
    protected boolean mHasSearch = false;
    protected OnItemSelectedListener<T> onItemSelectedListener;

    protected DynamicSelectableEditText(Context context) {
        super(context);
        mHint = getHint();
    }

    protected DynamicSelectableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHint = getHint();
    }

    protected DynamicSelectableEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mHint = getHint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setFocusable(false);
        setClickable(true);
    }

    public void setHasSearch(boolean value) {
        this.mHasSearch = value;
    }

    public void setItems(List<T> items) {
        this.mItems = items;
        this.mListableItems = new String[items.size()];

        int i = 0;

        for (T item : mItems) {
            mListableItems[i++] = item.getLabel();
        }
        configureOnClickListener();
    }

    protected void configureOnClickListener() {
        setOnClickListener(((DynamicAlertDialog) this)::buildAlertDialog);
    }

    protected void setTextOfSelectedIndex(int selectedIndex) {
        setText(mItems.get(selectedIndex).getLabel());
        mSelectedItem = mItems.get(selectedIndex);
        mSelectedIndex = selectedIndex;
        if (onItemSelectedListener != null) {
            onItemSelectedListener.onItemSelectedListener(mItems.get(selectedIndex), selectedIndex);
        }
    }

    protected void setTextOfSelectedItem(String selectedItem) {
        setText(selectedItem);
        mSelectedItem = getItemByValue(selectedItem);
        mSelectedIndex = getIndexByValue(selectedItem);
        if (onItemSelectedListener != null) {
            onItemSelectedListener.onItemSelectedListener(mSelectedItem, mSelectedIndex);
        }
    }

    public void setSelectedItem(int idx) {
        setText(mListableItems[idx]);
        mSelectedItem = mItems.get(idx);
        mSelectedIndex = idx;
    }

    public T getItemByValue(String item) {
        for (int i = 0; i < mItems.size(); i++) {
            if (mItems.get(i).getLabel().equals(item))
                return mItems.get(i);
        }
        return null;
    }

    public int getIndexByValue(String item) {
        for (int i = 0; i < mItems.size(); i++) {
            if (mItems.get(i).getLabel().equals(item))
                return i;
        }
        return 0;
    }

    public void setSelectedItem(String stringValue) {
        int i = 0;
        for (Selectable l : mItems) {
            if (l.getLabel().equals(stringValue)) {
                setText(mListableItems[i]);
                mSelectedItem = mItems.get(i);
                mSelectedIndex = i;
                i = 0;
            }
            i++;
        }
    }

    public int getSelectedIndex() {
        return mSelectedIndex;
    }

    public T getSelectedItem() {
        return mSelectedItem;
    }

    public void setOnItemSelectedListener(OnItemSelectedListener<T> onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;
    }

    protected interface OnItemSelectedListener<T> {
        void onItemSelectedListener(T item, int selectedIndex);
    }

    @Override
    public void onRefreshList() {
        mSearchInit = 0;
    }

    protected void setUpOnScrollChangeListener(RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager llm = (LinearLayoutManager) recyclerView.getLayoutManager();
                RVSearchAdapter adapter = (RVSearchAdapter) recyclerView.getAdapter();
                int lastVisibleItemPosition = llm.findLastCompletelyVisibleItemPosition() + 1;
                if (adapter.getItems().size() == lastVisibleItemPosition &&
                        lastVisibleItemPosition < adapter.getFilterableList().size()) {
                    int positionMaxSize = mSearchInit + mSearchOffset;
                    for (int i = mSearchInit; i < positionMaxSize && i < adapter.getFilterableList().size(); i++) {
                        adapter.addItem(adapter.getFilterableList().get(i), adapter.getItemCount());
                        recyclerView.post(() -> adapter.notifyItemInserted(adapter.getItemCount() - 1));
                    }
                    mSearchInit = mSearchInit + mSearchOffset;
                }
            }
        });
    }
}
