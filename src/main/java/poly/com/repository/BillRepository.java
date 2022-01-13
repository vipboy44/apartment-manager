package poly.com.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import poly.com.entity.Bill;

public interface BillRepository extends JpaRepository<Bill, Integer>{
	
	@Query(value = "SELECT b.* " + 
			" FROM bills b INNER JOIN apartment_index a ON b.apartment_index_id = a.id " + 
			" WHERE month(a.date)  = ?1 and  year(a.date) = ?2 ", nativeQuery = true)
	List<Bill> findByMonth(int month, int year);
	
    List<Bill> findByPaid(Boolean paid);
    
}
