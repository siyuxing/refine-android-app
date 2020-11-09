package com.refine.activities.product.check;

import com.refine.activities.product.record.RecordDipInTaskHistoryActivity;
import com.refine.model.Operation;

public class CheckPendingDipInTasksActivity extends CheckPendingTasksCommon {

    public CheckPendingDipInTasksActivity() {
        super(Operation.配料浸泡, RecordDipInTaskHistoryActivity.class);
    }
}