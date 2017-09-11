package ie.oki.controller;

import ie.oki.enums.CaseType;
import ie.oki.enums.Classification;
import ie.oki.enums.CsvType;
import ie.oki.enums.SearchOperation;
import ie.oki.model.*;
import ie.oki.service.HospitalService;
import ie.oki.service.RecordService;
import ie.oki.service.SpecialityService;
import ie.oki.util.Constants;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

/**
 * @author Zoltan Toth
 */
@RunWith(MockitoJUnitRunner.class)
public class DetailsControllerTest {

    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT)
        .withLocale(Locale.UK)
        .withZone(ZoneId.systemDefault());

    @Mock
    private RecordService recordService;

    @Mock
    private HospitalService hospitalService;

    @Mock
    private SpecialityService specialityService;

    @InjectMocks
    private DetailsController detailsController;

    private MockMvc mockMvc;

    private List<Record> records;
    private HospitalGroup hospitalGroup;
    private String hospitalGroupName;

    private Hospital hospital;
    private String hospitalName;
    private int hospitalHipe;

    private Speciality speciality;
    private int specialityHipe;
    private String specialityName;

    private Record record;
    private Record record2;

    private UUID recordId;
    private UUID record2Id;
    private Date archivedDate;
    private CaseType caseType;
    private Classification classification;
    private int maximumAge;
    private int maximumWaitingTime;
    private int minimumAge;
    private int minimumWaitingTime;
    private CsvType csvType;
    private int waiting;

    @Before
    public void setup() {
        mockMvc = standaloneSetup(detailsController).build();

        records = new ArrayList<>();

        hospitalGroupName = "hospitalGroupName";

        hospitalGroup = new HospitalGroup();
        hospitalGroup.setName(hospitalGroupName);

        hospitalName = "hospitalName";
        hospitalHipe = 33;

        hospital = new Hospital();
        hospital.setName(hospitalName);
        hospital.setHipe(hospitalHipe);
        hospital.setHospitalGroup(hospitalGroup);

        specialityHipe = 5864;
        specialityName = "specialityName";

        speciality = new Speciality();
        speciality.setHipe(specialityHipe);
        speciality.setName(specialityName);

        recordId = UUID.randomUUID();
        archivedDate = new Date();
        caseType = CaseType.DAY_CASE;
        classification = Classification.CHILD;
        maximumAge = 20;
        maximumWaitingTime = 12;
        minimumAge = 5;
        minimumWaitingTime = 3;
        csvType = CsvType.OP;
        waiting = 68;

        record = new Record();
        record.setId(recordId);
        record.setArchivedDate(archivedDate);
        record.setCaseType(caseType);
        record.setClassification(classification);
        record.setHospital(hospital);
        record.setMinimumAge(minimumAge);
        record.setMaximumAge(maximumAge);
        record.setMinimumWaitingTime(minimumWaitingTime);
        record.setMaximumWaitingTime(maximumWaitingTime);
        record.setSpeciality(speciality);
        record.setType(csvType);
        record.setWaiting(waiting);

        record2Id = UUID.randomUUID();

        record2 = new Record();
        record2.setId(record2Id);

        records.add(record);
        records.add(record2);
    }

    @Test
    public void testRecords_noParameter() throws Exception {

        mockMvc.perform(get("/records").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void testRecords_wrongParameter() throws Exception {

        mockMvc.perform(get("/records?params=key::value").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void testRecords_correctParameter() throws Exception {
        List<SearchCriteria> criteria = new ArrayList<>();
        criteria.add(new SearchCriteria("key", SearchOperation.EQUAL, "value"));

        when(recordService.findByCriteriaList(criteria)).thenReturn(records);

        mockMvc.perform(get("/records?params=key:value").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].id", is(recordId.toString())))
            .andExpect(jsonPath("$[0].archivedDate", is(dateTimeFormatter.format(archivedDate.toInstant()))))
            .andExpect(jsonPath("$[0].caseType", is(caseType.name())))
            .andExpect(jsonPath("$[0].classification", is(classification.name())))
            .andExpect(jsonPath("$[0].hospital.hipe", is(hospitalHipe)))
            .andExpect(jsonPath("$[0].hospital.name", is(hospitalName)))
            .andExpect(jsonPath("$[0].hospital.hospitalGroup.name", is(hospitalGroupName)))
            .andExpect(jsonPath("$[0].minimumAge", is(minimumAge)))
            .andExpect(jsonPath("$[0].maximumAge", is(maximumAge)))
            .andExpect(jsonPath("$[0].minimumWaitingTime", is(minimumWaitingTime)))
            .andExpect(jsonPath("$[0].maximumWaitingTime", is(maximumWaitingTime)))
            .andExpect(jsonPath("$[0].speciality.hipe", is(specialityHipe)))
            .andExpect(jsonPath("$[0].speciality.name", is(specialityName)))
            .andExpect(jsonPath("$[0].type", is(csvType.name())))
            .andExpect(jsonPath("$[0].waiting", is(waiting)));

        verify(recordService).findByCriteriaList(criteria);
        verifyNoMoreInteractions(recordService);
    }

    @Test
    public void testHospitals_emptyResult() throws Exception {

        List<Hospital> expectedResult = new ArrayList<>();

        when(hospitalService.findAll()).thenReturn(expectedResult);

        mockMvc.perform(get("/hospitals").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$", hasSize(0)));

        verify(hospitalService).findAll();
    }

    @Test
    public void testHospitals_oneResult() throws Exception {

        List<Hospital> expectedResult = new ArrayList<>();

        expectedResult.add(hospital);

        when(hospitalService.findAll()).thenReturn(expectedResult);

        mockMvc.perform(get("/hospitals").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].name", is(hospitalName)))
            .andExpect(jsonPath("$[0].hospitalGroup.name", is(hospitalGroupName)))
            .andExpect(jsonPath("$[0].hipe", is(hospitalHipe)));

        verify(hospitalService).findAll();
    }

    @Test
    public void testSpecialities_emptyResult() throws Exception {

        List<Speciality> expectedResult = new ArrayList<>();

        when(specialityService.findAll()).thenReturn(expectedResult);

        mockMvc.perform(get("/specialities").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$", hasSize(0)));

        verify(specialityService).findAll();
    }

    @Test
    public void testSpecialities_oneResult() throws Exception {
        List<Speciality> expectedResult = new ArrayList<>();
        expectedResult.add(speciality);

        when(specialityService.findAll()).thenReturn(expectedResult);

        mockMvc.perform(get("/specialities").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].hipe", is(specialityHipe)))
            .andExpect(jsonPath("$[0].name", is(specialityName)));

        verify(specialityService).findAll();
    }

}
