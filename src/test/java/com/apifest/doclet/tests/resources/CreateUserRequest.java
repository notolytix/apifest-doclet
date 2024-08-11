package com.apifest.doclet.tests.resources;

import java.util.Map;
import java.util.Set;

public class CreateUserRequest {

    private String username;
    private Map<String, Set<String>> access;
    private String role;

    public CreateUserRequest() {
        // empty
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Map<String, Set<String>> getAccess() {
        return access;
    }

    public void setAccess(Map<String, Set<String>> access) {
        this.access = access;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
