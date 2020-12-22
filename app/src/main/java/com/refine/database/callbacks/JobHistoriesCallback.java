package com.refine.database.callbacks;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.StringUtils;
import com.refine.model.JobHistory;
import com.refine.model.Operation;

public class JobHistoriesCallback implements ResultSetCallback<List<JobHistory>> {
    private static final String ID_COLUMN = "id";
    private static final String DATE_COLUMN = "date";
    private static final String OWNER_COLUMN = "owner_username";
    private static final String NUM_OF_MATERIAL_COLUMN = "material_num";
    private static final String NUM_OF_SUCCESS_COLUMN = "success";
    private static final String NUM_OF_FAILURE_COLUMN = "failure";
    private static final String PRODUCT_NAME_COLUMN = "product_name";
    private static final String OPERATION_COLUMN = "operation";
    private static final String ADDITIONAL_NOTE_COLUMN = "additional_note";

    private List<JobHistory> jobHistories = new ArrayList<>();


    @Override
    public void processResultSet(ResultSet rs) throws Exception {
        do {
            JobHistory jobHistory = new JobHistory();
            jobHistory.setId(rs.getLong(ID_COLUMN));
            jobHistory.setDate(rs.getDate(DATE_COLUMN));
            jobHistory.setDate(rs.getDate(DATE_COLUMN));
            jobHistory.setOwner(rs.getString(OWNER_COLUMN));
            jobHistory.setNumOfSuccess(rs.getInt(NUM_OF_SUCCESS_COLUMN));
            jobHistory.setNumOfFailure(rs.getInt(NUM_OF_FAILURE_COLUMN));
            jobHistory.setProductName(rs.getString(PRODUCT_NAME_COLUMN));
            jobHistory.setOperation(Operation.fromOperationCode(rs.getString(OPERATION_COLUMN)));
            jobHistory.setAdditionNote(rs.getString(ADDITIONAL_NOTE_COLUMN));

            if (!StringUtils.isNullOrEmpty(rs.getString(NUM_OF_MATERIAL_COLUMN))) {
                jobHistory.setNumOfMaterial(rs.getInt(NUM_OF_MATERIAL_COLUMN));
            }

            jobHistories.add(jobHistory);
        } while(rs.next());
    }

    @Override
    public List<JobHistory> getResult() {
        return jobHistories;
    }
}
