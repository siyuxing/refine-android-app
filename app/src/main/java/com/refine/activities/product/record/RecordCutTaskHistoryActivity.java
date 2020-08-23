package com.refine.activities.product.record;

import com.refine.model.Operation;

public class RecordCutTaskHistoryActivity extends RecordWorkflowDetailsCommon {
    public RecordCutTaskHistoryActivity() {
        super(Operation.干加工切割, Operation.包装入库);
    }
}