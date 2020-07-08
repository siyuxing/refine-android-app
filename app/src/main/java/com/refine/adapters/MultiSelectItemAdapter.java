package com.refine.adapters;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.refine.R;

public class MultiSelectItemAdapter extends RecyclerView.Adapter<MultiSelectItemAdapter.ViewHolder> {

    private Context context;
    private List<String> activityList;
    private LayoutInflater mInflater;
    private Set<Integer> checkedPositions = new HashSet<>();

    public MultiSelectItemAdapter(Context context, List<String> activityList) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.activityList = activityList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.small_item_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.item.setText(activityList.get(position));
    }

    @Override
    public int getItemCount() {
        return activityList.size();
    }

    public Set<Integer> getCheckedPositions() {
        return checkedPositions;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView item;

        public ViewHolder(View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.itemtxt);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            if (checkedPositions.contains(pos)) {
                checkedPositions.remove(pos);
                v.setSelected(false);
            } else {
                checkedPositions.add(pos);
                v.setSelected(true);
            }
        }
    }
}