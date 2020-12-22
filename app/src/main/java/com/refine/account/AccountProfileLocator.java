package com.refine.account;

import java.util.List;

import com.refine.database.DatabaseAccessor;
import com.refine.database.DatabaseHelper;
import com.refine.database.callbacks.SingleNumberCallback;
import com.refine.database.callbacks.SingleStringListCallback;
import com.refine.model.Operation;
import com.refine.model.User;

public class AccountProfileLocator {

    private final DatabaseAccessor dbAccessor;
    private final List<String> allowedOperations;
    private final boolean isAdmin;
    private final List<Operation> roles;

    private static AccountProfileLocator profile = null;

    private AccountProfileLocator(String username,
                                  String password,
                                  User user) {
        this.roles = Operation.fromNames(user.getPermissions());
        this.isAdmin = user.getIsAdmin();
        this.allowedOperations = Operation.generateUserOperations(roles);
        this.dbAccessor = new DatabaseAccessor(username, password);
    }

    public static boolean login(String username, String password) {
        DatabaseAccessor accessor = new DatabaseAccessor(username, password);

        try {
            User user = DatabaseHelper.getUser(username, accessor);
            AccountProfileLocator.profile = new AccountProfileLocator(username, password, user);
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
