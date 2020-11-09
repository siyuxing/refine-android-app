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
import com.refine.model.WorkflowSheet;

public class WorkflowSheetAdapter extends RecyclerView.Adapter<WorkflowSheetAdapter.ViewHolder> {
    private Context context;
    private ViewGroup viewGroup;
    private List<WorkflowSheet> workflowSheets;
    private LayoutInflater mInflater;
    private int checkedPosition = -1;

    public WorkflowSheetAdapter(Context context, List<WorkflowSheet> workflowSheets) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.workflowSheets = workflowSheets;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.workflow_sheet_item_list, parent, false);
        return new ViewHolder(parent, view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.setWorkflowSheet(workflowSheets.get(position ));
    }

    @Override
    public int getItemCount() {
        return workflowSheets.size();
    }

    public WorkflowSheet getSelected() {
        return checkedPosition >= 0 ? workflowSheets.get(checkedPosition) : null;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ViewGroup parent;
        private final TextView workId;
        private final TextView product;
        private final TextView requester;
        private final TextView startDate;
        private final TextView requestCount;
        private final TextView finishCount;

        public ViewHolder(ViewGroup parent, View itemView) {
            super(itemView);
            this.parent = parent;
            this.workId = itemView.findViewById(R.id.work_id);
            this.product = itemView.findViewById(R.id.product);
            this.requester = itemView.findViewById(R.id.requester);
            this.startDate = itemView.findViewById(R.id.start_date);
            this.requestCount = itemView.findViewById(R.id.request_num);
            this.finishCount = itemView.findViewById(R.id.finish_num);
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

        private void setWorkflowSheet(WorkflowSheet workflowSheet) {
            SimpleDateFormat sdf = new SimpleDateFormat(CommonActivity.DATE_FORMAT, Locale.CHINA);

            workId.setText("任务单 " + workflowSheet.getSheetId());
            product.setText(workflowSheet.getProductName());
            requester.setText(workflowSheet.getRequester());
            startDate.setText(sdf.format(workflowSheet.getStartDate()));

            requestCount.setText(String.valueOf(workflowSheet.getNumOfRequested()));
            finishCount.setText(workflowSheet.getNumOfFinal() != null ?
                                String.valueOf(workflowSheet.getNumOfFinal()) : "未完成");
        }
    }
}
