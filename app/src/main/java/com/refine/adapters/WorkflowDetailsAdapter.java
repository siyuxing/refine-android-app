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
import com.refine.model.WorkflowDetails;

public class WorkflowDetailsAdapter extends RecyclerView.Adapter<WorkflowDetailsAdapter.ViewHolder> {
    private Context context;
    private ViewGroup viewGroup;
    private List<WorkflowDetails> workflowDetails;
    private LayoutInflater mInflater;
    private int checkedPosition = -1;

    public WorkflowDetailsAdapter(Context context, List<WorkflowDetails> workflowDetails) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.workflowDetails = workflowDetails;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.workflow_detail_item_list, parent, false);
        return new ViewHolder(parent, view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (!workflowDetails.isEmpty()) {
            if (position == 0) {
                holder.setHeader();
            } else {
                holder.setWorkflowDetails(workflowDetails.get(position - 1));
            }
        }
    }

    @Override
    public int getItemCount() {
        return workflowDetails.isEmpty() ? 0 : workflowDetails.size() + 1;
    }

    public WorkflowDetails getSelected() {
        if (checkedPosition > 0) {
            return workflowDetails.get(checkedPosition - 1);
        } else {
            return null;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ViewGroup parent;
        private TextView workId;
        private TextView product;
        private TextView operation;
        private TextView startDate;

        public ViewHolder(ViewGroup parent, View itemView) {
            super(itemView);
            this.parent = parent;
            this.workId = itemView.findViewById(R.id.work_id);
            this.product = itemView.findViewById(R.id.product);
            this.operation = itemView.findViewById(R.id.operation);
            this.startDate = itemView.findViewById(R.id.start_date);
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

        private void setWorkflowDetails(WorkflowDetails workflowDetails) {
            SimpleDateFormat sdf = new SimpleDateFormat(CommonActivity.DATE_FORMAT, Locale.CHINA);

            workId.setText(workflowDetails.getWorkflowId());
            product.setText(workflowDetails.getProductName());
            operation.setText(workflowDetails.getOperation().name());
            startDate.setText(sdf.format(workflowDetails.getStartDate()));
        }

        private void setHeader() {
            workId.setText("任务单号");
            workId.setTextColor(context.getResources().getColor(R.color.colorPrimary));

            product.setText("产品");
            product.setTextColor(context.getResources().getColor(R.color.colorPrimary));

            operation.setText("操作");
            operation.setTextColor(context.getResources().getColor(R.color.colorPrimary));

            startDate.setText("创建日期");
            startDate.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }
    }
}
