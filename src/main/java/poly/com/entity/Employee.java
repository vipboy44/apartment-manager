package poly.com.entity;


import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;

import lombok.NoArgsConstructor;

@Entity
@Table(name = "Employees")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee implements Serializable {

	private static final long serialVersionUID = 4548470369384115251L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

    @NotNull(message = "Fullname can  not  be null ")
    @Column(length = 50)
	private String fullName;

    @NotNull(message = "Gender can not be  null ")
    private Boolean gender;

    @NotNull(message = "Birthday can not be null")
  	@Temporal(TemporalType.DATE)	 
  	@DateTimeFormat(pattern = "yyyy-MM-dd") //MM/dd/yyyy
    private Date birthday;

    @NotNull()
    @Column(length = 12, unique = true)
    @Pattern(regexp = "[0-9]{9,12}", message = "Identitycard from 9 to 12 digits long")
    private String identitycard;

    @NotNull
    @Column(length = 11 )
    @Pattern(regexp = "[0-9]{9,11}", message = "Phone numbers from 9 to 11 digits long")
    private String phone;
    
    @NotNull(message = "Address can not be null ")
    private String address;
    
    @NotNull(message = "Email can not be null ")
    private String email;

    @Size( max = 50, message = "The image length is less than or equal to 50 characters")
    private String image;
    
    @NotNull
	@Column(unique = true, length = 20)
    @Size(min = 5, max = 20, message = "Username numbers from 5 to 20 characters")
	private String username;
	
	@NotNull
	@Column(length = 120)
	private String password;// no set size
	
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_roles",//user_roles bảng trung gian
					joinColumns = @JoinColumn(name= "userId"),
					inverseJoinColumns = @JoinColumn(name="roleId"))
	private Set<Role> roles = new HashSet<>();

}

