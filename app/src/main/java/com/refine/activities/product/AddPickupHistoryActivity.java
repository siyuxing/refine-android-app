package com.refine.activities.product;

import com.refine.model.Operation;
import com.refine.model.ProductStatus;

public class AddPickupHistoryActivity extends AddCommonHistoryActivity {
    public AddPickupHistoryActivity() {
        super(Operation.出库, ProductStatus.成品, null);
    }
}