package com.refine.database.callbacks;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.refine.model.Operation;
import com.refine.model.OwnerSummary;

public class OwnerSummaryCallback implements ResultSetCallback<List<OwnerSummary>> {

    private static final String OWNER_COLUMN = "owner_username";
    private static final String PRODUCT_NAME_COLUMN = "product_name";
    private static final String OPERATION_COLUMN = "operation";
    private static final String NUM_OF_SUCCESS_COLUMN = "total_success";
    private static final String NUM_OF_FAILURE_COLUMN = "total_failure";

    private List<OwnerSummary> ownerSummaries = new ArrayList<>();


    @Override
    public void processResultSet(ResultSet rs) throws Exception {
        do {
            OwnerSummary ownerSummary = new OwnerSummary();
            ownerSummary.setOwner(rs.getString(OWNER_COLUMN));
            ownerSummary.setNumOfSuccess(rs.getInt(NUM_OF_SUCCESS_COLUMN));
            ownerSummary.setNumOfFailure(rs.getInt(NUM_OF_FAILURE_COLUMN));
            ownerSummary.setProductName(rs.getString(PRODUCT_NAME_COLUMN));
            ownerSummary.setOperation(Operation.fromOperationCode(rs.getString(OPERATION_COLUMN)));

            ownerSummaries.add(ownerSummary);
        } while(rs.next());
    }

    @Override
    public List<OwnerSummary> getResult() {
        return ownerSummaries;
    }
}