package poly.com.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import poly.com.entity.Resident;

@Repository
public interface ResidentRepository extends JpaRepository <Resident,Integer> {

    Optional<Resident> findByIdentitycard(String identitycard);
    @Query("select a.id from Resident a  ")
    List<String> findAllId();
}
