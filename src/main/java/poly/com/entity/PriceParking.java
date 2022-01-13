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
@Table(name = "PricesParking")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceParking implements Serializable {

	private static final long serialVersionUID = 2182751926313749043L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotNull
	@Min(value = 0, message = "Price must be greater than 0" )
	private Double price;

	@NotNull(message = "Date is not null")
	@Temporal(TemporalType.DATE)	 
  	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date date;

	@NotNull(message = "Employee is not null")
	@ManyToOne
	@JoinColumn(name = "id_employee", referencedColumnName = "id")
	private Employee employee;
	
	@NotNull(message = "Type is not null")
	@ManyToOne
	@JoinColumn(name = "id_type", referencedColumnName = "id")
	private TypeVehicel typeVehicel;
	
	@Size( max = 255, message = "The note length is less than or equal to 50 characters")
	private String note;
}
