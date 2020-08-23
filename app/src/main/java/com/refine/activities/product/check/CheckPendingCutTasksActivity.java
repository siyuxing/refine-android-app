package com.refine.activities.product.check;

import com.refine.activities.product.record.RecordCutTaskHistoryActivity;
import com.refine.model.Operation;

public class CheckPendingCutTasksActivity extends CheckPendingTasksCommon {

    public CheckPendingCutTasksActivity() {
        super(Operation.干加工切割, RecordCutTaskHistoryActivity.class);
    }
}
