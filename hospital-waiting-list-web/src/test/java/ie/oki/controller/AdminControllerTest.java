package ie.oki.controller;

import ie.oki.enums.CsvType;
import ie.oki.model.UriComponents;
import ie.oki.service.CommonService;
import ie.oki.service.DownloadService;
import ie.oki.service.ProcessFileService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.InputStream;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

/**
 * @author Zoltan Toth
 */
public class AdminControllerTest {

    @Mock
    private MessageSource messageSource;

    @Mock
    private DownloadService downloadService;

    @Mock
    private ProcessFileService processFileService;

    @Mock
    private CommonService commonService;

    @InjectMocks
    private AdminController adminController;

    private MockMvc mockMvc;

    private int year;
    private CsvType csvType;
    private String errorYear2014 = "yearToBeGreaterThan2014";
    private String errorCannotDownloadFile = "cannotDownloadFile";
    private String messageSuccess = "SUCCESS";
    private InputStream inputStream;
    private UriComponents uriComponents;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = standaloneSetup(adminController).build();

        year = 2014;
        csvType = CsvType.OP;
        inputStream = Mockito.mock(InputStream.class);

        uriComponents = new UriComponents();
    }

    @Test
    public void testClearCache_success() throws Exception {

        mockMvc.perform(get("/admin/cache/clear").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN));
    }

    @Test
    public void testUpdate_yearIsPre2014() throws Exception {

        when(messageSource.getMessage(anyString(), anyObject(), any(Locale.class))).thenReturn(errorYear2014);

        year = 2013;

        MvcResult result = mockMvc.perform(get("/admin/update?year=" + year + "&type=" + csvType).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andReturn();

        String content = result.getResponse().getContentAsString();

        assertEquals(errorYear2014, content);

        verify(messageSource).getMessage(anyString(), anyObject(), any(Locale.class));
    }

    @Test
    public void testUpdate_cannotDownloadFile() throws Exception {

        when(commonService.constructUriComponents(csvType, year)).thenReturn(uriComponents);
        when(downloadService.downloadFile(uriComponents)).thenReturn(null);
        when(messageSource.getMessage(anyString(), anyObject(), any(Locale.class))).thenReturn(errorCannotDownloadFile);

        MvcResult result = mockMvc.perform(get("/admin/update?year=" + year + "&type=" + csvType).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andReturn();

        String content = result.getResponse().getContentAsString();

        assertEquals(errorCannotDownloadFile, content);

        verify(commonService).constructUriComponents(csvType, year);
        verify(downloadService).downloadFile(uriComponents);
        verify(messageSource).getMessage(anyString(), anyObject(), any(Locale.class));
    }

    @Test
    public void testUpdate_opDownloadSuccessful() throws Exception {

        when(commonService.constructUriComponents(csvType, year)).thenReturn(uriComponents);
        when(downloadService.downloadFile(uriComponents)).thenReturn(inputStream);
        doNothing().when(processFileService).readFile(inputStream, csvType);

        MvcResult result = mockMvc.perform(get("/admin/update?year=" + year + "&type=" + csvType).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

        String content = result.getResponse().getContentAsString();

        assertEquals(messageSuccess, content);

        verify(commonService).constructUriComponents(csvType, year);
        verify(downloadService).downloadFile(uriComponents);
        verify(processFileService).readFile(inputStream, csvType);
    }

    @Test
    public void testUpdate_ipdcDownloadSuccessful() throws Exception {

        csvType = CsvType.IPDC;

        when(commonService.constructUriComponents(csvType, year)).thenReturn(uriComponents);
        when(downloadService.downloadFile(uriComponents)).thenReturn(inputStream);
        doNothing().when(processFileService).readFile(inputStream, csvType);

        MvcResult result = mockMvc.perform(get("/admin/update?year=" + year + "&type=" + csvType).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

        String content = result.getResponse().getContentAsString();

        assertEquals(messageSuccess, content);

        verify(commonService).constructUriComponents(csvType, year);
        verify(downloadService).downloadFile(uriComponents);
        verify(processFileService).readFile(inputStream, csvType);
    }

}
