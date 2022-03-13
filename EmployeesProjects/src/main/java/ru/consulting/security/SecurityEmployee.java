package ru.consulting.security;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.consulting.entitity.Employee;
import ru.consulting.entitity.Status;

import java.util.Collection;
import java.util.List;

@Data
public class SecurityEmployee implements UserDetails {

    private final String name;
    private final String password;
    private final List<SimpleGrantedAuthority> authorities;
    private final boolean isActive;

    public SecurityEmployee(String name, String password,
                            List<SimpleGrantedAuthority> authorities, boolean isActive) {
        this.name = name;
        this.password = password;
        this.authorities = authorities;
        this.isActive = isActive;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isActive;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isActive;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isActive;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }

    public static UserDetails fromUSer(Employee employee) {
        return new org.springframework.security.core.userdetails.User(employee.getEmail(),
                employee.getPassword(),
                employee.getStatus().equals(Status.ACTIVE),
                employee.getStatus().equals(Status.ACTIVE),
                employee.getStatus().equals(Status.ACTIVE),
                employee.getStatus().equals(Status.ACTIVE),
                employee.getAllAuthorities());
    }
}
