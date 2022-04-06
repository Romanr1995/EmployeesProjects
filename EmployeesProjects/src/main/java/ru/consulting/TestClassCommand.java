package ru.consulting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import ru.consulting.entitity.Department;
import ru.consulting.entitity.Employee;
import ru.consulting.entitity.Position;
import ru.consulting.repositories.*;

import java.math.BigDecimal;
import java.time.LocalDate;

//@Component
public class TestClassCommand implements CommandLineRunner {
    private ClientRepo clientRepo;
    private DepartmentRepo departmentRepo;
    private EmployeeOnProjectRepo employeeOnProjectRepo;
    private EmployeeRepo employeeRepo;
    private PositionRepo positionRepo;
    private ProjectRepo projectRepo;
    private ProjectRoleRepo projectRoleRepo;
    private TestRepo testRepo;

    @Autowired
    public TestClassCommand(ClientRepo clientRepo, DepartmentRepo departmentRepo,
                            EmployeeOnProjectRepo employeeOnProjectRepo, EmployeeRepo employeeRepo,
                            PositionRepo positionRepo, ProjectRepo projectRepo, ProjectRoleRepo projectRoleRepo,
                            TestRepo testRepo) {
        this.clientRepo = clientRepo;
        this.departmentRepo = departmentRepo;
        this.employeeOnProjectRepo = employeeOnProjectRepo;
        this.employeeRepo = employeeRepo;
        this.positionRepo = positionRepo;
        this.projectRepo = projectRepo;
        this.projectRoleRepo = projectRoleRepo;
        this.testRepo = testRepo;
    }

    @Override
    public void run(String... args) throws Exception {
        Department higherDepartment = new Department();
        higherDepartment.setTitle("HigherDepartment");

        Employee topManager = new Employee();
        topManager.setName("Ivan");
        topManager.setSurname("Ivanov");
        topManager.setSalary(new BigDecimal(100_000.234));
        topManager.setDateOfEmployment(LocalDate.of(1996, 3, 2));
        topManager.setEmail("ivanov@yandex.ru");
        topManager.setPhone("89998453345");
        topManager.setDepartment(higherDepartment);
        higherDepartment.setDepartmentHead(topManager);

        Position manager = new Position();
        manager.setTitle("manager");
        manager.setJobFunction(new String[]{"func1", "func2", "func3"});
        topManager.setPosition(manager);

//        departmentRepo.save(higherDepartment);
//        employeeRepo.save(topManager);
//        positionRepo.save(manager);

        Department it = new Department();
        it.setTitle("IT");
        it.setHigherDepartment(departmentRepo.findByTitle("HigherDepartment"));

        Employee itBoss = new Employee();
        itBoss.setName("Petr");
        itBoss.setSurname("Petrov");
        itBoss.setSalary(new BigDecimal(80_000.4));
        itBoss.setDateOfEmployment(LocalDate.of(2004, 4, 22));
        itBoss.setEmail("petrov2004@yandex.ru");
        itBoss.setPhone("89942223419");
        itBoss.setDepartment(it);
        itBoss.setPosition(positionRepo.findByTitleIgnoreCase("manager"));
        it.setDepartmentHead(itBoss);

//        employeeRepo.save(itBoss);
//        departmentRepo.save(it);

        Department sales = new Department();
        sales.setTitle("Sales");
        sales.setHigherDepartment(departmentRepo.findByTitle("HigherDepartment"));

        Employee salesBoss = new Employee();
        salesBoss.setName("Konstantin");
        salesBoss.setSurname("Govorov");
        salesBoss.setPatronymic("Petrovich");
        salesBoss.setSalary(new BigDecimal(67_400));
        salesBoss.setDateOfEmployment(LocalDate.of(2006, 12, 15));
        salesBoss.setEmail("govorovK@yandex.ru");
        salesBoss.setPhone("89254301134");
        salesBoss.setDepartment(sales);
        salesBoss.setPosition(positionRepo.findByTitleIgnoreCase("manager"));
        sales.setDepartmentHead(salesBoss);

//        employeeRepo.save(salesBoss);
//        departmentRepo.save(sales);

        Position engineer = new Position();
        engineer.setTitle("engineer");
        engineer.setJobFunction(new String[]{"engineering"});

        Position developer = new Position();
        developer.setTitle("developer");
        developer.setJobFunction(new String[]{"development", "testing"});

        Position seller = new Position();
        seller.setTitle("seller");
        seller.setJobFunction(new String[]{"sales", "service"});

        Position tester = new Position();
        tester.setTitle("tester");
        tester.setJobFunction(new String[]{"testing"});

//        positionRepo.save(engineer);
//        positionRepo.save(developer);
//        positionRepo.save(seller);
//        positionRepo.save(tester);

        Employee engineer1 = new Employee();
        engineer1.setName("Andrei");
        engineer1.setSurname("Zuev");
        engineer1.setSalary(new BigDecimal(55_000));
        engineer1.setDateOfEmployment(LocalDate.of(2010, 12, 27));
        engineer1.setEmail("zueV@yandex.ru");
        engineer1.setPhone("89453234533");
        engineer1.setDepartment(departmentRepo.findByTitle("IT"));
        engineer1.setPosition(positionRepo.findByTitleIgnoreCase("engineer"));

//        employeeRepo.save(engineer1);

        Employee engineer2 = new Employee();
        engineer2.setName("Mihail");
        engineer2.setSurname("Suvorov");
        engineer2.setSalary(new BigDecimal(25_000));
        engineer2.setDateOfEmployment(LocalDate.of(2017, 2, 11));
        engineer2.setEmail("mihailSuv@yandex.ru");
        engineer2.setPhone("89443212121");
        engineer2.setDepartment(departmentRepo.findByTitle("IT"));
        engineer2.setPosition(positionRepo.findByTitleIgnoreCase("engineer"));

//        employeeRepo.save(engineer2);

        Employee developer1 = new Employee();
        developer1.setName("Ilya");
        developer1.setSurname("Vasilev");
        developer1.setSalary(new BigDecimal(68_245));
        developer1.setDateOfEmployment(LocalDate.of(2009, 2, 5));
        developer1.setEmail("vasileff@yandex.ru");
        developer1.setPhone("89773453322");
        developer1.setDepartment(departmentRepo.findByTitle("IT"));
        developer1.setPosition(positionRepo.findByTitleIgnoreCase("developer"));

//        employeeRepo.save(developer1);

        Employee developer2 = new Employee();
        developer2.setName("Anatoliy");
        developer2.setSurname("Lunev");
        developer2.setSalary(new BigDecimal(77_123));
        developer2.setDateOfEmployment(LocalDate.of(2007, 7, 7));
        developer2.setEmail("lunevA@yandex.ru");
        developer2.setPhone("89223454425");
        developer2.setDepartment(departmentRepo.findByTitle("IT"));
        developer2.setPosition(positionRepo.findByTitleIgnoreCase("developer"));

//        employeeRepo.save(developer2);

        Employee seller1 = new Employee();
        seller1.setName("Fedor");
        seller1.setSurname("Fedorov");
        seller1.setSalary(new BigDecimal(34_000));
        seller1.setDateOfEmployment(LocalDate.of(2015, 12, 12));
        seller1.setEmail("fedodovfedya@yandex.ru");
        seller1.setPhone("89776564556");
        seller1.setDepartment(departmentRepo.findByTitle("Sales"));
        seller1.setPosition(positionRepo.findByTitleIgnoreCase("seller"));

//        employeeRepo.save(seller1);

        Employee tester1 = new Employee();
        tester1.setName("Anton");
        tester1.setSurname("Uzlov");
        tester1.setSalary(new BigDecimal(44_000));
        tester1.setDateOfEmployment(LocalDate.of(2016, 11, 23));
        tester1.setEmail("uzlov@yandex.ru");
        tester1.setPhone("89122343235");
        tester1.setDepartment(departmentRepo.findByTitle("IT"));
        tester1.setPosition(positionRepo.findByTitleIgnoreCase("tester"));

//        employeeRepo.save(tester1);

        Employee tester2 = new Employee();
        tester2.setName("Anna");
        tester2.setSurname("Titova");
        tester2.setSalary(new BigDecimal(41_000));
        tester2.setDateOfEmployment(LocalDate.of(2020, 2, 29));
        tester2.setEmail("titova@yandex.ru");
        tester2.setPhone("89234356677");
        tester2.setDepartment(departmentRepo.findByTitle("IT"));
        tester2.setPosition(positionRepo.findByTitleIgnoreCase("tester"));

//        employeeRepo.save(tester2);

        Employee tester3 = new Employee();
        tester3.setName("Sergei");
        tester3.setSurname("Gorbunov");
        tester3.setSalary(new BigDecimal(56_000));
        tester3.setDateOfEmployment(LocalDate.of(2011, 4, 30));
        tester3.setEmail("gorbunov@yandex.ru");
        tester3.setPhone("8923434684");
        tester3.setDepartment(departmentRepo.findByTitle("IT"));
        tester3.setPosition(positionRepo.findByTitleIgnoreCase("tester"));

//        employeeRepo.save(tester3);

        Employee developer3 = new Employee();
        developer3.setName("Ivan");
        developer3.setSurname("Krugvov");
        developer3.setSalary(new BigDecimal(57_321));
        developer3.setDateOfEmployment(LocalDate.of(2013, 6, 7));
        developer3.setEmail("kruglovIvan@yandex.ru");
        developer3.setPhone("8965343567");
        developer3.setDepartment(departmentRepo.findByTitle("IT"));
        developer3.setPosition(positionRepo.findById(3L).get());
//        employeeRepo.save(developer3);

        Employee developer4 = new Employee();
        developer4.setName("Nikita");
        developer4.setSurname("Ermolaev");
        developer4.setPatronymic("Alexandrovich");
        developer4.setSalary(new BigDecimal(23_621));
        developer4.setDateOfEmployment(LocalDate.of(2018, 11, 4));
        developer4.setEmail("nikitaErmolaev@yandex.ru");
        developer4.setPhone("89264321234");
        developer4.setDepartment(departmentRepo.findByTitle("IT"));
        developer4.setPosition(positionRepo.findById(3L).get());

//        employeeRepo.save(developer4);

//        employeeRepo.delete(employeeRepo.findByPhone("8965343567"));
//        employeeRepo.deleteById(22L);

//        Iterable<Employee> employeeIterable = () -> {
//            List<Employee> employees = List.of(developer3, developer4);
//            return employees.iterator();
//        };
//        employeeRepo.saveAll(employeeIterable);

//        Iterable<Long> deleteEmployees = () -> {
//           List<Long> longs = List.of(23L,24L);
//            return longs.iterator();
//        };
//        employeeRepo.deleteAllById(deleteEmployees);

//        System.out.println(departmentRepo.existsById(3L));
//        System.out.println(departmentRepo.existsById(10L));

        Department subIt = new Department();
        subIt.setTitle("subIT");
//        serviceClass.addDepartment(subIt);

        Employee subItBoss = new Employee();
        subItBoss.setName("Nikolai");
        subItBoss.setSurname("Sizov");
        subItBoss.setPatronymic("Sergeevich");
        subItBoss.setSalary(new BigDecimal(71_321.6565657));
        subItBoss.setDateOfEmployment(LocalDate.of(2012, 1, 24));
        subItBoss.setEmail("sizov@yandex.ru");
        subItBoss.setPhone("89245432345");
//        serviceClass.addEmployee(subItBoss);

//        serviceClass.setDepartmentHeadByDepartment(serviceClass.getDepartmentByTitle("subIT"),
//                serviceClass.getEmployeeByNameAndSurname("NIKOLAI", "sizov"));

//        serviceClass.setPositionByEmployee(serviceClass.getEmployeeByNameAndSurname("Nikolai", "Sizov"),
//                serviceClass.getPositionByTitle("developer"));
//
//        serviceClass.setDepartmentByEmployee(serviceClass.getEmployeeByNameAndSurname("Nikolai", "sizov"),
//                serviceClass.getDepartmentByTitle("subIT"));

    }
}