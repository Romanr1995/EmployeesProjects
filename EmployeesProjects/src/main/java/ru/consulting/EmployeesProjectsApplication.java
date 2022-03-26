package ru.consulting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@SpringBootApplication
public class EmployeesProjectsApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeesProjectsApplication.class, args);
	}

}
