package ru.consulting.entitity.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ru.consulting.entitity.security.Permission;

import java.util.Set;
import java.util.stream.Collectors;

public enum Role {
    USER(Set.of(Permission.EMPLOYEE_READ)),
    ADMIN(Set.of(Permission.EMPLOYEE_READ, Permission.EMPLOYEE_WRITE, Permission.EMPLOYEE_PARTIAL_WRITE)),
    MAINUSER(Set.of(Permission.EMPLOYEE_READ, Permission.EMPLOYEE_PARTIAL_WRITE));

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getAuthorities() {
        return getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
    }
}