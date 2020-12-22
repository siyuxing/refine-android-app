package com.refine.database.callbacks;

import java.sql.ResultSet;

public class SingleNumberCallback implements ResultSetCallback<Long> {
    private Long result;

    @Override
    public void processResultSet(ResultSet rs) throws Exception {
        String result = rs.getString(1);
        if (result != null) {
            this.result = Long.parseLong(result);
        }
    }

    @Override
    public Long getResult() {
        return this.result;
    }
}
