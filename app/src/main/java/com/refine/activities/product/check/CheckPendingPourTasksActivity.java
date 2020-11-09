package com.refine.activities.product.check;

import com.refine.activities.product.record.RecordPourTaskHistoryActivity;
import com.refine.model.Operation;

public class CheckPendingPourTasksActivity extends CheckPendingTasksCommon {

    public CheckPendingPourTasksActivity() {
        super(Operation.煮滤, RecordPourTaskHistoryActivity.class);
    }
}
