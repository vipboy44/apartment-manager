package poly.com.dto;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OwnApartmentDTO {

	private Integer id;
	
	@NotBlank(message = "Fullname is not null!")
	private String fullname;
	
	private Boolean gender;
	
	@NotBlank(message = "Hometown is not null!")
	private String homeTown;
	
	@NotBlank(message = "Phone is not null!")
	private String phone;
	
	@NotBlank(message = "Fullname is not null!")
    private String email;
    
    private Date birthday;
    
    @NotBlank(message = "Job is not null!")
    private String job;
        
    @NotBlank(message = "Identitycard is not null!")
    private String identitycard;
    
    private List<String> apartments;
   
	
}
