package poly.com.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "PricesManagement")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceManagement implements Serializable {
	
	private static final long serialVersionUID = -235170374690314904L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id; 

	@NotNull
	@Min(value = 0, message = "Price must be greater than 0" )
	private Double price;
	
	@Column(unique =  true)
	@NotNull(message = "Date is not null")
	@Temporal(TemporalType.DATE)	 
  	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date date;

	
	@ManyToOne
	@JoinColumn(name = "id_employee", referencedColumnName = "id")
	@NotNull(message = "Employee is not null")
	private Employee employee;

	@Size( max = 255, message = "The note length is less than or equal to 50 characters")
	private String note;
}
