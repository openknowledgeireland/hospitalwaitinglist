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
@Table(name = "hospital_groups")
public class HospitalGroup implements Serializable {

    private static final long serialVersionUID = -269877932495661821L;

    @Id
    @Column
    @ApiModelProperty(value = "Name of the hospital group")
    private String name;
}
