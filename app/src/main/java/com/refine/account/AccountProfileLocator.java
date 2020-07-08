package com.refine.account;

import java.util.List;

import com.refine.database.DatabaseAccessor;
import com.refine.database.SingleNumberCallback;
import com.refine.database.SingleStringListCallback;
import com.refine.model.Operation;

public class AccountProfileLocator {

    private final DatabaseAccessor dbAccessor;
    private final List<String> allowedOperations;
    private final boolean isAdmin;
    private final List<Operation> roles;

    private static AccountProfileLocator profile = null;

    private AccountProfileLocator(String username,
                                  String password,
                                  List<String> permissionCodes,
                                  boolean isAdmin) {
        this.roles = Operation.fromPermissionCodes(permissionCodes);
        this.isAdmin = isAdmin;
        this.allowedOperations = Operation.generateUserOperations(roles);
        this.dbAccessor = new DatabaseAccessor(username, password);
    }

    public static boolean login(String username, String password) {
        DatabaseAccessor accessor = new DatabaseAccessor(username, password);

        try {
            SingleStringListCallback callback = new SingleStringListCallback();
            accessor.query("SELECT permission from user_permissions where username = ?", new String[] {username},
                           callback);

            SingleNumberCallback checkAdmin = new SingleNumberCallback();
            accessor.query("SELECT is_admin from users where username = ?", new String[] {username},
                           checkAdmin);

            boolean isAdmin = checkAdmin.getResult() == 1;

            AccountProfileLocator.profile = new AccountProfileLocator(
                    username, password, callback.getResult(), isAdmin);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static AccountProfileLocator getProfile() {
        return profile;
    }

    public DatabaseAccessor getDbAccessor() {
        return dbAccessor;
    }

    public List<String> getAllowedOperations() {
        if (isAdmin) {
            return Operation.getAllOperations();
        } else {
            return allowedOperations;
        }
    }

    public String getCurrentUser() {
        return dbAccessor.getUsername();
    }

    public boolean isAdminUser() {
        return isAdmin;
    }

    public static void resetProfile() {
        AccountProfileLocator.profile = null;
    }

}
