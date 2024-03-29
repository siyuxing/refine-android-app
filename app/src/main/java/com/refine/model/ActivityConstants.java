package com.refine.model;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.refine.activities.admin.AddProductActivity;
import com.refine.activities.admin.AddUserActivity;
import com.refine.activities.admin.AdminOperations;
import com.refine.activities.admin.AdminUserHome;
import com.refine.activities.admin.CheckProductActivity;
import com.refine.activities.admin.CheckUserActivity;
import com.refine.activities.product.AddPickupHistoryActivity;
import com.refine.activities.product.CreateWorkflowSheetActivity;
import com.refine.activities.product.ProductOwnerOperations;
import com.refine.activities.product.check.CheckAllPendingTasksActivity;
import com.refine.activities.product.record.RecordCutTaskHistoryActivity;
import com.refine.activities.product.record.RecordDipInTaskHistoryActivity;
import com.refine.activities.product.record.RecordDryTaskHistoryActivity;
import com.refine.activities.product.record.RecordHydroTaskHistoryActivity;
import com.refine.activities.product.record.RecordPackTaskHistoryActivity;
import com.refine.activities.product.record.RecordPourTaskHistoryActivity;
import com.refine.activities.search.AdminWorkflowSheetSearchActivity;
import com.refine.activities.search.OwnerSummarySearchActivity;
import com.refine.activities.search.ProductStockSearchActivity;
import com.refine.activities.search.SearchOperations;

public final class ActivityConstants {
    public static final Map<String, Class<?>> ACTIVITY_CLASS_MAP
            = new ImmutableMap.Builder<String, Class<?>>()
                      .put(AdminUserHome.ADMIN_OPTIONS, AdminOperations.class)
                      .put(AdminOperations.ADD_USER, AddUserActivity.class)
                      .put(AdminOperations.CHECK_USERS, CheckUserActivity.class)
                      .put(AdminOperations.ADD_PRODUCT, AddProductActivity.class)
                      .put(AdminOperations.CHECK_PRODUCT, CheckProductActivity.class)

                      .put(AdminUserHome.PRODUCT_OWNER_OPTIONS, ProductOwnerOperations.class)
                      .put(ProductOwnerOperations.PRODUCT_STOCK_SEARCH, ProductStockSearchActivity.class)
                      .put(Operation.创建新任务.name(), CreateWorkflowSheetActivity.class)
                      .put(ProductOwnerOperations.WORK_DETAILS_CHECK, CheckAllPendingTasksActivity.class)
                      .put(Operation.出库.name(), AddPickupHistoryActivity.class)

                      .put(AdminUserHome.SEARCH_OPTIONS, SearchOperations.class)
                      .put(SearchOperations.FULL_JOB_HISTORY_SEARCH, AdminWorkflowSheetSearchActivity.class)
                      .put(SearchOperations.OWNER_SUMMARY_SEARCH, OwnerSummarySearchActivity.class)
                      .build();


    public static final Map<Operation, Class<?>> RECORD_CLASS_MAP
            = new ImmutableMap.Builder<Operation, Class<?>>()
                      .put(Operation.配料浸泡, RecordDipInTaskHistoryActivity.class)
                      .put(Operation.煮滤, RecordPourTaskHistoryActivity.class)
                      .put(Operation.湿加工, RecordHydroTaskHistoryActivity.class)
                      .put(Operation.干燥硬化, RecordDryTaskHistoryActivity.class)
                      .put(Operation.干加工切割, RecordCutTaskHistoryActivity.class)
                      .put(Operation.包装入库, RecordPackTaskHistoryActivity.class)
                      .build();


    public static final String START_DATE_EXTRA = "START_DATE";
    public static final String END_DATE_EXTRA = "END_DATE";
    public static final String OWNER_NAME_EXTRA = "OWNER_NAME";
    public static final String PRODUCT_NAME_EXTRA = "PRODUCT_NAME";
    public static final String OPERATION_EXTRA = "OPERATION";
    public static final String USER_NAME_EXTRA = "USER_NAME";
    public static final String WORKFLOW_DETAILS_EXTRA = "WORKFLOW_DETAILS";
    public static final String WORKFLOW_SHEET_EXTRA = "WORKFLOW_SHEET";

    public static final String JOB_HISTORY_ID_EXTRA = "JOB_HISTORY_ID";

    public static final String SPINNER_PLACE_HOLDER_OPTION = "请选择";
    public static final String UNKNOWN_FIELD_VALUE = "未知";
    public static final String UNFINISHED_FIELD_VALUE = "未完成";
}
