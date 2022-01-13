package poly.com.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import poly.com.entity.Vehicle;

@Repository
public interface VehicleRespository extends JpaRepository<Vehicle, Integer> {

	
    Optional<Vehicle> findByLicensePlates(String licensePlates );

    @Query(value = "SELECT count(v.id) as i " + 
    		"FROM  vehicles v " + 
    		"		INNER JOIN residents r  ON v.id_resident = r.id " + 
    		"       INNER JOIN apartments a  ON r.id_apartment = a.id " +
    		"WHERE a.id =?1 and v.id_typevehicle =?2 ", nativeQuery = true)

    Integer findTotalVehicelByType(String name , int id);
    
    
   
    

}
