package com.refine.model;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.refine.activities.admin.AddProductActivity;
import com.refine.activities.admin.AddUserActivity;
import com.refine.activities.admin.AdminOperations;
import com.refine.activities.admin.AdminUserHome;
import com.refine.activities.admin.DeleteProductActivity;
import com.refine.activities.admin.DeleteUserActivity;
import com.refine.activities.search.AdminJobHistorySearchActivity;
import com.refine.activities.search.OwnerSummarySearchActivity;
import com.refine.activities.search.ProductStockSearchActivity;
import com.refine.activities.search.SearchOperations;
import com.refine.activities.product.AddCutHistoryActivity;
import com.refine.activities.product.AddDryHistoryActivity;
import com.refine.activities.product.AddHydroHistoryActivity;
import com.refine.activities.product.AddPackHistoryActivity;
import com.refine.activities.product.AddPickupHistoryActivity;
import com.refine.activities.product.AddPourHistoryActivity;
import com.refine.activities.product.ProductOwnerJobHistorySearchActivity;
import com.refine.activities.product.ProductOwnerOperations;

public final class ActivityConstants {
    public static final Map<String, Class<?>> ACTIVITY_CLASS_MAP
            = new ImmutableMap.Builder<String, Class<?>>()
                      .put(AdminUserHome.ADMIN_OPTIONS, AdminOperations.class)
                      .put("添加用户", AddUserActivity.class)
                      .put("删除用户", DeleteUserActivity.class)
                      .put("添加产品", AddProductActivity.class)
                      .put("删除产品", DeleteProductActivity.class)

                      .put(AdminUserHome.PRODUCT_OWNER_OPTIONS, ProductOwnerOperations.class)
                      .put(ProductOwnerOperations.JOB_HISTORY, ProductOwnerJobHistorySearchActivity.class)
                      .put(ProductOwnerOperations.PRODUCT_STOCK_SEARCH, ProductStockSearchActivity.class)
                      .put(Operation.浇注.name(), AddPourHistoryActivity.class)
                      .put(Operation.醛化.name(), AddHydroHistoryActivity.class)
                      .put(Operation.干燥.name(), AddDryHistoryActivity.class)
                      .put(Operation.干加工切割.name(), AddCutHistoryActivity.class)
                      .put(Operation.包装入库.name(), AddPackHistoryActivity.class)
                      .put(Operation.出库.name(), AddPickupHistoryActivity.class)

                      .put(AdminUserHome.SEARCH_OPTIONS, SearchOperations.class)
                      .put(SearchOperations.FULL_JOB_HISTORY_SEARCH, AdminJobHistorySearchActivity.class)
                      .put(SearchOperations.OWNER_SUMMARY_SEARCH, OwnerSummarySearchActivity.class)
                      .build();

    public static final String START_DATE_EXTRA = "START_DATE";
    public static final String END_DATE_EXTRA = "END_DATE";
    public static final String OWNER_NAME_EXTRA = "OWNER_NAME";
    public static final String PRODUCT_NAME_EXTRA = "PRODUCT_NAME";
    public static final String OPERATION_EXTRA = "OPERATION";

    public static final String JOB_HISTORY_ID_EXTRA = "JOB_HISTORY_ID";

    public static final String SPINNER_PLACE_HOLDER_OPTION = "请选择";
}
