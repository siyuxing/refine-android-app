package com.refine.database;

import java.sql.ResultSet;

import com.refine.model.Operation;
import com.refine.model.User;

public class UserCallback implements ResultSetCallback<User> {
    private static final String USER_NAME_COLUMN = "username";
    private static final String DISPLAY_NAME_COLUMN = "display_name";
    private static final String IS_ADMIN_COLUMN = "is_admin";

    private User user = null;

    @Override
    public void processResultSet(ResultSet rs) throws Exception {
        User user = new User();
        user.setUsername(rs.getString(USER_NAME_COLUMN));
        user.setDisplayName(rs.getString(DISPLAY_NAME_COLUMN));
        user.setIsAdmin(rs.getBoolean(IS_ADMIN_COLUMN));

        this.user = user;

        if (Boolean.TRUE == user.getIsAdmin()) {
            user.addPermission(Operation.ADMIN_PERMISSION_NAME);
        }
    }

    @Override
    public User getResult() {
        return user;
    }
}