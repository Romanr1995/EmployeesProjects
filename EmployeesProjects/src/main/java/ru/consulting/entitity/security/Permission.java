package ru.consulting.entitity.security;


public enum Permission {
    EMPLOYEE_READ("employee:read"),
    EMPLOYEE_WRITE("employee:write"),
    EMPLOYEE_PARTIAL_WRITE("employee:partial_write"),
    PROJECT_WRITE("project_write"),
    MAIN_USER_WRITE("main_user_write");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}