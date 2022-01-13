package poly.com.repository;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import poly.com.entity.PriceElectricity;

public interface PriceElectricityRepository extends JpaRepository<PriceElectricity, Integer> {

    @Query("select p from PriceElectricity p where year(p.date) = ?1 and month(p.date) = ?2 and p.limits = ?3")
    PriceElectricity findByLimit(int year, int month, int limit);
    
    Optional<PriceElectricity> findFirstByDateLessThanEqualAndLimitsOrderByDateDesc(Date date, int limit);

}
