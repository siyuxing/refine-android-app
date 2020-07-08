package com.refine.adapters;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.refine.R;
import com.refine.model.ActivityConstants;

public class ActivityListAdapter extends RecyclerView.Adapter<ActivityListAdapter.ViewHolder> {

    private Context context;
    private List<String> activityList;
    private LayoutInflater mInflater;

    public ActivityListAdapter(Context context, List<String> activityList) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.activityList = activityList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_item_view, parent, false);
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView item;

        public ViewHolder(View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.itemtxt);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            String activity = item.getText().toString();
            if (ActivityConstants.ACTIVITY_CLASS_MAP.containsKey(activity)) {
                Intent i = new Intent(context, ActivityConstants.ACTIVITY_CLASS_MAP.get(activity));
                context.startActivity(i);
            }
        }
    }
}