package poly.com.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import poly.com.entity.Employee;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    @Query("Select e.username from Employee e where  e.id = ?1 ")
    String findUserNameById(int id);

    Boolean existsByUsername(String username);

    Optional<Employee> findByPhone(String phone);

    Optional<Employee> findByIdentitycard(String identitycard);

    Optional<Employee> findByUsername(String username);

    Optional<Employee> findByEmail(String email);


}