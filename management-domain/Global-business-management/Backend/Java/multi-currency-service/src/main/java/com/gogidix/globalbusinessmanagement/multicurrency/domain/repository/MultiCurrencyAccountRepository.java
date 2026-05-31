package com.gogidix.globalbusinessmanagement.multicurrency.domain.repository;

import com.gogidix.globalbusinessmanagement.multicurrency.domain.model.MultiCurrencyAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for MultiCurrencyAccount domain model.
 */
@Repository
public interface MultiCurrencyAccountRepository extends MongoRepository<MultiCurrencyAccount, String> {

    Optional<MultiCurrencyAccount> findByAccountId(String accountId);

    List<MultiCurrencyAccount> findByOwnerId(String ownerId);

    List<MultiCurrencyAccount> findByOwnerType(String ownerType);

    List<MultiCurrencyAccount> findByOwnerTypeAndOwnerId(String ownerType, String ownerId);

    List<MultiCurrencyAccount> findByStatus(MultiCurrencyAccount.AccountStatus status);

    Optional<MultiCurrencyAccount> findByOwnerIdAndIsPrimaryTrue(String ownerId);

    List<MultiCurrencyAccount> findByOwnerIdAndStatus(String ownerId, MultiCurrencyAccount.AccountStatus status);

    List<MultiCurrencyAccount> findByBaseCurrency(String baseCurrency);

    List<MultiCurrencyAccount> findByRegion(String region);

    List<MultiCurrencyAccount> findByBusinessUnit(String businessUnit);

    List<MultiCurrencyAccount> findByAccountType(MultiCurrencyAccount.AccountType accountType);

    Page<MultiCurrencyAccount> findAllByOrderByCreatedAtDesc(Pageable pageable);

    @Query("{ 'balances.currency': ?0, 'status': 'ACTIVE' }")
    List<MultiCurrencyAccount> findAccountsWithCurrencyBalance(String currencyCode);

    @Query("{ 'balances.currency': ?0, 'balances.balance': { $gt: ?1 } }")
    List<MultiCurrencyAccount> findAccountsWithMinimumBalance(String currencyCode, java.math.BigDecimal minBalance);

    @Query("{ 'ownerId': ?0, 'balances.currency': { $in: ?1 } }")
    List<MultiCurrencyAccount> findByOwnerIdAndCurrencies(String ownerId, List<String> currencies);

    boolean existsByAccountId(String accountId);

    boolean existsByOwnerIdAndIsPrimaryTrue(String ownerId);

    void deleteByAccountId(String accountId);

    @Query("{ 'ownerType': ?0, 'status': 'ACTIVE' }")
    List<MultiCurrencyAccount> findActiveByOwnerType(String ownerType);

    @Query("{ 'lastActivityAt': { $gte: ?0 } }")
    List<MultiCurrencyAccount> findRecentlyActive(java.time.Instant since);

    @Query("{ 'totalBalanceInBase': { $gte: ?0 } }")
    List<MultiCurrencyAccount> findHighValueAccounts(java.math.BigDecimal minBalance);

    long countByOwnerType(String ownerType);

    long countByStatus(MultiCurrencyAccount.AccountStatus status);

    List<MultiCurrencyAccount> findByOwnerIdOrderByCreatedAtDesc(String ownerId);
}
