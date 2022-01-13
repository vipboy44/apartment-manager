package poly.com.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Apartments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Apartment implements Serializable {

	private static final long serialVersionUID = 7572562617485997180L;

	@Id
	@Column(length = 8, unique = true)
	private String id;
	
	@NotNull(message = "Password is not null") 
	@Column(length = 120)
	private String password;
	
	@NotNull(message = "Acreage is not null") 
	private Integer acreage;
	
	@NotNull(message = "Location is not null") 
	@Column(length = 50)
	private String location;
	
	private String note;
	
	@ManyToOne
	@JoinColumn(name = "id_own", referencedColumnName = "id")
	private OwnApartment ownApartment;

	
	
	
	
}
