package com.kmsoft.financialcalculator.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.kmsoft.financialcalculator.Model.DrawerItem;
import com.kmsoft.financialcalculator.R;

import java.util.List;

public class DrawerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    boolean nightMode = false;
    private final int VIEW_TYPE_HEADER = 0;
    private final Context mContext;
    private final List<DrawerItem> mDrawerItems;
    private final DrawerItemClickedListener mlistener;
    private View mHeaderView;
    private int selectedItemPosition = 1;

    public interface DrawerItemClickedListener {
        void onItemClicked(DrawerItem drawerItem,int position);
    }

    public DrawerAdapter(Context context, List<DrawerItem> drawerItems, DrawerItemClickedListener listener) {
        mContext = context;
        mDrawerItems = drawerItems;
        mlistener = listener;
    }

    public void setSelectedItemPosition(int position) {
        int previousSelectedItemPosition = selectedItemPosition;
        selectedItemPosition = position;
        notifyItemChanged(previousSelectedItemPosition);
        notifyItemChanged(selectedItemPosition);
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
            ((ItemViewHolder) holder).bind(drawerItem, position);
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

        public void bind(DrawerItem drawerItem, int position) {
            item_text.setText(drawerItem.getName());
            item_icon.setImageResource(drawerItem.getIcon());

            if (position == selectedItemPosition) {
                int color = ContextCompat.getColor(mContext,R.color.white);
                Drawable drawable = ContextCompat.getDrawable(mContext,drawerItem.getIcon());
                if (drawable != null) {
                    drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
                }
                item_icon.setImageDrawable(drawable);
                linearLayout.setBackgroundColor(Color.rgb(13,91,104));
                item_text.setTextColor(Color.WHITE);
            } else {
                int color = ContextCompat.getColor(mContext,R.color.dark_blue);
                Drawable drawable = ContextCompat.getDrawable(mContext,drawerItem.getIcon());
                if (drawable != null) {
                    drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
                }
                item_icon.setImageDrawable(drawable);
                linearLayout.setBackgroundColor(ContextCompat.getColor(mContext,R.color.navigation_color));
                item_text.setTextColor(ContextCompat.getColor(mContext,R.color.dark_blue));
            }

            if (drawerItem.getName().equals("Dark mode")) {
                switchBtn.setVisibility(View.VISIBLE);
            } else {
                switchBtn.setVisibility(View.GONE);
            }

            sharedPreferences = mContext.getSharedPreferences("MODE", Context.MODE_PRIVATE);
            nightMode = sharedPreferences.getBoolean("nightMode", false);

            if (nightMode) {
                switchBtn.setChecked(true);
            }

            switchBtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("nightMode", true);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("nightMode", false);
                }
                editor.commit();
            });
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition() - 1;
            DrawerItem drawerItem = mDrawerItems.get(position);
            mlistener.onItemClicked(drawerItem,position);
        }
    }
}
