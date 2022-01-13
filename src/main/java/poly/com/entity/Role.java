package poly.com.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Roles")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role implements Serializable {

	private static final long serialVersionUID = -1412667887842228250L;

	@Id
	@GeneratedValue(strategy =  GenerationType.IDENTITY)
	private Integer id;
	
	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	@NotNull
	private ERole name;
	//
	@Column(length = 50)
	private String note;
	
}
