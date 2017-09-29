package ie.oki.controller;

import ie.oki.enums.SearchOperation;
import ie.oki.model.Hospital;
import ie.oki.model.Record;
import ie.oki.model.SearchCriteria;
import ie.oki.model.Speciality;
import ie.oki.service.HospitalService;
import ie.oki.service.RecordService;
import ie.oki.service.SpecialityService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ie.oki.util.Constants.INPUT_PARAM_PATTERN;
import static ie.oki.util.Utils.isValidLookupParam;

/**
 * @author Zoltan Toth
 */
@RestController
public class DetailsController {

    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private SpecialityService specialityService;

    @Autowired
    private RecordService recordService;

    /**
     * Retrieves the records based on the input parameters.
     *
     * <p>The structure of one parameter is the following: <br>
     * The <b>first</b> part is the path to a certain field, which is based on the structure of the {@link Record} object (examples below). <br>
     * The <b>second</b> part can be:
     * </p>
     * <pre>
     *  ":" when checking for equality
     *  "&lt;" when the field retrieved by the path has to be <b>less</b> than the value provided
     *  "&gt;" when the field retrieved by the path has to be <b>greater</b> than the value provided
     * </pre>
     *
     * <p>The <b>third</b> part is the value that will be compared against the field that's retrieved by the path.</p>
     *
     * <p>The parameters should be provided in a certain pattern.<br>
     * When you want to lookup a record by id:
     * </p>
     * <pre>
     *  /records?param=id:abcdefg
     * </pre>
     *
     * <p>When you want to lookup a record where the minimumAge is at least 16:</p>
     * <pre>
     *  /records?param=minimumAge&gt;15
     * </pre>
     *
     * <p>When you want to lookup a record where the maximumAge is less than 64:</p>
     * <pre>
     *  /records?param=maximumAge&lt;65
     * </pre>
     *
     * <p>When you want to lookup a record by the name of the hospital group:</p>
     * <pre>
     *  /records?param=hospital.hospitalGroup.name:hospital group name
     * </pre>
     *
     * <p>Or when you want to lookup a record by the hospital HIPE code:</p>
     * <pre>
     *  /records?param=hospital.hipe:1234
     * </pre>
     *
     * <p>Multiple parameters can be provided at the same time to fine tune the query.</p>
     * <pre>
     *  /records?param=minimumAge&gt;15&amp;param=maximumAge&lt;65&amp;classification:child&amp;hospital.hipe:1234
     * </pre>
     *
     * @param params a list of parameters to create a query
     * @return list of records if all the parameters were successfully parsed, otherwise an empty list
     */
    @GetMapping("/records")
    @ApiOperation(value = "Retrieves the records", produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
        notes = "Please check the source code/javadoc for more information.")
    public List<Record> records(@RequestParam(value = "params") List<String> params) {
        List<Record> records = new ArrayList<>();
        List<SearchCriteria> searchCriteria = new LinkedList<>();

        Pattern pattern = Pattern.compile(INPUT_PARAM_PATTERN);

        for (String param : params) {
            if (isValidLookupParam(param)) {
                Matcher matcher = pattern.matcher(param);
                while (matcher.find()) {
                    searchCriteria.add(new SearchCriteria(matcher.group(1), SearchOperation.getByValue(matcher.group(2)), matcher.group(3)));
                }
            }
        }

        if (!searchCriteria.isEmpty()) {
            records.addAll(recordService.findByCriteriaList(searchCriteria));
        }

        return records;
    }

    @GetMapping("/hospitals")
    @ApiOperation(value = "Retrieves the hospitals", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<Hospital> hospitals() {
        return hospitalService.findAll();
    }

    @GetMapping("/specialities")
    @ApiOperation(value = "Retrieves the specialities", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<Speciality> specialities() {
        return specialityService.findAll();
    }

}
