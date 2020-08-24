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
        if (!workflowSheets.isEmpty()) {
            if (position == 0) {
                holder.setHeader();
            } else {
                holder.setWorkflowSheet(workflowSheets.get(position - 1));
            }
        }
    }

    @Override
    public int getItemCount() {
        return workflowSheets.isEmpty() ? 0 : workflowSheets.size() + 1;
    }

    public WorkflowSheet getSelected() {
        if (checkedPosition > 0) {
            return workflowSheets.get(checkedPosition - 1);
        } else {
            return null;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ViewGroup parent;
        private TextView workId;
        private TextView product;
        private TextView startDate;
        private TextView requestCount;
        private TextView finishCount;

        public ViewHolder(ViewGroup parent, View itemView) {
            super(itemView);
            this.parent = parent;
            this.workId = itemView.findViewById(R.id.work_id);
            this.product = itemView.findViewById(R.id.product);
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

        private void setWorkflowSheet(WorkflowSheet workflowSheet) {
            SimpleDateFormat sdf = new SimpleDateFormat(CommonActivity.DATE_FORMAT, Locale.CHINA);

            workId.setText(workflowSheet.getSheetId());
            product.setText(workflowSheet.getProductName());
            startDate.setText(sdf.format(workflowSheet.getStartDate()));

            requestCount.setText(String.valueOf(workflowSheet.getNumOfRequested()));
            finishCount.setText(workflowSheet.getNumOfFinal() != null ?
                                String.valueOf(workflowSheet.getNumOfFinal()) : "未完成");
        }

        private void setHeader() {
            workId.setText("任务单号");
            workId.setTextColor(context.getResources().getColor(R.color.colorPrimary));

            product.setText("产品");
            product.setTextColor(context.getResources().getColor(R.color.colorPrimary));

            startDate.setText("创建日期");
            startDate.setTextColor(context.getResources().getColor(R.color.colorPrimary));

            requestCount.setText("预期");
            requestCount.setTextColor(context.getResources().getColor(R.color.colorPrimary));

            finishCount.setText("完成");
            finishCount.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }
    }
}
