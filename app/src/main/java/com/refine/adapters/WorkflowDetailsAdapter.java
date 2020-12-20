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
        holder.setWorkflowDetails(workflowDetails.get(position));
    }

    @Override
    public int getItemCount() {
        return workflowDetails.size();
    }

    public WorkflowDetails getSelected() {
        return checkedPosition >= 0 ? workflowDetails.get(checkedPosition) : null;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ViewGroup parent;
        private final View itemView;
        private final View title;
        private final TextView workId;
        private final TextView product;
        private final TextView operation;
        private final TextView startDate;
        private final TextView totalCount;

        private boolean enabled = true;

        public ViewHolder(ViewGroup parent, View itemView) {
            super(itemView);
            this.parent = parent;
            this.itemView = itemView;
            this.title = itemView.findViewById(R.id.title);
            this.workId = itemView.findViewById(R.id.work_id);
            this.product = itemView.findViewById(R.id.product);
            this.operation = itemView.findViewById(R.id.operation);
            this.startDate = itemView.findViewById(R.id.start_date);
            this.totalCount = itemView.findViewById(R.id.total_count);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (enabled) {
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

        private void setWorkflowDetails(WorkflowDetails workflowDetails) {
            SimpleDateFormat sdf = new SimpleDateFormat(CommonActivity.DATE_FORMAT, Locale.CHINA);

            workId.setText(workflowDetails.getWorkflowId());
            product.setText(workflowDetails.getProductName());
            operation.setText(workflowDetails.getOperation().name());
            startDate.setText(sdf.format(workflowDetails.getStartDate()));
            totalCount.setText(String.valueOf(workflowDetails.getNumOfTotal()));

            enabled = workflowDetails.isEnabled();

            itemView.setEnabled(enabled);

            if (enabled) {
                title.setBackground(parent.getResources().getDrawable(R.color.colorSuccess));
            } else {
                title.setBackground(parent.getResources().getDrawable(R.color.tab_unselect_txt));
            }
        }
    }
}
