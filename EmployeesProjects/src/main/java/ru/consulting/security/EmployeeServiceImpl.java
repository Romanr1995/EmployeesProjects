package ru.consulting.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.consulting.entitity.Employee;
import ru.consulting.repositories.EmployeeRepo;

@Service("employeeServiceImpl")
public class EmployeeServiceImpl implements UserDetailsService {
    private final EmployeeRepo employeeRepo;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepo employeeRepo) {
        this.employeeRepo = employeeRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Employee employee = employeeRepo.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException("User not found"));
        return SecurityEmployee.fromUSer(employee);
    }
}
