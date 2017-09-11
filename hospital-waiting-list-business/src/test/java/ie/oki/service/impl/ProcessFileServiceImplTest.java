package ie.oki.service.impl;

import ie.oki.enums.CsvType;
import ie.oki.model.Record;
import ie.oki.repository.HospitalGroupRepository;
import ie.oki.repository.HospitalRepository;
import ie.oki.repository.SpecialityRepository;
import ie.oki.service.CommonService;
import ie.oki.service.RecordService;
import ie.oki.util.TestUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.BeanUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.*;

/**
 * @author Zoltan Toth
 */
@RunWith(MockitoJUnitRunner.class)
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

    private Record recordOp;
    private Record recordIpdc;

    private TestUtil testUtil;

    @Before
    public void setup() {
        testUtil = new TestUtil();

        recordOp = testUtil.createRecord();
        recordIpdc = new Record();

        BeanUtils.copyProperties(recordOp, recordIpdc);

        recordIpdc.setCaseType(testUtil.getCaseType());
        recordIpdc.setType(CsvType.IPDC);

        opLineAsString = "Archive Date,Group,Hospital HIPE,Hospital,Specialty HIPE,Speciality,Adult/Child,Age Profile,Time Bands,Total\n"
            + testUtil.getDateAsString() + "," + testUtil.getGroupName() + ",0" + testUtil.getHospitalHipe() + "," + testUtil.getHospitalName() + ",0"
            + testUtil.getSpecialityHipe() + "," + testUtil.getSpecialityName() + ","
            + testUtil.getClassification().name() + ", " + testUtil.getMinimumAge() + "-" + testUtil.getMaximumAge() + ", "
            + testUtil.getMinimumWaitingTime() + "-" + testUtil.getMaximumWaitingTime() + " Months," + testUtil.getWaiting();

        opLineAsStream = new ByteArrayInputStream(opLineAsString.getBytes(StandardCharsets.UTF_8));

        ipdcLineAsString = "Archive Date,Group,Hospital HIPE,Hospital,Specialty HIPE,Speciality,Case Type,Adult/Child,Age Profile,Time Bands,Total\n"
            + testUtil.getDateAsString() + "," + testUtil.getGroupName() + ",0" + testUtil.getHospitalHipe() + "," + testUtil.getHospitalName() + ",0"
            + testUtil.getSpecialityHipe() + "," + testUtil.getSpecialityName() + ","
            + testUtil.getCaseType().getValue() + "," + testUtil.getClassification() + "," + testUtil.getMinimumAge() + "-"
            + testUtil.getMaximumAge() + ", " + testUtil.getMinimumWaitingTime() + "-"
            + testUtil.getMaximumWaitingTime() + " Months," + testUtil.getWaiting();

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
            + testUtil.getDateAsString() + "," + testUtil.getGroupName() + ",0" + testUtil.getHospitalHipe() + "," + testUtil.getHospitalName() + ",0"
            + testUtil.getSpecialityHipe() + "," + testUtil.getSpecialityName() + ","
            + testUtil.getClassification().name() + ", " + testUtil.getMinimumAge() + "-" + testUtil.getMaximumAge() + ", "
            + testUtil.getMinimumWaitingTime() + "-" + testUtil.getMaximumWaitingTime() + " Months";

        opLineAsStream = new ByteArrayInputStream(opLineAsString.getBytes(StandardCharsets.UTF_8));

        processFileServiceImpl.readFile(opLineAsStream, CsvType.OP);

        verifyZeroInteractions(recordService);
        verify(commonService).clearAllCaches();
    }

    @Test
    public void testReadOpFile_lineAlreadyProcessed() {
        when(recordService.isRowAlreadyProcessed(testUtil.getArchivedDate(), CsvType.OP)).thenReturn(true);

        processFileServiceImpl.readFile(opLineAsStream, CsvType.OP);

        verify(recordService).isRowAlreadyProcessed(testUtil.getArchivedDate(), CsvType.OP);
        verifyNoMoreInteractions(recordService);
        verify(commonService).clearAllCaches();
    }

    @Test
    public void testReadOpFile_successfullyProcessed() {
        when(recordService.isRowAlreadyProcessed(testUtil.getArchivedDate(), CsvType.OP)).thenReturn(false);

        processFileServiceImpl.readFile(opLineAsStream, CsvType.OP);

        verify(recordService).isRowAlreadyProcessed(testUtil.getArchivedDate(), CsvType.OP);
        verify(recordService).save(recordOp);
        verify(commonService).clearAllCaches();
    }

    @Test
    public void testReadOpFile_wronglyFormattedAge() {
        opLineAsString = "Archive Date,Group,Hospital HIPE,Hospital,Specialty HIPE,Speciality,Adult/Child,Age Profile,Time Bands,Total\n"
            + testUtil.getDateAsString() + "," + testUtil.getGroupName() + ",0" + testUtil.getHospitalHipe() + "," + testUtil.getHospitalName() + ",0"
            + testUtil.getSpecialityHipe() + "," + testUtil.getSpecialityName() + ","
            + testUtil.getClassification().name() + ", " + "WRONG_AGE" + ", "
            + testUtil.getMinimumWaitingTime() + "-" + testUtil.getMaximumWaitingTime() + " Months," + testUtil.getWaiting();

        opLineAsStream = new ByteArrayInputStream(opLineAsString.getBytes(StandardCharsets.UTF_8));

        when(recordService.isRowAlreadyProcessed(testUtil.getArchivedDate(), CsvType.OP)).thenReturn(false);

        processFileServiceImpl.readFile(opLineAsStream, CsvType.OP);

        recordOp.setMinimumAge(null);
        recordOp.setMaximumAge(null);

        verify(recordService).isRowAlreadyProcessed(testUtil.getArchivedDate(), CsvType.OP);
        verify(recordService).save(recordOp);
        verify(commonService).clearAllCaches();
    }

    @Test
    public void testReadOpFile_wronglyFormattedTimeBand() {
        opLineAsString = "Archive Date,Group,Hospital HIPE,Hospital,Specialty HIPE,Speciality,Adult/Child,Age Profile,Time Bands,Total\n"
            + testUtil.getDateAsString() + "," + testUtil.getGroupName() + ",0" + testUtil.getHospitalHipe() + "," + testUtil.getHospitalName() + ",0"
            + testUtil.getSpecialityHipe() + "," + testUtil.getSpecialityName() + ","
            + testUtil.getClassification().name() + ", " + testUtil.getMinimumAge() + "-" + testUtil.getMaximumAge() + ", "
            + "WRONG_BAND" + " Months," + testUtil.getWaiting();

        opLineAsStream = new ByteArrayInputStream(opLineAsString.getBytes(StandardCharsets.UTF_8));

        when(recordService.isRowAlreadyProcessed(testUtil.getArchivedDate(), CsvType.OP)).thenReturn(false);

        processFileServiceImpl.readFile(opLineAsStream, CsvType.OP);

        recordOp.setMinimumWaitingTime(null);
        recordOp.setMaximumWaitingTime(null);

        verify(recordService).isRowAlreadyProcessed(testUtil.getArchivedDate(), CsvType.OP);
        verify(recordService).save(recordOp);
        verify(commonService).clearAllCaches();
    }

    @Test
    public void testReadOpFile_successfullyProcessed_hospitalFound() {
        testUtil.getHospital().setName("customName");

        when(recordService.isRowAlreadyProcessed(testUtil.getArchivedDate(), CsvType.OP)).thenReturn(false);
        when(hospitalRepository.findOne(testUtil.getHospitalHipe())).thenReturn(testUtil.getHospital());

        processFileServiceImpl.readFile(opLineAsStream, CsvType.OP);

        verify(recordService).isRowAlreadyProcessed(testUtil.getArchivedDate(), CsvType.OP);
        verify(recordService).save(recordOp);
        verify(commonService).clearAllCaches();
    }

    @Test
    public void testReadOpFile_successfullyProcessed_hospitalGroupFound() {
        testUtil.getHospitalGroup().setName("customtestUtil.getGroupName()");

        when(recordService.isRowAlreadyProcessed(testUtil.getArchivedDate(), CsvType.OP)).thenReturn(false);
        when(hospitalGroupRepository.findOne(testUtil.getGroupName())).thenReturn(testUtil.getHospitalGroup());

        processFileServiceImpl.readFile(opLineAsStream, CsvType.OP);

        verify(recordService).isRowAlreadyProcessed(testUtil.getArchivedDate(), CsvType.OP);
        verify(recordService).save(recordOp);
        verify(commonService).clearAllCaches();
    }

    @Test
    public void testReadOpFile_successfullyProcessed_specialityFound() {
        testUtil.getSpeciality().setName("customSpecialityName");

        when(recordService.isRowAlreadyProcessed(testUtil.getArchivedDate(), CsvType.OP)).thenReturn(false);
        when(specialityRepository.findOne(testUtil.getSpecialityHipe())).thenReturn(testUtil.getSpeciality());

        processFileServiceImpl.readFile(opLineAsStream, CsvType.OP);

        verify(recordService).isRowAlreadyProcessed(testUtil.getArchivedDate(), CsvType.OP);
        verify(recordService).save(recordOp);
        verify(commonService).clearAllCaches();
    }

    /**
     * Since the testUtil.getHospitalHipe() is invalid in the incoming message, it will be set as -1 in the record
     * and that's what we test here.
     */
    @Test
    public void testReadOpFile_successfullyProcessed_invalidHospitalHipe() {
        opLineAsString = "Archive Date,Group,Hospital HIPE,Hospital,Specialty HIPE,Speciality,Adult/Child,Age Profile,Time Bands,Total\n"
            + testUtil.getDateAsString() + "," + testUtil.getGroupName() + "," + "NULL" + "," + testUtil.getHospitalName() + ",0"
            + testUtil.getSpecialityHipe() + "," + testUtil.getSpecialityName() + ","
            + testUtil.getClassification().name() + ", " + testUtil.getMinimumAge() + "-" + testUtil.getMaximumAge() + ", "
            + testUtil.getMinimumWaitingTime() + "-" + testUtil.getMaximumWaitingTime() + " Months," + testUtil.getWaiting();

        opLineAsStream = new ByteArrayInputStream(opLineAsString.getBytes(StandardCharsets.UTF_8));

        when(recordService.isRowAlreadyProcessed(testUtil.getArchivedDate(), CsvType.OP)).thenReturn(false);

        processFileServiceImpl.readFile(opLineAsStream, CsvType.OP);

        verify(recordService).isRowAlreadyProcessed(testUtil.getArchivedDate(), CsvType.OP);

        recordOp.getHospital().setHipe(-1);

        verify(recordService).save(recordOp);
        verify(commonService).clearAllCaches();
    }

    /**
     * Since the testUtil.getSpecialityHipe() is invalid in the incoming message, it will be set as 9000 in the record
     * and that's what we test here.
     * The reason it's set to 9000, because for some entries it's set to that value, rather than NULL, so I went with consistency.
     */
    @Test
    public void testReadOpFile_successfullyProcessed_invalidSpecialityHipe() {
        opLineAsString = "Archive Date,Group,Hospital HIPE,Hospital,Specialty HIPE,Speciality,Adult/Child,Age Profile,Time Bands,Total\n"
            + testUtil.getDateAsString() + "," + testUtil.getGroupName() + ",0" + testUtil.getHospitalHipe() + "," + testUtil.getHospitalName()
            + "," + "NULL" + "," + testUtil.getSpecialityName() + ","
            + testUtil.getClassification().name() + ", " + testUtil.getMinimumAge() + "-" + testUtil.getMaximumAge() + ", "
            + testUtil.getMinimumWaitingTime() + "-" + testUtil.getMaximumWaitingTime() + " Months," + testUtil.getWaiting();

        opLineAsStream = new ByteArrayInputStream(opLineAsString.getBytes(StandardCharsets.UTF_8));

        when(recordService.isRowAlreadyProcessed(testUtil.getArchivedDate(), CsvType.OP)).thenReturn(false);

        processFileServiceImpl.readFile(opLineAsStream, CsvType.OP);

        verify(recordService).isRowAlreadyProcessed(testUtil.getArchivedDate(), CsvType.OP);

        recordOp.getSpeciality().setHipe(9000);

        verify(recordService).save(recordOp);
        verify(commonService).clearAllCaches();
    }

    @Test
    public void testReadOpIpdcFile_columnLengthDoesntMatch() {
        ipdcLineAsString = "Archive Date,Group,Hospital HIPE,Hospital,Specialty HIPE,Speciality,Case Type,Adult/Child,Age Profile,Time Bands,Total\n"
            + testUtil.getDateAsString() + "," + testUtil.getGroupName() + ",0" + testUtil.getHospitalHipe() + "," + testUtil.getHospitalName() + ",0"
            + testUtil.getSpecialityHipe() + "," + testUtil.getSpecialityName() + ","
            + testUtil.getCaseType().getValue() + "," + testUtil.getClassification() + "," + testUtil.getMinimumAge() + "-"
            + testUtil.getMaximumAge() + ", "
            + testUtil.getMinimumWaitingTime() + "-" + testUtil.getMaximumWaitingTime() + " Months";

        ipdcLineAsStream = new ByteArrayInputStream(ipdcLineAsString.getBytes(StandardCharsets.UTF_8));

        processFileServiceImpl.readFile(ipdcLineAsStream, CsvType.IPDC);

        verifyZeroInteractions(recordService);
        verify(commonService).clearAllCaches();
    }

    @Test
    public void testReadIpdcFile_lineAlreadyProcessed() {
        when(recordService.isRowAlreadyProcessed(testUtil.getArchivedDate(), CsvType.IPDC)).thenReturn(true);

        processFileServiceImpl.readFile(ipdcLineAsStream, CsvType.IPDC);

        verify(recordService).isRowAlreadyProcessed(testUtil.getArchivedDate(), CsvType.IPDC);
        verifyNoMoreInteractions(recordService);
        verify(commonService).clearAllCaches();
    }

    @Test
    public void testReadIpdcFile_successfullyProcessed() {
        when(recordService.isRowAlreadyProcessed(testUtil.getArchivedDate(), CsvType.IPDC)).thenReturn(false);

        processFileServiceImpl.readFile(ipdcLineAsStream, CsvType.IPDC);

        verify(recordService).isRowAlreadyProcessed(testUtil.getArchivedDate(), CsvType.IPDC);
        verify(recordService).save(recordIpdc);
        verify(commonService).clearAllCaches();
    }

    @Test
    public void testReadIpdcFile_wronglyFormattedAge() {
        ipdcLineAsString = "Archive Date,Group,Hospital HIPE,Hospital,Specialty HIPE,Speciality,Adult/Child,Age Profile,Time Bands,Total\n"
            + testUtil.getDateAsString() + "," + testUtil.getGroupName() + ",0" + testUtil.getHospitalHipe() + "," + testUtil.getHospitalName() + ",0"
            + testUtil.getSpecialityHipe() + "," + testUtil.getSpecialityName() + ","
            + testUtil.getCaseType().getValue() + "," + testUtil.getClassification().name() + ", " + "WRONG_AGE" + ", "
            + testUtil.getMinimumWaitingTime() + "-" + testUtil.getMaximumWaitingTime() + " Months," + testUtil.getWaiting();

        ipdcLineAsStream = new ByteArrayInputStream(ipdcLineAsString.getBytes(StandardCharsets.UTF_8));

        when(recordService.isRowAlreadyProcessed(testUtil.getArchivedDate(), CsvType.IPDC)).thenReturn(false);

        processFileServiceImpl.readFile(ipdcLineAsStream, CsvType.IPDC);

        recordIpdc.setMinimumAge(null);
        recordIpdc.setMaximumAge(null);

        verify(recordService).isRowAlreadyProcessed(testUtil.getArchivedDate(), CsvType.IPDC);
        verify(recordService).save(recordIpdc);
        verify(commonService).clearAllCaches();
    }

    @Test
    public void testReadIpdcFile_wronglyFormattedTimeBand() {
        ipdcLineAsString = "Archive Date,Group,Hospital HIPE,Hospital,Specialty HIPE,Speciality,Adult/Child,Age Profile,Time Bands,Total\n"
            + testUtil.getDateAsString() + "," + testUtil.getGroupName() + ",0" + testUtil.getHospitalHipe() + "," + testUtil.getHospitalName() + ",0"
            + testUtil.getSpecialityHipe() + "," + testUtil.getSpecialityName() + ","
            + testUtil.getCaseType().getValue() + "," + testUtil.getClassification().name() + ", " + testUtil.getMinimumAge() + "-"
            + testUtil.getMaximumAge() + ", "
            + "WRONG_BAND" + " Months," + testUtil.getWaiting();

        ipdcLineAsStream = new ByteArrayInputStream(ipdcLineAsString.getBytes(StandardCharsets.UTF_8));

        when(recordService.isRowAlreadyProcessed(testUtil.getArchivedDate(), CsvType.IPDC)).thenReturn(false);

        processFileServiceImpl.readFile(ipdcLineAsStream, CsvType.IPDC);

        recordIpdc.setMinimumWaitingTime(null);
        recordIpdc.setMaximumWaitingTime(null);

        verify(recordService).isRowAlreadyProcessed(testUtil.getArchivedDate(), CsvType.IPDC);
        verify(recordService).save(recordIpdc);
        verify(commonService).clearAllCaches();
    }

    /**
     * Since the testUtil.getHospitalHipe() is invalid in the incoming message, it will be set as -1 in the record
     * and that's what we test here.
     */
    @Test
    public void testReadIpdcFile_successfullyProcessed_invalidHospitalHipe() {
        ipdcLineAsString = "Archive Date,Group,Hospital HIPE,Hospital,Specialty HIPE,Speciality,Case Type,Adult/Child,Age Profile,Time Bands,Total\n"
            + testUtil.getDateAsString() + "," + testUtil.getGroupName() + ",0" + "NULL" + "," + testUtil.getHospitalName() + ",0"
            + testUtil.getSpecialityHipe() + "," + testUtil.getSpecialityName() + ","
            + testUtil.getCaseType().getValue() + "," + testUtil.getClassification() + "," + testUtil.getMinimumAge() + "-"
            + testUtil.getMaximumAge() + ", "
            + testUtil.getMinimumWaitingTime() + "-" + testUtil.getMaximumWaitingTime() + " Months," + testUtil.getWaiting();

        ipdcLineAsStream = new ByteArrayInputStream(ipdcLineAsString.getBytes(StandardCharsets.UTF_8));

        when(recordService.isRowAlreadyProcessed(testUtil.getArchivedDate(), CsvType.IPDC)).thenReturn(false);

        processFileServiceImpl.readFile(ipdcLineAsStream, CsvType.IPDC);

        verify(recordService).isRowAlreadyProcessed(testUtil.getArchivedDate(), CsvType.IPDC);

        recordIpdc.getHospital().setHipe(-1);

        verify(recordService).save(recordIpdc);
        verify(commonService).clearAllCaches();
    }

    /**
     * Since the testUtil.getSpecialityHipe() is invalid in the incoming message, it will be set as 9000 in the record
     * and that's what we test here.
     * The reason it's set to 9000, because for some entries it's set to that value, rather than NULL, so I went with consistency.
     */
    @Test
    public void testReadIpdcFile_successfullyProcessed_invalidSpecialityHipe() {
        ipdcLineAsString = "Archive Date,Group,Hospital HIPE,Hospital,Specialty HIPE,Speciality,Case Type,Adult/Child,Age Profile,Time Bands,Total\n"
            + testUtil.getDateAsString() + "," + testUtil.getGroupName() + ",0" + testUtil.getHospitalHipe() + ","
            + testUtil.getHospitalName() + ",0" + "NULL" + "," + testUtil.getSpecialityName() + ","
            + testUtil.getCaseType().getValue() + "," + testUtil.getClassification() + "," + testUtil.getMinimumAge() + "-"
            + testUtil.getMaximumAge() + ", "
            + testUtil.getMinimumWaitingTime() + "-" + testUtil.getMaximumWaitingTime() + " Months," + testUtil.getWaiting();

        ipdcLineAsStream = new ByteArrayInputStream(ipdcLineAsString.getBytes(StandardCharsets.UTF_8));

        when(recordService.isRowAlreadyProcessed(testUtil.getArchivedDate(), CsvType.IPDC)).thenReturn(false);

        processFileServiceImpl.readFile(ipdcLineAsStream, CsvType.IPDC);

        verify(recordService).isRowAlreadyProcessed(testUtil.getArchivedDate(), CsvType.IPDC);

        recordIpdc.getSpeciality().setHipe(9000);

        verify(recordService).save(recordIpdc);
        verify(commonService).clearAllCaches();
    }

}
