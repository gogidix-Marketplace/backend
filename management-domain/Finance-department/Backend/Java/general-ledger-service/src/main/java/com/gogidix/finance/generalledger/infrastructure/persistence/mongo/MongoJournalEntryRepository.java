package com.gogidix.finance.generalledger.infrastructure.persistence.mongo;

import com.gogidix.finance.generalledger.domain.repository.JournalEntryRepository;
import com.gogidix.finance.generalledger.shared.requestcontext.RequestContextHolder;
import com.gogidix.finance.ledger.domain.model.JournalEntry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * MongoDB Repository Implementation - Journal Entry
 * Implements journal entry persistence with mandatory tenant filtering
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoJournalEntryRepository implements JournalEntryRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public JournalEntry save(JournalEntry journalEntry) {
        log.debug("Saving journal entry: {} for tenant: {}",
            journalEntry.getJournalEntryId(), journalEntry.getTenantId());
        return mongoTemplate.save(journalEntry);
    }

    @Override
    public List<JournalEntry> saveAll(List<JournalEntry> journalEntries) {
        return journalEntries.stream()
            .map(mongoTemplate::save)
            .toList();
    }

    @Override
    public Optional<JournalEntry> findById(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        Query query = Query.query(
            Criteria.where("id").is(id)
                .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, JournalEntry.class));
    }

    @Override
    public Optional<JournalEntry> findByJournalEntryIdAndTenantId(String journalEntryId, String tenantId) {
        Query query = Query.query(
            Criteria.where("journalEntryId").is(journalEntryId)
                .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, JournalEntry.class));
    }

    @Override
    public Optional<JournalEntry> findByEntryNumberAndTenantId(String entryNumber, String tenantId) {
        Query query = Query.query(
            Criteria.where("entryNumber").is(entryNumber)
                .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, JournalEntry.class));
    }

    @Override
    public List<JournalEntry> findByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, JournalEntry.class);
    }

    @Override
    public List<JournalEntry> findByTenantIdAndStatus(String tenantId, JournalEntry.JournalEntryStatus status) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("status").is(status)
        );
        return mongoTemplate.find(query, JournalEntry.class);
    }

    @Override
    public List<JournalEntry> findByTenantIdAndEntryDateBetween(String tenantId, LocalDate startDate, LocalDate endDate) {
        Date start = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(endDate.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());

        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("entryDate").gte(start).lte(end)
        );
        return mongoTemplate.find(query, JournalEntry.class);
    }

    @Override
    public List<JournalEntry> findByTenantIdAndAccountId(String tenantId, String accountId, LocalDate startDate, LocalDate endDate) {
        Date start = startDate != null ? Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant()) : null;
        Date end = endDate != null ? Date.from(endDate.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant()) : null;

        Criteria criteria = Criteria.where("tenantId").is(tenantId)
            .and("lines.accountId").is(accountId);

        if (start != null && end != null) {
            criteria = criteria.and("entryDate").gte(start).lte(end);
        }

        Query query = Query.query(criteria);
        return mongoTemplate.find(query, JournalEntry.class);
    }

    @Override
    public List<JournalEntry> findByTenantIdAndFiscalPeriod(String tenantId, Integer fiscalYear, Integer fiscalPeriod) {
        Criteria criteria = Criteria.where("tenantId").is(tenantId)
            .and("fiscalYear").is(fiscalYear);

        if (fiscalPeriod != null) {
            criteria = criteria.and("fiscalPeriod").is(fiscalPeriod);
        }

        Query query = Query.query(criteria);
        return mongoTemplate.find(query, JournalEntry.class);
    }

    @Override
    public List<JournalEntry> findPendingApprovalByTenantId(String tenantId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("status").is(JournalEntry.JournalEntryStatus.PENDING_APPROVAL)
        );
        return mongoTemplate.find(query, JournalEntry.class);
    }

    @Override
    public List<JournalEntry> findPostedByTenantIdAndDateRange(String tenantId, LocalDate startDate, LocalDate endDate) {
        Date start = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(endDate.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());

        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("status").is(JournalEntry.JournalEntryStatus.POSTED)
                .and("postingDate").gte(start).lte(end)
        );
        return mongoTemplate.find(query, JournalEntry.class);
    }

    @Override
    public List<JournalEntry> findByTenantIdAndSourceDocument(String tenantId, String sourceDocumentType, String sourceDocumentId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("sourceDocumentType").is(sourceDocumentType)
                .and("sourceDocumentId").is(sourceDocumentId)
        );
        return mongoTemplate.find(query, JournalEntry.class);
    }

    @Override
    public List<JournalEntry> findByTenantIdAndBatchId(String tenantId, String batchId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("batchId").is(batchId)
        );
        return mongoTemplate.find(query, JournalEntry.class);
    }

    @Override
    public boolean existsByJournalEntryIdAndTenantId(String journalEntryId, String tenantId) {
        Query query = Query.query(
            Criteria.where("journalEntryId").is(journalEntryId)
                .and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, JournalEntry.class);
    }

    @Override
    public boolean existsByEntryNumberAndTenantId(String entryNumber, String tenantId) {
        Query query = Query.query(
            Criteria.where("entryNumber").is(entryNumber)
                .and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, JournalEntry.class);
    }

    @Override
    public void deleteById(String id) {
        mongoTemplate.remove(Query.query(Criteria.where("id").is(id)), JournalEntry.class);
    }

    @Override
    public void deleteByJournalEntryIdAndTenantId(String journalEntryId, String tenantId) {
        Query query = Query.query(
            Criteria.where("journalEntryId").is(journalEntryId)
                .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, JournalEntry.class);
    }

    @Override
    public void deleteAllByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        mongoTemplate.remove(query, JournalEntry.class);
    }

    @Override
    public long countByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.count(query, JournalEntry.class);
    }

    @Override
    public long countByTenantIdAndStatus(String tenantId, JournalEntry.JournalEntryStatus status) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("status").is(status)
        );
        return mongoTemplate.count(query, JournalEntry.class);
    }

    @Override
    public String generateNextEntryNumber(String tenantId) {
        // Find the highest entry number for this tenant
        Query query = Query.query(Criteria.where("tenantId").is(tenantId))
            .with(org.springframework.data.domain.PageRequest.of(0, 1,
                org.springframework.data.domain.Sort.by(
                    org.springframework.data.domain.Sort.Order.desc("entryNumber"))));

        JournalEntry lastEntry = mongoTemplate.findOne(query, JournalEntry.class);

        if (lastEntry == null || lastEntry.getEntryNumber() == null) {
            return tenantId + "-JE-000001";
        }

        // Extract numeric part and increment
        String lastNumber = lastEntry.getEntryNumber();
        String prefix = tenantId + "-JE-";

        if (lastNumber.startsWith(prefix)) {
            try {
                int numericPart = Integer.parseInt(lastNumber.substring(prefix.length()));
                return String.format("%sJE-%06d", tenantId, numericPart + 1);
            } catch (NumberFormatException e) {
                // If parsing fails, start fresh
                return prefix + "000001";
            }
        }

        return prefix + "000001";
    }
}
