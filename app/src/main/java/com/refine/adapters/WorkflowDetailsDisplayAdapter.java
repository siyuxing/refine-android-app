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
import com.refine.model.ActivityConstants;
import com.refine.model.WorkflowDetails;

public class WorkflowDetailsDisplayAdapter extends RecyclerView.Adapter<WorkflowDetailsDisplayAdapter.ViewHolder> {
    private Context context;
    private ViewGroup viewGroup;
    private List<WorkflowDetails> workflowDetails;
    private LayoutInflater mInflater;
    private int checkedPosition = -1;

    public WorkflowDetailsDisplayAdapter(Context context, List<WorkflowDetails> workflowDetails) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.workflowDetails = workflowDetails;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.workflow_detail_display_item, parent, false);
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
        return workflowDetails.get(checkedPosition);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ViewGroup parent;
        private TextView operation;
        private TextView owner;
        private TextView finished;
        private TextView success;
        private TextView fail;

        public ViewHolder(ViewGroup parent, View itemView) {
            super(itemView);
            this.parent = parent;
            this.operation = itemView.findViewById(R.id.operation);
            this.owner = itemView.findViewById(R.id.owner);
            this.finished = itemView.findViewById(R.id.finished);
            this.success = itemView.findViewById(R.id.success_count);
            this.fail = itemView.findViewById(R.id.fail_count);
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

        private void setWorkflowDetails(WorkflowDetails workflowDetails) {
            operation.setText(workflowDetails.getOperation() != null ? workflowDetails.getOperation().name()
                                                                     : ActivityConstants.UNKNOWN_FIELD_VALUE);
            owner.setText(workflowDetails.getOwner() != null ? workflowDetails.getOwner() : ActivityConstants.UNKNOWN_FIELD_VALUE);
            finished.setText(Boolean.TRUE.equals(workflowDetails.isFinish()) ? "已完成" : "未完成");
            success.setText(workflowDetails.getNumOfSuccess() != null ? String.valueOf(workflowDetails.getNumOfSuccess())
                                                                      : ActivityConstants.UNKNOWN_FIELD_VALUE);
            fail.setText(workflowDetails.getNumOfFailure() != null ? String.valueOf(workflowDetails.getNumOfFailure())
                                                                   : ActivityConstants.UNKNOWN_FIELD_VALUE);
        }
    }
}
