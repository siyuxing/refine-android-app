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
import com.refine.model.OwnerSummary;

public class OwnerSummaryAdapter extends RecyclerView.Adapter<OwnerSummaryAdapter.ViewHolder> {
    private Context context;
    private List<OwnerSummary> ownerSummaries;
    private LayoutInflater mInflater;
    private int checkedPosition = -1;

    public OwnerSummaryAdapter(Context context, List<OwnerSummary> ownerSummaries) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.ownerSummaries = ownerSummaries;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.owner_summary_list, parent, false);
        return new ViewHolder(parent, view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (position == 0) {
            holder.setHeader();
        } else {
            holder.setJobHistory(ownerSummaries.get(position - 1));
        }
    }

    @Override
    public int getItemCount() {
        return ownerSummaries.size() + 1;
    }

    public OwnerSummary getSelected() {
        if (checkedPosition > 0) {
            return ownerSummaries.get(checkedPosition - 1);
        } else {
            return null;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ViewGroup parent;
        private TextView product;
        private TextView operation;
        private TextView success;
        private TextView failure;

        public ViewHolder(ViewGroup parent, View itemView) {
            super(itemView);
            this.product = itemView.findViewById(R.id.product);
            this.operation = itemView.findViewById(R.id.operation);
            this.success = itemView.findViewById(R.id.success);
            this.failure = itemView.findViewById(R.id.failure);
            this.parent = parent;
            itemView.setOnClickListener(this);
        }

        private void setJobHistory(OwnerSummary ownerSummary) {
            product.setText(ownerSummary.getProductName());
            operation.setText(ownerSummary.getOperation().name());
            success.setText(String.valueOf(ownerSummary.getNumOfSuccess()));
            failure.setText(String.valueOf(ownerSummary.getNumOfFailure()));
        }

        private void setHeader() {
            product.setText("产品");
            product.setTextColor(context.getResources().getColor(R.color.colorPrimary));

            operation.setText("操作");
            operation.setTextColor(context.getResources().getColor(R.color.colorPrimary));

            success.setText("合格数量");
            success.setTextColor(context.getResources().getColor(R.color.colorPrimary));

            failure.setText("报废数量");
            failure.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }

        @Override
        public void onClick(View v) {
            if (getAdapterPosition() == checkedPosition) {
                v.setSelected(false);
                checkedPosition = -1;
            } else {
                checkedPosition = getAdapterPosition();
                if (checkedPosition > 0) {
                    v.setSelected(true);
                }
                for (int i = 0; i < parent.getChildCount(); i++) {
                    if (i != checkedPosition) {
                        parent.getChildAt(i).setSelected(false);
                    }
                }
            }
        }
    }
}
