package poly.com.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import poly.com.entity.Apartment;
import poly.com.entity.OwnApartment;


public interface ApartmentRepository extends JpaRepository<Apartment, String> {

	@Query("select a.id from Apartment a where a.ownApartment = ?1")
	List<String> findIds(OwnApartment own);
	@Query("select a.id from Apartment a ")
	List<String> findAllId();
}
