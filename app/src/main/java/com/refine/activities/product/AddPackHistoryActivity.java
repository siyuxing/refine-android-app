package com.refine.activities.product;

import com.refine.model.Operation;
import com.refine.model.ProductStatus;

public class AddPackHistoryActivity extends AddCommonHistoryActivity {
    public AddPackHistoryActivity() {
        super(Operation.包装入库, ProductStatus.待包装入库, ProductStatus.成品);
    }
}
