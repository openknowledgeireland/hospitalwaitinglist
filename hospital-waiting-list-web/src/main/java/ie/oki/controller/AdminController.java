package ie.oki.controller;

import ie.oki.enums.CsvType;
import ie.oki.model.UriComponents;
import ie.oki.service.CommonService;
import ie.oki.service.DownloadService;
import ie.oki.service.ProcessFileService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.InputStream;

import static ie.oki.util.Constants.CSV_STARTING_YEAR;

/**
 * @author Zoltan Toth
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private DownloadService downloadService;

    @Autowired
    private ProcessFileService processFileService;

    @Autowired
    private CommonService commonService;

    @Autowired
    private MessageSource messageSource;

    @ResponseBody
    @GetMapping("/cache/clear")
    @ApiOperation(value = "Clears the cache", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> clearCache() {
        commonService.clearAllCaches();
        return ResponseEntity.ok("SUCCESS");
    }

    @ResponseBody
    @GetMapping("/update")
    @ApiOperation(value = "Loads the specified CSV into the database", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> update(
        @RequestParam(value = "type") CsvType type,
        @RequestParam(value = "year") int year) {

        if (year < CSV_STARTING_YEAR) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                messageSource.getMessage("error.admin.yearToBeGreaterThan2014", null, LocaleContextHolder.getLocale()));
        }

        UriComponents uriComponents = commonService.constructUriComponents(type, year);

        InputStream inputStream = downloadService.downloadFile(uriComponents);

        if (inputStream == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                messageSource.getMessage("error.admin.downloadUnsuccessful", null, LocaleContextHolder.getLocale()));
        }

        processFileService.readFile(inputStream, type);

        return ResponseEntity.ok("SUCCESS");
    }
}
