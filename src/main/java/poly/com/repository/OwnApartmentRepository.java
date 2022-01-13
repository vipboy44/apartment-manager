package poly.com.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import poly.com.entity.OwnApartment;

public interface OwnApartmentRepository extends JpaRepository<OwnApartment, Integer> {

	 Boolean existsByPhone(String phone);
	
	 Boolean existsByIdentitycard(String identitycard);
	 
	 Optional<OwnApartment> findByPhone(String phone);
	 
	 Optional<OwnApartment> findByIdentitycard(String identitycard);
	 
	 @Query("SELECT coalesce(max(own.id), 0) FROM OwnApartment own")
	 Integer getMaxId();
	 
	 
}
