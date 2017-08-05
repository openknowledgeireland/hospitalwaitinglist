package ie.oki.service.impl;

import ie.oki.enums.CsvType;
import ie.oki.model.Record;
import ie.oki.repository.RecordRepository;
import ie.oki.service.RecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * This is responsible for retrieving the record data.
 *
 * @author Zoltan Toth
 */
@Slf4j
@Service
@CacheConfig(cacheNames = "recordService")
public class RecordServiceImpl implements RecordService {

    @Autowired
    private RecordRepository recordRepository;

    @Override
    @Cacheable
    public Record findById(final String id) {
        try {
            return recordRepository.findOne(UUID.fromString(id));
        } catch (IllegalArgumentException iae) {
            if (log.isDebugEnabled()) {
                log.debug("Couldn't convert input parameter[" + id + "] to a UUID.", iae);
            }
        }
        return null;
    }

    @Override
    @Cacheable(key = "{#root.methodName, #hipe}")
    public List<Record> findByHospitalHipe(final int hipe) {
        return recordRepository.findByHospitalHipe(hipe);
    }

    @Override
    @Cacheable(key = "{#root.methodName, #hipe}")
    public List<Record> findBySpecialityHipe(final int hipe) {
        return recordRepository.findBySpecialityHipe(hipe);
    }

    @Override
    @Cacheable
    public boolean isRowAlreadyProcessed(final Date date, final CsvType type) {
        return recordRepository.existsByArchivedDateAndType(date, type);
    }

    @Override
    public Record save(final Record record) {
        return recordRepository.save(record);
    }
}
