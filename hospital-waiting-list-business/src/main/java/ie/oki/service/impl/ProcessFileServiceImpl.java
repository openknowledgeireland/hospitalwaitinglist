package ie.oki.service.impl;

import ie.oki.enums.CaseType;
import ie.oki.enums.Classification;
import ie.oki.enums.CsvType;
import ie.oki.model.Hospital;
import ie.oki.model.HospitalGroup;
import ie.oki.model.Record;
import ie.oki.model.Speciality;
import ie.oki.repository.HospitalGroupRepository;
import ie.oki.repository.HospitalRepository;
import ie.oki.repository.SpecialityRepository;
import ie.oki.service.CommonService;
import ie.oki.service.ProcessFileService;
import ie.oki.service.RecordService;
import ie.oki.util.Constants;
import ie.oki.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static ie.oki.util.Constants.LIST_SIZE_IPDC;
import static ie.oki.util.Constants.LIST_SIZE_OP;

/**
 * This service is responsible for processing the different CSV files.
 *
 * @author Zoltan Toth
 */
@Slf4j
@Service
public class ProcessFileServiceImpl implements ProcessFileService {

    @Autowired
    private RecordService recordService;

    @Autowired
    private CommonService commonService;

    @Autowired
    private HospitalRepository hospitalRepository;

    @Autowired
    private HospitalGroupRepository hospitalGroupRepository;

    @Autowired
    private SpecialityRepository specialityRepository;

    @Override
    @Transactional
    public void readFile(final InputStream inputStream, final CsvType csvType) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            if (csvType == CsvType.OP) {
                br.lines().skip(1).forEach(this::processOpLine);
            } else {
                br.lines().skip(1).forEach(this::processIpdcLine);
            }
        } catch (IOException e) {
            log.info("Couldn't process the file.", e);
        }

        // Let's clear the cache after the CSV file is processed, so the next time something is queried,
        // it can return with the up-to-date information
        commonService.clearAllCaches();
    }

    /**
     * Processing the OP file.
     */
    private void processOpLine(final String line) {

        List<String> csvRow = Arrays.asList(line.split(Constants.COMMA));

        csvRow = Utils.normalizeAndTrimList(csvRow);

        if (csvRow.size() != LIST_SIZE_OP) {
            if (log.isInfoEnabled()) {
                log.info("The number of columns while processing the line[" + csvRow + "] is not " + LIST_SIZE_OP + ". Aborting the function.");
            }
            return;
        }

        Record record = new Record();
        record.setArchivedDate(Utils.convertStringToDate(csvRow.get(0)));

        if (recordService.isRowAlreadyProcessed(record.getArchivedDate(), CsvType.OP)) {
            return;
        }

        record.setHospital(processHospital(csvRow));
        record.setSpeciality(processSpeciality(csvRow));

        record.setClassification(Classification.getByValue(csvRow.get(6)));

        List<Integer> ages = Utils.parseInterval(csvRow.get(7));
        if (!ages.isEmpty()) {
            record.setMinimumAge(ages.get(0));
            record.setMaximumAge(ages.get(1));
        }

        List<Integer> timeBands = Utils.parseTimeBands(csvRow.get(8));
        if (!timeBands.isEmpty()) {
            record.setMinimumWaitingTime(timeBands.get(0));
            record.setMaximumWaitingTime(timeBands.get(1));
        }

        record.setWaiting(Integer.parseInt(csvRow.get(9)));

        record.setType(CsvType.OP);

        recordService.save(record);
    }

    /**
     * Processing the IPDC file.
     */
    private void processIpdcLine(final String line) {
        Record record = new Record();

        List<String> csvRow = Arrays.asList(line.split(Constants.COMMA));

        csvRow = Utils.normalizeAndTrimList(csvRow);

        if (csvRow.size() != LIST_SIZE_IPDC) {
            if (log.isInfoEnabled()) {
                log.info("The number of columns while processing the line[" + csvRow + "] is not " + LIST_SIZE_IPDC + ". Aborting the function.");
            }
            return;
        }

        record.setArchivedDate(Utils.convertStringToDate(csvRow.get(0)));

        if (recordService.isRowAlreadyProcessed(record.getArchivedDate(), CsvType.IPDC)) {
            return;
        }

        record.setHospital(processHospital(csvRow));
        record.setSpeciality(processSpeciality(csvRow));

        record.setCaseType(CaseType.getByValue(csvRow.get(6)));
        record.setClassification(Classification.getByValue(csvRow.get(7)));

        List<Integer> ages = Utils.parseInterval(csvRow.get(8));
        if (!ages.isEmpty()) {
            record.setMinimumAge(ages.get(0));
            record.setMaximumAge(ages.get(1));
        }

        List<Integer> timeBands = Utils.parseTimeBands(csvRow.get(9));
        if (!timeBands.isEmpty()) {
            record.setMinimumWaitingTime(timeBands.get(0));
            record.setMaximumWaitingTime(timeBands.get(1));
        }

        record.setWaiting(Integer.parseInt(csvRow.get(10)));

        record.setType(CsvType.IPDC);

        recordService.save(record);
    }

    private Hospital processHospital(final List<String> csvRow) {
        Integer hospitalHipe = -1;

        try {
            hospitalHipe = Integer.valueOf(csvRow.get(2));
        } catch (NumberFormatException nfe) {
            if (log.isDebugEnabled()) {
                log.debug("Couldn't parse the value [" + csvRow.get(2) + "] to integer.", nfe);
            }
        }

        Hospital hospital = hospitalRepository.findOne(hospitalHipe);

        if (hospital == null) {
            HospitalGroup hospitalGroup = hospitalGroupRepository.findOne(csvRow.get(1));
            if (hospitalGroup == null) {
                hospitalGroup = new HospitalGroup();
                hospitalGroup.setName(csvRow.get(1));
            }

            hospital = new Hospital();
            hospital.setHipe(hospitalHipe);
            hospital.setName(csvRow.get(3));

            hospital.setHospitalGroup(hospitalGroup);
        }

        return hospital;
    }

    private Speciality processSpeciality(final List<String> csvRow) {

        // The reason it's 9000 is because in some cases the CSV has this number down for "Other"
        // but in other cases it's "NULL". For now we use this number to be consistent.
        Integer specialityHipe = 9000;

        try {
            specialityHipe = Integer.valueOf(csvRow.get(4));
        } catch (NumberFormatException nfe) {
            if (log.isDebugEnabled()) {
                log.debug("Couldn't parse the value [" + csvRow.get(4) + "] to integer.", nfe);
            }
        }

        Speciality speciality = specialityRepository.findOne(specialityHipe);

        if (speciality == null) {
            speciality = new Speciality();

            speciality.setHipe(specialityHipe);
            speciality.setName(csvRow.get(5));
        }

        return speciality;
    }
}
