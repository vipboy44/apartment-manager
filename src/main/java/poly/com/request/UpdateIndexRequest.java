package poly.com.request;

import java.util.Date;


import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import poly.com.entity.Apartment;
import poly.com.entity.Employee;


@Getter
@Setter
public class UpdateIndexRequest extends CreateIndexRequest {
	
	@NotNull(message = "Bicycle number is not null!")
	private int bicycleNumber;
	
	@NotNull(message = "Motocycle number is not null!")
	private int motocycleNumber;
	
	@NotNull(message = "Car number is not null!")
	private int carNumber;
	
	@Builder
	public UpdateIndexRequest(Apartment apartment, int electricityNumber, int warterNumber, Date date,
			Employee employee, int bicycleNumber, int motocycleNumber, int carNumber) {
		super(apartment, electricityNumber, warterNumber, date, employee);
		this.bicycleNumber = bicycleNumber;
		this.motocycleNumber = motocycleNumber;
		this.carNumber = carNumber;
	}
	

}
