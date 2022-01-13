package poly.com.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import poly.com.entity.Employee;
import poly.com.entity.TokenResetPassword;

import java.util.Date;
import java.util.Optional;

public interface PasswordResetRespository extends JpaRepository<TokenResetPassword, Integer> {
    @Query("select a from TokenResetPassword a where a.employee = ?1")
    TokenResetPassword findByEmployee(Employee employee);

    TokenResetPassword findByToken(String token);

    @Modifying
    @Query("delete from TokenResetPassword t where t.expiryDate <= ?1")
    void deleteAllByExpiryDate(Date now);

}