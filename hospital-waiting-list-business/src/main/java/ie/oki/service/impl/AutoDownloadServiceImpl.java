package ie.oki.service.impl;

import ie.oki.enums.CsvType;
import ie.oki.model.UriComponents;
import ie.oki.properties.AppProperties;
import ie.oki.service.AutoDownloadService;
import ie.oki.service.CommonService;
import ie.oki.service.DownloadService;
import ie.oki.service.ProcessFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.time.Year;

/**
 * This service will run at certain times (depends on what defined in the {@code appProperties.autoDownload.cron} property).
 * It will create a download url, download the file from that url and process it.
 *
 * <p>Note: the cron expression is read by the {@link AppProperties} configuration class.
 *
 * @author Zoltan Toth
 */
@Slf4j
@Service
public class AutoDownloadServiceImpl implements AutoDownloadService {

    @Autowired
    private DownloadService downloadService;

    @Autowired
    private CommonService commonService;

    @Autowired
    private ProcessFileService processFileService;

    @Override
    @Scheduled(cron = "#{appProperties.autoDownload.cron}")
    public void checkFiles() {
        log.info("Downloading the CSV files and potentially updating the database.");

        for (CsvType csvType : CsvType.values()) {
            UriComponents uriComponents = commonService.constructUriComponents(csvType, Year.now().getValue());
            InputStream inputStream = downloadService.downloadFile(uriComponents);
            processFileService.readFile(inputStream, csvType);
        }
    }
}
