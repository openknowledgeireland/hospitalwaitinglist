package ie.oki.controller;

import ie.oki.model.Hospital;
import ie.oki.model.Record;
import ie.oki.model.Speciality;
import ie.oki.service.HospitalService;
import ie.oki.service.RecordService;
import ie.oki.service.SpecialityService;
import ie.oki.util.Utils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
     * Currently the evaluation order of the parameters is the following: id > hospitalHipe > specialityHipe
     * Which means that if all three of them are provided, only one record with the same id as specified will return.
     *
     * @param id the id of the record
     * @param hospitalHipe the hospital hipe of the records
     * @param specialityHipe the speciality hipe of the records
     * @return list of records
     */
    @GetMapping("/records")
    @ApiOperation(value = "Retrieves the records", produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
        notes = "The evaluation order of the parameters is the following: id > hospitalHipe > specialityHipe")
    public List<Record> records(
        @RequestParam(value = "id", required = false) String id,
        @RequestParam(value = "hospitalHipe", required = false) Integer hospitalHipe,
        @RequestParam(value = "specialityHipe", required = false) Integer specialityHipe) {

        List<Record> records = new ArrayList<>();

        if (!Utils.isNullOrEmpty(id)) {
            records.add(recordService.findById(id));
        } else if (hospitalHipe != null) {
            records.addAll(recordService.findByHospitalHipe(hospitalHipe));
        } else if (specialityHipe != null) {
            records.addAll(recordService.findBySpecialityHipe(specialityHipe));
        }

        records.removeIf(Objects::isNull);
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
