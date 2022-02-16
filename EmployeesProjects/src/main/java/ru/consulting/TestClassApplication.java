package ru.consulting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import ru.consulting.repositories.DepartmentRepo;
import ru.consulting.repositories.EmployeeRepo;
import ru.consulting.repositories.PositionRepo;
import ru.consulting.repositories.TestRepo;

import java.math.BigDecimal;
import java.util.List;

@Component
public class TestClassApplication implements ApplicationRunner {

    private TestRepo testRepo;
    private EmployeeRepo employeeRepo;
    private DepartmentRepo departmentRepo;
    private PositionRepo positionRepo;

    @Autowired
    public TestClassApplication(TestRepo testRepo, EmployeeRepo employeeRepo, DepartmentRepo departmentRepo,
                                PositionRepo positionRepo) {
        this.testRepo = testRepo;
        this.employeeRepo = employeeRepo;
        this.departmentRepo = departmentRepo;
        this.positionRepo = positionRepo;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
//        System.out.println(employeeRepo.count());
//        System.out.println(employeeRepo.findByNameAndEmail("Ivan", "ivanov@yandex.ru").getSalary());
//        System.out.println(Arrays.toString(positionRepo.findByIdOrTitle(14L, "developer").getJobFunction()));
//        System.out.println(departmentRepo.findByTitleEqualsIgnoreCase("iT").getId());
//        System.out.println(employeeRepo.findBySalaryBetween(new BigDecimal(67300), BigDecimal.valueOf(67500)).getSurname());
//        employeeRepo.findByIdBetween(1L,8L).forEach(employee -> System.out.println(employee.getName()));
//        employeeRepo.findByDateOfEmploymentAfterAndDateOfEmploymentBefore(LocalDate.of(2005, 1, 1),
//                LocalDate.of(2012, 5, 12)).forEach(employee -> System.out.println(employee.getName()));
//            employeeRepo.findByIdGreaterThanEqualAndSalaryLessThan(2L,new BigDecimal(60000))
//                    .forEach(employee -> System.out.println(employee.getName()));
//        employeeRepo.findByPatronymicIsNull().forEach(employee -> System.out.println(employee.getSurname()));
//        employeeRepo.findByPatronymicNotNull().forEach(employee -> System.out.println(employee.getSurname()));
//        System.out.println(departmentRepo.findByTitleLike("__").getTitle());
//        System.out.println(departmentRepo.findByTitleNotLike("__").size());
//        System.out.println(positionRepo.findByTitleStartingWithIgnoreCase("dEv").getTitle());
//        System.out.println(positionRepo.findByTitleEndingWithIgnoreCase("teR").getTitle());
//        System.out.println(departmentRepo.findByTitleContaining("Department").get(0).getTitle());
//        System.out.println(employeeRepo.findByEmailNotOrderByDateOfEmploymentDesc("zueV@yandex.ru").get(0).getSurname());
//        employeeRepo.findBySalaryIn(List.of(new BigDecimal(55000), new BigDecimal(41000)))
//                .forEach(employee -> System.out.println(employee.getSurname()));
//        System.out.println(employeeRepo.findByPositionAndDepartment(positionRepo.findById(1L).get(), departmentRepo.findById(3L).get()).getSurname());
//        employeeRepo.findByDepartmentTitle("IT").forEach(employee -> System.out.println(employee.getSurname()));
//        employeeRepo.findByDepartmentHeadSurname("Petrov").forEach(employee -> System.out.println(employee.getSurname()));

    }
}
