package ie.oki.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author Zoltan Toth
 */
@Data
@Entity
@Table(name = "specialities")
public class Speciality implements Serializable {

    private static final long serialVersionUID = 6861416955143048548L;

    @Id
    @Column(unique = true, nullable = false)
    @ApiModelProperty(value = "Unique identifier of the speciality")
    private Integer hipe;

    @Column(nullable = false)
    @ApiModelProperty(value = "Name of the speciality")
    private String name;
}
