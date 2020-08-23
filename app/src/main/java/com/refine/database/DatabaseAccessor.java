package com.refine.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

public class DatabaseAccessor {

    //private static final String DB_HOST_NAME = "rm-bp190y61vjv52poq7vo.mysql.rds.aliyuncs.com";
    private static final String DB_HOST_NAME = "10.0.2.2";
    private static final int QUERY_TIMEOUT = 20;

    private final String username;
    private final String password;

    public DatabaseAccessor(String username,
                            String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void query(String sql, ResultSetCallback callback) throws Exception {
        query(sql, null, callback);
    }

    public void query(String sql, Object[] arguments, ResultSetCallback callback) throws Exception {

        try (final Connection connection = getConnection();
             final PreparedStatement st = connection.prepareStatement(sql)) {
            st.setQueryTimeout(QUERY_TIMEOUT);

            if (arguments != null) {
                for (int i = 0; i < arguments.length; i++) {
                    st.setObject(i + 1, arguments[i]);
                }
            }
            st.executeQuery();
            final ResultSet rs = st.getResultSet();
            while (rs.next()) {
                callback.processResultSet(rs);
            }
        }
    }

    public void insert(String sql, Object[] arguments) throws Exception {

        try (final Connection connection = getConnection();
             final PreparedStatement st = connection.prepareStatement(sql)) {
            st.setQueryTimeout(QUERY_TIMEOUT);

            if (arguments != null) {
                for (int i = 0; i < arguments.length; i++) {
                    st.setObject(i + 1, arguments[i]);
                }
            }
            st.executeUpdate();
        }
    }

    public void execute(String sql, Object[] arguments) throws Exception {

        try (final Connection connection = getConnection();
             final PreparedStatement st = connection.prepareStatement(sql)) {
            st.setQueryTimeout(QUERY_TIMEOUT);

            if (arguments != null) {
                for (int i = 0; i < arguments.length; i++) {
                    st.setObject(i + 1, arguments[i]);
                }
            }
            st.execute();
        }
    }

    synchronized Connection getConnection() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");

        return DriverManager.getConnection(computeConnectionString(), getConnectionProperties());
    }

    private String computeConnectionString() {
        return String.format("jdbc:mysql://%s:3306/refine2", DB_HOST_NAME);
    }

    private Properties getConnectionProperties() {
        Properties properties = new Properties();
        properties.put("user", username);
        properties.put("password", password);

        return properties;
    }
}
