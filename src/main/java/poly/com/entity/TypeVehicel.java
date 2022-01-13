package poly.com.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TypeVehicels")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TypeVehicel implements Serializable {

	private static final long serialVersionUID = 7800400832966420294L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(unique = true)
	@NotNull
	@Size( max = 20, message = "The name length is less than or equal to 20 characters")
	private String name;

	@Size( max = 255, message = "The note length is less than or equal to 50 characters")
	private String note;
	
}
