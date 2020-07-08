package com.refine.activities.product;

import com.refine.model.Operation;
import com.refine.model.ProductStatus;

public class AddDryHistoryActivity  extends AddCommonHistoryActivity {
    public AddDryHistoryActivity() {
        super(Operation.干燥, ProductStatus.待干燥, ProductStatus.待切割);
    }
}