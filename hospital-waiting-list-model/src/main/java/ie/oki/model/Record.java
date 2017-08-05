package ie.oki.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import ie.oki.enums.CaseType;
import ie.oki.enums.Classification;
import ie.oki.enums.CsvType;
import ie.oki.util.Constants;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * It represents a CSV row.
 *
 * @author Zoltan Toth
 */
@Data
@Entity
@Table(name = "records",
    indexes = {
        @Index(name = "archived_date_type_index", columnList = "archived_date, type"),
        @Index(name = "hospital_hipe_index", columnList = "hospital_hipe"),
        @Index(name = "speciality_hipe_index", columnList = "speciality_hipe")
    }
)
public class Record implements Serializable {

    private static final long serialVersionUID = -6315065898237986255L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(value = "Unique identifier of the record. This is an auto generated Id")
    private UUID id;

    @Temporal(TemporalType.DATE)
    @Column(name = "archived_date")
    @ApiModelProperty(value = "Archive date")
    @JsonFormat(pattern = Constants.DATE_FORMAT)
    private Date archivedDate;

    @ManyToOne(cascade = CascadeType.ALL)
    @ApiModelProperty(value = "Hospital object")
    @JoinColumn(name = "hospital_hipe", referencedColumnName = "hipe")
    private Hospital hospital;

    @ManyToOne(cascade = CascadeType.ALL)
    @ApiModelProperty(value = "Speciality object")
    @JoinColumn(name = "speciality_hipe", referencedColumnName = "hipe")
    private Speciality speciality;

    @Column(name = "case_type")
    @Enumerated(EnumType.STRING)
    private CaseType caseType;

    @Column
    private Classification classification;

    @Column
    @Enumerated(EnumType.STRING)
    @ApiModelProperty(value = "Type of the CSV file")
    private CsvType type;

    @Column
    private Integer minimumAge;

    @Column
    private Integer maximumAge;

    @Column
    private Integer minimumWaitingTime;

    @Column
    private Integer maximumWaitingTime;

    @Column
    private Integer waiting;

    public Date getArchivedDate() {
        return this.archivedDate == null ? null : new Date(this.archivedDate.getTime());
    }

    public void setArchivedDate(final Date input) {
        this.archivedDate = input == null ? null : new Date(input.getTime());
    }
}
