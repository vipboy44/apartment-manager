package poly.com.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "OwnApartments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OwnApartment implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotNull(message = "Name is not null")
	@Column(length = 50)
	private String fullname;

	@NotNull(message = "Gender is not null")
	private Boolean gender;

	@NotNull(message = "Birthday is not null") 
	//@Past() time quá khứ
  	@Temporal(TemporalType.DATE)	 
  	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;
	
	@NotNull(message = "Job is not null")
	@Column(length = 50)
	private String job;

	@Column(length = 11)
	@NotNull
	@Pattern(regexp = "[0-9]{9,11}", message = "Phone numbers from 9 to 11 digits long")
	private String phone;
	
	@NotNull(message = "Email town is not null")
	@Size( max = 50, message = "The email length is less than or equal to 50 characters")
	private String email;
	
	@NotNull(message = "Home town is not null")
	@Size( max = 50, message = "The home town length is less than or equal to 50 characters")
	private String homeTown;

	@NotNull()
	@Column(length = 12, unique = true)
	@Pattern(regexp = "[0-9]{9,12}", message = "Identitycard from 9 to 12 digits long")
	private String identitycard;
	
}
