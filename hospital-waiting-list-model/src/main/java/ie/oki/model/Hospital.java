package ie.oki.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Zoltan Toth
 */
@Data
@Entity
@Table(name = "hospitals")
public class Hospital implements Serializable {

    private static final long serialVersionUID = 9098602048031278219L;

    @Id
    @Column(unique = true, nullable = false)
    @ApiModelProperty(value = "Unique identifier of the hospital")
    private Integer hipe;

    @Column
    @ApiModelProperty(value = "Name of the hospital")
    private String name;

    @ManyToOne(cascade = CascadeType.ALL)
    @ApiModelProperty(value = "Hospital group name")
    @JoinColumn(name = "hospital_group_name", referencedColumnName = "name")
    private HospitalGroup group;
}
