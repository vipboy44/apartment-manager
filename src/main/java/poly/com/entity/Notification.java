package poly.com.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "Notifications")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Title is not null")
    private String title;

    
    @Column(length = 20000)
    @NotNull(message = "Content is not null")
    private String content;
    
    @NotNull(message = "Description is not null")
    @Column(length = 1000)
    private String description;

    private String image;

    @NotNull
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;

    @NotNull(message = "Employee is not null")
    @ManyToOne
    @JoinColumn(name = "id_employee", referencedColumnName = "id")
    private Employee employee;
}
