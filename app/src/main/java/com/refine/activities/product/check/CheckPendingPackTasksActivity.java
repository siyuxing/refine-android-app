package com.refine.activities.product.check;

import com.refine.activities.product.record.RecordPackTaskHistoryActivity;
import com.refine.model.Operation;

public class CheckPendingPackTasksActivity extends CheckPendingTasksCommon {

    public CheckPendingPackTasksActivity() {
        super(Operation.包装入库, RecordPackTaskHistoryActivity.class);
    }
}