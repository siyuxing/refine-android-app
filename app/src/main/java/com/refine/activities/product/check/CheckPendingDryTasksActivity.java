package com.refine.activities.product.check;

import com.refine.activities.product.record.RecordDryTaskHistoryActivity;
import com.refine.model.Operation;

public class CheckPendingDryTasksActivity extends CheckPendingTasksCommon {

    public CheckPendingDryTasksActivity() {
        super(Operation.干燥硬化, RecordDryTaskHistoryActivity.class);
    }
}
