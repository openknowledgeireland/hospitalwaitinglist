package ie.oki.service.impl;

import ie.oki.enums.CsvType;
import ie.oki.model.UriComponents;
import ie.oki.service.CommonService;
import ie.oki.service.DownloadService;
import ie.oki.service.ProcessFileService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.InputStream;
import java.time.Year;

import static org.mockito.Mockito.*;

/**
 * @author Zoltan Toth
 */
@RunWith(MockitoJUnitRunner.class)
public class AutoDownloadServiceImplTest {

    @Mock
    private DownloadService downloadService;

    @Mock
    private CommonService commonService;

    @Mock
    private ProcessFileService processFileService;

    @InjectMocks
    private AutoDownloadServiceImpl autoDownloadServiceImpl;

    @Mock
    private UriComponents uriComponents;

    @Mock
    private InputStream inputStream;

    @Test
    public void testCheckFiles_success() {
        doReturn(uriComponents).when(commonService).constructUriComponents(CsvType.OP, Year.now().getValue());
        doReturn(uriComponents).when(commonService).constructUriComponents(CsvType.IPDC, Year.now().getValue());

        doReturn(inputStream).when(downloadService).downloadFile(uriComponents);
        doReturn(inputStream).when(downloadService).downloadFile(uriComponents);

        doNothing().when(processFileService).readFile(inputStream, CsvType.OP);
        doNothing().when(processFileService).readFile(inputStream, CsvType.IPDC);

        autoDownloadServiceImpl.checkFiles();

        verify(commonService).constructUriComponents(CsvType.OP, Year.now().getValue());
        verify(commonService).constructUriComponents(CsvType.IPDC, Year.now().getValue());
        verify(downloadService, times(2)).downloadFile(uriComponents);
        verify(processFileService).readFile(inputStream, CsvType.OP);
        verify(processFileService).readFile(inputStream, CsvType.IPDC);

        verifyNoMoreInteractions(commonService);
        verifyNoMoreInteractions(downloadService);
        verifyNoMoreInteractions(processFileService);
    }

}
