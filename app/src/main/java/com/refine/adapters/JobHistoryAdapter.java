package com.refine.adapters;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.refine.R;
import com.refine.activities.CommonActivity;
import com.refine.model.JobHistory;

public class JobHistoryAdapter extends RecyclerView.Adapter<JobHistoryAdapter.ViewHolder> {
    private Context context;
    private ViewGroup viewGroup;
    private List<JobHistory> jobHistories;
    private LayoutInflater mInflater;
    private int checkedPosition = -1;

    public JobHistoryAdapter(Context context, List<JobHistory> jobHistories) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.jobHistories = jobHistories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.job_history_item_list, parent, false);
        return new ViewHolder(parent, view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (position == 0) {
            holder.setHeader();
        } else {
            holder.setJobHistory(jobHistories.get(position - 1));
        }
    }

    @Override
    public int getItemCount() {
        return jobHistories.size() + 1;
    }

    public JobHistory getSelected() {
        if (checkedPosition > 0) {
            return jobHistories.get(checkedPosition - 1);
        } else {
            return null;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ViewGroup parent;
        private TextView operator;
        private TextView product;
        private TextView operation;
        private TextView date;

        public ViewHolder(ViewGroup parent, View itemView) {
            super(itemView);
            this.parent = parent;
            this.operator = itemView.findViewById(R.id.operator);
            this.product = itemView.findViewById(R.id.product);
            this.operation = itemView.findViewById(R.id.operation);
            this.date = itemView.findViewById(R.id.date);
            itemView.setOnClickListener(this);
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

        private void setJobHistory(JobHistory jobHistory) {
            SimpleDateFormat sdf = new SimpleDateFormat(CommonActivity.DATE_FORMAT, Locale.CHINA);

            operator.setText(jobHistory.getOwner());
            product.setText(jobHistory.getProductName());
            operation.setText(jobHistory.getOperation().name());
            date.setText(sdf.format(jobHistory.getDate()));
        }

        private void setHeader() {
            operator.setText("负责人");
            operator.setTextColor(context.getResources().getColor(R.color.colorPrimary));

            product.setText("产品");
            product.setTextColor(context.getResources().getColor(R.color.colorPrimary));

            operation.setText("操作");
            operation.setTextColor(context.getResources().getColor(R.color.colorPrimary));

            date.setText("日期");
            date.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }
    }
}
