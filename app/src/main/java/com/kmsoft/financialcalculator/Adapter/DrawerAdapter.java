package com.kmsoft.financialcalculator.Adapter;

import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kmsoft.financialcalculator.MainActivity;
import com.kmsoft.financialcalculator.Model.DrawerItem;
import com.kmsoft.financialcalculator.R;

import java.util.List;

public class DrawerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_HEADER = 0;
    private final MainActivity mContext;
    private final List<DrawerItem> mDrawerItems;
    private final DrawerItemClickedListener mlistener;
    private View mHeaderView;

    public interface DrawerItemClickedListener {
        void onItemClicked(DrawerItem drawerItem);
    }

    public DrawerAdapter(MainActivity context, List<DrawerItem> drawerItems, DrawerItemClickedListener listener) {
        mContext = context;
        mDrawerItems = drawerItems;
        mlistener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_HEADER) {
            return new HeaderViewHolder(mHeaderView);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.drawer_item, parent, false);
            return new ItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
        } else {
            DrawerItem drawerItem = mDrawerItems.get(position - 1);
            ((ItemViewHolder) holder).bind(drawerItem);
        }
    }

    @Override
    public int getItemCount() {
        return mDrawerItems.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_HEADER;
        } else {
            return 1;
        }
    }

    public void addHeaderView(View headerView) {
        mHeaderView = headerView;
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView item_text;
        ImageView item_icon;
        Switch switchBtn;
        RelativeLayout linearLayout;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            item_icon = itemView.findViewById(R.id.item_icon);
            item_text = itemView.findViewById(R.id.item_text);
            linearLayout = itemView.findViewById(R.id.linearLayout);
            switchBtn = itemView.findViewById(R.id.switch_btn);
        }

        public void bind(DrawerItem drawerItem) {
            item_text.setText(drawerItem.getName());
            item_icon.setImageResource(drawerItem.getIcon());

            if (drawerItem.getName().equals("Dark mode")) {
                switchBtn.setVisibility(View.VISIBLE);
            } else {
                switchBtn.setVisibility(View.GONE);
            }

            switchBtn.setChecked(mContext.isDarkModeEnabled());

            switchBtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    mContext.setDarkMode(Configuration.UI_MODE_NIGHT_YES);
                } else {
                    mContext.setDarkMode(Configuration.UI_MODE_NIGHT_NO);
                }
            });
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition() - 1;
            DrawerItem drawerItem = mDrawerItems.get(position);
            mlistener.onItemClicked(drawerItem);
        }
    }
}
