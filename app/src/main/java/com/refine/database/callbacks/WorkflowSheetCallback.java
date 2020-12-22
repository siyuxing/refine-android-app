package com.refine.database.callbacks;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.StringUtils;
import com.refine.model.WorkflowSheet;

public class WorkflowSheetCallback implements ResultSetCallback<List<WorkflowSheet>>  {
    private static final String ID_COLUMN = "id";
    private static final String REQUESTER_COLUMN = "requester";
    private static final String PRODUCT_ID_COLUMN = "product_id";
    private static final String PRODUCT_NAME_COLUMN = "product_name";

    private static final String MATERIAL_NAME_COLUMN = "material";
    private static final String NUM_OF_MATERIAL_COLUMN = "material_num";

    private static final String NUM_OF_REQUESTED_COLUMN = "expected_product_count";
    private static final String NUM_OF_FINAL_COLUMN = "final_product_count";

    private static final String START_DATE_COLUMN = "start_date";
    private static final String FINISH_DATE_COLUMN = "end_date";

    private List<WorkflowSheet> workflowSheets;


    @Override
    public void processResultSet(ResultSet rs) throws Exception {
        workflowSheets = new ArrayList<>();
        do {
            WorkflowSheet sheet = new WorkflowSheet();
            sheet.setSheetId(rs.getString(ID_COLUMN));
            sheet.setRequester(rs.getString(REQUESTER_COLUMN));
            sheet.setProductName(rs.getString(PRODUCT_NAME_COLUMN));
            sheet.setMaterial(rs.getString(MATERIAL_NAME_COLUMN));

            if (!StringUtils.isNullOrEmpty(rs.getString(PRODUCT_ID_COLUMN))) {
                sheet.setProductId(rs.getLong(PRODUCT_ID_COLUMN));
            }

            if (!StringUtils.isNullOrEmpty(rs.getString(NUM_OF_MATERIAL_COLUMN))) {
                sheet.setNumOfMaterial(rs.getInt(NUM_OF_MATERIAL_COLUMN));
            }

            if (!StringUtils.isNullOrEmpty(rs.getString(NUM_OF_REQUESTED_COLUMN))) {
                sheet.setNumOfRequested(rs.getInt(NUM_OF_REQUESTED_COLUMN));
            }
            if (!StringUtils.isNullOrEmpty(rs.getString(NUM_OF_FINAL_COLUMN))) {
                sheet.setNumOfFinal(rs.getInt(NUM_OF_FINAL_COLUMN));
            }

            if (!StringUtils.isNullOrEmpty(rs.getString(START_DATE_COLUMN))) {
                sheet.setStartDate(rs.getDate(START_DATE_COLUMN));
            }
            if (!StringUtils.isNullOrEmpty(rs.getString(FINISH_DATE_COLUMN))) {
                sheet.setFinishDate(rs.getDate(FINISH_DATE_COLUMN));
            }
            workflowSheets.add(sheet);
        } while(rs.next());
    }

    @Override
    public List<WorkflowSheet> getResult() {
        return workflowSheets;
    }
}
