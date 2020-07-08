package com.refine.database;


import java.sql.ResultSet;

public interface ResultSetCallback<T> {
    /**
     * Perform operations on the result set. This method will only be called if a non-empty resultset is returned
     * from the SQL statement. The first time this method is called the resultset will be positioned at the first
     * row of the result (ie. next() will have been called exactly once)
     * @param rs
     * @throws Exception
     */
    void processResultSet(ResultSet rs) throws Exception;


    T getResult();
}
