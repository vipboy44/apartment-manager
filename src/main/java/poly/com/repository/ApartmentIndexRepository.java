package poly.com.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import poly.com.entity.Apartment;
import poly.com.entity.ApartmentIndex;

public interface ApartmentIndexRepository extends JpaRepository<ApartmentIndex, Integer> {

	Optional<ApartmentIndex> findFirstByApartmentAndDateLessThanOrderByDateDesc(Apartment apartment,Date date);	
	
	@Query("select a from ApartmentIndex a where  a.apartment = ?1 and  year(a.date) = ?2 and month(a.date) = ?3")
	Optional<ApartmentIndex> findByApartmentAndYearAndMonth(Apartment apartment ,int year, int month);
	
	Boolean existsByApartment(Apartment apartment);
	
	@Query(value = "SELECT * " + 
			" FROM  apartment_index  " + 
			" WHERE  id_apartment = ?1 and  year(date) = ?2 and month(date)< ?3 ", nativeQuery = true)
	List<ApartmentIndex> findByDateLessThan(String id_apartmen ,int year, int month);
	
	@Query(value = "SELECT * " + 
			" FROM  apartment_index  " + 
			" WHERE  id_apartment = ?1 and  year(date) = ?2 and month(date)= ?3 ", nativeQuery = true)
	Optional<ApartmentIndex> findByMonthInYear(String id_apartment, int year, int month);
	
	@Query(value = "SELECT  month(date) " + 
			" FROM  apartment_index  " + 
			" WHERE year(date) = ?1 " +
		    " GROUP BY month(date) " +
		    " ORDER BY month(date) ASC ", nativeQuery = true)
	List<Integer> findALLMonth( int year);
	
	@Query(value = "SELECT  year(date) " + 
			" FROM  apartment_index  " + 	
		    " GROUP BY year(date) " +
		    " ORDER BY year(date) ASC ", nativeQuery = true)
	List<Integer> findALLYear();
	
	
}
