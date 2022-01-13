package poly.com.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import poly.com.entity.TypeVehicel;

public interface TypeVehicelRepository extends JpaRepository<TypeVehicel, Integer> {

	Boolean existsByName(String name);
		
	Optional<TypeVehicel> findByName(String name);
}
