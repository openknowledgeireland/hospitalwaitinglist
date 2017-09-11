package ie.oki.util;

import ie.oki.enums.CaseType;
import ie.oki.enums.Classification;
import ie.oki.enums.CsvType;
import ie.oki.model.Hospital;
import ie.oki.model.HospitalGroup;
import ie.oki.model.Record;
import ie.oki.model.Speciality;
import lombok.Getter;

import java.util.Date;
import java.util.Random;

/**
 * @author Zoltan Toth
 */
@Getter
public final class TestUtil {

    private String dateAsString;
    private Date archivedDate;
    private String groupName;
    private int hospitalHipe;
    private String hospitalName;
    private int specialityHipe;
    private String specialityName;
    private CaseType caseType;
    private Classification classification;
    private int minimumAge;
    private int maximumAge;
    private int minimumWaitingTime;
    private int maximumWaitingTime;
    private int waiting;

    private Record record;
    private Hospital hospital;
    private HospitalGroup hospitalGroup;
    private Speciality speciality;

    public Record createRecord() {
        int random = new Random().nextInt(100000001);

        dateAsString = "2017-01-31";
        archivedDate = Utils.convertStringToDate(dateAsString);
        groupName = "Children's Hospital Group";
        hospitalHipe = random;
        hospitalName = "Childrens University Hospital Temple Street";
        specialityHipe = random;
        specialityName = "Cardiology";
        caseType = CaseType.DAY_CASE;
        classification = Classification.CHILD;
        minimumAge = 0;
        maximumAge = 15;
        minimumWaitingTime = 0;
        maximumWaitingTime = 3;
        waiting = 124;

        hospitalGroup = new HospitalGroup();
        hospitalGroup.setName(groupName);

        hospital = new Hospital();
        hospital.setHipe(hospitalHipe);
        hospital.setName(hospitalName);
        hospital.setHospitalGroup(hospitalGroup);

        speciality = new Speciality();
        speciality.setHipe(specialityHipe);
        speciality.setName(specialityName);

        record = new Record();
        record.setArchivedDate(archivedDate);
        record.setHospital(hospital);
        record.setSpeciality(speciality);
        record.setClassification(classification);
        record.setType(CsvType.OP);
        record.setMinimumAge(minimumAge);
        record.setMaximumAge(maximumAge);
        record.setMinimumWaitingTime(minimumWaitingTime);
        record.setMaximumWaitingTime(maximumWaitingTime);
        record.setWaiting(waiting);

        return record;
    }
}
