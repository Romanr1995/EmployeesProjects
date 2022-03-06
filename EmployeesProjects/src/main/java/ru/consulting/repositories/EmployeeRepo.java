package ru.consulting.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.consulting.entitity.Department;
import ru.consulting.entitity.Employee;
import ru.consulting.entitity.Position;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {

    Employee findByPhone(String phone);

    Optional<Employee> findByPhoneOrEmailIgnoreCase(String phone, String email);

    Employee findByNameAndEmail(String name, String email);

    Employee findBySalaryBetween(BigDecimal salaryMin, BigDecimal salaryMax);

    List<Employee> findByIdBetween(Long idMin, Long idMax);

    List<Employee> findByDateOfEmploymentAfterAndDateOfEmploymentBefore(LocalDate afterDate, LocalDate beforeDate);

    List<Employee> findByIdGreaterThanEqualAndSalaryLessThan(Long id, BigDecimal salary);

    List<Employee> findByPatronymicIsNull();

    List<Employee> findByPatronymicNotNull();

    List<Employee> findByEmailNotOrderByDateOfEmploymentDesc(String email);

    List<Employee> findBySalaryIn(List<BigDecimal> bigDecimals);

    Optional<Employee> findByNameIgnoreCaseAndSurnameIgnoreCase(String name, String surname);

    @Query("Select emp From Employee emp Where emp.position = :position And emp.department = :department")
    Employee findByPositionAndDepartment(@Param("position") Position position, @Param("department") Department department);

    @Query("Select emp From Employee emp Where emp.department.title = :title")
    List<Employee> findByDepartmentTitle(@Param("title") String title);

    @Query("Select emp From Employee emp Where emp.department.departmentHead.surname = :surname And emp.surname <> :surname")
    List<Employee> findByDepartmentHeadSurname(@Param("surname") String surname);
}
