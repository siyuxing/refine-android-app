package com.refine.database.callbacks;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.StringUtils;
import com.refine.model.Operation;
import com.refine.model.WorkflowDetails;

public class WorkflowDetailsCallback implements ResultSetCallback<List<WorkflowDetails>>  {
    private static final String ID_COLUMN = "id";
    private static final String WORKFLOW_ID_COLUMN = "workflow_id";
    private static final String OWNER_COLUMN = "owner_username";
    private static final String NUM_OF_TOTAL_COLUMN = "total";
    private static final String NUM_OF_SUCCESS_COLUMN = "success";
    private static final String NUM_OF_FAILURE_COLUMN = "failure";
    private static final String PRODUCT_ID_COLUMN = "product_id";
    private static final String PRODUCT_NAME_COLUMN = "product_name";
    private static final String OPERATION_COLUMN = "operation";
    private static final String ADDITIONAL_NOTE_COLUMN = "additional_note";
    private static final String START_DATE_COLUMN = "start_date";
    private static final String FINISH_DATE_COLUMN = "finish_date";
    private static final String IS_FINISH_COLUMN = "is_finish";

    private List<WorkflowDetails> detailsList;


    @Override
    public void processResultSet(ResultSet rs) throws Exception {
        detailsList = new ArrayList<>();
        do {
            WorkflowDetails details = new WorkflowDetails();
            details.setId(rs.getLong(ID_COLUMN));
            details.setWorkflowId(rs.getString(WORKFLOW_ID_COLUMN));
            details.setOwner(rs.getString(OWNER_COLUMN));
            details.setOperation(Operation.fromOperationCode(rs.getString(OPERATION_COLUMN)));
            details.setProductName(rs.getString(PRODUCT_NAME_COLUMN));

            if (!StringUtils.isNullOrEmpty(rs.getString(PRODUCT_ID_COLUMN))) {
                details.setProductId(rs.getLong(PRODUCT_ID_COLUMN));
            }
            if (!StringUtils.isNullOrEmpty(rs.getString(NUM_OF_TOTAL_COLUMN))) {
                details.setNumOfTotal(rs.getInt(NUM_OF_TOTAL_COLUMN));
            }
            if (!StringUtils.isNullOrEmpty(rs.getString(NUM_OF_SUCCESS_COLUMN))) {
                details.setNumOfSuccess(rs.getInt(NUM_OF_SUCCESS_COLUMN));
            }
            if (!StringUtils.isNullOrEmpty(rs.getString(NUM_OF_FAILURE_COLUMN))) {
                details.setNumOfFailure(rs.getInt(NUM_OF_FAILURE_COLUMN));
            }
            details.setAdditionNote(rs.getString(ADDITIONAL_NOTE_COLUMN));

            if (!StringUtils.isNullOrEmpty(rs.getString(START_DATE_COLUMN))) {
                details.setStartDate(rs.getDate(START_DATE_COLUMN));
            }
            if (!StringUtils.isNullOrEmpty(rs.getString(FINISH_DATE_COLUMN))) {
                details.setFinishDate(rs.getDate(FINISH_DATE_COLUMN));
            }

            details.setFinish(rs.getBoolean(IS_FINISH_COLUMN));

            detailsList.add(details);
        } while(rs.next());
    }

    @Override
    public List<WorkflowDetails> getResult() {
        return detailsList;
    }
}
