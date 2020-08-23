package com.refine.model;

import java.util.ArrayList;
import java.util.List;

public class User {

    public static User createPlaceHolderUser(String displayName) {
        User placeholder = new User();
        placeholder.setDisplayName(displayName);
        placeholder.setUsername("placeholder");
        return placeholder;
    }

    private String username;
    private Boolean isAdmin;
    private String displayName;
    private List<String> permissions;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public void addPermission(String permission) {
        if (permissions == null) {
            permissions = new ArrayList<>();
        }
        permissions.add(permission);
    }

    public void addPermissions(List<String> permissions) {
        if (this.permissions == null) {
            this.permissions = new ArrayList<>();
        }
        this.permissions.addAll(permissions);
    }
}
