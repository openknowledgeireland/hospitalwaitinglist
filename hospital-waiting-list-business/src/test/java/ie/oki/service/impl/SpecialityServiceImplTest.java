package ie.oki.service.impl;

import ie.oki.model.Speciality;
import ie.oki.repository.SpecialityRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Zoltan Toth
 */
@RunWith(MockitoJUnitRunner.class)
public class SpecialityServiceImplTest {

    @Mock
    private SpecialityRepository specialityRepository;

    @InjectMocks
    private SpecialityServiceImpl specialityServiceImpl;

    private List<Speciality> specialities;
    private Speciality speciality;
    private Integer hipe;
    private String name;
    private String name2;
    private String name3;

    @Before
    public void setup() {
        specialities = new ArrayList<>();
        speciality = new Speciality();

        hipe = 100;
        name = "name";
        name2 = "name2";
        name3 = "name3";

        speciality.setHipe(hipe);
        speciality.setName(name);

        specialities.add(speciality);
    }

    @Test
    public void testFindAll_oneResult() {
        when(specialityRepository.findAll()).thenReturn(specialities);

        List<Speciality> result = specialityServiceImpl.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(hipe, result.get(0).getHipe());
        assertEquals(name, result.get(0).getName());

        verify(specialityRepository).findAll();
    }

    @Test
    public void testFindAll_twoResults() {

        speciality = new Speciality();
        speciality.setName(name2);

        specialities.add(speciality);

        when(specialityRepository.findAll()).thenReturn(specialities);

        List<Speciality> result = specialityServiceImpl.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(hipe, result.get(0).getHipe());
        assertEquals(name, result.get(0).getName());
        assertEquals(name2, result.get(1).getName());

        verify(specialityRepository).findAll();
    }

    @Test
    public void testFindAll_twoResultsSorted() {

        speciality = new Speciality();
        speciality.setName(name3);
        specialities.add(speciality);

        speciality = new Speciality();
        speciality.setName(name2);
        specialities.add(speciality);

        when(specialityRepository.findAll()).thenReturn(specialities);

        List<Speciality> result = specialityServiceImpl.findAll();

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(hipe, result.get(0).getHipe());
        assertEquals(name, result.get(0).getName());
        assertEquals(name2, result.get(1).getName());
        assertEquals(name3, result.get(2).getName());

        verify(specialityRepository).findAll();
    }

    @Test
    public void testFindAll_noResult() {
        specialities = new ArrayList<>();

        when(specialityRepository.findAll()).thenReturn(specialities);

        List<Speciality> result = specialityServiceImpl.findAll();

        assertNotNull(result);
        assertEquals(0, result.size());

        verify(specialityRepository).findAll();
    }


}
