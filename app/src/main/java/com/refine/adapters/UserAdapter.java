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
import com.refine.model.User;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private static final String USER_LIST_FORMAT = "%s - %s";

    private Context context;
    private List<User> users;
    private LayoutInflater mInflater;
    private ViewGroup group;
    private int checkedPosition = -1;

    public UserAdapter(Context context, List<User> users) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.users = users;
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
        User user = users.get(position);
        String userString = String.format(USER_LIST_FORMAT, user.getUsername(), user.getDisplayName());
        holder.item.setText(userString);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public User getSelected() {
        return checkedPosition >= 0 ? users.get(checkedPosition) : null;
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