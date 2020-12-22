package com.refine.database.callbacks;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;
import com.refine.model.Operation;
import com.refine.model.User;

public class UsersCallback implements ResultSetCallback<List<User>> {
    private static final String USER_NAME_COLUMN = "username";
    private static final String DISPLAY_NAME_COLUMN = "display_name";
    private static final String IS_ADMIN_COLUMN = "is_admin";
    private static final String PERMISSIONS_COLUMN = "permissions";

    private List<User> users;

    @Override
    public void processResultSet(ResultSet rs) throws Exception {
        users = new ArrayList<>();
        do {
            User user = new User();
            user.setUsername(rs.getString(USER_NAME_COLUMN));
            user.setDisplayName(rs.getString(DISPLAY_NAME_COLUMN));
            user.setIsAdmin(rs.getBoolean(IS_ADMIN_COLUMN));

            if (Boolean.TRUE == user.getIsAdmin()) {
                user.addPermission(Operation.ADMIN_PERMISSION_NAME);
            }

            String permissionString = rs.getString(PERMISSIONS_COLUMN);
            if (permissionString != null) {
                List<String> permissions = Lists.newArrayList(permissionString.split(","));
                user.addPermissions(Operation.generateUserOperations(Operation.fromPermissionCodes(permissions)));
            }
            users.add(user);
        } while (rs.next());
    }

    @Override
    public List<User> getResult() {
        return users;
    }
}
