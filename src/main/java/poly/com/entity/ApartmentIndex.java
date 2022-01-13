package poly.com.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ApartmentIndex")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApartmentIndex implements Serializable {
	
	private static final long serialVersionUID = 8153674086528799765L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@NotNull(message = "Electricity number is not null") 
	private Integer newElectricityNumber;
	
	@NotNull(message = "Water number is not null") 
	private Integer newWaterNumber;
	
	@NotNull(message = "Bicycle number is not null") 
	private Integer bicycleNumber;
	
	@NotNull(message = "Motocycle number is not null") 
	private Integer motocycleNumber;
	
	@NotNull(message = "Car number is not null") 
	private Integer carNumber;
	
	@NotNull(message = "Birthday is not null") 
  	@Temporal(TemporalType.DATE)	 
  	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "id_apartment", referencedColumnName = "id")
	private Apartment apartment;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "id_employee", referencedColumnName = "id")
	private Employee employee;
	
	
}
