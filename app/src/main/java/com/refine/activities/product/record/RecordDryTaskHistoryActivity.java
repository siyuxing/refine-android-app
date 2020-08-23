package com.refine.activities.product.record;

import com.refine.model.Operation;

public class RecordDryTaskHistoryActivity extends RecordWorkflowDetailsCommon {
    public RecordDryTaskHistoryActivity() {
        super(Operation.干燥, Operation.干加工切割);
    }
}