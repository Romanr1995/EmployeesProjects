package ru.consulting.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.consulting.entitity.security.Permission;

//@Configuration
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userServise;

    @Autowired
    public SecurityConfig(@Qualifier("employeeServiceImpl") UserDetailsService userServise) {
        this.userServise = userServise;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
//                .formLogin()
                .httpBasic()
                .and().csrf().disable()
                .authorizeRequests()
                .mvcMatchers("/").permitAll()
                .mvcMatchers(HttpMethod.GET, "/department/**", "/position/**")
                .hasAnyAuthority(Permission.EMPLOYEE_PARTIAL_WRITE.getPermission(), Permission.PROJECT_WRITE.getPermission())
                .mvcMatchers(HttpMethod.POST, "/department", "/client/**", "/position/**")
                .hasAuthority(Permission.EMPLOYEE_WRITE.getPermission())
                .mvcMatchers(HttpMethod.PUT, "/department/**").hasAuthority(Permission.EMPLOYEE_WRITE.getPermission())
                .mvcMatchers(HttpMethod.DELETE, "/department/**", "/client/**", "/position/**")
                .hasAuthority(Permission.EMPLOYEE_WRITE.getPermission())
                .mvcMatchers(HttpMethod.POST, "/project/**")
                .hasAnyAuthority(Permission.EMPLOYEE_PARTIAL_WRITE.getPermission())
                .mvcMatchers(HttpMethod.DELETE, "/project")
                .hasAnyAuthority(Permission.EMPLOYEE_PARTIAL_WRITE.getPermission())
                .mvcMatchers("/admin/**").hasAnyAuthority(Permission.EMPLOYEE_WRITE.getPermission())
                .mvcMatchers(HttpMethod.DELETE, "/employee/**")
                .hasAnyAuthority(Permission.EMPLOYEE_PARTIAL_WRITE.getPermission())
                .mvcMatchers(HttpMethod.PUT, "/employee/update/**")
                .hasAnyAuthority(Permission.EMPLOYEE_PARTIAL_WRITE.getPermission())
                .anyRequest().authenticated()
                .and()
                .logout()
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID");
    }

//    @Bean
//    @Override
//    protected UserDetailsService userDetailsService() {
//        return new InMemoryUserDetailsManager(
//                User.builder()
//                        .username("admin")
//                        .password(passwordEncoder().encode("admin"))
//                        .roles("ADMIN")
//                        .build()
//        );
//    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userServise);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }
}
