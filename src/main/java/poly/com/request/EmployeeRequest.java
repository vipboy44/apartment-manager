package poly.com.request;

import java.util.Date;
import java.util.Set;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRequest {

	@NotNull
	private int id;
	
	@NotBlank(message = "Fullname is not null!")
	@Size(max = 50)
	private String fullName;

    private boolean gender;

	@Temporal(TemporalType.DATE)	 
  	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

	@NotBlank()
	@Pattern(regexp = "[0-9]{9,12}", message = "Identitycard from 9 to 12 digits long!")
    private String identitycard;

	@NotBlank
	@Pattern(regexp = "[0-9]{9,11}", message = "Phone numbers from 9 to 11 digits long!")
    private String phone;
    
	@NotBlank(message = "Address is not null!")
    private String address;
    
	@NotBlank(message = "Email is not null!")
    private String email;

    private String image;
	
	@NotBlank
	@Size(min = 5, max = 20, message = "Username numbers from 5 to 20 characters!")
	private String username;
	
	@Size(min = 6 ,max = 12, message = "Password from 6 to 12 characters!")
	private String password;
	
	private Set<String> roles;
	
}
