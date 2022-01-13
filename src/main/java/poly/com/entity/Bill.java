package poly.com.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Bills")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bill implements Serializable {

	private static final long serialVersionUID = 8913859421655009503L;

	@Id
	@Column(name = "id")	
    private Integer id;
	
	@NotNull(message = "Electricity number is not null") 
	private Integer electricityNumber;
	
	@NotNull(message = "Electricity price is not null") 
	private Double electricityPrice1;
	
	@NotNull(message = "Electricity price is not null") 
	private Double electricityPrice2;
	
	@NotNull(message = "Electricity price is not null") 
	private Double electricityPrice3;
	
	@NotNull(message = "Electricity price is not null") 
	private Double electricityPrice4;
	
	@NotNull(message = "Electricity price is not null") 
	private Double electricityPrice5;
	
	@NotNull(message = "Electricity price is not null") 
	private Double electricityPrice6;
	
	@NotNull(message = "ElectricityPriceTotal is not null") 
	private Double electricityPriceTotal;
	
	@NotNull(message = "Water number is not null") 
	private Integer waterNumber;
	
	@NotNull(message = "Water price is not null") 
	private Double waterPrice;
	
	@NotNull(message = "Bicycle number is not null") 
	private Integer bicycleNumber;
	
	@NotNull(message = "Bicycle price is not null") 
	private Double bicyclePrice;
	
	@NotNull(message = "Motocycle number is not null") 
	private Integer motocycleNumber;
	
	@NotNull(message = "Motocycle price is not null") 
	private Double motocyclePrice;
	
	@NotNull(message = "Car number is not null") 
	private Integer carNumber;
	
	@NotNull(message = "Car price is not null") 
	private Double carPrice;
	
	@NotNull(message = "ParkingPriceTotal is not null") 
	private Double parkingPriceTotal;
	
	@NotNull(message = "Management fee is not null") 
	private Double managementPrice;
	
	@NotNull(message = "Management fee is not null") 
	private Double garbagesPrice;
	
	
	@NotNull(message = "Total price is not null") 
	private Integer totalPrice;

	@NotNull
	private Boolean paid;
	
	@OneToOne()
	@MapsId()
	private ApartmentIndex apartmentIndex;

	
}
