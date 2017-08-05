package ie.oki.service.impl;

import ie.oki.model.Hospital;
import ie.oki.repository.HospitalRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Zoltan Toth
 */
public class HospitalServiceImplTest {

    @Mock
    private HospitalRepository hospitalRepository;

    @InjectMocks
    private HospitalServiceImpl hospitalServiceImpl;

    private List<Hospital> hospitals;
    private Hospital hospital;
    private Integer hipe;
    private String name;
    private String name2;
    private String name3;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        hospitals = new ArrayList<>();
        hospital = new Hospital();

        hipe = 100;
        name = "name";
        name2 = "name2";
        name3 = "name3";

        hospital.setHipe(hipe);
        hospital.setName(name);

        hospitals.add(hospital);
    }

    @Test
    public void testGetSpecialities_oneResult() {
        when(hospitalRepository.findAll()).thenReturn(hospitals);

        List<Hospital> result = hospitalServiceImpl.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(hipe, result.get(0).getHipe());
        assertEquals(name, result.get(0).getName());

        verify(hospitalRepository).findAll();
    }

    @Test
    public void testGetSpecialities_twoResults() {

        hospital = new Hospital();
        hospital.setName(name2);

        hospitals.add(hospital);

        when(hospitalRepository.findAll()).thenReturn(hospitals);

        List<Hospital> result = hospitalServiceImpl.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(hipe, result.get(0).getHipe());
        assertEquals(name, result.get(0).getName());
        assertEquals(name2, result.get(1).getName());

        verify(hospitalRepository).findAll();
    }

    @Test
    public void testGetSpecialities_twoResultsSorted() {

        hospital = new Hospital();
        hospital.setName(name3);
        hospitals.add(hospital);

        hospital = new Hospital();
        hospital.setName(name2);
        hospitals.add(hospital);

        when(hospitalRepository.findAll()).thenReturn(hospitals);

        List<Hospital> result = hospitalServiceImpl.findAll();

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(hipe, result.get(0).getHipe());
        assertEquals(name, result.get(0).getName());
        assertEquals(name2, result.get(1).getName());
        assertEquals(name3, result.get(2).getName());

        verify(hospitalRepository).findAll();
    }

    @Test
    public void testGetSpecialities_noResult() {
        hospitals = new ArrayList<>();

        when(hospitalRepository.findAll()).thenReturn(hospitals);

        List<Hospital> result = hospitalServiceImpl.findAll();

        assertNotNull(result);
        assertEquals(0, result.size());

        verify(hospitalRepository).findAll();
    }


}
