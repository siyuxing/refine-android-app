package com.refine.adapters;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.refine.R;

public class SingleSelectItemAdapter extends RecyclerView.Adapter<SingleSelectItemAdapter.ViewHolder> {

    private Context context;
    private List<String> activityList;
    private LayoutInflater mInflater;
    private ViewGroup group;
    private int checkedPosition = -1;

    public SingleSelectItemAdapter(Context context, List<String> activityList) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.activityList = activityList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.group = parent;
        View view = mInflater.inflate(R.layout.small_item_view, parent, false);
        return new ViewHolder(parent, view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.item.setText(activityList.get(position));
    }

    @Override
    public int getItemCount() {
        return activityList.size();
    }

    public String getSelected() {
        if (checkedPosition >= 0) {
            return activityList.get(checkedPosition);
        } else {
            return null;
        }
    }

    public void clearSelected() {
        if (checkedPosition >= 0) {
            group.getChildAt(checkedPosition).setSelected(false);
            checkedPosition = -1;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ViewGroup parent;
        private TextView item;

        public ViewHolder(ViewGroup parent, View itemView) {
            super(itemView);
            this.parent = parent;
            item = itemView.findViewById(R.id.itemtxt);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (getAdapterPosition() == checkedPosition) {
                v.setSelected(false);
                checkedPosition = -1;
            } else {
                checkedPosition = getAdapterPosition();
                v.setSelected(true);
                for (int i = 0; i < parent.getChildCount(); i++) {
                    if (i != checkedPosition) {
                        parent.getChildAt(i).setSelected(false);
                    }
                }
            }
        }
    }
}
