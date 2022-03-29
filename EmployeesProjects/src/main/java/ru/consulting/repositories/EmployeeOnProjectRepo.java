package ru.consulting.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.consulting.entitity.EmployeeOnProject;

import java.time.LocalDate;

@Repository
public interface EmployeeOnProjectRepo extends CrudRepository<EmployeeOnProject, Long> {

    @Query("Select emp From EmployeeOnProject emp Where emp.employee.phone = :phone And emp.startWorks =:startWorks")
    EmployeeOnProject getByEmployeePhone(@Param("phone") String phone, @Param("startWorks") LocalDate startWorks);
}
