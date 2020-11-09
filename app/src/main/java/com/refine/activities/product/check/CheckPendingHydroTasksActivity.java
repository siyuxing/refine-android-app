package com.refine.activities.product.check;

import com.refine.activities.product.record.RecordHydroTaskHistoryActivity;
import com.refine.model.Operation;

public class CheckPendingHydroTasksActivity extends CheckPendingTasksCommon {

    public CheckPendingHydroTasksActivity() {
        super(Operation.湿加工, RecordHydroTaskHistoryActivity.class);
    }
}
