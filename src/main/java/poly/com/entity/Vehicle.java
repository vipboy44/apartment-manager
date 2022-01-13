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
@Table(name = "Vehicles")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vehicle implements Serializable {

    private static final long serialVersionUID = -7051941017444489454L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @Column( unique = true)
    private String licensePlates;

    @NotNull(message = " color vehicle can not be null" )
    @Column(length = 20)
    private String color;

    @NotNull
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd") //MM/dd/yyyy
    private Date date;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_resident" , referencedColumnName = "id")
    private Resident resident;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_typevehicle" , referencedColumnName = "id")
    private TypeVehicel typeVehicle;

 
}
