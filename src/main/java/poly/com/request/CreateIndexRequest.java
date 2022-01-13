package poly.com.request;

import java.util.Date;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import poly.com.entity.Apartment;
import poly.com.entity.Employee;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateIndexRequest {
	
	private Apartment apartment;

	@NotNull(message = "Electricity number is not null!")
	private int electricityNumber;
	
	@NotNull(message = "Water number is not null!")
	private int warterNumber;
	
	private Date date;
	
	@NotNull(message = "Employee is not null!")
	private Employee employee;
 
}
