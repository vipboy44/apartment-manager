package poly.com.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import poly.com.entity.ERole;
import poly.com.entity.Role;

public interface RoleRepository  extends JpaRepository<Role, Integer>{

	Optional<Role>  findByName(ERole name);
	
}
