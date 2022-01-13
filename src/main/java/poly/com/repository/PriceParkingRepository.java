package poly.com.repository;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import poly.com.entity.PriceParking;
import poly.com.entity.TypeVehicel;

public interface PriceParkingRepository extends JpaRepository<PriceParking, Integer>{

	
	@Query("select p from PriceParking p where year(p.date) = ?1 and month(p.date) = ?2 and p.typeVehicel = ?3")
	PriceParking findByYearMonthAndLimit(int year, int month, TypeVehicel typeVehicel);
	
	Optional<PriceParking> findFirstByDateLessThanEqualAndTypeVehicelOrderByDateDesc(Date date, TypeVehicel typeVehicel);
}
