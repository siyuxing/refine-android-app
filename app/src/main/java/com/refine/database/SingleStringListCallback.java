package com.refine.database;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SingleStringListCallback implements ResultSetCallback<List<String>> {
    private List<String> result;

    public SingleStringListCallback() {
        result = new ArrayList<>();
    }

    @Override
    public void processResultSet(ResultSet rs) throws Exception {
        do {
            result.add(rs.getString(1));
        } while(rs.next());
    }

    @Override
    public List<String> getResult() {
        return this.result;
    }
}
