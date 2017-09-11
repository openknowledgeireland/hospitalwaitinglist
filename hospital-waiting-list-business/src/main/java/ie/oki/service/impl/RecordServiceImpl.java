package ie.oki.service.impl;

import ie.oki.enums.CsvType;
import ie.oki.model.Record;
import ie.oki.model.SearchCriteria;
import ie.oki.predicate.RecordSpecificationsBuilder;
import ie.oki.repository.RecordRepository;
import ie.oki.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Responsible for dealing with the record data.
 *
 * @author Zoltan Toth
 */
@Service
@CacheConfig(cacheNames = "recordService")
public class RecordServiceImpl implements RecordService {

    @Autowired
    private RecordRepository recordRepository;

    @Override
    @Cacheable
    public List<Record> findByCriteriaList(final List<SearchCriteria> criteria) {
        RecordSpecificationsBuilder builder = new RecordSpecificationsBuilder().with(criteria);
        return recordRepository.findAll(builder.build());
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
