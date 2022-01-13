package poly.com.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Regulations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Regulation implements Serializable {
	
	private static final long serialVersionUID = 2594565013703029436L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id; 
	
	@Column(length = 20000)
	@NotNull
	private String content;
	
}
