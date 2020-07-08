package com.refine.database;

import java.sql.ResultSet;

public class SingleStringCallback implements ResultSetCallback<String> {
    private String result;

    @Override
    public void processResultSet(ResultSet rs) throws Exception {
        this.result = rs.getString(1);
    }

    @Override
    public String getResult() {
        return this.result;
    }
}
