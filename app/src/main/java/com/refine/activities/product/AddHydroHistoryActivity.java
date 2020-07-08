package com.refine.activities.product;

import com.refine.model.Operation;
import com.refine.model.ProductStatus;

public class AddHydroHistoryActivity extends AddCommonHistoryActivity {
    public AddHydroHistoryActivity() {
        super(Operation.醛化, ProductStatus.待醛化, ProductStatus.待干燥);
    }
}