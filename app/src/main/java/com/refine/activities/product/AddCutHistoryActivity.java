package com.refine.activities.product;

import com.refine.model.Operation;
import com.refine.model.ProductStatus;

public class AddCutHistoryActivity extends AddCommonHistoryActivity {
    public AddCutHistoryActivity() {
        super(Operation.干加工切割, ProductStatus.待切割, ProductStatus.待包装入库);
    }
}