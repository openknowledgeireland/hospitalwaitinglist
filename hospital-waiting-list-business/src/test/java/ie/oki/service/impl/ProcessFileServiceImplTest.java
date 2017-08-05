package ie.oki.service.impl;

import ie.oki.enums.CaseType;
import ie.oki.enums.Classification;
import ie.oki.enums.CsvType;
import ie.oki.model.Hospital;
import ie.oki.model.HospitalGroup;
import ie.oki.model.Record;
import ie.oki.model.Speciality;
import ie.oki.repository.HospitalGroupRepository;
import ie.oki.repository.HospitalRepository;
import ie.oki.repository.SpecialityRepository;
import ie.oki.service.CommonService;
import ie.oki.service.RecordService;
import ie.oki.util.Utils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.BeanUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static org.mockito.Mockito.*;

/**
 * @author Zoltan Toth
 */
public class ProcessFileServiceImplTest {

    @Mock
    private RecordService recordService;

    @Mock
    private CommonService commonService;

    @Mock
    private HospitalRepository hospitalRepository;

    @Mock
    private HospitalGroupRepository hospitalGroupRepository;

    @Mock
    private SpecialityRepository specialityRepository;

    @InjectMocks
    private ProcessFileServiceImpl processFileServiceImpl;

    private String opLineAsString;
    private String ipdcLineAsString;

    private InputStream opLineAsStream;
    private InputStream ipdcLineAsStream;

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

    private Record recordOp;
    private Record recordIpdc;
    private Hospital hospital;
    private HospitalGroup hospitalGroup;
    private Speciality speciality;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        dateAsString = "2017-01-31";
        archivedDate = Utils.convertStringToDate(dateAsString);
        groupName = "Children's Hospital Group";
        hospitalHipe = 940;
        hospitalName = "Childrens University Hospital Temple Street";
        specialityHipe = 100;
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
        hospital.setGroup(hospitalGroup);

        speciality = new Speciality();
        speciality.setHipe(specialityHipe);
        speciality.setName(specialityName);

        recordOp = new Record();
        recordOp.setArchivedDate(archivedDate);
        recordOp.setHospital(hospital);
        recordOp.setSpeciality(speciality);
        recordOp.setClassification(classification);
        recordOp.setType(CsvType.OP);
        recordOp.setMinimumAge(minimumAge);
        recordOp.setMaximumAge(maximumAge);
        recordOp.setMinimumWaitingTime(minimumWaitingTime);
        recordOp.setMaximumWaitingTime(maximumWaitingTime);
        recordOp.setWaiting(waiting);

        recordIpdc = new Record();

        BeanUtils.copyProperties(recordOp, recordIpdc);

        recordIpdc.setCaseType(caseType);
        recordIpdc.setType(CsvType.IPDC);

        opLineAsString = "Archive Date,Group,Hospital HIPE,Hospital,Specialty HIPE,Speciality,Adult/Child,Age Profile,Time Bands,Total\n"
            + dateAsString + "," + groupName + ",0" + hospitalHipe + "," + hospitalName + ",0" + specialityHipe + "," + specialityName + ","
            + classification.name() + ", " + minimumAge + "-" + maximumAge + ", "
            + minimumWaitingTime + "-" + maximumWaitingTime + " Months," + waiting;

        opLineAsStream = new ByteArrayInputStream(opLineAsString.getBytes(StandardCharsets.UTF_8));

        ipdcLineAsString = "Archive Date,Group,Hospital HIPE,Hospital,Specialty HIPE,Speciality,Case Type,Adult/Child,Age Profile,Time Bands,Total\n"
            + dateAsString + "," + groupName + ",0" + hospitalHipe + "," + hospitalName + ",0" + specialityHipe + "," + specialityName + ","
            + caseType.getValue() + "," + classification + "," + minimumAge + "-" + maximumAge + ", "
            + minimumWaitingTime + "-" + maximumWaitingTime + " Months," + waiting;

        ipdcLineAsStream = new ByteArrayInputStream(ipdcLineAsString.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void testReadFile_ioException() {
        opLineAsStream = new ByteArrayInputStream("".getBytes(StandardCharsets.UTF_8));

        processFileServiceImpl.readFile(opLineAsStream, CsvType.OP);

        verifyZeroInteractions(recordService);
        verify(commonService).clearAllCaches();
    }

    @Test
    public void testReadOpFile_columnLengthDoesntMatch() {
        opLineAsString = "Archive Date,Group,Hospital HIPE,Hospital,Specialty HIPE,Speciality,Adult/Child,Age Profile,Time Bands,Total\n"
            + dateAsString + "," + groupName + ",0" + hospitalHipe + "," + hospitalName + ",0" + specialityHipe + "," + specialityName + ","
            + classification.name() + ", " + minimumAge + "-" + maximumAge + ", "
            + minimumWaitingTime + "-" + maximumWaitingTime + " Months";

        opLineAsStream = new ByteArrayInputStream(opLineAsString.getBytes(StandardCharsets.UTF_8));

        processFileServiceImpl.readFile(opLineAsStream, CsvType.OP);

        verifyZeroInteractions(recordService);
        verify(commonService).clearAllCaches();
    }

    @Test
    public void testReadOpFile_lineAlreadyProcessed() {
        when(recordService.isRowAlreadyProcessed(archivedDate, CsvType.OP)).thenReturn(true);

        processFileServiceImpl.readFile(opLineAsStream, CsvType.OP);

        verify(recordService).isRowAlreadyProcessed(archivedDate, CsvType.OP);
        verifyNoMoreInteractions(recordService);
        verify(commonService).clearAllCaches();
    }

    @Test
    public void testReadOpFile_successfullyProcessed() {
        when(recordService.isRowAlreadyProcessed(archivedDate, CsvType.OP)).thenReturn(false);

        processFileServiceImpl.readFile(opLineAsStream, CsvType.OP);

        verify(recordService).isRowAlreadyProcessed(archivedDate, CsvType.OP);
        verify(recordService).save(recordOp);
        verify(commonService).clearAllCaches();
    }

    @Test
    public void testReadOpFile_wronglyFormattedAge() {
        opLineAsString = "Archive Date,Group,Hospital HIPE,Hospital,Specialty HIPE,Speciality,Adult/Child,Age Profile,Time Bands,Total\n"
            + dateAsString + "," + groupName + ",0" + hospitalHipe + "," + hospitalName + ",0" + specialityHipe + "," + specialityName + ","
            + classification.name() + ", " + "WRONG_AGE" + ", "
            + minimumWaitingTime + "-" + maximumWaitingTime + " Months," + waiting;

        opLineAsStream = new ByteArrayInputStream(opLineAsString.getBytes(StandardCharsets.UTF_8));

        when(recordService.isRowAlreadyProcessed(archivedDate, CsvType.OP)).thenReturn(false);

        processFileServiceImpl.readFile(opLineAsStream, CsvType.OP);

        recordOp.setMinimumAge(null);
        recordOp.setMaximumAge(null);

        verify(recordService).isRowAlreadyProcessed(archivedDate, CsvType.OP);
        verify(recordService).save(recordOp);
        verify(commonService).clearAllCaches();
    }

    @Test
    public void testReadOpFile_wronglyFormattedTimeBand() {
        opLineAsString = "Archive Date,Group,Hospital HIPE,Hospital,Specialty HIPE,Speciality,Adult/Child,Age Profile,Time Bands,Total\n"
            + dateAsString + "," + groupName + ",0" + hospitalHipe + "," + hospitalName + ",0" + specialityHipe + "," + specialityName + ","
            + classification.name() + ", " + minimumAge + "-" + maximumAge + ", "
            + "WRONG_BAND" + " Months," + waiting;

        opLineAsStream = new ByteArrayInputStream(opLineAsString.getBytes(StandardCharsets.UTF_8));

        when(recordService.isRowAlreadyProcessed(archivedDate, CsvType.OP)).thenReturn(false);

        processFileServiceImpl.readFile(opLineAsStream, CsvType.OP);

        recordOp.setMinimumWaitingTime(null);
        recordOp.setMaximumWaitingTime(null);

        verify(recordService).isRowAlreadyProcessed(archivedDate, CsvType.OP);
        verify(recordService).save(recordOp);
        verify(commonService).clearAllCaches();
    }

    @Test
    public void testReadOpFile_successfullyProcessed_hospitalFound() {
        hospital.setName("customName");

        when(recordService.isRowAlreadyProcessed(archivedDate, CsvType.OP)).thenReturn(false);
        when(hospitalRepository.findOne(hospitalHipe)).thenReturn(hospital);

        processFileServiceImpl.readFile(opLineAsStream, CsvType.OP);

        verify(recordService).isRowAlreadyProcessed(archivedDate, CsvType.OP);
        verify(recordService).save(recordOp);
        verify(commonService).clearAllCaches();
    }

    @Test
    public void testReadOpFile_successfullyProcessed_hospitalGroupFound() {
        hospitalGroup.setName("customGroupName");

        when(recordService.isRowAlreadyProcessed(archivedDate, CsvType.OP)).thenReturn(false);
        when(hospitalGroupRepository.findOne(groupName)).thenReturn(hospitalGroup);

        processFileServiceImpl.readFile(opLineAsStream, CsvType.OP);

        verify(recordService).isRowAlreadyProcessed(archivedDate, CsvType.OP);
        verify(recordService).save(recordOp);
        verify(commonService).clearAllCaches();
    }

    @Test
    public void testReadOpFile_successfullyProcessed_specialityFound() {
        speciality.setName("customSpecialityName");

        when(recordService.isRowAlreadyProcessed(archivedDate, CsvType.OP)).thenReturn(false);
        when(specialityRepository.findOne(specialityHipe)).thenReturn(speciality);

        processFileServiceImpl.readFile(opLineAsStream, CsvType.OP);

        verify(recordService).isRowAlreadyProcessed(archivedDate, CsvType.OP);
        verify(recordService).save(recordOp);
        verify(commonService).clearAllCaches();
    }

    /**
     * Since the hospitalHipe is invalid in the incoming message, it will be set as -1 in the record
     * and that's what we test here.
     */
    @Test
    public void testReadOpFile_successfullyProcessed_invalidHospitalHipe() {
        opLineAsString = "Archive Date,Group,Hospital HIPE,Hospital,Specialty HIPE,Speciality,Adult/Child,Age Profile,Time Bands,Total\n"
            + dateAsString + "," + groupName + "," + "NULL" + "," + hospitalName + ",0" + specialityHipe + "," + specialityName + ","
            + classification.name() + ", " + minimumAge + "-" + maximumAge + ", "
            + minimumWaitingTime + "-" + maximumWaitingTime + " Months," + waiting;

        opLineAsStream = new ByteArrayInputStream(opLineAsString.getBytes(StandardCharsets.UTF_8));

        when(recordService.isRowAlreadyProcessed(archivedDate, CsvType.OP)).thenReturn(false);

        processFileServiceImpl.readFile(opLineAsStream, CsvType.OP);

        verify(recordService).isRowAlreadyProcessed(archivedDate, CsvType.OP);

        recordOp.getHospital().setHipe(-1);

        verify(recordService).save(recordOp);
        verify(commonService).clearAllCaches();
    }

    /**
     * Since the specialityHipe is invalid in the incoming message, it will be set as 9000 in the record
     * and that's what we test here.
     * The reason it's set to 9000, because for some entries it's set to that value, rather than NULL, so I went with consistency.
     */
    @Test
    public void testReadOpFile_successfullyProcessed_invalidSpecialityHipe() {
        opLineAsString = "Archive Date,Group,Hospital HIPE,Hospital,Specialty HIPE,Speciality,Adult/Child,Age Profile,Time Bands,Total\n"
            + dateAsString + "," + groupName + ",0" + hospitalHipe + "," + hospitalName + "," + "NULL" + "," + specialityName + ","
            + classification.name() + ", " + minimumAge + "-" + maximumAge + ", "
            + minimumWaitingTime + "-" + maximumWaitingTime + " Months," + waiting;

        opLineAsStream = new ByteArrayInputStream(opLineAsString.getBytes(StandardCharsets.UTF_8));

        when(recordService.isRowAlreadyProcessed(archivedDate, CsvType.OP)).thenReturn(false);

        processFileServiceImpl.readFile(opLineAsStream, CsvType.OP);

        verify(recordService).isRowAlreadyProcessed(archivedDate, CsvType.OP);

        recordOp.getSpeciality().setHipe(9000);

        verify(recordService).save(recordOp);
        verify(commonService).clearAllCaches();
    }

    @Test
    public void testReadOpIpdcFile_columnLengthDoesntMatch() {
        ipdcLineAsString = "Archive Date,Group,Hospital HIPE,Hospital,Specialty HIPE,Speciality,Case Type,Adult/Child,Age Profile,Time Bands,Total\n"
            + dateAsString + "," + groupName + ",0" + hospitalHipe + "," + hospitalName + ",0" + specialityHipe + "," + specialityName + ","
            + caseType.getValue() + "," + classification + "," + minimumAge + "-" + maximumAge + ", "
            + minimumWaitingTime + "-" + maximumWaitingTime + " Months";

        ipdcLineAsStream = new ByteArrayInputStream(ipdcLineAsString.getBytes(StandardCharsets.UTF_8));

        processFileServiceImpl.readFile(ipdcLineAsStream, CsvType.IPDC);

        verifyZeroInteractions(recordService);
        verify(commonService).clearAllCaches();
    }

    @Test
    public void testReadIpdcFile_lineAlreadyProcessed() {
        when(recordService.isRowAlreadyProcessed(archivedDate, CsvType.IPDC)).thenReturn(true);

        processFileServiceImpl.readFile(ipdcLineAsStream, CsvType.IPDC);

        verify(recordService).isRowAlreadyProcessed(archivedDate, CsvType.IPDC);
        verifyNoMoreInteractions(recordService);
        verify(commonService).clearAllCaches();
    }

    @Test
    public void testReadIpdcFile_successfullyProcessed() {
        when(recordService.isRowAlreadyProcessed(archivedDate, CsvType.IPDC)).thenReturn(false);

        processFileServiceImpl.readFile(ipdcLineAsStream, CsvType.IPDC);

        verify(recordService).isRowAlreadyProcessed(archivedDate, CsvType.IPDC);
        verify(recordService).save(recordIpdc);
        verify(commonService).clearAllCaches();
    }

    @Test
    public void testReadIpdcFile_wronglyFormattedAge() {
        ipdcLineAsString = "Archive Date,Group,Hospital HIPE,Hospital,Specialty HIPE,Speciality,Adult/Child,Age Profile,Time Bands,Total\n"
            + dateAsString + "," + groupName + ",0" + hospitalHipe + "," + hospitalName + ",0" + specialityHipe + "," + specialityName + ","
            + caseType.getValue() + "," + classification.name() + ", " + "WRONG_AGE" + ", "
            + minimumWaitingTime + "-" + maximumWaitingTime + " Months," + waiting;

        ipdcLineAsStream = new ByteArrayInputStream(ipdcLineAsString.getBytes(StandardCharsets.UTF_8));

        when(recordService.isRowAlreadyProcessed(archivedDate, CsvType.IPDC)).thenReturn(false);

        processFileServiceImpl.readFile(ipdcLineAsStream, CsvType.IPDC);

        recordIpdc.setMinimumAge(null);
        recordIpdc.setMaximumAge(null);

        verify(recordService).isRowAlreadyProcessed(archivedDate, CsvType.IPDC);
        verify(recordService).save(recordIpdc);
        verify(commonService).clearAllCaches();
    }

    @Test
    public void testReadIpdcFile_wronglyFormattedTimeBand() {
        ipdcLineAsString = "Archive Date,Group,Hospital HIPE,Hospital,Specialty HIPE,Speciality,Adult/Child,Age Profile,Time Bands,Total\n"
            + dateAsString + "," + groupName + ",0" + hospitalHipe + "," + hospitalName + ",0" + specialityHipe + "," + specialityName + ","
            + caseType.getValue() + "," + classification.name() + ", " + minimumAge + "-" + maximumAge + ", "
            + "WRONG_BAND" + " Months," + waiting;

        ipdcLineAsStream = new ByteArrayInputStream(ipdcLineAsString.getBytes(StandardCharsets.UTF_8));

        when(recordService.isRowAlreadyProcessed(archivedDate, CsvType.IPDC)).thenReturn(false);

        processFileServiceImpl.readFile(ipdcLineAsStream, CsvType.IPDC);

        recordIpdc.setMinimumWaitingTime(null);
        recordIpdc.setMaximumWaitingTime(null);

        verify(recordService).isRowAlreadyProcessed(archivedDate, CsvType.IPDC);
        verify(recordService).save(recordIpdc);
        verify(commonService).clearAllCaches();
    }

    /**
     * Since the hospitalHipe is invalid in the incoming message, it will be set as -1 in the record
     * and that's what we test here.
     */
    @Test
    public void testReadIpdcFile_successfullyProcessed_invalidHospitalHipe() {
        ipdcLineAsString = "Archive Date,Group,Hospital HIPE,Hospital,Specialty HIPE,Speciality,Case Type,Adult/Child,Age Profile,Time Bands,Total\n"
            + dateAsString + "," + groupName + ",0" + "NULL" + "," + hospitalName + ",0" + specialityHipe + "," + specialityName + ","
            + caseType.getValue() + "," + classification + "," + minimumAge + "-" + maximumAge + ", "
            + minimumWaitingTime + "-" + maximumWaitingTime + " Months," + waiting;

        ipdcLineAsStream = new ByteArrayInputStream(ipdcLineAsString.getBytes(StandardCharsets.UTF_8));

        when(recordService.isRowAlreadyProcessed(archivedDate, CsvType.IPDC)).thenReturn(false);

        processFileServiceImpl.readFile(ipdcLineAsStream, CsvType.IPDC);

        verify(recordService).isRowAlreadyProcessed(archivedDate, CsvType.IPDC);

        recordIpdc.getHospital().setHipe(-1);

        verify(recordService).save(recordIpdc);
        verify(commonService).clearAllCaches();
    }

    /**
     * Since the specialityHipe is invalid in the incoming message, it will be set as 9000 in the record
     * and that's what we test here.
     * The reason it's set to 9000, because for some entries it's set to that value, rather than NULL, so I went with consistency.
     */
    @Test
    public void testReadIpdcFile_successfullyProcessed_invalidSpecialityHipe() {
        ipdcLineAsString = "Archive Date,Group,Hospital HIPE,Hospital,Specialty HIPE,Speciality,Case Type,Adult/Child,Age Profile,Time Bands,Total\n"
            + dateAsString + "," + groupName + ",0" + hospitalHipe + "," + hospitalName + ",0" + "NULL" + "," + specialityName + ","
            + caseType.getValue() + "," + classification + "," + minimumAge + "-" + maximumAge + ", "
            + minimumWaitingTime + "-" + maximumWaitingTime + " Months," + waiting;

        ipdcLineAsStream = new ByteArrayInputStream(ipdcLineAsString.getBytes(StandardCharsets.UTF_8));

        when(recordService.isRowAlreadyProcessed(archivedDate, CsvType.IPDC)).thenReturn(false);

        processFileServiceImpl.readFile(ipdcLineAsStream, CsvType.IPDC);

        verify(recordService).isRowAlreadyProcessed(archivedDate, CsvType.IPDC);

        recordIpdc.getSpeciality().setHipe(9000);

        verify(recordService).save(recordIpdc);
        verify(commonService).clearAllCaches();
    }

}
