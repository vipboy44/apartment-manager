package poly.com.security;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import poly.com.entity.Employee;
import poly.com.entity.Role;
import poly.com.repository.EmployeeRepository;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    /* ------------------------------------- Class EmployeeDetailService -------------------------------------*/
    @Autowired
    EmployeeRepository employeeRepository;

    /*------------- Load employee by username------------------------ */
 
    @Override
    public UserDetails loadUserByUsername(String username) {
        Employee employee = employeeRepository.findByUsername(username).orElse(null);
        if (employee == null) 
        	 throw new UsernameNotFoundException(username);
        
        if (employee.getRoles().size() == 0)  
            	throw new UsernameNotFoundException(username); 	      
              
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        Set<Role> roles = employee.getRoles();
        
        for (Role role : roles) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()+""));
        }
        
        return new org.springframework.security.core.userdetails.User(
        		employee.getUsername(), employee.getPassword(), grantedAuthorities);
    }

}