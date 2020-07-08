package com.refine.model;

import java.util.List;

public class UserPermission {

    private final boolean isAdmin;
    private final List<String> permissions;

    public UserPermission(boolean isAdmin,
                          List<String> permissions) {
        this.isAdmin = isAdmin;
        this.permissions = permissions;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public List<String> getPermissions() {
        return permissions;
    }
}
